package core;

import java.util.EventListener;

import core.events.BoardEvent;

public interface BoardListener extends EventListener
{
	public PromotionChoice pawnReachedBaseline(BoardEvent e);
}
