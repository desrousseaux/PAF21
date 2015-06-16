
public abstract class Gate2 {
	
	private String output;
	private String input1;
	private String input2;
	private int[][] ptm;
	
	public Gate2(String output, String input1, String input2) {
		
		this.output = output;
		this.input1 = input1;
		this.input2 = input2;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getInput1() {
		
		return input1;
		
	}
	
	public String getInput2() {
		
		return input2;
		
	}
	
	public int[][] getPtm() {
		
		return ptm;
		
	}
}
