package core;

import java.util.*;

import core.Piece.Type;
import core.pieces.*;

/**
 * This class represents a chess board. A chess board contains 8 rows and 8 columns which together form 64 squares (cells). Each square is represented
 * by a Square object. Objects of this class keep a set of all pieces captured on the board.
 * 
 * @author Richard Schubert
 */
public final class Board
{
	/**
	 * Each row of a chessboard can be identified through an integer ID with 1 <= ID <= 8.
	 */
	public static final int[] ROW_IDS =
	{ 1, 2, 3, 4, 5, 6, 7, 8 };
	/**
	 * Each column of a chessboard can be identified through a String ID that is element of {"A", "B", "C", "D", "E", "F", "G", "H"}.
	 */
	public static final String[] COLUMN_IDS =
	{ "A", "B", "C", "D", "E", "F", "G", "H" };
	/**
	 * Equals 64.
	 */
	public static final int SQUARE_COUNT = Board.ROW_IDS.length * Board.COLUMN_IDS.length;

	public static final int MAX_PIECE_COUNT = 32;

	/**
	 * The board's squares.
	 */
	private HashSet<Square> squares = new HashSet<Square>(Board.SQUARE_COUNT);

	/**
	 * Constructs a new Board filled with squares which in turn contain the pieces of a chess board before the start of the game.
	 * 
	 * @param defaultSetup If true the board's squares will be filled with pieces to create the setting of a board at the beginning of a game. If
	 * false, the board's squares will be empty.
	 */
	public Board(boolean defaultSetup)
	{
		if (defaultSetup)
		{
			createDefaultSetup();
		}
	}

	/**
	 * Returns the Piece located on this board's square with the given ID.
	 * 
	 * @param squareID the ID of the square whose piece is to be retrieved
	 * @return the Piece located on the square with the given ID
	 */
	public Piece getPiece(SquareID squareID)
	{
		return getSquare(squareID).getPiece();
	}

	/**
	 * Returns this board's Square with the specified ID.
	 * 
	 * @param squareID the ID of the square which is to be retrieved
	 * @return the Square with the given ID
	 */
	public Square getSquare(SquareID squareID)
	{
		for (Square square : squares)
		{
			if (square.getID().equals(squareID))
			{
				return square;
			}
		}
		return null;
	}

	/**
	 * Looks for a square that contains a piece of a specified type and a specified color and returns that square. Squares are not checked in any
	 * particular order.
	 * 
	 * @param typeOfPiece The type of piece located on the square
	 * @param isWhite The color of the piece located on the square
	 * @return a square that contains a piece of a specified type. If no such square exists returns null
	 */
	public Square getSquare(Type pieceType, ChessColor colorOfPieceOnSquare)
	{
		for (Square square : squares)
		{
			Piece piece = square.getPiece();
			if (!square.isEmpty() && (piece.getColor() == colorOfPieceOnSquare) && (piece.getType() == pieceType))
			{
				return square;
			}
		}
		return null;
	}

	/**
	 * Returns a set containing those of this board's squares that contain a piece of a certain color.
	 * 
	 * @param pieceColor the color
	 * 
	 * @return a set containing those of this board's squares that contain a piece of the given color.
	 */
	public Set<Square> getSquaresWithPiecesOfColor(ChessColor pieceColor)
	{
		HashSet<Square> res = new HashSet<Square>();
		for (Square square : squares)
		{
			Piece piece = square.getPiece();
			if ((piece != null) && (piece.getColor() == pieceColor))
			{
				res.add(square);
			}
		}
		return res;
	}

	/**
	 * Returns a deep copy of this board. All fields are cloned. The cloned fields become part of the returned clone.
	 */
	@Override
	public Board clone()
	{
		Board clonedBoard = new Board(false); // create empty board

		squares.forEach(square -> clonedBoard.squares.add(square.clone()));

		return clonedBoard;
	}

	/**
	 * Executes an normal move on this board. The move's square of origin is cleared. If the move's target square contains a piece, that piece is
	 * removed from the target square and is added to this board's list of captured pieces. This method neither checks whether the move is a valid nor
	 * whether it is an ordinary move. A move is called normal if it does not make use of any of the following rules:
	 * <ul>
	 * <li>en passant</li>
	 * <li>castling</li>
	 * <li>pawn promotion</li>
	 * </ul>
	 * 
	 * @param move the move to be executed
	 */
	public void makeNormalMove(Move move)
	{
		Square fromSquare = move.getFrom();
		Square toSquare = move.getTo();
		Piece movingPiece = fromSquare.getPiece();
		fromSquare.clear();
		toSquare.setPiece(movingPiece);
	}

