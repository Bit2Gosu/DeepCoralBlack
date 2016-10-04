package core;

import user.Player;
import core.events.BoardEvent;
import core.exception.RuleViolation;

public final class Game
{

	private final Player whitePlayer;
	private final Player blackPlayer;
	private boolean isBeingPlayed;
	private boolean isPaused;
	private ChessColor colorToMove;
	private PromotionChoice pawnTradeChoice;
	private BoardListener listener;

	public Board board;
	public ChessClock chessClock;
	public GameCourse gameCourse;
	public GameCalc gameCalc;

	public Game(Player whitePlayer, Player blackPlayer, long timeWhite, long timeBlack)
	{
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		board = new Board(true);
		gameCourse = new GameCourse();
		chessClock = new ChessClock(timeWhite, timeBlack);
		gameCalc = new GameCalc(board, gameCourse);
	}

	public void start()
	{
		colorToMove = ChessColor.WHITE;
		chessClock.push(blackPlayer);
		whitePlayer.noticeGameHasStarted(this);
		blackPlayer.noticeGameHasStarted(this);
		isBeingPlayed = true;
	}

	public void end()
	{
		if (isBeingPlayed)
		{
			whitePlayer.noticeGameHasEnded();
			blackPlayer.noticeGameHasEnded();
			chessClock.stop();
			isBeingPlayed = false;
		}
	}

	public void pause()
	{
		whitePlayer.requestPause();
		blackPlayer.requestPause();
		chessClock.stop();
		isPaused = true;
	}

	public void unpause()
	{
		whitePlayer.requestUnpause();
		blackPlayer.requestUnpause();
		if (colorToMove == ChessColor.WHITE)
		{
			chessClock.push(blackPlayer);
		}
		else
		{
			chessClock.push(whitePlayer);
		}
		isPaused = false;
	}

	public Player getPlayerToMove()
	{
		if (getColorToMove() == ChessColor.WHITE)
		{
			return whitePlayer;
		}
		return blackPlayer;
	}

	public Player getWhitePlayer()
	{
		return whitePlayer;
	}

	public Player getBlackPlayer()
	{
		return blackPlayer;
	}

	public ChessColor getColorToMove()
	{
		return colorToMove;
	}

	public boolean isBeingPlayed()
	{
		return isBeingPlayed;
	}

	public boolean isPaused()
	{
		return isPaused;
	}

	public void update(Player player, Move move)
	{
		if (player.getColor() != colorToMove)
		{
			throw new RuleViolation("Player " + player + " tried to make a move although its not his turn.");
		}

		long playerRemainingTime = chessClock.getRemainingTime(player.getColor());
		gameCourse.addMoveInfo(move, playerRemainingTime);
		colorToMove = colorToMove.getOpposite();
		if (GameCalc.isCastling(move))
		{
			board.makeCastlingMove(move);
		}
		else if (GameCalc.isPromotion(move))
		{
			BoardEvent e = new BoardEvent(this, move.getTo().getColor(), move.getFrom().getPiece().getColor(), pawnTradeChoice);
			PromotionChoice choice = fireBoardEvent(e);
			board.makePawnTradeMove(move, choice);
		}
		else if (GameCalc.isEnPassant(move))
		{
			board.makeEnPassantMove(move);
		}
		else
		{
			board.makeNormalMove(move);
		}
		chessClock.push(player);
	}

	// protected because not everyone may construct choice answers
	protected void addBoardListener(BoardListener listener)
	{
		this.listener = listener;
	}

	protected void removeBoardListener(BoardListener listener)
	{
		this.listener = null;
	}

	// throws an exception if there is no listener - that is if there is no one
	// who can choose the piece. there is max 1 listener because only 1 thing is
	// allowed to construct the choice answer in his pawnReachedBaseLine method.
	private PromotionChoice fireBoardEvent(BoardEvent e)
	{
		return listener.pawnReachedBaseline(e);
	}
}
