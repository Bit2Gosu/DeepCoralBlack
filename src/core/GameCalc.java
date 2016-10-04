package core;

import java.util.HashSet;
import java.util.Set;

import core.Piece.Type;
import core.pieces.King;

public final class GameCalc
{
	private final Board board;
	private final GameCourse gameCourse;

	public GameCalc(Board board, GameCourse gameCourse)
	{
		this.board = board;
		this.gameCourse = gameCourse;
	}

	/**
	 * Checks if a player is checkmate.
	 * 
	 * @param color the color of the player to be checked for checkmate
	 * @return True if the player with the given color is checkmate, otherwise false
	 */
	public boolean isCheckmate(ChessColor color)
	{
		Square kingSquare = board.getSquare(Type.KING, color);
		boolean kingCannotMove = getTargetSquares(kingSquare).isEmpty();
		boolean kingIsAttacked = isAttacked(kingSquare);

		return (kingCannotMove && kingIsAttacked);
	}

	/**
	 * Checks if a given square is attacked by a foe.
	 * 
	 * @param square the square
	 * @return True if the square is attacked by a foe, otherwise false.
	 */
	public boolean isAttacked(Square square)
	{
		if (square.isEmpty())
		{
			return false;
		}

		Set<Square> squaresWithFoes = board.getSquaresWithPiecesOfColor(square.getPiece().getColor().getOpposite());

		for (Square squareWithFoe : squaresWithFoes)
		{
			// if the square contains our king, the opponent's king's safety does not have to be checked while looking for target squares
			Set<Square> targetSquares = (square.getPiece().getType() == Type.KING) ? getTargetSquaresNoCheck(squareWithFoe)
					: getTargetSquares(squareWithFoe);

			if (targetSquares.contains(square))
			{
				return true;
			}

		}
		return false;
	}

	/**
	 * Checks if a given square would be attacked if a piece on a given square moved to another given square.
	 * 
	 * @param fromSquareID the Square-ID of the square from which the piece moves away
	 * @param toSquareID the Square-ID of the square the piece moves to
	 * @param addressedSquareID this method checks whether the square with this Square-ID would be under attack after the move
	 * @return True if the addressed square would be under attack if the move were made, otherwise false.
	 */
	public boolean wouldBeAttacked(SquareID fromSquareID, SquareID toSquareID, SquareID addressedSquareID)
	{
		Board simulationBoard = board.clone();
		GameCourse clonedGameCourse = gameCourse.clone();
		GameCalc simulationGameCalculator = new GameCalc(simulationBoard, clonedGameCourse);

		Square equivalentFromSquare = simulationBoard.getSquare(fromSquareID);
		Square equivalentToSquare = simulationBoard.getSquare(toSquareID);
		Move simulatedMove = new Move(equivalentFromSquare, equivalentToSquare);
		simulationBoard.makeNormalMove(simulatedMove);
		Square equivalentAddressedSquare = simulationBoard.getSquare(addressedSquareID);

		return simulationGameCalculator.isAttacked(equivalentAddressedSquare);
	}

	/**
	 * Returns the squares the piece on a given square could move to. The safety of the friendly king is not checked.
	 * 
	 * @pre the given square is not empty
	 * 
	 * @param square The possible target squares for the piece on this square will be calculated. *
	 * @return the squares the piece on the given square could move to, not taking into account the friendly king's safety
	 */
	public Set<Square> getTargetSquaresNoCheck(Square square)
	{
		switch (square.getPiece().getType())
		{
		case PAWN:
			return getPawnTargetSquaresNoCheck(square);
		case ROOK:
			return getRookTargetSquaresNoCheck(square);
		case KNIGHT:
			return getKnightTargetSquaresNoCheck(square);
		case BISHOP:
			return getBishopTargetSquaresNoCheck(square);
		case QUEEN:
			return getQueenTargetSquaresNoCheck(square);
		default:
			return getKingTargetSquaresNoCheck(square);
		}
	}

