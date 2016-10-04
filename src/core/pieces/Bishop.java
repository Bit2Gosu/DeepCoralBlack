package core.pieces;

import javax.swing.Icon;

import core.ChessColor;
import core.IconFactory;
import core.Piece;

public final class Bishop extends Piece
{
	public static final double DEFAULT_VALUE = 3.33;

	private static final Icon WHITE_WHITE_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.WHITE, Piece.Type.BISHOP);
	private static final Icon WHITE_BLACK_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.BLACK, Piece.Type.BISHOP);
	private static final Icon BLACK_WHITE_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.WHITE, Piece.Type.BISHOP);
	private static final Icon BLACK_BLACK_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.BLACK, Piece.Type.BISHOP);

	public Bishop(ChessColor playerColor)
	{
		super(playerColor, Type.BISHOP);
	}

	@Override
	public Icon getWhiteOnWhiteIcon()
	{
		return Bishop.WHITE_WHITE_ICON;
	}

	@Override
	public Icon getWhiteOnBlackIcon()
	{
		return Bishop.WHITE_BLACK_ICON;
	}

	@Override
	public Icon getBlackOnWhiteIcon()
	{
		return Bishop.BLACK_WHITE_ICON;
	}

	@Override
	public Icon getBlackOnBlackIcon()
	{
		return Bishop.BLACK_BLACK_ICON;
	}

	@Override
	public double getDefaultValue()
	{
		return DEFAULT_VALUE;
	}

}