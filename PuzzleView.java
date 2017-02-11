import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.SpringLayout;



public class PuzzleView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	// NCOIC
	private NCOIC myNCOIC;

	// Cell size
	public final static int CELL_SIZE = 60;
	
	// background color
	private final Color BACKGROUND_COLOR = new Color(200, 178, 55);
	
	// Layout
	private SpringLayout layout;
	
	
	
	// Constructor
	public PuzzleView(NCOIC creator, int puzzleHeight, int puzzleWidth) {
		// call superclass constructor
		super();
		
		myNCOIC = creator;
		
		// create and set layout
		layout = new SpringLayout();
		setLayout(layout);
		
		// set size of this view
		setPreferredSize(new Dimension(puzzleHeight * CELL_SIZE, puzzleWidth * CELL_SIZE));
		
		// set background color
		setBackground(BACKGROUND_COLOR);
	}
	
	
	// Pass up to NCOIC to un select every box
	public void unselectAll() {
		myNCOIC.unselectAll();
	}
	
	
	
	// Passes arrow key event from CellView to NCOIC
	public void cellArrowKey(KeyEvent e, int x, int y) {
		myNCOIC.cellArrowKey(e, x, y);
	}
	
	
	
	// Add cell view to puzzleView
	public void addCellView(CellView cellView, int x, int y) {
		
		cellView.setParent(this);
		
		// position node view
		int frameOffset = 20;
		int groupOffsetX = 0;
		int groupOffsetY = 0;
		
		int groupSpace = 7;
		
		if (x >= 3) groupOffsetX = groupOffsetX + groupSpace;
		if (x >= 6) groupOffsetX = groupOffsetX + groupSpace;
		
		if (y >= 3) groupOffsetY = groupOffsetY + groupSpace;
		if (y >= 6) groupOffsetY = groupOffsetY + groupSpace;
		
		layout.putConstraint(SpringLayout.WEST, cellView, x * CELL_SIZE + frameOffset + groupOffsetX, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, cellView, y * CELL_SIZE + frameOffset + groupOffsetY, SpringLayout.NORTH, this);
		
		// add node view to layout
		this.add(cellView);
	}
	
	
	
}
