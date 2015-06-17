import java.util.ArrayList;


public class Test {

	public static void main(String[] args) {

		CircuitReader cr = new CircuitReader("PTM/src/c17.v");
		/*ArrayList<String> inputs = cr.getInputs();
		for (String input : inputs) {
			System.out.println(input);
		}*/
		
		/*ArrayList<String> outputs = cr.getOutputs();
		for (String output : outputs) {
			System.out.println(output);
		}*/
		
		/*ArrayList<Gate> gates = cr.getGates();
		for (Gate gate : gates) {
			System.out.println("type " + gate.getType() + " output " + gate.getOutput() + " inputs " + gate.getInput());
		}*/
		
		System.out.println(cr.getFormula());

	}

}
