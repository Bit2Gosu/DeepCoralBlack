package user;

import core.ChessColor;
import core.Game;

public final class Player
{
	private final String name;
	private final ChessColor color;
	private final Brain brain;

	public Player(String name, ChessColor color)
	{
		this.name = name;
		this.color = color;
		brain = new Brain(this);
	}

	public String getName()
	{
		return name;
	}

	public ChessColor getColor()
	{
		return color;
	}

	public void noticeGameHasStarted(Game game)
	{
		brain.startThinking(game);
	}

	public void noticeGameHasEnded()
	{
		brain.requestThinkStop();
	}

	public Brain getBrain()
	{
		return brain;
	}

	public void requestPause()
	{
		brain.requestPause();
	}

	public void requestUnpause()
	{
		brain.requestUnpause();
	}

}
