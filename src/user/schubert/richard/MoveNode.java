package user.schubert.richard;

import java.util.*;

import core.*;

public class MoveNode
{
	private MoveNode parent;
	private Set<MoveNode> children;

	private Move move;
	private Board boardBeforeMove;
	private Board boardAfterMove;
	private PositionEvaluation evaluationAfterMove;
	private ChessColor movingPlayerColor;

	public MoveNode(Board boardAfterMove, ChessColor movingPlayerColor, PositionEvaluator positionEvaluator)
	{
		children = new HashSet<>();
		this.boardAfterMove = boardAfterMove;
		this.movingPlayerColor = movingPlayerColor;
		evaluationAfterMove = positionEvaluator.getPositionEvaluation(boardAfterMove);
	}

	public MoveNode(MoveNode parent, Board boardBeforeMove, Move move, PositionEvaluator positionEvaluator)
	{
		this.parent = parent;
		this.move = move;
		this.boardBeforeMove = boardBeforeMove;

		movingPlayerColor = move.getFrom().getPiece().getColor();
		children = new HashSet<>();
		boardAfterMove = boardBeforeMove.clone();
		Calc.makeMove(move, boardAfterMove);
		evaluationAfterMove = positionEvaluator.getPositionEvaluation(boardAfterMove);
	}

	public void addChild(Board boardBeforeMove, Move move, PositionEvaluator positionEvaluator)
	{
		MoveNode childMoveNode = new MoveNode(this, boardBeforeMove, move, positionEvaluator);
		children.add(childMoveNode);
	}

	public ChessColor getMovingPlayerColor()
	{
		return movingPlayerColor;
	}

	public MoveNode getParent()
	{
		return parent;
	}

	public Set<MoveNode> getChildren()
	{
		return children;
	}

	public Move getMove()
	{
		return move;
	}

	public Board getBoardBeforeMove()
	{
		return boardBeforeMove;
	}

	public Board getBoardAfterMove()
	{
		return boardAfterMove;
	}

	public PositionEvaluation getEvaluationAfterMove()
	{
		return evaluationAfterMove;
	}
}
