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
		FileReader fr = null;
		BufferedReader bis = null;
		try {
			fr = new FileReader(file);
			bis = new BufferedReader(fr);
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
		} finally {
			try {
				bis.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		i++;
		while(array[i] != ',') {
			input1 = input1 + array[i];
			i++;
		}
		i++;
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
		ArrayList<String> fanouts = new ArrayList<String>();
		while (!remainingGates.isEmpty()) {
			formula = formula + "*(";
			int nbParentheses = 0;
			for (String out : currentLevel) {
				if (inputs.contains(out)) {
					formula = formula + "kron(ID,";
					nbParentheses++;
				}
			}
			for (Gate gate : remainingGates) {
				String output = gate.getOutput();
				if (currentLevel.contains(output) || fanouts.contains(output)) {
					
						try {
							Class<? extends Gate> gateN = gate.getClass();
							if (gateN==Class.forName("Gate1")) {
								String input = gate.getInput();
								if (!currentLevel.contains(input)) {
									currentLevel.add(input);
									currentLevel.remove(currentLevel.indexOf(output));
								} else if (!fanouts.contains(input)){
									Gate2 fanout = new Gate2(input,input,input,"fanout"+input, "FANOUT");
									newRemainingGates.add(fanout);
									fanouts.add(input);
								}
							} else if (gateN==Class.forName("Gate2")) {
								Gate2 newGate = (Gate2)gate;
								String input1 = newGate.getInput1();
								String input2 = newGate.getInput2();
								if (!currentLevel.contains(input1)) {
									currentLevel.add(input1);
									currentLevel.remove(currentLevel.indexOf(output));
								} else if (!fanouts.contains(input1)){
									Gate2 fanout = new Gate2(input1,input1,input1,"fanout"+input1, "FANOUT");
									newRemainingGates.add(fanout);
									fanouts.add(input1);
								}
								if (!currentLevel.contains(input2)) {
									currentLevel.add(input2);
									if (currentLevel.contains(output)) {
										currentLevel.remove(currentLevel.indexOf(output));
									}
								} else if (!fanouts.contains(input2)){
									Gate2 fanout = new Gate2(input2,input2,input2,"fanout"+input2, "FANOUT");
									newRemainingGates.add(fanout);
									fanouts.add(input2);
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
