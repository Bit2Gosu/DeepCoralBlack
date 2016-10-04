package core;

import javax.swing.Icon;

/**
 * A chess piece. Objects of this class are immutable.
 */
public abstract class Piece
{
	public enum Type
	{
		PAWN("pawn"),
		BISHOP("bishop"),
		KNIGHT("knight"),
		ROOK("rook"),
		QUEEN("queen"),
		KING("king");

		private final String label;

		private Type(String label)
		{
			this.label = label;
		}

		public String getLabel()
		{
			return label;
		}
	}

	private final ChessColor color;
	private final Type type;

	public Piece(ChessColor color, Type type)
	{
		this.color = color;
		this.type = type;
	}

	public ChessColor getColor()
	{
		return color;
	}

	public Type getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return type.getLabel() + " (" + color.toString() + ")";
	}

	public abstract double getDefaultValue();

	public abstract Icon getWhiteOnWhiteIcon();

	public abstract Icon getWhiteOnBlackIcon();

	public abstract Icon getBlackOnWhiteIcon();

	public abstract Icon getBlackOnBlackIcon();
}
