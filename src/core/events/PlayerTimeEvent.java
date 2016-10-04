package core.events;

import core.ChessColor;

public final class PlayerTimeEvent extends TimeEvent
{
	private ChessColor timedColor;

	public PlayerTimeEvent(Object source, long remainingTime, ChessColor timedColor)
	{
		super(source, remainingTime);
		this.timedColor = timedColor;
	}

	public ChessColor getTimedColor()
	{
		return timedColor;
	}
}
