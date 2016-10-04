package core;

/**
 * Each object of this class represents the position of exactly one square of a chess board.
 * 
 * Objects of this class are immutable.
 */
public final class SquareID
{
	/**
	 * The column of the square whose position is represented by the SquareID object.
	 */
	private final int col;
	/**
	 * The row of the square whose position is represented by the SquareID object.
	 */
	private final int row;

	/**
	 * Constructs a new SquareID object representing the position of a square contained in a cell with the given column and row.
	 * 
	 * @param col the column of the square
	 * @param row the row of the square
	 */
	public SquareID(int col, int row)
	{
		boolean columnIsValid = (col >= 1) && (col <= Board.COLUMN_IDS.length);
		boolean rowIsValid = (row >= 1) && (row <= Board.ROW_IDS.length);

		if (!columnIsValid || !rowIsValid)
		{
			throw new IllegalArgumentException("Illegal Index for column or row. Column: " + col + " Row: " + row);
		}

		this.col = col;
		this.row = row;
	}

	public SquareID(String name)
	{
		if (name.length() != 2)
		{
			throw new IllegalArgumentException(name + " ist not a valid name for a Square-ID");
		}

		name = name.toLowerCase();

		char character = name.charAt(0);
		int characterAsCol = (character - 'a') + 1;

		int row = (name.charAt(1)) - '0';

		this.col = characterAsCol;
		this.row = row;
	}

	/**
	 * Returns the column of the square whose position is represented by the SquareID object.
	 * 
	 * @return the column of the square whose position is represented
	 */
	public int getColumn()
	{
		return col;
	}

	/**
	 * Returns the row of the square whose position is represented by the SquareID object.
	 * 
	 * @return the row of the square whose position is represented
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Checks if two SquareID objects are equal. Two SquareID objects are equal if they represent the same position, that is if they have the same
	 * column and row values.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof SquareID))
		{
			return false;
		}
		SquareID id = (SquareID) o;
		return (id.col == col) && (id.row == row);
	}

	@Override
	public int hashCode()
	{
		return col * 100 + row;
	}

	@Override
	public String toString()
	{
		return Board.COLUMN_IDS[col - 1].toLowerCase() + row;
	}
}
