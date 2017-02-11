import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;



public class CellView extends JPanel implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	
	
	// Whether the user can edit the cell or not
	private boolean isEnabled;
	
	
	
	// Font for set numbers
	private final Font NUM_FONT = new Font("Verdana", Font.BOLD, 30);
	
	// Color for set number
	private final Color NUM_COLOR = Color.BLACK;
	
	// Font for pencil marks
	private final Font NODE_FONT = new Font("Verdana", Font.BOLD, 10);
		
	// color for pencil marks
	private final Color POSSIBILITY_COLOR = Color.GRAY;
	
	
	
	// Background colors
	private final Color DEFAULT_COLOR = new Color(200, 178, 55);
	private final Color SELECTED_COLOR = new Color(79, 79, 252);		// blue
	private final Color FOCUS_COLOR_1 = new Color(255, 204, 50);	// orange
	private final Color FOCUS_COLOR_2 = new Color(255, 255, 101);	// yellow
	
	
	
	// Enclosing PuzzleView
	private PuzzleView ParentView;
	private SpringLayout layout;
	
	
	// Focus
	private boolean isSelected;
	
	
	// Set number displayed in cell
	private int SetNumber;
	
	
	// Pencil mark booleans
	private boolean onepos;
	private boolean twopos;
	private boolean threepos;
	private boolean fourpos;
	private boolean fivepos;
	private boolean sixpos;
	private boolean sevenpos;
	private boolean eightpos;
	private boolean ninepos;
	
	
	// This cells position
	int PosX;
	int PosY;
	
	
	private JLabel SetNumberLabel; // Number Label
	private JLabel PossibilityLabel; // Pencil marks first level
	private JLabel PossibilityLabel2; // Pencil marks second level
	
	
	
	
	// Constructor
	public CellView(int x, int y) {
		super();
		
		isEnabled = true;
		
		PosX = x;
		PosY = y;
		
		// create and set layout for child components
		layout = new SpringLayout();
		this.setLayout(layout);
		
		// initialize child components
		initComponents();
		
		// position and display child components
		layoutComponents();
		
		// set background color
		setBackground(DEFAULT_COLOR);
		
		// set border
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		// set size
		setPreferredSize(new Dimension(PuzzleView.CELL_SIZE, PuzzleView.CELL_SIZE));
		
		// add mouse and key listeners
		addMouseListener(this);
		addKeyListener(this);
	}
	
	
	
	// Create child views
	private void initComponents() {
		
		// Create number level
		SetNumberLabel = new JLabel("");
		SetNumberLabel.setFont(NUM_FONT);
		SetNumberLabel.setForeground(NUM_COLOR);
		
		// First level of pencil marks
		PossibilityLabel = new JLabel("");
		PossibilityLabel.setFont(NODE_FONT);
		PossibilityLabel.setForeground(POSSIBILITY_COLOR);
		
		// Second level of pencil marks
		PossibilityLabel2 = new JLabel("");
		PossibilityLabel2.setFont(NODE_FONT);
		PossibilityLabel2.setForeground(POSSIBILITY_COLOR);
		
		updateDisplay();
	}
	
	
	
	
	private void layoutComponents() {
		
		// Position number label
		layout.putConstraint(SpringLayout.WEST, SetNumberLabel, 18, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, SetNumberLabel, 8, SpringLayout.NORTH, this);
		
		// Position pencil mark label 1
		layout.putConstraint(SpringLayout.WEST, PossibilityLabel, 2, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, PossibilityLabel, 5, SpringLayout.NORTH, this);
		
		// Position pencil mark label 2
		layout.putConstraint(SpringLayout.WEST, PossibilityLabel2, 2, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, PossibilityLabel2, 20, SpringLayout.NORTH, this);
		
		
		// add to view
		this.add(SetNumberLabel);
		this.add(PossibilityLabel);
		this.add(PossibilityLabel2);
	}
	
	
	// Set PuzzleView parent view for selection and editing purposes
	public void setParent(PuzzleView parent) {ParentView = parent;}
	
	
	
	// Set whether the view is editable by the user
	public void setEnabled(boolean en) {
		isEnabled = en;
		if (!isEnabled) setSelected(false);
	}
	public boolean isEnabled() {return isEnabled;}
	
	
	
	// Set the background color of the display to visually show what the algorithm is doing and
	// what its focusing on
	public void setFocusLevel(int x) {
		if (x == 0) setBackground(DEFAULT_COLOR);
		if (x == 1) setBackground(FOCUS_COLOR_1);
		if (x == 2) setBackground(FOCUS_COLOR_2);
	}
	
	
	
	// Update text views with any new information
	// New pencil marks, new number, etc
	private void updateDisplay() {
		
		// Show or hide pencil marks based on set number
		// If there is no set number, show pencil marks
		// Otherwise, hide them
		if (SetNumber == 0) {
			PossibilityLabel.setVisible(true);
			PossibilityLabel2.setVisible(true);
		}
		else {
			PossibilityLabel.setVisible(false);
			PossibilityLabel2.setVisible(false);
		}
		
		// Clear pencil marks
		PossibilityLabel.setText("");
		PossibilityLabel2.setText("");
		
		// How many pencil marks there are
		int trueCount = 0;
		
		// Strings for both labels
		String label1Text = "";
		String label2Text = "";
		
		// 1
		if (onepos) {
			trueCount++;
			label1Text = label1Text + "1 ";
		}
		
		// 2
		if (twopos) {
			trueCount++;
			label1Text = label1Text + "2 ";
		}
		
		// 3
		if (threepos) {
			trueCount++;
			label1Text = label1Text + "3 ";
		}
		
		// 4
		if (fourpos) {
			trueCount++;
			label1Text = label1Text + "4 ";
		}
		
		// 5
		if (fivepos) {
			trueCount++;
			label1Text = label1Text + "5 ";
		}
		
		// 6
		// Labels can only hold 5 values before the need to wrap text
		// 6 and above will check the trueCount to determine if the second
		// label should be used
		if (sixpos) {
			if (trueCount >= 5) label2Text = label2Text + "6 ";
			else label1Text = label1Text + "6 ";
			trueCount++;
		}
		
		// 7
		if (sevenpos) {
			if (trueCount >= 5) label2Text = label2Text + "7 ";
			else label1Text = label1Text + "7 ";
			trueCount++;
		}
		
		// 8
		if (eightpos) {
			if (trueCount >= 5) label2Text = label2Text + "8 ";
			else label1Text = label1Text + "8 ";
			trueCount++;
		}
		
		// 9
		if (ninepos) {
			if (trueCount >= 5) label2Text = label2Text + "9 ";
			else label1Text = label1Text + "9 ";
			trueCount++;
		}
		
		// Set text pencil mark labels
		PossibilityLabel.setText(label1Text);
		PossibilityLabel2.setText(label2Text);
		
		// Set number text
		if (SetNumber != 0) SetNumberLabel.setText("" + SetNumber);
		else SetNumberLabel.setText("");
	}
	
	
	
	
	
	
	public void setSelected(boolean sel) {
		if (sel == true && isEnabled) {
			ParentView.unselectAll(); // Tell the parent view to unselect any selected views other than this one
			setBackground(SELECTED_COLOR); // Paint the text
			requestFocusInWindow(); // Claim focus for key listening
		}
		else setBackground(DEFAULT_COLOR); // Boring old background if not selected
		
		isSelected = sel;
	}
	public boolean isSelected() {return isSelected;}
	
	
	
	
	public void setNumber(int x) {
		
		// Make sure the input is 1-9
		if (x < 0 || x > 9) x = 0;
		
		// Once the number is set, the pencil marks are erased
		onepos = false;
		twopos = false;
		threepos = false;
		fourpos = false;
		fivepos = false;
		sixpos = false;
		sevenpos = false;
		eightpos = false;
		ninepos = false;
		
		SetNumber = x;
		
		// Update display with new information
		updateDisplay();
	}
	public int getNumber() {return SetNumber;}
	
	
	
	// Pencil marks are stored in boolean values
	// Pencil marks are edited by calling this function
	public void setPossibility(int x, boolean newPos) {
		if (x == 1) onepos = newPos;
		else if (x == 2) twopos = newPos;
		else if (x == 3) threepos = newPos;
		else if (x == 4) fourpos = newPos;
		else if (x == 5) fivepos = newPos;
		else if (x == 6) sixpos = newPos;
		else if (x == 7) sevenpos = newPos;
		else if (x == 8) eightpos = newPos;
		else if (x == 9) ninepos = newPos;
		updateDisplay();
	}
	
	// Pencil marks are read using this function
	public boolean getPossibility(int x) {
		if (x == 1) return onepos;
		else if (x == 2) return twopos;
		else if (x == 3) return threepos;
		else if (x == 4) return fourpos;
		else if (x == 5) return fivepos;
		else if (x == 6) return sixpos;
		else if (x == 7) return sevenpos;
		else if (x == 8) return eightpos;
		else if (x == 9) return ninepos;
		return false;
	}
	
	
	
	

	
	
	@Override
	public void mousePressed(MouseEvent e) {
		setSelected(!isSelected());
	}
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// Only respond to key events if enabled and selected
		if (isSelected && isEnabled) {
			
			// Arrow keys. Passes event up to PuzzleView
			if (arg0.getKeyCode() == KeyEvent.VK_UP || arg0.getKeyCode() == KeyEvent.VK_DOWN || arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
				ParentView.cellArrowKey(arg0, PosX, PosY);
			}
			else {
				// Numbers
				char k = arg0.getKeyChar();
			
				if (k == '1') setNumber(1);
				else if (k == '2') setNumber(2);
				else if (k == '3') setNumber(3);
				else if (k == '4') setNumber(4);
				else if (k == '5') setNumber(5);
				else if (k == '6') setNumber(6);
				else if (k == '7') setNumber(7);
				else if (k == '8') setNumber(8);
				else if (k == '9') setNumber(9);
				else setNumber(0);
			}
		}
	}


	// For key listener
	public boolean isFocusable() {
		return true;
	}
	
	
	
	
	
	
	
	
	
	// Methods I'm not actually using but I have to have them for the implementations
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	
	
}
