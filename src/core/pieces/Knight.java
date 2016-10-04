package core.pieces;

import javax.swing.Icon;

import core.ChessColor;
import core.IconFactory;
import core.Piece;

public final class Knight extends Piece
{
	public static final double DEFAULT_VALUE = 3.2;

	private static final Icon WHITE_WHITE_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.WHITE, Piece.Type.KNIGHT);
	private static final Icon WHITE_BLACK_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.BLACK, Piece.Type.KNIGHT);
	private static final Icon BLACK_WHITE_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.WHITE, Piece.Type.KNIGHT);
	private static final Icon BLACK_BLACK_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.BLACK, Piece.Type.KNIGHT);

	public Knight(ChessColor playerColor)
	{
		super(playerColor, Type.KNIGHT);
	}

	@Override
	public Icon getWhiteOnWhiteIcon()
	{
		return Knight.WHITE_WHITE_ICON;
	}

	@Override
	public Icon getWhiteOnBlackIcon()
	{
		return Knight.WHITE_BLACK_ICON;
	}

	@Override
	public Icon getBlackOnWhiteIcon()
	{
		return Knight.BLACK_WHITE_ICON;
	}

	@Override
	public Icon getBlackOnBlackIcon()
	{
		return Knight.BLACK_BLACK_ICON;
	}

	@Override
	public double getDefaultValue()
	{
		return DEFAULT_VALUE;
	}
}