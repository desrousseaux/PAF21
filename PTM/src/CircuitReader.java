import java.io.*;
import java.util.ArrayList;


public class CircuitReader {

	private File file;
	private ArrayList<String> inputs;
	private ArrayList<String> outputs;
	
	public CircuitReader(String fileName) {
		
		file = new File(fileName);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bis = new BufferedReader(fr);
			String line;
			while ((line = bis.readLine()) != null) {
				if (line.substring(0, 5).equals("output")) {
					createOutputs(line);
				} else if (line.substring(0, 4).equals("input")) {
					createInputs(line);
				} else {
				String type = line.substring(0, 3);
				if (type.equals("and ") || type.equals("or O") || type.equals("xor ") || type.equals("nand") || type.equals("nor ") || type.equals("xnor")) {
					Gate2 gate2 = createGate2(line);
				} else if (type.equals("not ")) {
					Gate1 gate1 = createGate1(line);
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
		
		
	}
	
	private Gate1 createGate1(String line) {
		
		
	}
}
