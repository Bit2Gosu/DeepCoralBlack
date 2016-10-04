package core.events;

import java.util.EventObject;

import core.PromotionChoice;
import core.ChessColor;

public final class BoardEvent extends EventObject
{
	private final ChessColor squareColor;
	private final ChessColor pieceColor;
	private final PromotionChoice choice;

	public BoardEvent(Object source, ChessColor squareColor, ChessColor pieceColor, PromotionChoice choice)
	{
		super(source);
		this.squareColor = squareColor;
		this.pieceColor = pieceColor;
		this.choice = choice;
	}

	public ChessColor getSquareColor()
	{
		return squareColor;
	}

	public ChessColor getPieceColor()
	{
		return pieceColor;
	}

	public PromotionChoice getChoice()
	{
		return choice;
	}
}
