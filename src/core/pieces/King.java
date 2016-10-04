package core.pieces;

import javax.swing.Icon;

import core.ChessColor;
import core.IconFactory;
import core.Piece;

public final class King extends Piece
{
	public static final double DEFAULT_VALUE = 0;

	private static final Icon WHITE_WHITE_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.WHITE, Piece.Type.KING);
	private static final Icon WHITE_BLACK_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.BLACK, Piece.Type.KING);
	private static final Icon BLACK_WHITE_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.WHITE, Piece.Type.KING);
	private static final Icon BLACK_BLACK_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.BLACK, Piece.Type.KING);

	public King(ChessColor playerColor)
	{
		super(playerColor, Type.KING);
	}

	@Override
	public Icon getWhiteOnWhiteIcon()
	{
		return King.WHITE_WHITE_ICON;
	}

	@Override
	public Icon getWhiteOnBlackIcon()
	{
		return King.WHITE_BLACK_ICON;
	}

	@Override
	public Icon getBlackOnWhiteIcon()
	{
		return King.BLACK_WHITE_ICON;
	}

	@Override
	public Icon getBlackOnBlackIcon()
	{
		return King.BLACK_BLACK_ICON;
	}

	@Override
	public double getDefaultValue()
	{
		return DEFAULT_VALUE;
	}
}