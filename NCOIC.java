import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class NCOIC implements ActionListener {

	
	// Number of cells
	public final static int ENVIRONMENT_SIZE_WIDTH = 9;
	public final static int ENVIRONMENT_SIZE_HEIGHT = 9;
	
	
	
	private static MyGUI myGUI;
	private static PuzzleView puzzleView;
	private static CellView[][] PuzzleCellArray;
	
	
	
	NCOIC() {
		// Create Timer
		myTimer = new Timer(timerRate, this);
		
		myGUI = new MyGUI();
		myGUI.setNCOIC(this);
		
		// Create the puzzle view
		puzzleView = new PuzzleView(this, ENVIRONMENT_SIZE_WIDTH, ENVIRONMENT_SIZE_HEIGHT);
		
		
		myGUI.initGUI(puzzleView);
		
		// Create Puzzle Cell Array
		PuzzleCellArray = new CellView[ENVIRONMENT_SIZE_WIDTH][ENVIRONMENT_SIZE_HEIGHT];
		

		// Populate Puzzle Cell Array to PuzzleView
		for (int x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
			for (int y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
				
				// Create the ColonyNodeView and add it to the NodeView
				PuzzleCellArray[x][y] = new CellView(x, y);
				puzzleView.addCellView(PuzzleCellArray[x][y], x, y);
			}
		}
		
		
		myGUI.setVisible(true);
	}

	
	// Unselect and undo highlighting for every cell
	public void unselectAll() {
		for (int x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
			for (int y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
				PuzzleCellArray[x][y].setSelected(false);
				PuzzleCellArray[x][y].setFocusLevel(0);
			}
		}
	}
	
	
	// Enable or disable all cells
	public void setEnabledAll(boolean enab) {
		for (int x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
			for (int y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
				PuzzleCellArray[x][y].setEnabled(enab);
			}
		}
	}
	
	
	
	// Returns the number of pencil marks for a given number in a given row
	public int checkForPossInRow(int y, int num) {
		int count = 0;
		for (int x = 0; x < 9; x++) {
			if (PuzzleCellArray[x][y].getPossibility(num)) count++;
		}
		return count;
	}
	
	// Returns the number of pencil marks for a given number in a given column
	public int checkForPossInColumn(int x, int num) {
		int count = 0;
		for (int y = 0; y < 9; y++) {
			if (PuzzleCellArray[x][y].getPossibility(num)) count++;
		}
		return count;
	}
	
	// Returns the number of pencil marks for a given number in a given box
	public int checkForPossInBox(int bx, int by, int num) {
		
		int count = 0;
		
		int hx = -1;
		int hy = -1;
		
		while (hx < 2) {
			while (hy < 2) {
				if (PuzzleCellArray[bx + hx][by + hy].getPossibility(num)) count++;
				hy++;
			}
			hx++;
			hy = -1;
		}
		
		return count;
	}
	
	
	
	
	// Checks whether or not a specific number is marked in a given row
	public boolean checkForNumberInRow(int y, int num) {
		for (int x = 0; x < 9; x++) {
			if (PuzzleCellArray[x][y].getNumber() == num) return true;
		}
		return false;
	}
	
	// Checks whether or not a specific number is marked in a given column
	public boolean checkForNumberInColumn(int x, int num) {
		for (int y = 0; y < 9; y++) {
			if (PuzzleCellArray[x][y].getNumber() == num) return true;
		}
		return false;
	}
	
	// Checks whether or not a specific number is marked in a given box
	public boolean checkForNumberInBox(int bx, int by, int num) {
		
		int hx = -1;
		int hy = -1;
		
		while (hx < 2) {
			while (hy < 2) {
				if (PuzzleCellArray[bx + hx][by + hy].getNumber() == num) return true;
				hy++;
			}
			hx++;
			hy = -1;
		}
		
		
		return false;
	}
	
	
	
	// Number of cells not filled in
	private int incompleteCount;
	
	
	// Current focus cell
	private int CurrentCellX;
	private int CurrentCellY;
	
	// Number of marked numbers in this algorithm
	private int MarkCount;
	
	// 0 = fill possibilities
	// 1 = find isolated numbers
	private int CurrentMode;
	
	// Step 0 = Check Row
	// Step 1 = Check Column
	// Step 2 = Check Box
	// Step 3 = Check for 1 possibility
	private int CurrentStep;
	
	// Stops a last iteration if pause button is clicked just after the timer calls another step()
	private boolean TimerStopped;
	
	
	private Timer myTimer;
	private int timerRate = 10;
	
	
	
	public void begin() {
		
		
		// Check if all positions are filled
		incompleteCount = 0;
		for (int x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
			for (int y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
				if (PuzzleCellArray[x][y].getNumber() == 0) incompleteCount++;
			}
		}
		
		TimerStopped = false;
		
		CurrentCellX = 0;
		CurrentCellY = 0;
		MarkCount = 0;
		CurrentMode = 0;
		CurrentStep = 0;
		
		unselectAll();
		setEnabledAll(false);
		
		myTimer.start();
		
	}
	
	
	public void pause() {
		TimerStopped = true;
		myTimer.stop();
		unselectAll();
		setEnabledAll(true);
	}
	
	
	
	public void step() {
		
		// Reset colors
		unselectAll();
		
		if (incompleteCount == 0) {
			pause();
		}
		
		
		
		// Skip cells with numbers already assigned
		boolean hasNumber = false;
		if (PuzzleCellArray[CurrentCellX][CurrentCellY].getNumber() != 0) hasNumber = true;
		while (hasNumber) {
			CurrentStep++;
			if (CurrentStep > 2) {
				CurrentStep = 0;
				CurrentCellX++;
			}
			if (CurrentCellX > 8) {
				CurrentCellX = 0;
				CurrentCellY++;
			}
			if (CurrentCellY > 8) {
				CurrentCellX = 0;
				CurrentCellY = 0;
				
				if (MarkCount == 0) {
					if (CurrentMode == 0) CurrentMode = 1;
					else if (CurrentMode == 1) {
						CurrentMode = 0;
						pause();
					}
				}
				
				MarkCount = 0;
				
			}
			hasNumber = false;
			if (PuzzleCellArray[CurrentCellX][CurrentCellY].getNumber() != 0 && incompleteCount > 0) hasNumber = true;
		}
		
		
		
		if (CurrentMode == 0) {
			
			
			// Do More calculations
		
			// Check Row
			if (CurrentStep == 0) {
			
				// Set all possible numbers to true
				for (int c = 1; c <= 9; c++) PuzzleCellArray[CurrentCellX][CurrentCellY].setPossibility(c, true);
				
				
				// Highlight row
				for (int x = 0; x < 9; x++) PuzzleCellArray[x][CurrentCellY].setFocusLevel(2);
				
			
				// Check cells in row
				for (int c = 1; c <= 9; c++) {
					if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(c)) {
						PuzzleCellArray[CurrentCellX][CurrentCellY].setPossibility(c, !checkForNumberInRow(CurrentCellY, c));
					}
				}
				
			}
		
			
			// Check Column
			if (CurrentStep == 1) {
				
				// Highlight column
				for (int y = 0; y < 9; y++) PuzzleCellArray[CurrentCellX][y].setFocusLevel(2);
			
				// Check cells in column
				for (int c = 1; c <= 9; c++) {
					if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(c)) {
						PuzzleCellArray[CurrentCellX][CurrentCellY].setPossibility(c, !checkForNumberInColumn(CurrentCellX, c));
					}
				}
			}
			
			
			// Check Box
			if (CurrentStep == 2) {
				int currentBoxX = 1;
				int currentBoxY = 1;
				
				if (CurrentCellX >= 3 && CurrentCellX <= 5) currentBoxX = 4;
				else if (CurrentCellX >= 6) currentBoxX = 7;
				
				if (CurrentCellY >= 3 && CurrentCellY <= 5) currentBoxY = 4;
				else if (CurrentCellY >= 6) currentBoxY = 7;
				
				
				// Highlight box
				for (int hx = -1; hx < 2; hx++) {
					for (int hy = -1; hy < 2; hy++) {
						PuzzleCellArray[currentBoxX + hx][currentBoxY + hy].setFocusLevel(2);
					}
				}
				
				
				// Do an actual check
				for (int c = 1; c <= 9; c++) {
					if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(c)) PuzzleCellArray[CurrentCellX][CurrentCellY].setPossibility(c, !checkForNumberInBox(currentBoxX, currentBoxY, c));
				}
			}
			
			
			// Check for 1 possibility
			if (CurrentStep == 3) {
				
				int possCount = 0;
				int solePoss = 0;
				
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(1)) {
					possCount++;
					solePoss = 1;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(2)) {
					possCount++;
					solePoss = 2;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(3)) {
					possCount++;
					solePoss = 3;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(4)) {
					possCount++;
					solePoss = 4;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(5)) {
					possCount++;
					solePoss = 5;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(6)) {
					possCount++;
					solePoss = 6;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(7)) {
					possCount++;
					solePoss = 7;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(8)) {
					possCount++;
					solePoss = 8;
				}
				if (PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(9)) {
					possCount++;
					solePoss = 9;
				}
				
				if (possCount == 1) {
					PuzzleCellArray[CurrentCellX][CurrentCellY].setNumber(solePoss);
					MarkCount++;
					incompleteCount--;
				}
				
			}
			
			
			
		}
		
		
		
		
		// TODO Check if a cell has the only possibility for a certain number after clearing a box
		if (CurrentMode == 1) {
			
			
			// Check other cells for each possibility
			// If one possibility is unique to this cell, mark it
			
			checkForPossInRow(CurrentCellY, 0);
			
			boolean flag = false;
			int currentNum = 1;
			
			while (currentNum <= 9) {
				
				if (!PuzzleCellArray[CurrentCellX][CurrentCellY].getPossibility(currentNum)) {
					currentNum++;
					continue; // Skip this number
				}
				
				
				int currentBoxX = 1;
				int currentBoxY = 1;
				
				if (CurrentCellX >= 3 && CurrentCellX <= 5) currentBoxX = 4;
				else if (CurrentCellX >= 6) currentBoxX = 7;
				
				if (CurrentCellY >= 3 && CurrentCellY <= 5) currentBoxY = 4;
				else if (CurrentCellY >= 6) currentBoxY = 7;
				
				
				if (checkForPossInRow(CurrentCellY, currentNum) == 1) {
					PuzzleCellArray[CurrentCellX][CurrentCellY].setNumber(currentNum);
					flag = true;
					break;
				}
				else if (checkForPossInColumn(CurrentCellX, currentNum) == 1) {
					PuzzleCellArray[CurrentCellX][CurrentCellY].setNumber(currentNum);
					flag = true;
					break;
				}
				
				// TODO Check for cell in box
				else if (checkForPossInBox(currentBoxX, currentBoxY, currentNum) == 1) {
					PuzzleCellArray[CurrentCellX][CurrentCellY].setNumber(currentNum);
					flag = true;
					break;
				}
				
				
				
				
				currentNum++;
			}
			
			if (flag) {
				CurrentMode = 0;
				CurrentCellX = 0;
				CurrentCellY = 0;
				MarkCount = 0;
				incompleteCount--;
			}
			
			
		}
		
		
		
		
		PuzzleCellArray[CurrentCellX][CurrentCellY].setFocusLevel(1);
		
		
		CurrentStep++;
		if (CurrentStep > 3) {
			CurrentStep = 0;
			CurrentCellX++;
		}
		if (CurrentCellX > 8) {
			CurrentCellX = 0;
			CurrentCellY++;
		}
		if (CurrentCellY > 8) {
			CurrentCellX = 0;
			CurrentCellY = 0;
			
			
			if (MarkCount == 0) {
				if (CurrentMode == 0) CurrentMode = 1;
				else if (CurrentMode == 1) {
					CurrentMode = 0;
					pause();
				}
			}
			
			MarkCount = 0;
			
		}
		
		
		if (TimerStopped) {
			TimerStopped = false;
			unselectAll();
		}
	}
	
	
	
	
	
	
	
	public void cellArrowKey(KeyEvent e, int x, int y) {
		
		// Degree of change variables
		int xadd = 0;
		int yadd = 0;
		
		
		// Key codes
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			yadd = -1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			yadd = 1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			xadd = -1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xadd = 1;
		}
		
		
		// Constraints
		if (x + xadd < 0 || x + xadd > 8) xadd = 0;
		if (y + yadd < 0 || y + yadd > 8) yadd = 0;
		
		
		// Select the new cell
		PuzzleCellArray[x + xadd][y + yadd].setSelected(true);
	}
	
	
	
	
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		step();
	}


	
	public void sudokuSolverEventOccurred(SudokuSolverEvent simEvent) {
		
		// TODO Disable buttons
		
		if (simEvent.getEventType() == SudokuSolverEvent.RUN_EVENT) {
			begin();
		}
		else if (simEvent.getEventType() == SudokuSolverEvent.STEP_EVENT) {
			
			// Check if all positions are filled
			incompleteCount = 0;
			for (int x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
				for (int y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
					if (PuzzleCellArray[x][y].getNumber() == 0) incompleteCount++;
				}
			}
			
			step();
		}
		else if (simEvent.getEventType() == SudokuSolverEvent.STOP_EVENT) {
			pause();
		}
		else if (simEvent.getEventType() == SudokuSolverEvent.RESET_EVENT) {
			pause();
			int x;
			int y;
			for (x = 0; x < ENVIRONMENT_SIZE_WIDTH; x++) {
				for (y = 0; y < ENVIRONMENT_SIZE_HEIGHT; y++) {
					PuzzleCellArray[x][y].setNumber(0);
				}
			}
		}
		else {
			// invalid event occurred - probably will never happen
		}
	}
	
	
	
	
}
