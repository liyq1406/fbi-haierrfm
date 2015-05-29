package pub.platform.debug;

public class DebugInfo {
	private Throwable stack;
	private String action;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Throwable getStack() {
		return stack;
	}
	public void setStack(Throwable stack) {
		this.stack = stack;
	}
	
	public DebugInfo(Throwable stack) {
		this.stack = stack;
	}

}
