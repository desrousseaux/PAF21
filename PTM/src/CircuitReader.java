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
			//Lecture du fichier
			fr = new FileReader(file);
			bis = new BufferedReader(fr);
			String line;
			while ((line = bis.readLine()) != null) {
				//On décide quoi faire selon le début de la ligne
				if (!line.equals("")) {
				if (line.substring(0, 6).equals("output")) {
					createOutputs(line);
				} else if (line.substring(0, 5).equals("input")) {
					createInputs(line);
				} else {
				String type = line.substring(0, 4);
				if (type.equals("and ") || type.substring(0,2).equals("or") || type.equals("xor ") || type.equals("nand") || type.equals("nor ") || type.equals("xnor")) {
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
	
	//Ajoute les sorties à la liste outputs
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
	
	//Ajoute les entrées à la liste inputs
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
	
	//Transforme une ligne commençant par and, or, nand, nor, xor, xnor en une porte à 2 entrées
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
	
	//Transforme une ligne commençant par not en une porte à une entrée
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
	
	//Calcul de la formule de la PTM
	public String getFormula() {
		
		String formula = "1";//formule à compléter au fur et à mesure
		ArrayList<Gate> remainingGates = gates;//portes à tester pour un tour donné
		ArrayList<Gate> newRemainingGates = new ArrayList<Gate>();//portes à tester au tour suivant
		ArrayList<String> currentLevel = outputs;//fils du niveau
		ArrayList<String> fanouts = new ArrayList<String>();//cas de fanouts à traiter
		//On itère jusqu'à avoir pris en compte toutes les portes (et les fanouts juste après le dernier niveau)
		while (!remainingGates.isEmpty()) {
			formula = formula + "*(";
			int nbParentheses = 0;
			//Vérifier si le fil est une entrée primaire
			for (String out : currentLevel) {
				if (inputs.contains(out)) {
					formula = formula + "kron(ID,";
					nbParentheses++;
				}
			}
			
			for (Gate gate : remainingGates) {
				String output = gate.getOutput();
				if (currentLevel.contains(output) || fanouts.contains(output)) {
						//Traitement des portes dont la sortie est dans le niveau actuel
						try {
							//On sépare le cas des portes à 1 entrée et à 2 entrées
							Class<? extends Gate> gateN = gate.getClass();
							if (gateN==Class.forName("Gate1")) {
								String input = gate.getInput();
								if (!currentLevel.contains(input)) {
									//Pas de fanout, on ajoute l'entrée au prochain niveau
									currentLevel.add(input);
									currentLevel.remove(currentLevel.indexOf(output));
								} else if (!fanouts.contains(input)) {
									//Fanout à traiter
									fanouts.add(input);
								}
							} else if (gateN==Class.forName("Gate2")) {
								//Même chose avec 2 entrées
								Gate2 newGate = (Gate2)gate;
								String input1 = newGate.getInput1();
								String input2 = newGate.getInput2();
								if (!currentLevel.contains(input1)) {
									currentLevel.add(input1);
								} else if (!fanouts.contains(input1)){
									fanouts.add(input1);
								}
								if (!currentLevel.contains(input2)) {
									currentLevel.add(input2);
								} else if (!fanouts.contains(input2)){
									fanouts.add(input2);
								}
								if (currentLevel.contains(output)) {
									currentLevel.remove(currentLevel.indexOf(output));
								}
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						formula = formula + "kron(" + gate.getType() + ","; //Produit tensoriel pour chaque nouvelle porte du niveau
						nbParentheses++;
				} else {
					//Si la sortie de la porte n'est pas dans le niveau actuel, on traite la porte au prochain tour
					newRemainingGates.add(gate);
				}
			}
			//On referme les parenthèses et ainsi le niveau
			formula = formula + "1";
			for (int i=0; i<=nbParentheses; i++) {
				formula = formula + ")";
			}
			//Traitement des fanouts
			if (!fanouts.isEmpty()) {
				//On crée un autre niveau
				formula = formula + "*(";
				int nbParentheses2 = 0;
				for (String fan : fanouts) {
					formula = formula + "kron(FANOUT,";
					nbParentheses2++;
				}
				//On rajoute ID pour tous les fils du niveau qui ne sont pas dans un fanout
				for (String wire : currentLevel) {
					if (!fanouts.contains(wire)) {
						formula = formula + "kron(ID,";
						nbParentheses2++;
					}
				}
				//On referme le niveau
				formula = formula + "1";
				for (int i=0; i<=nbParentheses2; i++) {
					formula = formula + ")";
				}
			}
			//réinitialisation pour le tour suivant
			remainingGates = newRemainingGates;
			newRemainingGates = new ArrayList<Gate>();
			fanouts = new ArrayList<String>();
			
		}
		
		return formula;
	}

	//Calcul de la formule de l'ITM
	//Exactement le même algorithme mais en changeant les String des matrices de la bibliothèque
	public String getFormula1() {
			
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
						formula = formula + "kron(ID1,";
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
									} else if (!fanouts.contains(input)) {
										fanouts.add(input);
									}
								} else if (gateN==Class.forName("Gate2")) {
									Gate2 newGate = (Gate2)gate;
									String input1 = newGate.getInput1();
									String input2 = newGate.getInput2();
									if (!currentLevel.contains(input1)) {
										currentLevel.add(input1);
									} else if (!fanouts.contains(input1)){
										fanouts.add(input1);
									}
									if (!currentLevel.contains(input2)) {
										currentLevel.add(input2);
									} else if (!fanouts.contains(input2)){
										fanouts.add(input2);
									}
									if (currentLevel.contains(output)) {
										currentLevel.remove(currentLevel.indexOf(output));
									}
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							formula = formula + "kron(" + gate.getType() + "1,";
							nbParentheses++;
					} else {
						newRemainingGates.add(gate);
					}
				}
				formula = formula + "1";
				for (int i=0; i<=nbParentheses; i++) {
					formula = formula + ")";
				}
				if (!fanouts.isEmpty()) {
					formula = formula + "*(";
					int nbParentheses2 = 0;
					for (String fan : fanouts) {
						formula = formula + "kron(FANOUT1,";
						nbParentheses2++;
					}
					for (String wire : currentLevel) {
						if (!fanouts.contains(wire)) {
							formula = formula + "kron(ID1,";
							nbParentheses2++;
						}
					}
					formula = formula + "1";
					for (int i=0; i<=nbParentheses2; i++) {
						formula = formula + ")";
					}
				}
				remainingGates = newRemainingGates;
				newRemainingGates = new ArrayList<Gate>();
				fanouts = new ArrayList<String>();
				
			}
			
			return formula;
		}

}
