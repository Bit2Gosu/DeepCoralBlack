package user.schubert.richard;

import java.util.*;

import core.*;

public class PieceDefaultValueEvaluation extends PositionEvaluation
{
	public PieceDefaultValueEvaluation(Board board)
	{
		super(board);
		super.whiteValue = getValue(ChessColor.WHITE);
		super.blackValue = getValue(ChessColor.BLACK);
	}

	private double getValue(ChessColor color)
	{
		Set<Square> squares = board.getSquaresWithPiecesOfColor(color);
		return squares.stream().mapToDouble(square -> square.getPiece().getDefaultValue()).sum();
	}
}
