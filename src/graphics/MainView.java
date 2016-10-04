package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import support.TimeTool;
import core.Board;
import core.ChessColor;
import core.GameCourse;
import core.MainViewController;
import core.Piece;
import core.Square;
import core.SquareID;
import core.WindowController;

public final class MainView
{
	private static String PAUSE_TEXT = "pause";
	private static String CONT_TEXT = "continue";
	private static String STOP_AI_TEXT = "stop AI";
	private static String START_AI_TEXT = "start AI";

	private JFrame frame;
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	private JLabel whiteTimeLabel;
	private JLabel blackTimeLabel;
	private JButton pauseContButton;
	private JButton stopStartAIButton;
	private JButton gameCourseButton;
	private BoardButton[][] boardButtons;
	private BoardButton selectedBoardButton;
	private HashSet<BoardButton> bluedBoardButtons;
	private WindowController windowController;
	private MainViewController buttonController;
	private PawnTradeView pawnTradeView;
	private GameInfoView gameInfoView;
	private TimeTool timeTool;

	public void setWindowController(WindowController windowController)
	{
		this.windowController = windowController;
	}

	public void setButtonController(MainViewController buttonController)
	{
		this.buttonController = buttonController;
	}

	public BoardButton getSelectedBoardButton()
	{
		return selectedBoardButton;
	}

	public HashSet<BoardButton> getBluedBoardButtons()
	{
		return bluedBoardButtons;
	}

