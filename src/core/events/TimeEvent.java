package core.events;

import java.util.EventObject;

public class TimeEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	private final long remainingTime;

	public TimeEvent(Object source, long remainingTime)
	{
		super(source);
		this.remainingTime = remainingTime;
	}

	public long getRemainingTime()
	{
		return remainingTime;
	}
}
