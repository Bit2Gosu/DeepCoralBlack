package core;

import java.util.*;

import core.pieces.*;

/**
 * An object of this class holds information about the moves of a game.
 */
public final class GameCourse
{
	private final ArrayList<MoveInfo> course;

	public GameCourse()
	{
		course = new ArrayList<MoveInfo>();
	}

	public GameCourse(GameCourse gameCourse, Move move, long remainingTime)
	{
		course = new ArrayList<>(gameCourse.course);
		course.add(new MoveInfo(move, remainingTime));
	}

	/**
	 * Analyses the game course to calculate the list of captured pieces in chronological order.
	 * 
	 * @return the captured pieces in chronological order
	 */
	public List<Piece> getCapturedPieces()
	{
		List<Piece> capturedPieces = new ArrayList<>();

		course.stream().forEach(moveInfo ->
		{
			if (moveInfo.isCapture())
			{
				if (!moveInfo.isEnPassant())
				{
					capturedPieces.add(moveInfo.getToPiece());
				}
				else
				{
					capturedPieces.add(new Pawn(moveInfo.getFromPiece().getColor().getOpposite()));
				}
			}
		});

		return capturedPieces;
	}

	/**
	 * Returns information about the nth move of the game. The first move has the index 0.
	 * 
	 * @param index the index of the move
	 * @return information about the moves
	 * 
	 * @throws IllegalArgumentException if the index is invalid
	 */
	public MoveInfo getMoveInfo(int index)
	{
		if ((index >= 0) && (index < course.size()))
		{
			return course.get(index);
		}
		throw new IllegalArgumentException("Illegal MoveInfo index: " + index);
	}

	/**
	 * Returns information about the latest move of the game.
	 * 
	 * @return information about the latest move of the game or null, if no moves have been made yet
	 * 
	 */
	public MoveInfo getLastMoveInfo()
	{
		if (course.size() > 0)
		{
			return course.get(course.size() - 1);
		}
		return null;
	}

	/**
	 * Returns the number of moves made in the game thus far.
	 * 
	 * @return the number of moves made in the game thus far
	 */
	public int getMoveCount()
	{
		return course.size();
	}

	/**
	 * Returns a deep copy of this game course.
	 * 
	 * @return a deep copy of this game course
	 */
	@Override
	public GameCourse clone()
	{
		GameCourse clone = new GameCourse();
		for (int i = 0; i < course.size(); i++)
		{
			clone.course.add(i, course.get(i).clone());
		}
		return clone;
	}

	@Override
	public String toString()
	{
		String result = "---------------------" + "\n";
		for (MoveInfo moveInfo : course)
		{
			result += moveInfo.toString() + "\n";
		}
		return result + "---------------------";
	}

	/**
	 * Adds a new move to this game course. The move is regarded as the newest move thus far. This method must be called before the move is executed
	 * so that information about the affected squares and their pieces can be preserved.
	 * 
	 * @param move the new move
	 * @param remainingTime the time remaining for the executor of the move
	 */
	public void addMoveInfo(Move move, long remainingTime)
	{
		course.add(new MoveInfo(move, remainingTime));
	}
}
