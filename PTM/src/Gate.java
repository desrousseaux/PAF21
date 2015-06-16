
public abstract class Gate {

	private String output;
	private String name;
	
	public Gate(String output, String name) {
		
		this.output = output;
		this.name = name;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public abstract String getInput();
	
}
