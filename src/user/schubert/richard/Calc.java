package user.schubert.richard;

import java.util.*;

import core.*;

public class Calc
{
	public static Set<Move> getPossibleMoves(Board board, GameCourse gameCourse, ChessColor playerColor)
	{

		GameCalc gameCalc = new GameCalc(board, gameCourse);
		Set<Square> myPiecesSquares = board.getSquaresWithPiecesOfColor(playerColor);

		Set<Move> moves = new HashSet<>();

		myPiecesSquares.stream().forEach(square ->
		{

			gameCalc.getTargetSquares(square).forEach(targetSquare ->
			{
				moves.add(new Move(square, targetSquare));
			});
		});

		return moves;
	}

	public static Move getBestMove(Board board, GameCourse gameCourse, ChessColor playerToMove)
	{

		PositionEvaluator pieceDefaultValueEvaluator = PositionEvaluatorFactory.createPieceDefaultValueEvaluator();

		LinkedList<MoveNode> moveNodeQueue = new LinkedList<>();
		MoveNode pastMoveNode = new MoveNode(board, playerToMove.getOpposite(), pieceDefaultValueEvaluator);
		moveNodeQueue.add(pastMoveNode);

		while (true)
		{
			MoveNode lastMoveNode = moveNodeQueue.pop();

			ChessColor movingPlayerColor = lastMoveNode.getMovingPlayerColor().getOpposite();
			Set<Move> possibleMoves = getPossibleMoves(lastMoveNode.getBoardAfterMove(), gameCourse, lastMoveNode.getMovingPlayerColor());

			possibleMoves.stream().forEach(move ->
			{
				lastMoveNode.addChild(lastMoveNode.getBoardAfterMove(), move, pieceDefaultValueEvaluator);
			});
		}

		Set<MoveNode> moveNodes = new HashSet<>();

		possibleMoves.stream().forEach(move ->
		{
			MoveNode moveNode = new MoveNode(board, move, pieceDefaultValueEvaluator);
			moveNodes.add(moveNode);
		});

		moveNodes.stream().forEach(moveNode ->
		{
			Set<Move> possibleMoves2 = getPossibleMoves(playerColor.getOpposite());

		});

		return null;
	}

	/* ------------------------------------------------ STATIC METHODS ------------------------------------------------ */

	protected static void makeMove(Move move, Board board)
	{
		if (GameCalc.isCastling(move))
		{
			board.makeCastlingMove(move);
		}
		else if (GameCalc.isPromotion(move))
		{
			board.makePawnTradeMove(move, PromotionChoice.QUEEN);
		}
		else if (GameCalc.isEnPassant(move))
		{
			board.makeEnPassantMove(move);
		}
		else
		{
			board.makeNormalMove(move);
		}
	}

}
