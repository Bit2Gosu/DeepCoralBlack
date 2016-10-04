package core;

import core.events.TimeEvent;

public interface ExpirationClockListener
{
	public void noticeTimeUpdate(TimeEvent e);
}
