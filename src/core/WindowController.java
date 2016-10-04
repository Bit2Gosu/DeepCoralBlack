package core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowController implements WindowListener
{
	private final Game game;

	public WindowController(Game game)
	{
		this.game = game;
	}

	@Override
	public void windowActivated(WindowEvent e)
	{

	}

	@Override
	public void windowClosed(WindowEvent e)
	{

	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		game.end();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{

	}

	@Override
	public void windowIconified(WindowEvent e)
	{

	}

	@Override
	public void windowOpened(WindowEvent e)
	{

	}
}
