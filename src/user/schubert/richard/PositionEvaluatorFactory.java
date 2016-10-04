package user.schubert.richard;

import core.*;

public class PositionEvaluatorFactory
{
	public static PositionEvaluator createPieceDefaultValueEvaluator()
	{
		return new PositionEvaluator()
		{
			@Override
			public PositionEvaluation getPositionEvaluation(Board board)
			{
				return new PieceDefaultValueEvaluation(board);
			}
		};
	}
}
