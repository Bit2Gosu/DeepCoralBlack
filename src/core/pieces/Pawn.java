package core.pieces;

import javax.swing.Icon;

import core.ChessColor;
import core.IconFactory;
import core.Piece;

public final class Pawn extends Piece
{
	public static final double DEFAULT_VALUE = 1;

	private static final Icon WHITE_WHITE_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.WHITE, Piece.Type.PAWN);
	private static final Icon WHITE_BLACK_ICON = IconFactory.createIcon(ChessColor.WHITE, ChessColor.BLACK, Piece.Type.PAWN);
	private static final Icon BLACK_WHITE_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.WHITE, Piece.Type.PAWN);
	private static final Icon BLACK_BLACK_ICON = IconFactory.createIcon(ChessColor.BLACK, ChessColor.BLACK, Piece.Type.PAWN);

	public Pawn(ChessColor playerColor)
	{
		super(playerColor, Type.PAWN);
	}

	@Override
	public Icon getWhiteOnWhiteIcon()
	{
		return Pawn.WHITE_WHITE_ICON;
	}

	@Override
	public Icon getWhiteOnBlackIcon()
	{
		return Pawn.WHITE_BLACK_ICON;
	}

	@Override
	public Icon getBlackOnWhiteIcon()
	{
		return Pawn.BLACK_WHITE_ICON;
	}

	@Override
	public Icon getBlackOnBlackIcon()
	{
		return Pawn.BLACK_BLACK_ICON;
	}

	@Override
	public double getDefaultValue()
	{
		return DEFAULT_VALUE;
	}
}
