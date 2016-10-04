package core;

import graphics.*;

import java.awt.event.*;
import java.util.*;

import core.events.*;

public class MainViewController implements ActionListener, BoardListener, ChessClockListener
{
	private final MainView mainView;
	private PromotionChoice upcomingChoice;
	private final Game game;
	private final long timeDisplayUpdateDist;
	private long lastTimeUpdate;

	public MainViewController(MainView view, Game game, long timeDisplayUpdateDist)
	{
		mainView = view;
		game.addBoardListener(this);
		this.game = game;
		this.timeDisplayUpdateDist = timeDisplayUpdateDist;
		game.chessClock.addListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "pauseCont")
		{
			if (mainView.pauseTextDisplayed())
			{
				game.pause();
			}
			else
			{
				game.unpause();
			}
			mainView.switchPauseContButton();
		}
		else if (e.getActionCommand() == "stopStart")
		{
			mainView.switchStopStartAIButton();
		}
		else if (e.getActionCommand() == "gameCourse")
		{
			GameCourse course = game.gameCourse;
			List<Piece> captPieces = game.gameCourse.getCapturedPieces();
			mainView.createAndShowGameInfoView(course, captPieces);
		}
		else if (e.getActionCommand() == "board")
		{
			BoardButton button = (BoardButton) e.getSource();
			Piece piece = game.board.getSquare(button.getSquareID()).getPiece();
			BoardButton selBoardButton = mainView.getSelectedBoardButton();

			if (selBoardButton == null)
			{
				// select square in ownership
				if ((piece != null) && (piece.getColor() == game.getColorToMove()))
				{
					selectButton(button);
				}
			}
			else
			{
				// select target square
				if ((piece == null) || (piece.getColor() != game.getColorToMove()))
				{
					Square selectedSquare = game.board.getSquare(selBoardButton.getSquareID());
					Square targetSquare = game.board.getSquare(button.getSquareID());
					Set<Square> possibleSquares = game.gameCalc.getTargetSquares(selectedSquare);
					if (possibleSquares.contains(targetSquare))
					{
						Move move = new Move(selectedSquare, targetSquare);
						game.update(game.getPlayerToMove(), move);
						mainView.unselectButton(selBoardButton);
						mainView.updateMiddlePanel(game.board);
					}
				}
				else
				{
					// unselect square in ownership
					if (button == selBoardButton)
					{
						mainView.unselectButton(button);
					}
					// select new square in ownership
					else
					{
						mainView.unselectButton(selBoardButton);
						selectButton(button);
					}
				}
			}
		}
		else if (e.getActionCommand() == "pawnTradeChoiceView")
		{
			ChoiceButton button = (ChoiceButton) e.getSource();
			upcomingChoice = button.getChoice();
			mainView.closePawnTradeChoiceView();
		}
	}

	@Override
	public PromotionChoice pawnReachedBaseline(BoardEvent e)
	{
		mainView.createAndShowPawnTradeView(e.getSquareColor(), e.getPieceColor());
		return upcomingChoice;
	}

	@Override
	public void noticeTimeUpdate(PlayerTimeEvent e)
	{
		long passedTime = System.currentTimeMillis() - lastTimeUpdate;
		if (passedTime >= timeDisplayUpdateDist)
		{
			mainView.setTimeLabel(e.getTimedColor(), e.getRemainingTime());

			lastTimeUpdate = System.currentTimeMillis();
		}
	}

	private void selectButton(BoardButton button)
	{
		Square square = game.board.getSquare(button.getSquareID());
		Set<Square> possibleSquares = game.gameCalc.getTargetSquares(square);
		mainView.selectButton(button, possibleSquares);
	}
}
