package user;

import core.Game;

public class MoveThoughts implements Runnable
{
	private boolean thinkStopRequested;
	private boolean thinkPauseRequested;
	private boolean isThinking;
	private boolean isPaused;
	private Game game;

	public MoveThoughts(Game game)
	{
		this.game = game;
	}

	@Override
	public void run()
	{
		isThinking = true;

		isThinking = false;
	}

	public synchronized boolean isThinkStopRequested()
	{
		return thinkStopRequested;
	}

	public synchronized void setThinkStopRequested(boolean thinkStopRequested)
	{
		this.thinkStopRequested = thinkStopRequested;
	}

	public synchronized boolean isThinkPauseRequested()
	{
		return thinkPauseRequested;
	}

	public synchronized void setThinkPauseRequested(boolean thinkPauseRequested)
	{
		this.thinkPauseRequested = thinkPauseRequested;
	}

	/**
	 * Checks if the brain is currently thinking about the next move. This is not the case if the brain has realised that a think stop or think pause
	 * has been requested. This method can however return true if a think stop or pause has been requested but not yet realised.
	 * 
	 * @return True if the brain is currently thinking about the next move, otherwise false.
	 */
	public synchronized boolean isThinking()
	{
		return isThinking;
	}

	public synchronized boolean isPaused()
	{
		return isPaused;
	}

}
