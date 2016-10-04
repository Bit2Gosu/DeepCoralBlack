package project;

import graphics.MainView;
import user.Player;
import core.ChessColor;
import core.Game;
import core.MainViewController;
import core.WindowController;

public final class Starter
{
	public static void main(String[] args)
	{
		new Starter();
	}

	public Starter()
	{
		Player whitePlayer = new Player("Dominik", ChessColor.WHITE);
		Player blackPlayer = new Player("Eve", ChessColor.BLACK);
		Game game = new Game(whitePlayer, blackPlayer, 20 * 60 * 1000, 20 * 60 * 1000);
		MainView gui = new MainView();
		WindowController windowController = new WindowController(game);
		MainViewController buttonController = new MainViewController(gui, game, 1000);
		gui.setWindowController(windowController);
		gui.setButtonController(buttonController);
		long whiteStartTime = game.chessClock.getRemainingTime(ChessColor.WHITE);
		long blackStartTime = game.chessClock.getRemainingTime(ChessColor.BLACK);
		gui.createAndShow(game.board, whiteStartTime, blackStartTime);
		game.start();
	}
}
