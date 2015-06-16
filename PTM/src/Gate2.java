
public class Gate2 extends Gate {
	
	private String input1;
	private String input2;
	
	public Gate2(String output, String input1, String input2, String name) {
		
		super(output, name);
		this.input1 = input1;
		this.input2 = input2;
		
	}
	
	public String getInput1() {
		
		return input1;
		
	}
	
	public String getInput2() {
		
		return input2;
		
	}
	
	public String getInput() {
		
		return input1 + " " + input2;
		
	}
}
