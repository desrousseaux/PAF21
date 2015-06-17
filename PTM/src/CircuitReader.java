import java.io.*;
import java.util.ArrayList;


public class CircuitReader {

	private File file;
	private ArrayList<String> inputs;
	private ArrayList<String> outputs;
	private ArrayList<Gate> gates;
	
	public CircuitReader(String fileName) {
		
		file = new File(fileName);
		inputs = new ArrayList<String>();
		outputs = new ArrayList<String>();
		gates = new ArrayList<Gate>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bis = new BufferedReader(fr);
			String line;
			while ((line = bis.readLine()) != null) {
				if (!line.equals("")) {
				if (line.substring(0, 6).equals("output")) {
					createOutputs(line);
				} else if (line.substring(0, 5).equals("input")) {
					createInputs(line);
				} else {
				String type = line.substring(0, 4);
				if (type.equals("and ") || type.equals("or O") || type.equals("xor ") || type.equals("nand") || type.equals("nor ") || type.equals("xnor")) {
					gates.add(createGate2(line));
				} else if (type.equals("not ")) {
					gates.add(createGate1(line));
				}
				}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void createOutputs(String line) {
		
		char[] array = line.toCharArray();
		int l = array.length;
		String current = "";
		for (int i=7; i<l; i++) {
			if (array[i] == ',' || array[i] == ';') {
				outputs.add(current);
				current = "";
			} else {
				current = current + array[i];
			}
		}
	}
	
	private void createInputs(String line) {
		
		char[] array = line.toCharArray();
		int l = array.length;
		String current = "";
		for (int i=6; i<l; i++) {
			if (array[i] == ',' || array[i] == ';') {
				inputs.add(current);
				current = "";
			} else {
				current = current + array[i];
			}
		}
	}
	
	private Gate2 createGate2(String line) {
		
		char[] array = line.toCharArray();
		String output = "";
		String input1 = "";
		String input2 = "";
		String name = "";
		String type = "";
		int i = 0;
		while(array[i] != ' ') {
			type = type + array[i];
			i++;
		}
		i++;
		while(array[i] != ' ') {
			name = name + array[i];
			i++;
		}
		while (array[i] != '(') {
			i++;
		}
		i++;
		while(array[i] != ',') {
			output = output + array[i];
			i++;
		}
		i++;
		while(array[i] != ',') {
			input1 = input1 + array[i];
			i++;
		}
		i++;
		while(array[i] != ')') {
			input2 = input2 + array[i];
			i++;
		}
		return new Gate2(output, input1, input2, name, type);
	}
	
	private Gate1 createGate1(String line) {
		
		char[] array = line.toCharArray();
		String output = "";
		String input = "";
		String name = "";
		String type = "";
		int i = 0;
		while (array[i] != ' ') {
			type = type + array[i];
			i++;
		}
		while(array[i] != ' ') {
			name = name + array[i];
			i++;
		}
		while(array[i] != ',') {
			output = output + array[i];
			i++;
		}
		i++;
		while(array[i] != ')') {
			input = input + array[i];
			i++;
		}
		return new Gate1(output, input, name, type);
		
		
	}
	
	public ArrayList<String> getInputs() {
		
		return inputs;
		
	}
	
	public ArrayList<String> getOutputs() {
		
		return outputs;
		
	}
	
	public ArrayList<Gate> getGates() {
		
		return gates;
		
	}
	
	public String getFormula() {
		
		String formula = "1";
		ArrayList<Gate> remainingGates = gates;
		ArrayList<Gate> newRemainingGates = new ArrayList<Gate>();
		ArrayList<String> currentLevel = outputs;
		int iteration = 0;
		while (!remainingGates.isEmpty()) {
			iteration++;
			if (iteration==10) return "stop";
			formula = formula + "*(";
			int nbParentheses = 0;
			for (String out : currentLevel) {
				System.out.println(iteration + " " + out);
				if (inputs.contains(out)) {
					formula = formula + "kron(eye(2),";
					nbParentheses++;
				}
			}
			for (Gate gate : remainingGates) {
				System.out.println(iteration + " " + gate.getName() + " out " + gate.getOutput());
				if (currentLevel.contains(gate.getOutput())) {
					currentLevel.remove(currentLevel.indexOf(gate.getOutput()));
					System.out.println(iteration + " " + gate.getOutput() + "removed");
						try {
							if (gate.getClass()==Class.forName("Gate1")) {
								if (!currentLevel.contains(gate.getInput())) {
									currentLevel.add(gate.getInput());
								}
							} else if (gate.getClass()==Class.forName("Gate2")) {
								if (!currentLevel.contains(((Gate2)gate).getInput1())) {
									currentLevel.add(((Gate2)gate).getInput1());
									System.out.println(iteration + " " + ((Gate2)gate).getInput1() + "added");
								}
								if (!currentLevel.contains(((Gate2)gate).getInput2())) {
									currentLevel.add(((Gate2)gate).getInput2());
									System.out.println(iteration + " " + ((Gate2)gate).getInput2() + "added");
								}
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						formula = formula + "kron(" + gate.getType() + ",";
						nbParentheses++;
				} else {
					newRemainingGates.add(gate);
				}
			}
			remainingGates = newRemainingGates;
			newRemainingGates = new ArrayList<Gate>();
			formula = formula + "1";
			for (int i=0; i<=nbParentheses; i++) {
				formula = formula + ")";
			}
		}
		
		return formula;
	}
}
