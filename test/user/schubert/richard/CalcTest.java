package user.schubert.richard;

import static core.ChessColor.*;

import java.util.*;

import org.junit.*;

import user.*;
import core.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class CalcTest
{
	private Calc calc;
	private Game game;
	private Board board;

	@Before
	public void setUp()
	{

		Player whitePlayer = new Player("Markus", WHITE);
		Player blackPlayer = new Player("Paul", BLACK);
		long playerTime = 1000 * 60 * 60; // 1 Stunde
		game = new Game(whitePlayer, blackPlayer, playerTime, playerTime);
		board = game.board;
		calc = new Calc(game);

		game.start();
	}

	@Test
	public void testGetPossibleMoves()
	{

		assertThat(calc.getPossibleMoves(BLACK).isEmpty(), is(true));

		Set<Move> possibleWhiteMoves = calc.getPossibleMoves(WHITE);

		Set<Move> correctPossibleWhiteMoves = new HashSet<>();
		correctPossibleWhiteMoves.add(new Move(board, "a2", "a3"));
		correctPossibleWhiteMoves.add(new Move(board, "a2", "a4"));
		correctPossibleWhiteMoves.add(new Move(board, "b2", "b3"));
		correctPossibleWhiteMoves.add(new Move(board, "b2", "b4"));
		correctPossibleWhiteMoves.add(new Move(board, "c2", "c3"));
		correctPossibleWhiteMoves.add(new Move(board, "c2", "c4"));
		correctPossibleWhiteMoves.add(new Move(board, "d2", "d3"));
		correctPossibleWhiteMoves.add(new Move(board, "d2", "d4"));
		correctPossibleWhiteMoves.add(new Move(board, "e2", "e3"));
		correctPossibleWhiteMoves.add(new Move(board, "e2", "e4"));
		correctPossibleWhiteMoves.add(new Move(board, "f2", "f3"));
		correctPossibleWhiteMoves.add(new Move(board, "f2", "f4"));
		correctPossibleWhiteMoves.add(new Move(board, "g2", "g3"));
		correctPossibleWhiteMoves.add(new Move(board, "g2", "g4"));
		correctPossibleWhiteMoves.add(new Move(board, "h2", "h3"));
		correctPossibleWhiteMoves.add(new Move(board, "h2", "h4"));
		correctPossibleWhiteMoves.add(new Move(board, "b1", "a3"));
		correctPossibleWhiteMoves.add(new Move(board, "b1", "c3"));
		correctPossibleWhiteMoves.add(new Move(board, "g1", "h3"));
		correctPossibleWhiteMoves.add(new Move(board, "g1", "f3"));

		assertThat(possibleWhiteMoves.equals(correctPossibleWhiteMoves), is(true));

		game.update(game.getWhitePlayer(), new Move(board, "d2", "d4"));

		assertThat(calc.getPossibleMoves(BLACK).contains(new Move(board, "d7", "d5")), is(true));
	}

	@Test
	public void testGetPieceDefaultValueEvaluation()
	{

		PositionEvaluation gameEvaluation = new PieceDefaultValueEvaluation(game);
		assertThat(gameEvaluation.getBlackValue() == gameEvaluation.getWhiteValue(), is(true));

		game.update(game.getPlayerToMove(), new Move(board, "d2", "d4"));
		game.update(game.getPlayerToMove(), new Move(board, "e7", "d5"));
		game.update(game.getPlayerToMove(), new Move(board, "d4", "e5"));

		gameEvaluation = new PieceDefaultValueEvaluation(game);

		assertThat(gameEvaluation.getBlackValue() < gameEvaluation.getWhiteValue(), is(true));
	}
}