	public boolean createAndShow(Board board, long whiteStartTime, long blackStartTime)
	{
		if ((windowController == null) || (buttonController == null))
		{
			return false;
		}
		timeTool = new TimeTool();
		frame = new JFrame();
		bluedBoardButtons = new HashSet<BoardButton>();
		frame.setTitle("Richard wins!");
		frame.addWindowListener(windowController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildTopPanel();
		buildMiddlePanel(board);
		buildBottomPanel();
		setTimeLabel(ChessColor.WHITE, whiteStartTime);
		setTimeLabel(ChessColor.BLACK, blackStartTime);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(middlePanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return true;
	}

	public void updateMiddlePanel(Board board)
	{
		frame.remove(middlePanel);
		buildMiddlePanel(board);
		frame.add(middlePanel, BorderLayout.CENTER);
	}

	public void selectButton(BoardButton button, Set<Square> SquaresToBlue)
	{
		if (button != selectedBoardButton)
		{
			reddenButton(button);
			for (Square squareToBlue : SquaresToBlue)
			{
				SquareID squareID = squareToBlue.getID();
				BoardButton buttonToBlue = boardButtons[squareID.getColumn() - 1][squareID.getRow() - 1];
				blueButton(buttonToBlue);
			}
			selectedBoardButton = button;
		}
	}

	public boolean pauseTextDisplayed()
	{
		return pauseContButton.getText() == PAUSE_TEXT;
	}

	public void switchPauseContButton()
	{
		if (pauseContButton.getText() == PAUSE_TEXT)
		{
			pauseContButton.setText(CONT_TEXT);
		}
		else
		{
			pauseContButton.setText(PAUSE_TEXT);
		}
	}

	public void switchStopStartAIButton()
	{
		if (stopStartAIButton.getText() == STOP_AI_TEXT)
		{
			stopStartAIButton.setText(START_AI_TEXT);
		}
		else
		{
			stopStartAIButton.setText(STOP_AI_TEXT);
		}
	}

	public void setTimeLabel(ChessColor color, long milliseconds)
	{
		JLabel label = (color == ChessColor.WHITE) ? whiteTimeLabel : blackTimeLabel;

		label.setText(timeTool.getDisplayText(milliseconds));
	}

	public void unselectButton(BoardButton button)
	{
		if (button == selectedBoardButton)
		{
			button.setBorder(BorderFactory.createEmptyBorder());
			selectedBoardButton = null;
			unblueButtons();
		}
	}

	public void createAndShowPawnTradeView(ChessColor squareColor, ChessColor pieceColor)
	{
		pawnTradeView = new PawnTradeView(frame, buttonController, squareColor, pieceColor);
		pawnTradeView.createAndShow();
	}

	public void createAndShowGameInfoView(GameCourse course, List<Piece> captPieces)
	{
		gameInfoView = new GameInfoView(frame);
		gameInfoView.createAndShow(course, captPieces);
	}

	public boolean closePawnTradeChoiceView()
	{
		if (pawnTradeView != null)
		{
			pawnTradeView.close();
			pawnTradeView = null;
			return true;
		}
		return false;
	}

	public boolean closeGameInfoView()
	{
		if (gameInfoView != null)
		{
			gameInfoView.close();
			gameInfoView = null;
			return true;
		}
		return false;
	}

	private void reddenButton(BoardButton buttonToRedden)
	{
		buttonToRedden.setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	private void blueButton(BoardButton buttonToBlue)
	{
		buttonToBlue.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		bluedBoardButtons.add(buttonToBlue);
	}

	private void unblueButtons()
	{
		for (BoardButton bluedButton : bluedBoardButtons)
		{
			bluedButton.setBorder(BorderFactory.createEmptyBorder());
		}
		bluedBoardButtons.clear();
	}

	private void buildTopPanel()
	{
		topPanel = new JPanel();
		pauseContButton = new JButton(PAUSE_TEXT);
		stopStartAIButton = new JButton(STOP_AI_TEXT);
		blackTimeLabel = new JLabel();
		GridBagConstraints constr = new GridBagConstraints();

		topPanel.setLayout(new GridBagLayout());
		topPanel.setBackground(Color.WHITE);
		pauseContButton.setActionCommand("pauseCont");
		stopStartAIButton.setActionCommand("stopStart");
		pauseContButton.addActionListener(buttonController);
		stopStartAIButton.addActionListener(buttonController);
		constr.fill = GridBagConstraints.BOTH;
		constr.weightx = 1;
		constr.gridy = 0;
		constr.gridx = 0;
		topPanel.add(pauseContButton, constr);
		constr.gridx = 1;
		topPanel.add(stopStartAIButton, constr);
		constr.gridx = 2;
		topPanel.add(new JLabel("black time:"), constr);
		constr.gridx = 3;
		topPanel.add(blackTimeLabel, constr);
	}

	private void buildMiddlePanel(Board board)
	{
		middlePanel = new JPanel();
		boardButtons = new BoardButton[Board.COLUMN_IDS.length][Board.ROW_IDS.length];
		LayoutManager middleLayout = new GridLayout(Board.ROW_IDS.length + 2, Board.COLUMN_IDS.length + 2);
		middlePanel.setLayout(middleLayout);
		/*
		 * fill top row
		 */
		middlePanel.add(new JLabel(""));
		for (int column = 1; column <= Board.COLUMN_IDS.length; column++)
		{
			JLabel label = new JLabel(Board.COLUMN_IDS[column - 1]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			middlePanel.add(label);
		}
		middlePanel.add(new JLabel(""));
		for (int row = Board.ROW_IDS.length; row >= 1; row--)
		{
			/*
			 * add left digit to row
			 */
			JLabel labelLeft = new JLabel(String.valueOf(Board.ROW_IDS[row - 1]));
			labelLeft.setHorizontalAlignment(SwingConstants.CENTER);
			labelLeft.setVerticalAlignment(SwingConstants.CENTER);
			middlePanel.add(labelLeft);
			/*
			 * fill row with squares
			 */
			for (int column = 1; column <= Board.COLUMN_IDS.length; column++)
			{
				Square square = board.getSquare(new SquareID(column, row));
				Icon icon = square.getIcon();
				SquareID squareID = new SquareID(column, row);
				boardButtons[column - 1][row - 1] = new BoardButton(icon, squareID);
				Border border = BorderFactory.createEmptyBorder();
				boardButtons[column - 1][row - 1].setBorder(border);
				boardButtons[column - 1][row - 1].setActionCommand("board");
				boardButtons[column - 1][row - 1].addActionListener(buttonController);
				middlePanel.add(boardButtons[column - 1][row - 1]);
			}
			/*
			 * add right digit to row
			 */
			JLabel labelRight = new JLabel(String.valueOf(Board.ROW_IDS[row - 1]));
			labelRight.setHorizontalAlignment(SwingConstants.CENTER);
			labelRight.setVerticalAlignment(SwingConstants.CENTER);
			middlePanel.add(labelRight);
		}
		/*
		 * fill bottom row
		 */
		middlePanel.add(new JLabel(""));
		for (int column = 1; column <= Board.COLUMN_IDS.length; column++)
		{
			JLabel label = new JLabel(Board.COLUMN_IDS[column - 1]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			middlePanel.add(label);
		}
		middlePanel.add(new JLabel(""));
	}

	private void buildBottomPanel()
	{
		bottomPanel = new JPanel();
		gameCourseButton = new JButton("game course");
		whiteTimeLabel = new JLabel();
		GridBagConstraints constr = new GridBagConstraints();

		bottomPanel.setLayout(new GridBagLayout());
		bottomPanel.setBackground(Color.WHITE);
		gameCourseButton.setActionCommand("gameCourse");
		gameCourseButton.addActionListener(buttonController);
		constr.fill = GridBagConstraints.BOTH;
		constr.weightx = 1;
		constr.gridy = 0;
		constr.gridx = 0;
		bottomPanel.add(gameCourseButton, constr);
		constr.gridx = 1;
		bottomPanel.add(new JLabel("white time:"), constr);
		constr.gridx = 2;
		bottomPanel.add(whiteTimeLabel, constr);
	}
}
