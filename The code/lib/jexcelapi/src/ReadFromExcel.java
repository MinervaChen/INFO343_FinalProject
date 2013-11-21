package reader;

import java.io.*;
import java.util.*;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadFromExcel {
	
	private static final int POSITION_COLUMN_START = 6; //copied from PSCScheduler
	private static Map<Integer, Person> people; //copied from PSCScheduler
	public static Map<Integer, String> positions = new HashMap<Integer, String>(); //copied from PSCScheduler
	private static Map<Integer, ArrayList<Integer>> isTrainedList = new HashMap<Integer, ArrayList<Integer>>(); //copied from PSC
	private String inputFile;
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void read() throws IOException {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			//mapping position IDs to position names
			for (int j = POSITION_COLUMN_START; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(1, j);
				//position IDs start from 0
				positions.put(j - POSITION_COLUMN_START, cell.getContents());
			}
			// get iterator over 'positions' map keyset
			Set<Integer> positionIDs = positions.keySet();
			Iterator<Integer> positionIt = positionIDs.iterator();
			// initialize an empty isTrained list for each position
			while (positionIt.hasNext()) {
				int positionID = (Integer)positionIt.next();
				isTrainedList.put(positionID, new ArrayList<Integer>());
			}
			
			for (int i = 0; i < sheet.getRows(); i++) {
				Cell id = sheet.getCell(i, 1);
				Cell name = sheet.getCell(i, 2);
				Cell title = sheet.getCell(i, 3);
				Cell age = sheet.getCell(i, 4);
				Cell start = sheet.getCell(i, 5); //beginning of shift
				Cell end = sheet.getCell(i, 6); //end of shift
				Map<Integer, Boolean> positions = new HashMap<Integer, Boolean>();
				for (int j = POSITION_COLUMN_START; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(i, j);
					boolean isTrained = Boolean.valueOf(cell.getContents());
					int positionID = j - POSITION_COLUMN_START;
					// store whether person is trained for curent positionID
					positions.put(positionID, isTrained);
					if (isTrained) {
						// add person to isTrained list of current positionID
						isTrainedList.get(positionID).add((Integer)id.getContents());
					}
				}
				// create person and add to people list
				people.put(id.getContents(), new Person(id.getContents(), name.getContents(), title.getContents(), age.getContents(), start.getContents(), end.getContents(), positions));
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		ReadFromExcel test = new ReadFromExcel();
		test.setInputFile("c:/temp/lars.xls");
		test.read();
	}
}