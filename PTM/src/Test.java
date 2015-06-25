import java.io.*;
import java.util.ArrayList;

import matlabcontrol.*;
import matlabcontrol.extensions.*;

public class Test {

	public static void main(String[] args) {

		CircuitReader cr = new CircuitReader("PTM/src/c17.v");
		CircuitReader cr1 = new CircuitReader("PTM/src/c17.v");
		
		/*File file = new File("PTM/src/formula.txt");
		File file1 = new File("PTM/src/formula1.txt");
		FileWriter fw = null;
		FileWriter fw1 = null;
		try {
			fw = new FileWriter(file);
			fw1 = new FileWriter(file1);
			fw.write(cr.getFormula());
			fw1.write(cr1.getFormula1());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				fw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}*/
		
		double rows = Math.pow(2, cr.getOutputs().size());
		double columns = Math.pow(2, cr.getInputs().size());
		double[][] PTM = new double[(int)rows][(int)columns];
		double[][] ITM = new double[(int)rows][(int)columns];
		double R = 0;
		MatlabProxyFactory factory = new MatlabProxyFactory();
		try {
			MatlabProxy proxy = factory.getProxy();
			
			try {
				proxy.eval("PTM");
				proxy.eval("ITM");
				proxy.setVariable("PTM", PTM);
				proxy.eval("PTM = " + cr.getFormula());
				MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
				MatlabNumericArray arrayPTM = processor.getNumericArray("PTM");
				PTM = arrayPTM.getRealArray2D();
				proxy.setVariable("ITM", ITM);
				proxy.eval("ITM = " + cr1.getFormula1());
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
			proxy.disconnect();
		} catch (MatlabConnectionException e) {
			e.printStackTrace();
		}
		/*for (int i=0; i<(int)rows; i++) {
			for (int j=0; j<(int)columns; j++) {
				System.out.println(PTM[i][j] + " ");
			}
			System.*/

	}

}
