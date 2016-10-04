package user;

import core.Game;

public class Brain
{
	private Player owner;
	private MoveThoughtsThread moveThoughtsThread;

	public Brain(Player owner)
	{
		this.owner = owner;
	}

	public void startThinking(Game game)
	{
		MoveThoughts moveThoughts = new MoveThoughts(game);
		moveThoughtsThread = new MoveThoughtsThread(moveThoughts, owner.getName());
		moveThoughtsThread.setMoveThoughts(moveThoughts);
		moveThoughtsThread.start();
	}

	public void requestThinkStop()
	{
		if (moveThoughtsThread != null && moveThoughtsThread.isAlive())
		{
			moveThoughtsThread.setThinkStopRequested(true);
		}
	}

	public void requestPause()
	{
		if (moveThoughtsThread != null)
		{
			moveThoughtsThread.setThinkPauseRequested(true);
		}
	}

	public void requestUnpause()
	{
		if (moveThoughtsThread != null)
		{
			moveThoughtsThread.setThinkPauseRequested(false);
		}
	}

	public boolean isThinkingAboutMove()
	{
		return moveThoughtsThread != null && moveThoughtsThread.isThinking();
	}

	public boolean moveThoughtsArePaused()
	{
		return moveThoughtsThread != null && moveThoughtsThread.isPaused();
	}

	public boolean moveThoughtsAreStopped()
	{
		return moveThoughtsThread != null && !moveThoughtsThread.isAlive();
	}

}
