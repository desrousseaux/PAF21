import matlabcontrol.*;
import matlabcontrol.extensions.*;

public class Test {

	public static void main(String[] args) {

		//Interprétation des circuits
		//On utilise les noms des fichiers .v
		//Le programme ne fonctionne que si les .v sont bien formatés
		//pas d'indentation, pas d'espace entre une parenthèse et un mot pour les portes
		//pas d'espace entre une virgule et un mot pour les inputs et outputs
		CircuitReader cr = new CircuitReader("PTM/src/RCA_8.v");
		CircuitReader cr1 = new CircuitReader("PTM/src/RCA_8.v");
		
		//Calcul des formules
		String formula = cr.getFormula();
		System.out.println(formula);
		String formula1 = cr1.getFormula1();
		System.out.println(formula1);
		
		//Initialisation et ouverture d'une session Matlab
		double rows = Math.pow(2, cr.getOutputs().size());
		double columns = Math.pow(2, cr.getInputs().size());
		double[][] PTM = new double[(int)rows][(int)columns];
		double[][] ITM = new double[(int)rows][(int)columns];
		double R = 0;
		double p;
		MatlabProxyFactory factory = new MatlabProxyFactory();
		
			try {
				MatlabProxy proxy = factory.getProxy();
				//On fait le calcul pour p variant de 0.5 à 1 (pas de 0.05)
				for (int i=0; i<=10; i++) {
					p = 0.5 + i*0.05;
					try {
						proxy.setVariable("p",p); //créer une variable Matlab
						proxy.eval("PTMp");//Entrer une commande Matlab
						proxy.eval("ITM");
						proxy.setVariable("PTM", PTM);
						proxy.eval("PTM = " + formula);
						MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
						//processor permet de convertir des vecteurs Matlab en objets Java
						MatlabNumericArray arrayPTM = processor.getNumericArray("PTM");
						PTM = arrayPTM.getRealArray2D();
						proxy.setVariable("ITM", ITM);
						proxy.eval("ITM = " + formula1);
						MatlabNumericArray arrayITM = processor.getNumericArray("ITM");
						ITM = arrayITM.getRealArray2D();
						proxy.setVariable("R", R);
						proxy.eval("R = reliability(PTM,ITM)");
						MatlabNumericArray arrayR = processor.getNumericArray("R");
						R = arrayR.getRealValue(0);
						System.out.println(R);
						
						
					} catch (MatlabInvocationException e) {
						e.printStackTrace();
					}
				}
					proxy.disconnect();
			} catch (MatlabConnectionException e) {
				e.printStackTrace();
			}
		}
		

	}
