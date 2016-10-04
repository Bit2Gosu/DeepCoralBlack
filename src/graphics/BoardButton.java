package graphics;

import javax.swing.Icon;
import javax.swing.JButton;

import core.SquareID;

public final class BoardButton extends JButton
{
	private static final long serialVersionUID = 1L;
	private final SquareID squareID;

	public BoardButton(Icon icon, SquareID squareID)
	{
		super(icon);
		this.squareID = squareID;
	}

	public SquareID getSquareID()
	{
		return squareID;
	}
}
