//Portes à 1 entrée
public class Gate1 extends Gate {
	
	private String input;
	
	public Gate1(String output, String input, String name, String type) {
		
		super(output, name, type);
		this.input = input;
		
	}
	
	public String getInput() {
		
		return input;
		
	}
}