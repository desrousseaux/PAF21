public abstract class Gate1 {
	
	private String output;
	private String input;
	private String name;
	
	public Gate1(String output, String input, String name) {
		
		this.output = output;
		this.input = input;
		this.name = name;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getInput() {
		
		return input;
		
	}
	
	public String getName() {
		
		return name;
		
	}
}