	/**
	 * Executes an en passant move on this board. If a foe is captured as a consequence of this move it is added to this board's list of captured
	 * pieces. This method neither checks whether the move is valid nor if it is an en passant move.
	 * 
	 * @param move the move to be executed
	 */
	public void makeEnPassantMove(Move move)
	{
		int removeCol = move.getTo().getID().getColumn();
		int removeRow = move.getFrom().getID().getRow();
		SquareID idToRemoveFrom = new SquareID(removeCol, removeRow);
		Square squareToRemoveFrom = getSquare(idToRemoveFrom);
		squareToRemoveFrom.clear();
		makeNormalMove(move);
	}

	/**
	 * Executes a pawn trade move on this board. The pawn is removed from this board and a piece chosen by the pawn's owner is placed onto the move's
	 * target square. This method neither checks whether the move is valid nor if it is a pawn trade move.
	 * 
	 * @param move the move to be executed
	 * @param choice the pawn's owner's choice concerning the piece that is to be placed onto the target square.
	 */
	public void makePawnTradeMove(Move move, PromotionChoice choice)
	{
		Square fromSquare = move.getFrom();
		Square toSquare = move.getTo();
		ChessColor pawnColor = fromSquare.getPiece().getColor();
		Piece piece;

		switch (choice)
		{
		case KNIGHT:
			piece = new Knight(pawnColor);
			break;
		case BISHOP:
			piece = new Bishop(pawnColor);
			break;
		case ROOK:
			piece = new Rook(pawnColor);
			break;
		default:
			piece = new Queen(pawnColor);
		}
		fromSquare.clear();
		toSquare.setPiece(piece);
	}

	/**
	 * Executes a castling move on this board. This method neither checks whether the move is valid nor if it is a castling move.
	 * 
	 * @param move the move to be executed
	 */
	public void makeCastlingMove(Move move)
	{
		int fromCol = move.getFrom().getID().getColumn();
		int toCol = move.getTo().getID().getColumn();
		int row = move.getFrom().getID().getRow();
		makeNormalMove(move);

		int oldRookCol = toCol < fromCol ? 1 : 8;
		int futureRookCol = toCol < fromCol ? fromCol - 1 : fromCol + 1;

		Square oldRookSquare = getSquare(new SquareID(oldRookCol, row));
		Square futureRookSquare = getSquare(new SquareID(futureRookCol, row));

		futureRookSquare.setPiece(oldRookSquare.getPiece());
		oldRookSquare.clear();
	}

	/* ------------------------------------------------ PRIVATE METHODS ------------------------------------------------ */

	private void createDefaultSetup()
	{
		for (int columnID = 1; columnID <= Board.COLUMN_IDS.length; columnID++)
		{
			for (int rowID = Board.ROW_IDS.length; rowID >= 1; rowID--)
			{
				Piece piece;
				if ((rowID == 1) || (rowID == 8))
				{
					ChessColor color = (rowID == 1) ? ChessColor.WHITE : ChessColor.BLACK;
					if ((columnID == 1) || (columnID == 8))
					{
						piece = new Rook(color);
					}
					else if ((columnID == 2) || (columnID == 7))
					{
						piece = new Knight(color);
					}
					else if ((columnID == 3) || (columnID == 6))
					{
						piece = new Bishop(color);
					}
					else if (columnID == 4)
					{
						piece = new Queen(color);
					}
					else
					{
						piece = new King(color);
					}
				}
				else if ((rowID == 2) || (rowID == 7))
				{
					ChessColor color = (rowID == 2) ? ChessColor.WHITE : ChessColor.BLACK;
					piece = new Pawn(color);
				}
				else
				{
					piece = null;
				}
				Square square = new Square(new SquareID(columnID, rowID));
				square.setPiece(piece);
				squares.add(square);
			}
		}
	}

	@Override
	public String toString()
	{
		String res = "---------------------" + "\n";
		for (Square square : squares)
		{
			res += square.toString() + "\n";
		}
		return res + "---------------------";
	}

}
