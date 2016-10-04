package user.schubert.richard;

import core.*;

/**
 * Evaluation of a chess position. An evaluation consists of double value representing the value of white's position and a double value representing
 * the value of black's position. Higher values are better.
 */
public abstract class PositionEvaluation
{
	protected Board board;
	protected double whiteValue;
	protected double blackValue;

	public PositionEvaluation(Board board)
	{
		this.board = board;
	}

	@Override
	public String toString()
	{
		return "white: " + whiteValue + "   |   black: " + blackValue;
	}

	public double getWhiteValue()
	{
		return whiteValue;
	}

	public double getBlackValue()
	{
		return blackValue;
	}
}
