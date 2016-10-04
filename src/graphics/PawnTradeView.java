package graphics;

import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import core.MainViewController;
import core.PromotionChoice;
import core.ChessColor;

public final class PawnTradeView
{
	private JDialog dialog;
	private JPanel panel;
	private ChoiceButton[] choiceButtons;
	private final MainViewController buttonController;
	private final ChessColor squareColor;
	private final ChessColor pieceColor;
	private final JFrame owner;

	public PawnTradeView(JFrame owner, MainViewController buttonController, ChessColor squareColor, ChessColor pieceColor)
	{
		this.buttonController = buttonController;
		this.squareColor = squareColor;
		this.pieceColor = pieceColor;
		this.owner = owner;
	}

	public void createAndShow()
	{
		dialog = new JDialog(owner, true);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		panel = new JPanel();
		choiceButtons = new ChoiceButton[PromotionChoice.values().length];
		String pathStart = "src/images/";
		String[] paths = new String[PromotionChoice.values().length];

		for (int i = 0; i < PromotionChoice.values().length; i++)
		{
			String type = PromotionChoice.values()[i].toString().toLowerCase();
			paths[i] = pathStart + type + "_" + pieceColor.getLabel() + "_" + squareColor.getLabel() + ".png";
		}
		try
		{
			for (int i = 0; i < choiceButtons.length; i++)
			{
				choiceButtons[i] = new ChoiceButton(new ImageIcon(ImageIO.read(new File(paths[i]))), PromotionChoice.values()[i]);
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (ChoiceButton choiceButton : choiceButtons)
		{
			choiceButton.setMargin(new Insets(0, 0, 0, 0));
			choiceButton.setActionCommand("pawnTradeChoiceView");
			choiceButton.addActionListener(buttonController);
			panel.add(choiceButton);
		}
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public boolean close()
	{
		if (dialog.isShowing())
		{
			dialog.dispose();
			return true;
		}
		return false;
	}
}
