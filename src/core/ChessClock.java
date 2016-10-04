package core;

import java.util.HashSet;

import user.Player;
import core.events.PlayerTimeEvent;
import core.events.TimeEvent;

public final class ChessClock implements ExpirationClockListener
{
	private static long CLOCK_SLEEP_TIME = 100;

	private final ExpirationClock whiteClock;
	private final ExpirationClock blackClock;
	private final HashSet<ChessClockListener> listeners;

	public ChessClock(long timeWhite, long timeBlack)
	{
		whiteClock = new ExpirationClock(timeWhite);
		blackClock = new ExpirationClock(timeBlack);
		whiteClock.addListener(this);
		blackClock.addListener(this);
		listeners = new HashSet<ChessClockListener>();
	}

	public void addListener(ChessClockListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(ChessClockListener listener)
	{
		listeners.remove(listener);
	}

	public long getRemainingTime(ChessColor color)
	{
		if (color == ChessColor.WHITE)
		{
			return whiteClock.getRemainingTime();
		}
		return blackClock.getRemainingTime();
	}

	@Override
	public void noticeTimeUpdate(TimeEvent e)
	{
		ChessColor timedColor = (e.getSource() == whiteClock) ? ChessColor.WHITE : ChessColor.BLACK;
		PlayerTimeEvent playerTimeEvent = new PlayerTimeEvent(this, e.getRemainingTime(), timedColor);
		fireEvent(playerTimeEvent);
	}

	/**
	 * Stops both white's and black's time (places the switch in the neutral position).
	 */
	protected void stop()
	{
		whiteClock.stop();
		blackClock.stop();
	}

	protected void push(Player pushingPlayer)
	{
		if (pushingPlayer.getColor() == ChessColor.BLACK)
		{
			blackClock.stop();
			whiteClock.start(CLOCK_SLEEP_TIME);
		}
		else
		{
			whiteClock.stop();
			blackClock.start(CLOCK_SLEEP_TIME);
		}
	}

	private void fireEvent(PlayerTimeEvent e)
	{
		if (listeners.size() != 0)
		{
			for (ChessClockListener listener : listeners)
			{
				listener.noticeTimeUpdate(e);
			}
		}
	}
}
