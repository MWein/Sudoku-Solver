import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class MyGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	
	// Panel containing buttons
	private ControlPanel controlPanel;
	
	// NCOIC
	private NCOIC myNCOIC;
	
	
	public MyGUI() {
		// Set title of window
		super("Sudoku Solver - Weinberg Software - Version 1");
		
		// Create listener to allow user to close window and end program
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			dispose();
			System.exit(0);
			}
		});
		
		// set the size of the window		
		setSize(615, 675);
		
		// validate all components
		validate();
	}
	
	
	void initGUI(PuzzleView puzzleView) {
		
		// create button control panel
		controlPanel = new ControlPanel();
		
		// Add control panel and puzzle view
		getContentPane().add(controlPanel, BorderLayout.NORTH);
		getContentPane().add(puzzleView, BorderLayout.CENTER);
		
		// validate all components
		validate();
	}
	
	
	// Set NCOIC for event listening purposes
	public void setNCOIC(NCOIC listener) {
		myNCOIC = listener;
	}
	
	
	
	
	// Button pressed, inform the NCOIC
	private void fireProgramEvent(int eventType) throws InterruptedException {
		// create event
		SudokuSolverEvent myEvent = new SudokuSolverEvent(this, eventType);
		// Inform NCOIC
		myNCOIC.sudokuSolverEventOccurred(myEvent);
	}
	
	
	
	
	
	// Control panel at the top of the window
	private class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// Run Button
		private JButton runButton;
		
		// Step Button
		private JButton stepButton;
		
		// Stop Button
		private JButton stopButton;
		
		// Reset Button
		private JButton resetButton;
		
		
		//private JLabel stepLabel;
		
		// Button Event Handler
		private ButtonHandler buttonHandler;
		
		
		// Constructor
		public ControlPanel() {
			super();
			
			// Create handler for button events
			buttonHandler = new ButtonHandler();
			
			
			// button for running program continuously
			runButton = new JButton("Run");
			runButton.addActionListener(buttonHandler);
			runButton.setToolTipText("Run the simulation continuously");
			
			// button for running program one step at a time
			stepButton = new JButton("Step");
			stepButton.addActionListener(buttonHandler);
			stepButton.setToolTipText("Step through the simulation one turn at a time");
			
			// button to stop program
			stopButton = new JButton("Stop");
			stopButton.addActionListener(buttonHandler);
			stopButton.setToolTipText("");
			
			// button to erase every box
			resetButton = new JButton("Reset");
			resetButton.addActionListener(buttonHandler);
			resetButton.setToolTipText("");
			
			// label for displaying number of steps
			//stepLabel = new JLabel("Go fuck yourself");
			//stepLabel.setFont(new Font("Verdana", Font.BOLD, 12));
			
			// position child components
			this.add(runButton);
			this.add(stepButton);
			this.add(stopButton);
			this.add(resetButton);
			//this.add(stepLabel);
		}
		
	
		
	
		// Button event handler
		private class ButtonHandler implements ActionListener {	
			
			public void actionPerformed(ActionEvent e) {
				
				JButton b = (JButton)e.getSource();
	
				if (b.getText().equals("Run")) {
					try {fireProgramEvent(SudokuSolverEvent.RUN_EVENT);}
					catch (InterruptedException e1) {e1.printStackTrace();}
				}
				else if (b.getText().equals("Step")) {
					try {fireProgramEvent(SudokuSolverEvent.STEP_EVENT);}
					catch (InterruptedException e1) {e1.printStackTrace();}
				}
				
				else if (b.getText().equals("Stop")) {
					try {fireProgramEvent(SudokuSolverEvent.STOP_EVENT);}
					catch (InterruptedException e1) {e1.printStackTrace();}
				}
				
				else if (b.getText().equals("Reset")) {
					// run the simulation one turn at a time
					try {fireProgramEvent(SudokuSolverEvent.RESET_EVENT);}
					catch (InterruptedException e1) {e1.printStackTrace();}
				}
			}
		}
	}
	
	
}