	/**
	 * Returns the squares the piece on a given square could move to. The safety of the friendly king is taken into account.
	 * 
	 * @param square The possible target squares for the piece on this square will be calculated. *
	 * @return the squares the piece on the given square could move to
	 */
	public Set<Square> getTargetSquares(Square square)
	{
		Set<Square> targetSquaresNoCheck = getTargetSquaresNoCheck(square);
		HashSet<Square> targetSquares = new HashSet<Square>();

		if (square.getPiece().getType() == Type.KING) // looking for squares for our king...
		{
			int ourKingsRow = square.getID().getRow();
			if (isCastlingPossible(square, true))
			{
				Square possibleSquare = board.getSquare(new SquareID(3, ourKingsRow));
				targetSquares.add(possibleSquare);
			}
			if (isCastlingPossible(square, false))
			{
				Square possibleSquare = board.getSquare(new SquareID(7, ourKingsRow));
				targetSquares.add(possibleSquare);
			}
		}

		// Check if the opponent's king can be captured. If so the capturing moves are the only possible moves.
		for (Square targetSquareNoCheck : targetSquaresNoCheck)
		{
			if (!targetSquareNoCheck.isEmpty() && (targetSquareNoCheck.getPiece().getType() == Type.KING) && (targetSquareNoCheck.getPiece()
					.getColor() != square.getPiece().getColor()))
			{
				targetSquares.add(targetSquareNoCheck);
				return targetSquares; // king has to be captured if possible
			}
		}

		Square friendlyKingSquare = board.getSquare(Type.KING, square.getPiece().getColor());

		for (Square targetSquareNoCheck : targetSquaresNoCheck)
		{
			// a target square is only a valid target if our king is not endangered after moving to the target
			boolean ownKingWouldBeAttacked;
			if (square.getPiece().getType() == Type.KING)
			{
				ownKingWouldBeAttacked = wouldBeAttacked(square.getID(), targetSquareNoCheck.getID(), targetSquareNoCheck.getID());
			}
			else
			{
				ownKingWouldBeAttacked = wouldBeAttacked(square.getID(), targetSquareNoCheck.getID(), friendlyKingSquare.getID());
			}
			if (!ownKingWouldBeAttacked)
			{
				targetSquares.add(targetSquareNoCheck);
			}
		}

		return targetSquares;
	}

