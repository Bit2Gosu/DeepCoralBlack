package core;

public enum ChessColor
{
	WHITE("white"),
	BLACK("black");

	private String label;

	private ChessColor(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	public ChessColor getOpposite()
	{
		return (this == WHITE) ? BLACK : WHITE;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
