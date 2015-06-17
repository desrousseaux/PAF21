
public abstract class Gate {

	private String output;
	private String name;
	private String type;
	
	public Gate(String output, String name, String type) {
		
		this.output = output;
		this.name = name;
		this.type = type;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public String getType() {
		
		return type;
		
	}
	
	public abstract String getInput();
	
}
