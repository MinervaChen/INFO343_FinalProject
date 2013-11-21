import java.io.IOException;

import reader.*;
import writer.*;


public class ExcelTest {

	/**s
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadExcel reader = new ReadExcel();
		reader.setInputFile("data/Positions.xls");
		try {
			reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
