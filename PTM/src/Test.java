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
		
		ArrayList<Gate2> gates2 = cr.getGates2();
		for (Gate2 gate : gates2) {
			System.out.println("name " + gate.getName() + " output " + gate.getOutput() + " inputs " + gate.getInput1() + " " + gate.getInput2());
		}

	}

}
