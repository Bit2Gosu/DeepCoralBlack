package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import core.ChessColor;
import core.GameCourse;
import core.MoveInfo;
import core.Piece;

public final class GameInfoView
{
	private static int COURSE_COLS = 5;
	private static int COURSE_HGAP = 50;
	private static int COURSE_VGAP = 5;
	private static int MAX_COURSE_HEIGHT = 500;

	private JDialog dialog;
	private final JFrame owner;
	private JPanel captPiecesPanel;
	private JPanel captWhitePiecesPanel;
	private JPanel captBlackPiecesPanel;
	private JScrollPane courseScrollPane;
	private JPanel coursePanel;
	private JPanel coursePanelHead;
	private JPanel coursePanelBottom;

	public GameInfoView(JFrame owner)
	{
		this.owner = owner;
	}

	public void createAndShow(GameCourse course, List<Piece> captPieces)
	{
		dialog = new JDialog(owner, true);
		captPiecesPanel = new JPanel();
		captWhitePiecesPanel = new JPanel();
		captBlackPiecesPanel = new JPanel();
		coursePanel = new JPanel();
		courseScrollPane = new JScrollPane();
		coursePanelHead = new JPanel();
		coursePanelBottom = new JPanel();

		/*
		 * dialog
		 */
		dialog.setLayout(new BorderLayout());
		dialog.add(courseScrollPane, BorderLayout.SOUTH);

		/*
		 * courseScrollPane
		 */
		courseScrollPane.setViewportView(coursePanel);
		courseScrollPane.setBorder(BorderFactory.createEmptyBorder());

		/*
		 * captPiecesPanel
		 */
		captPiecesPanel.setLayout(new BorderLayout());
		captPiecesPanel.setBackground(Color.WHITE);
		captPiecesPanel.add(captWhitePiecesPanel, BorderLayout.NORTH);
		captPiecesPanel.add(captBlackPiecesPanel, BorderLayout.SOUTH);
		captWhitePiecesPanel.setBackground(Color.WHITE);
		captBlackPiecesPanel.setBackground(Color.WHITE);
		for (int i = 0; i < captPieces.size(); i++)
		{
			Piece piece = captPieces.get(i);
			if (piece.getColor() == ChessColor.WHITE)
			{
				Icon icon = piece.getWhiteOnWhiteIcon();
				captWhitePiecesPanel.add(new JLabel(icon));
			}
			else
			{
				Icon icon = piece.getBlackOnWhiteIcon();
				captBlackPiecesPanel.add(new JLabel(icon));
			}
		}

		/*
		 * coursePanel
		 */
		int numOfMoves = course.getMoveCount();
		int numOfPlayerMoves = (int) Math.ceil(numOfMoves / 2.0);
		coursePanel.setLayout(new BorderLayout());
		coursePanel.add(coursePanelHead, BorderLayout.NORTH);
		coursePanel.add(coursePanelBottom, BorderLayout.SOUTH);

		/*
		 * coursePanelHead
		 */
		LayoutManager courseHeadLayout = new GridLayout(2, COURSE_COLS, COURSE_HGAP, COURSE_VGAP);
		coursePanelHead.setLayout(courseHeadLayout);
		coursePanelHead.add(new JLabel("no."));
		coursePanelHead.add(new JLabel("piece"));
		coursePanelHead.add(new JLabel("move"));
		coursePanelHead.add(new JLabel("piece"));
		coursePanelHead.add(new JLabel("move"));
		for (int i = 0; i < 5; i++)
		{
			coursePanelHead.add(new JLabel(""));
		}

		/*
		 * coursePanelBottom
		 */
		LayoutManager courseBottomLayout = new GridLayout(numOfPlayerMoves, COURSE_COLS, COURSE_HGAP, COURSE_VGAP);
		coursePanelBottom.setLayout(courseBottomLayout);
		coursePanelBottom.setBackground(Color.WHITE);
		for (int i = 0; i < numOfPlayerMoves; i++)
		{
			// no.
			coursePanelBottom.add(new JLabel(String.valueOf(i)));
			MoveInfo whiteMoveInfo = course.getMoveInfo(2 * i);
			// white piece
			String whitePieceName = whiteMoveInfo.getFromPiece().toString();
			coursePanelBottom.add(new JLabel(whitePieceName));
			// white move
			String whiteMoveText = getMoveText(whiteMoveInfo);
			coursePanelBottom.add(new JLabel(whiteMoveText));

			if ((2 * i + 1) < course.getMoveCount())
			{
				MoveInfo blackMoveInfo = course.getMoveInfo(2 * i + 1);

				// black piece
				String blackPieceName = blackMoveInfo.getFromPiece().toString();
				coursePanelBottom.add(new JLabel(blackPieceName));
				// black move
				String blackMoveText = getMoveText(blackMoveInfo);
				coursePanelBottom.add(new JLabel(blackMoveText));
			}
			else
			{
				coursePanelBottom.add(new JLabel(""));
				coursePanelBottom.add(new JLabel(""));
			}
		}

		if ((captWhitePiecesPanel.getComponentCount() > 0) || (captBlackPiecesPanel.getComponentCount() > 0))
		{
			dialog.add(captPiecesPanel, BorderLayout.NORTH);
		}
		if (courseScrollPane.getPreferredSize().height > MAX_COURSE_HEIGHT)
		{
			int currPrefWidth = courseScrollPane.getPreferredSize().width;
			courseScrollPane.setPreferredSize(new Dimension(MAX_COURSE_HEIGHT, currPrefWidth));
		}

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private String getMoveText(MoveInfo moveInfo)
	{
		if (moveInfo.isKingSideCastling())
		{
			return "cks"; // castling king's side
		}
		if (moveInfo.isKingSideCastling())
		{
			return "cqs"; // castling queen's side
		}
		String from = moveInfo.getFromSquareID().toString();
		String to = moveInfo.getToSquareID().toString();
		String connect = moveInfo.isCapture() ? " x " : " - ";
		return from + connect + to;
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
