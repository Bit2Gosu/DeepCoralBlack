package core;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconFactory
{

	public static ImageIcon createIcon(ChessColor pieceColor, ChessColor squareColor, Piece.Type pieceType)
	{
		try
		{
			return new ImageIcon(ImageIO.read(new File("src/images/" + pieceType.getLabel() + "_" + pieceColor.getLabel() + "_" + squareColor
					.getLabel() + ".png")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(pieceType.getLabel() + " " + pieceColor.getLabel() + " " + squareColor.getLabel(), e);
		}
	}

	public static ImageIcon createEmptySquareIcon(ChessColor squareColor)
	{
		try
		{
			return new ImageIcon(ImageIO.read(new File("src/images/empty_" + squareColor.getLabel() + ".png")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
