package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class Scroll extends JPanel
                        implements ItemListener {
    private Rule columnView;
    private Rule rowView;
    private JToggleButton isMetric;
    private ScrollablePicture picture;

    public Scroll() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        //Get the image to use.
        ImageIcon scotlandYard = createImageIcon("images/LondonOriginal.jpeg");
       //ImageIcon scotlandYard = createImageIcon("images/youngdad.jpeg");
        //Create the row and column headers.
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);

        if (scotlandYard != null) {
            columnView.setPreferredWidth(scotlandYard.getIconWidth());
            rowView.setPreferredHeight(scotlandYard.getIconHeight());
        } else {
            columnView.setPreferredWidth(1200);
            rowView.setPreferredHeight(1000);
        }

        //Create the corners.
        JPanel buttonCorner = new JPanel(); //use FlowLayout
        isMetric = new JToggleButton("Rand", true);
        isMetric.setFont(new Font("SansSerif", Font.PLAIN, 8));
        isMetric.setMargin(new Insets(2,2,2,2));
        isMetric.addItemListener(this);
        buttonCorner.add(isMetric); 

        //Set up the scroll pane.
        picture = new ScrollablePicture(scotlandYard, columnView.getIncrement());
        JScrollPane pictureScrollPane = new JScrollPane(picture);
        pictureScrollPane.setPreferredSize(new Dimension(800, 750));
        pictureScrollPane.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));

        pictureScrollPane.setColumnHeaderView(columnView);
        pictureScrollPane.setRowHeaderView(rowView);

	//Set the corners.
        //In theory, to support internationalization you would change
        //UPPER_LEFT_CORNER to UPPER_LEADING_CORNER,
        //LOWER_LEFT_CORNER to LOWER_LEADING_CORNER, and
        //UPPER_RIGHT_CORNER to UPPER_TRAILING_CORNER.  In practice,
        //bug #4467063 makes that impossible (in 1.4, at least).
        pictureScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                                    buttonCorner);
        pictureScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER,
                                    new Corner());
        pictureScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER,
                                    new Corner());

        //Put it in this panel.
        add(pictureScrollPane);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            //Turn it to metric.
            rowView.setIsMetric(true);
            columnView.setIsMetric(true);
        } else {
            //Turn it to inches.
            rowView.setIsMetric(false);
            columnView.setIsMetric(false);
        }
        picture.setMaxUnitIncrement(rowView.getIncrement());
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Scroll.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ScotlandYard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Scroll();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}