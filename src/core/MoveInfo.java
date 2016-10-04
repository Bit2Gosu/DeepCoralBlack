package core;

/**
 * An object of this class stores information about a move at the time just before the move was made. Therefore the Squares that can be accessed
 * through the move object are in the state just before the move was made. This state does did not change as the move was made because all mutable
 * objects are cloned as the MoveInfo object is created.
 */
public final class MoveInfo
{
	private final Move move;
	private final long remainingTime;

	/**
	 * Creates a new MoveInfo Object.
	 * 
	 * @param move
	 * @param remainingTime
	 */
	public MoveInfo(Move move, long remainingTime)
	{
		this.move = move.clone();
		this.remainingTime = remainingTime;
	}

	public Move getMove()
	{
		return move;
	}

	public ChessColor getColorOfPlayerThatMoved()
	{
		return move.getFrom().getPiece().getColor();
	}

	public SquareID getFromSquareID()
	{
		return move.getFrom().getID();
	}

	public SquareID getToSquareID()
	{
		return move.getTo().getID();
	}

	public Piece getFromPiece()
	{
		return move.getFrom().getPiece();
	}

	public Piece getToPiece()
	{
		return move.getTo().getPiece();
	}

	public boolean isCapture()
	{
		return GameCalc.isCapture(move);
	}

	public boolean isKingSideCastling()
	{
		return GameCalc.isKingSideCastling(move);
	}

	public boolean isQueenSideCastling()
	{
		return GameCalc.isQueenSideCastling(move);
	}

	public boolean isCastling()
	{
		return GameCalc.isCastling(move);
	}

	public boolean isEnPassant()
	{
		return GameCalc.isEnPassant(move);
	}

	public boolean isPawnTradeMove()
	{
		return GameCalc.isPromotion(move);
	}

	public long getRemainingTime()
	{
		return remainingTime;
	}

	@Override
	public MoveInfo clone()
	{
		return new MoveInfo(move.clone(), remainingTime);
	}

	@Override
	public String toString()
	{
		String result = "";
		result += getFromSquareID().toString();
		result += " -> ";
		result += getToSquareID().toString();

		return result;
	}
}
