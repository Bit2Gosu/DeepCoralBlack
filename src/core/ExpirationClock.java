package core;

import java.util.HashSet;

import core.events.TimeEvent;

public final class ExpirationClock implements Runnable
{
	private long remainingTime;
	private Thread timeThread;
	private long sleepingSpan;
	private boolean supposedToStop;
	private final HashSet<ExpirationClockListener> listeners;

	public ExpirationClock(long remainingTime)
	{
		this.remainingTime = remainingTime;
		listeners = new HashSet<ExpirationClockListener>();
	}

	public boolean isRunning()
	{
		return timeThread.isAlive();
	}

	public long getRemainingTime()
	{
		return remainingTime;
	}

	public void addListener(ExpirationClockListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(ExpirationClockListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void run()
	{
		while (!supposedToStop)
		{
			try
			{
				Thread.sleep(sleepingSpan);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			remainingTime -= sleepingSpan;
			TimeEvent timeEvent = new TimeEvent(this, remainingTime);
			fireEvent(timeEvent);
		}
	}

	protected boolean start(long sleepingSpan)
	{
		if ((timeThread == null) || !timeThread.isAlive())
		{
			supposedToStop = false;
			timeThread = new Thread(this, "ExpirationClock");
			this.sleepingSpan = sleepingSpan;
			timeThread.start();
			return true;
		}
		return false;
	}

	protected boolean stop()
	{
		if ((timeThread != null) && timeThread.isAlive())
		{
			supposedToStop = true;
			return true;
		}
		return false;
	}

	protected void setRemainingTime(long remainingTime)
	{
		if (!timeThread.isAlive())
		{
			this.remainingTime = remainingTime;
		}
	}

	private void fireEvent(TimeEvent e)
	{
		if (listeners.size() != 0)
		{
			for (ExpirationClockListener listener : listeners)
			{
				listener.noticeTimeUpdate(e);
			}
		}
	}
}
