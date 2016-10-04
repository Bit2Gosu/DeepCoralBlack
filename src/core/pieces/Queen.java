package core.pieces;

import javax.swing.Icon;

import core.ChessColor;
import core.IconFactory;
import core.Piece;

public final class Queen extends Piece
{
	public static final double DEFAULT_VALUE = 8.8;

	private static final Icon WHITE_WHITE_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.WHITE, Piece.Type.QUEEN);
	private static final Icon WHITE_BLACK_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.BLACK, Piece.Type.QUEEN);
	private static final Icon BLACK_WHITE_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.WHITE, Piece.Type.QUEEN);
	private static final Icon BLACK_BLACK_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.BLACK, Piece.Type.QUEEN);

	public Queen(ChessColor playerColor)
	{
		super(playerColor, Type.QUEEN);
	}

	@Override
	public Icon getWhiteOnWhiteIcon()
	{
		return Queen.WHITE_WHITE_ICON;
	}

	@Override
	public Icon getWhiteOnBlackIcon()
	{
		return Queen.WHITE_BLACK_ICON;
	}

	@Override
	public Icon getBlackOnWhiteIcon()
	{
		return Queen.BLACK_WHITE_ICON;
	}

	@Override
	public Icon getBlackOnBlackIcon()
	{
		return Queen.BLACK_BLACK_ICON;
	}

	@Override
	public double getDefaultValue()
	{
		return DEFAULT_VALUE;
	}
}