	/**
	 * Checks if a given king has moved in the game.
	 * 
	 * @param king the king
	 * @return True if the given king has moved in the game, otherwise false.
	 */
	public boolean kingHasMoved(King king)
	{
		for (int i = 0; i < gameCourse.getMoveCount(); i++)
		{
			if (gameCourse.getMoveInfo(i).getFromPiece().equals(king))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the rook of a given color and a given side of the board has moved in the game.
	 * 
	 * @param rookColor the color of the rook
	 * @param queenside True for the rook on queen side. False for the rook on the king side.
	 * @return True if the rook of the given color and the given side of the board has moved in the game, otherwise false.
	 */
	public boolean rookHasMoved(ChessColor rookColor, boolean queenside)
	{
		for (int i = 0; i < gameCourse.getMoveCount(); i++)
		{
			MoveInfo moveInfo = gameCourse.getMoveInfo(i);
			Piece movingPiece = moveInfo.getFromPiece();
			if (movingPiece.getType() == Type.KING && movingPiece.getColor() == rookColor)
			{
				int fromCol = moveInfo.getFromSquareID().getColumn();
				if ((queenside && (fromCol == 1)) || (!queenside && (fromCol == 8)))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if castling is safe on a given side for a king. It is not checked whether the squares between king and rook are empty.
	 * 
	 * @param kingSquare the king's square
	 * @param queenside True for checking the safety of castling queenside. False for checking the safety of castling kingside.
	 * @return True if castling is safe on the given side for the given king
	 */
	public boolean isCastlingSafe(Square kingSquare, boolean queenside)
	{
		if (isAttacked(kingSquare))
		{
			return false;
		}
		int row = kingSquare.getID().getRow();
		if (queenside)
		{
			for (int col = 3; col < kingSquare.getID().getColumn(); col++)
			{
				Square square = board.getSquare(new SquareID(col, row));
				if (wouldBeAttacked(kingSquare.getID(), square.getID(), square.getID()))
				{
					return false;
				}
			}
		}
		else
		{
			for (int col = kingSquare.getID().getColumn() + 1; col < 8; col++)
			{
				Square square = board.getSquare(new SquareID(col, row));
				if (wouldBeAttacked(kingSquare.getID(), square.getID(), square.getID()))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks whether the squares between a given king and the rook of a given side are empty.
	 * 
	 * @param kingSquare the king's square
	 * @param queenside True for checking the space for castling queenside. False for checking the space for castling kingside.
	 * @return True if the squares between a given king and the rook of a given side are empty, otherwise false.
	 */
	public boolean isSpaceForCastling(Square kingSquare, boolean queenside)
	{
		int row = kingSquare.getID().getRow();
		if (queenside)
		{
			for (int col = 2; col < kingSquare.getID().getColumn(); col++)
			{
				Square square = board.getSquare(new SquareID(col, row));
				if (!square.isEmpty())
				{
					return false;
				}
			}
		}
		else
		{
			for (int col = kingSquare.getID().getColumn() + 1; col < 8; col++)
			{
				Square square = board.getSquare(new SquareID(col, row));
				if (!square.isEmpty())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if castling is possible for a given king on a given side.
	 * 
	 * @param kingSquare the king's square
	 * @param queenside True for checking the possibility of castling queenside. False for checking the possibility of castling kingside.
	 * @return True if castling is possible for a given king on a given side, otherwise false.
	 */
	public boolean isCastlingPossible(Square kingSquare, boolean queenside)
	{
		King king = (King) kingSquare.getPiece();
		return !kingHasMoved(king) && !rookHasMoved(king.getColor(), queenside) && isSpaceForCastling(kingSquare, queenside) && isCastlingSafe(
				kingSquare, queenside);
	}

	public Set<Square> getRookTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		int potCol, potRow;

		for (int j = -1; j < 2; j += 2)
		{
			for (int k = -1; k < 2; k += 2)
			{
				for (int i = 1; i < 8; i++)
				{
					potCol = col + i * (j - k) / 2;
					potRow = row + i * (k + j) / 2;
					if (GameCalc.isInsideBoard(potCol, potRow))
					{
						SquareID potSquareID = new SquareID(potCol, potRow);
						Square potSquare = board.getSquare(potSquareID);
						if (potSquare.getPiece() == null)
						{
							targetSquaresNoCheck.add(potSquare);
						}
						else
						{
							if (square.getPiece().getColor() != potSquare.getPiece().getColor())
							{
								targetSquaresNoCheck.add(potSquare);
							}
							break;
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	public Set<Square> getKnightTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		int potCol, potRow;

		for (int i = 1; i < 3; i++)
		{
			for (int j = -1; j < 2; j += 2)
			{
				for (int k = -1; k < 2; k += 2)
				{
					potCol = col + i * j;
					potRow = row + (3 - i) * k;
					if (GameCalc.isInsideBoard(potCol, potRow))
					{
						SquareID potSquareID = new SquareID(potCol, potRow);
						Square potSquare = board.getSquare(potSquareID);
						if (potSquare.getPiece() == null)
						{
							targetSquaresNoCheck.add(potSquare);
						}
						else if (square.getPiece().getColor() != potSquare.getPiece().getColor())
						{
							targetSquaresNoCheck.add(potSquare);
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	public Set<Square> getBishopTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		int potCol, potRow;

		for (int j = -1; j < 2; j += 2)
		{
			for (int k = -1; k < 2; k += 2)
			{
				for (int i = 1; i < 8; i++)
				{
					potCol = col + i * j;
					potRow = row + i * k;
					if (GameCalc.isInsideBoard(potCol, potRow))
					{
						SquareID potSquareID = new SquareID(potCol, potRow);
						Square potSquare = board.getSquare(potSquareID);
						if (potSquare.getPiece() == null)
						{
							targetSquaresNoCheck.add(potSquare);
						}
						else
						{
							if (square.getPiece().getColor() != potSquare.getPiece().getColor())
							{
								targetSquaresNoCheck.add(potSquare);
							}
							break;
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	public Set<Square> getQueenTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		int[] potCol = new int[2];
		int[] potRow = new int[2];
		boolean[] stopSearch = new boolean[2];

		for (int j = -1; j < 2; j += 2)
		{
			for (int k = -1; k < 2; k += 2)
			{
				stopSearch[0] = false;
				stopSearch[1] = false;
				for (int i = 1; i < 8; i++)
				{
					potCol[0] = col + i * (j - k) / 2;
					potRow[0] = row + i * (k + j) / 2;
					potCol[1] = col + i * j;
					potRow[1] = row + i * k;
					for (int v = 0; v < 2; v++)
					{
						if (GameCalc.isInsideBoard(potCol[v], potRow[v]))
						{
							SquareID potSquareID = new SquareID(potCol[v], potRow[v]);
							Square potSquare = board.getSquare(potSquareID);
							if (!stopSearch[v])
							{
								if (potSquare.getPiece() == null)
								{
									targetSquaresNoCheck.add(potSquare);
								}
								else
								{
									if (square.getPiece().getColor() != potSquare.getPiece().getColor())
									{
										targetSquaresNoCheck.add(potSquare);
									}
									stopSearch[v] = true;
								}
							}
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	public Set<Square> getKingTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		int[] potCol = new int[2];
		int[] potRow = new int[2];

		for (int i = -1; i < 3; i += 2)
		{
			for (int j = -1; j < 3; j += 2)
			{
				potCol[0] = col + (i - j) / 2;
				potRow[0] = row + (j + i) / 2;
				potCol[1] = col + i;
				potRow[1] = row + j;
				SquareID[] potSquareIDs = new SquareID[2];
				Square[] potSquares = new Square[2];
				for (int v = 0; v < 2; v++)
				{
					if (GameCalc.isInsideBoard(potCol[v], potRow[v]))
					{
						potSquareIDs[v] = new SquareID(potCol[v], potRow[v]);
						potSquares[v] = board.getSquare(potSquareIDs[v]);
					}
					if (potSquares[v] != null)
					{
						if (potSquares[v].getPiece() == null)
						{
							targetSquaresNoCheck.add(potSquares[v]);
						}
						else
						{
							Piece otherPiece = potSquares[v].getPiece();
							if (square.getPiece().getColor() != otherPiece.getColor())
							{
								targetSquaresNoCheck.add(potSquares[v]);
							}
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	public Set<Square> getPawnTargetSquaresNoCheck(Square square)
	{
		HashSet<Square> targetSquaresNoCheck = new HashSet<Square>();
		SquareID squareID = square.getID();
		int col = squareID.getColumn();
		int row = squareID.getRow();
		Integer frontRow = GameCalc.getOneRowAhead(row, square.getPiece().getColor());

		// is there any front square?
		if (frontRow != null)
		{
			SquareID frontSquareID = new SquareID(col, frontRow);

			// can pawn move one square ahead?
			if (board.getSquare(frontSquareID).isEmpty())
			{
				targetSquaresNoCheck.add(board.getSquare(frontSquareID));

				// can pawn move two squares ahead?
				if (row == GameCalc.getEquivalent(2, square.getPiece().getColor()))
				{
					Integer frontfrontRow = GameCalc.getTwoRowsAhead(row, square.getPiece().getColor());

					if (frontfrontRow != null)
					{
						SquareID frontfrontSquareID = new SquareID(col, frontfrontRow);
						if (board.getSquare(frontfrontSquareID).isEmpty())
						{
							targetSquaresNoCheck.add(board.getSquare(frontfrontSquareID));
						}
					}
				}
			}
			// can pawn move to front left square or front right square?
			Integer[] sideColumns = new Integer[2];
			sideColumns[0] = GameCalc.getLeftColumn(col, square.getPiece().getColor());
			sideColumns[1] = GameCalc.getRightColumn(col, square.getPiece().getColor());
			for (int i = 0; i < 2; i++)
			{
				if (sideColumns[i] != null)
				{
					Square frontSideSquare = board.getSquare(new SquareID(sideColumns[i], frontRow));
					if (frontSideSquare.getPiece() != null && (square.getPiece().getColor() != frontSideSquare.getPiece().getColor()))
					{
						targetSquaresNoCheck.add(frontSideSquare);
					}
				}
			}

			// is "en passant" possible?
			if (row == GameCalc.getEquivalent(5, square.getPiece().getColor()))
			{
				MoveInfo lastMoveInfo = gameCourse.getLastMoveInfo();
				if (lastMoveInfo != null)
				{
					int foeFromRowE = lastMoveInfo.getFromSquareID().getRow();
					int foeToRowE = lastMoveInfo.getToSquareID().getRow();
					if ((foeFromRowE == GameCalc.getEquivalent(7, square.getPiece().getColor())) && (foeToRowE == GameCalc.getEquivalent(5, square
							.getPiece().getColor())))
					{
						int foeColumn = lastMoveInfo.getToSquareID().getColumn();
						if ((foeColumn == col + 1) || (foeColumn == col - 1))
						{
							SquareID squareBehindFoe = new SquareID(foeColumn, GameCalc.getEquivalent(6, square.getPiece().getColor()));
							if (board.getSquare(squareBehindFoe).isEmpty())
							{
								targetSquaresNoCheck.add(board.getSquare(squareBehindFoe));
							}
						}
					}
				}
			}
		}
		return targetSquaresNoCheck;
	}

	/* ------------------------------------------------ STATIC METHODS ------------------------------------------------ */

	/**
	 * Checks if a give valid move is a king side castling move.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is a king side castling move, otherwise false.
	 */
	public static boolean isKingSideCastling(Move move)
	{
		Square fromSquare = move.getFrom();
		int fromCol = fromSquare.getID().getColumn();
		int toCol = move.getTo().getID().getColumn();
		return (fromSquare.getPiece().getType() == Type.KING) && (toCol == fromCol + 2);
	}

	/**
	 * Checks if a give valid move is a queen side castling move.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is a queen side castling move, otherwise false.
	 */
	public static boolean isQueenSideCastling(Move move)
	{
		Square fromSquare = move.getFrom();
		int fromCol = fromSquare.getID().getColumn();
		int toCol = move.getTo().getID().getColumn();
		return (fromSquare.getPiece().getType() == Type.KING) && ((toCol == fromCol - 2));
	}

	/**
	 * Checks if a give valid move is a castling move.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is a castling move, otherwise false.
	 */
	public static boolean isCastling(Move move)
	{
		Square fromSquare = move.getFrom();
		int fromCol = fromSquare.getID().getColumn();
		int toCol = move.getTo().getID().getColumn();
		return (fromSquare.getPiece().getType() == Type.KING) && ((toCol == fromCol - 2) || (toCol == fromCol + 2));
	}

	/**
	 * Checks if a give valid move is an en passant move.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is an en passent move, otherwise false.
	 */
	public static boolean isEnPassant(Move move)
	{
		int fromCol = move.getFrom().getID().getColumn();
		int toCol = move.getTo().getID().getColumn();
		Piece piece = move.getFrom().getPiece();

		return piece != null && (piece.getType() == Type.PAWN) && move.getTo().isEmpty() && (fromCol != toCol);
	}

	/**
	 * Checks if a given valid move is a pawn promotion.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is a pawn promotion, otherwise false.
	 */
	public static boolean isPromotion(Move move)
	{
		int toRow = move.getTo().getID().getRow();
		Piece piece = move.getFrom().getPiece();

		return piece != null && (piece.getType() == Type.PAWN) && GameCalc.getEquivalent(toRow, piece.getColor()) == 8;
	}

	/**
	 * Checks if a given move is a capture move.
	 * 
	 * @param move the move (before it is executed)
	 * @return True if the move is a capture move, otherwise false.
	 */
	public static boolean isCapture(Move move)
	{
		return (!move.getTo().isEmpty()) || GameCalc.isEnPassant(move);
	}

	/**
	 * Checks if a given cell (as defined by a column and a row) is inside the board.
	 * 
	 * @param col the column
	 * @param row the row
	 * @return True if the given cell (as defined by a column and a row) is inside the board, otherwise false.
	 */
	public static boolean isInsideBoard(int col, int row)
	{
		return (col >= 1) && (col <= 8) && (row >= 1) && (row <= 8);
	}

	/**
	 * Translates row and column numbers into the perspective of the white player.
	 * 
	 * @param columnOrRow the column or row to be translated
	 * @param pieceColor the color of the player. If this color equals white, no translation is necessary.
	 * @return the (potentially translated) column or row
	 */
	public static int getEquivalent(int columnOrRow, ChessColor pieceColor)
	{
		return (pieceColor == ChessColor.WHITE) ? columnOrRow : 9 - columnOrRow;
	}

	/**
	 * Returns the number of the row that is two rows ahead (from the perspective of a player) of a given row.
	 * 
	 * @param row the row
	 * @param pieceColor the color defines the perspective which in turn defines the meaning of 'two rows ahead'
	 * @return the number of the row that is two rows ahead of a given row or null, if the result is outside the chess board.
	 */
	public static Integer getTwoRowsAhead(int row, ChessColor pieceColor)
	{
		if (pieceColor == ChessColor.WHITE && (row <= 6))
		{
			return row + 2;
		}
		else if (pieceColor == ChessColor.BLACK && (row >= 3))
		{
			return row - 2;
		}
		return null;
	}

	/**
	 * Returns the number of the row that is one row ahead (from the perspective of a player) of a given row.
	 * 
	 * @param row the row
	 * @param pieceColor the color defines the perspective which in turn defines the meaning of 'two rows ahead'
	 * @return the number of the row that is one row ahead of a given row or null, if the result is outside the chess board.
	 */
	public static Integer getOneRowAhead(int row, ChessColor pieceColor)
	{
		if ((pieceColor == ChessColor.WHITE) && (row < 8))
		{
			return row + 1;
		}
		else if ((pieceColor == ChessColor.BLACK) && (row > 1))
		{
			return row - 1;
		}
		return null;
	}

	/**
	 * Returns the number of the column that is left (from the perspective of a player) of a given column.
	 * 
	 * @param row the row
	 * @param pieceColor the color defines the perspective which in turn defines the meaning of 'two rows ahead'
	 * @return the number of the column that is left of a given column or null, if the result is outside the chess board.
	 */
	public static Integer getLeftColumn(int col, ChessColor pieceColor)
	{
		if (pieceColor == ChessColor.WHITE && (col > 1))
		{
			return col - 1;
		}
		else if (pieceColor == ChessColor.BLACK && (col < 8))
		{
			return col + 1;
		}
		return null;
	}

	/**
	 * Returns the number of the column that is right (from the perspective of a player) of a given column.
	 * 
	 * @param row the row
	 * @param pieceColor the color defines the perspective which in turn defines the meaning of 'two rows ahead'
	 * @return the number of the column that is right of a given column or null, if the result is outside the chess board.
	 */
	public static Integer getRightColumn(int col, ChessColor pieceColor)
	{
		if (pieceColor == ChessColor.WHITE && (col != 8))
		{
			return col + 1;
		}
		else if (pieceColor == ChessColor.BLACK && (col != 1))
		{
			return col - 1;
		}
		return null;
	}
}
