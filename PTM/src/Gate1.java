public abstract class Gate1 {
	
	private String output;
	private String input;
	private int[][] ptm;
	
	public Gate1(String output, String input) {
		
		this.output = output;
		this.input = input;
		
	}
	
	public String getOutput() {
		
		return output;
		
	}
	
	public String getInput() {
		
		return input;
		
	}
	
	public int[][] getPtm() {
		
		return ptm;
		
	}
}