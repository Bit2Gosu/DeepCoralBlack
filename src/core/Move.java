package core;

/**
 * A chess move. It consists of the information about the origin and target squares. These squares' state is not preserved so that the pieces located
 * on them change as this move and further moves are executed.
 */
public final class Move
{
	private final Square from;
	private final Square to;

	public Move(Square from, Square to)
	{
		this.from = from;
		this.to = to;
	}

	/**
	 * Creates a new move.
	 * 
	 * @param board the board where the move will be executed
	 * @param fromSquareName the name of the move's origin square. For example a1 (1 being the lowest row number and a being the lowest column
	 * descriptor).
	 * @param toSquareName the name of the move's target square.
	 */
	public Move(Board board, String fromSquareName, String toSquareName)
	{
		this.from = board.getSquare(new SquareID(fromSquareName));
		this.to = board.getSquare(new SquareID(toSquareName));
	}

	public Square getFrom()
	{
		return from;
	}

	public Square getTo()
	{
		return to;
	}

	@Override
	public String toString()
	{
		return from + "   -->   " + to;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Move))
		{
			return false;
		}
		Move move = (Move) o;
		if (move.getFrom().equals(from) && move.getTo().equals(to))
		{
			return true;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return from.hashCode() + to.hashCode();
	}

	@Override
	public Move clone()
	{
		return new Move(from.clone(), to.clone());
	}

}
