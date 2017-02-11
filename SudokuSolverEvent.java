
import java.util.EventObject;


public class SudokuSolverEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	
	// Event types
	public final static int RUN_EVENT = 1;
	public final static int STEP_EVENT = 2;
	public final static int STOP_EVENT = 3;
	public final static int RESET_EVENT = 4;
	
	
	private int eventType;
	
	public SudokuSolverEvent(Object source, int eventType) {
		super(source);
		this.eventType = eventType;
	}
	
	public int getEventType() {
		return eventType;
	}
}


