package graphics;

import javax.swing.Icon;
import javax.swing.JButton;

import core.PromotionChoice;

public final class ChoiceButton extends JButton
{
	private static final long serialVersionUID = 1L;
	private final PromotionChoice choice;

	public ChoiceButton(Icon icon, PromotionChoice choice)
	{
		super(icon);
		this.choice = choice;
	}

	public PromotionChoice getChoice()
	{
		return choice;
	}
}
