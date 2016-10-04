package user;

public class MoveThoughtsThread extends Thread
{
	private MoveThoughts moveThoughts;

	public MoveThoughtsThread(MoveThoughts moveThoughts, String playerName)
	{
		super(moveThoughts, playerName);
	}

	public void setMoveThoughts(MoveThoughts moveThoughts)
	{
		this.moveThoughts = moveThoughts;
	}

	public void setThinkStopRequested(boolean thinkStopRequested)
	{
		moveThoughts.setThinkStopRequested(thinkStopRequested);
	}

	public void setThinkPauseRequested(boolean thinkPauseRequested)
	{
		moveThoughts.setThinkPauseRequested(thinkPauseRequested);
	}

	public boolean isThinkStopRequested()
	{
		return moveThoughts.isThinkStopRequested();
	}

	public boolean isThinkPauseRequested()
	{
		return moveThoughts.isThinkPauseRequested();
	}

	public boolean isThinking()
	{
		return moveThoughts.isThinking();
	}

	public boolean isPaused()
	{
		return moveThoughts.isPaused();
	}

}
