package core;

import java.util.EventListener;

import core.events.PlayerTimeEvent;

public interface ChessClockListener extends EventListener
{
	public void noticeTimeUpdate(PlayerTimeEvent e);
}
