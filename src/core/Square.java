package core;

import javax.swing.Icon;

/**
 * An object of this class represents a square of a chess board. A square object contains info about its color and the position of the cell as well as
 * info about whether a piece is located on the square and if so which piece it is.
 * 
 * Objects of this class are mutable.
 */
public final class Square
{
	private final static Icon EMPTY_WHITE_SQUARE = IconFactory.createEmptySquareIcon(ChessColor.WHITE);
	private final static Icon EMPTY_BLACK_SQUARE = IconFactory.createEmptySquareIcon(ChessColor.BLACK);

	/**
	 * The SquareID of the Square object.
	 */
	private final SquareID squareID;
	/**
	 * A flag which is true if this square is white and false if it is black.
	 */
	private final ChessColor color;
	/**
	 * The piece which is located on this square null if this square contains no piece.
	 */
	private Piece piece;

	/**
	 * Constructs a new empty Square object with the specified SquareID. This constructor should only be used to create a chessboard, that is in the
	 * constructor of the Board class. This contructor determines the color of the square.
	 * 
	 * @param ID the Square's ID
	 */
	public Square(SquareID ID)
	{
		this.squareID = ID;
		color = calculateColor(ID);
	}

	/**
	 * Returns an icon of the square. What the icon looks like depends on this Square's color and the piece located on it (including the piece's
	 * color).
	 * 
	 * @return an icon representing this square
	 */
	public Icon getIcon()
	{
		if (piece == null)
		{
			if (color == ChessColor.WHITE)
			{
				return Square.EMPTY_WHITE_SQUARE;
			}
			return Square.EMPTY_BLACK_SQUARE;
		}
		else
		{
			if (color == ChessColor.WHITE)
			{
				if (piece.getColor() == ChessColor.WHITE)
				{
					return piece.getWhiteOnWhiteIcon();
				}
				return piece.getBlackOnWhiteIcon();
			}
			else
			{
				if (piece.getColor() == ChessColor.WHITE)
				{
					return piece.getWhiteOnBlackIcon();
				}
				return piece.getBlackOnBlackIcon();
			}
		}
	}

	/**
	 * Returns the piece located on this square.
	 * 
	 * @return the piece located on this square or null if there is no piece on this square.
	 */
	public Piece getPiece()
	{
		return piece;
	}

	/**
	 * Checks whether this square is empty.
	 * 
	 * @return true is this square does not contain a piece, otherwise false
	 */
	public boolean isEmpty()
	{
		return piece == null;
	}

	/**
	 * Returns the color of this square.
	 * 
	 * @return the color of this square
	 */
	public ChessColor getColor()
	{
		return color;
	}

	/**
	 * Returns the SquareID of this object.
	 * 
	 * @return the SquareID of this object
	 */
	public SquareID getID()
	{
		return squareID;
	}

	/**
	 * Creates and returns a new Square that has the old squareID and piece.
	 */
	@Override
	public Square clone()
	{
		Square clone = new Square(squareID);
		clone.setPiece(piece);
		return clone;
	}

	/**
	 * A square equals another square if both squares have the same SquareID. Hence comparisons of squares contained by different Board objects might
	 * have unexpected results.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Square))
		{
			return false;
		}
		Square square = (Square) o;
		return square.getID().equals(squareID);
	}

	@Override
	public String toString()
	{
		String res = squareID.toString();
		if (piece != null)
		{
			res += " {" + piece.toString() + "}";
		}
		return res;
	}

	@Override
	public int hashCode()
	{
		return squareID.hashCode();
	}

	/**
	 * Removes the piece located on this square if such a piece exists.
	 */
	protected void clear()
	{
		piece = null;
	}

	/**
	 * Places a piece onto this square.
	 * 
	 * @param piece the piece to be placed onto this square. Rather than passing a null reference the clear method should be used.
	 */
	protected void setPiece(Piece piece)
	{
		this.piece = piece;
	}

	private ChessColor calculateColor(SquareID squareID)
	{
		int col = squareID.getColumn();
		int row = squareID.getRow();
		boolean columnIsEven = (col % 2) == 0;
		boolean rowIsEven = (row % 2) == 0;
		return ((columnIsEven && !rowIsEven) || (!columnIsEven && rowIsEven)) ? ChessColor.WHITE : ChessColor.BLACK;
	}
}
