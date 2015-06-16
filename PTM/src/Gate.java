
public abstract class Gate {
	
	private String output;
	private String input1;
	private String input2;
	private int[][] ptm;
	
	public Gate(String output, String input1, String input2) {
		
		this.output = output;
		this.input1 = input1;
		this.input2 = input2;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getInput1() {
		
		return intput1;
		
	}
	
	public String getInput2() {
		
		return input;
		
	}
	
	public String getPtm() {
		
		return ptm;
		
	}
}
