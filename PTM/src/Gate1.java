public class Gate1 extends Gate {
	
	private String input;
	
	public Gate1(String output, String input, String name) {
		
		super(output, name);
		this.input = input;
		
	}
	
	public String getInput() {
		
		return input;
		
	}
}