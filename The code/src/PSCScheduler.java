import java.io.*;
import java.util.*;


public class PSCScheduler {
	// input files
	//private static final String dataFile = "data/ExEMPLshort.txt"; // 20 people
	private static final String dataFile = "ExEMPL.txt"; // 40 people
	private static final int POSITION_COLUMN_START = 6; // number of columns in input file before the position columns
	
	// main data structures
	private static Map<Integer, Person> people;
	public static Map<Integer, String> positions = new HashMap<Integer, String>(); // maps from position ID to position name
		// maps from position ID to list of people trained to work that position
	private static Map<Integer, ArrayList<Integer>> isTrainedList = new HashMap<Integer, ArrayList<Integer>>();
	
	private static int[][] schedule;
	
	// other global variables
	private static int NUM_POSITIONS;
	private static int NUM_TIMESLOTS = 14;
	
	// debugging
	private static int WATCH_ID = -1;
	private static int scheduleTries = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// read input file and create Person objects
		createPersons();
		
		boolean scheduleCreated = false;
		// begin scheduling loop
		System.out.println("Creating schedule...");
		long scheduleStart = System.currentTimeMillis();
		while (!scheduleCreated) {
			// schedule breaks for everyone
			Iterator it = people.keySet().iterator();
			while (it.hasNext()) {
				Person person = people.get(it.next());
				person.scheduleBreaks(NUM_TIMESLOTS);
			}
			// attempt to schedule around breaks
			scheduleCreated = createSchedule(NUM_TIMESLOTS);
		}
		long scheduleStop = System.currentTimeMillis();
		System.out.println("Schedule created! (" + scheduleTries + " tries)");
		System.out.println("Time taken: " + (scheduleStop - scheduleStart) / 1000.0 + "s");
		printSchedule();
		//createScheduleRecursive(16);
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);
		do {
			System.out.println("\n\n");
			System.out.print("Print schedule of person with ID# (max " + people.size() +"): ");
			
			String next = console.nextLine();
			int personID = Integer.parseInt(next);
			Person person = people.get(personID);
			
			System.out.println("--------------");
			System.out.println("Shift: " + person.shiftStart() + " to " + person.shiftEnd() 
					+ " (" + toTime(person.shiftStart()) + " to " + toTime(person.shiftEnd()) + ")  (end times exclusive)");
			System.out.println("Age: " + person.age());
			System.out.println("Key:");
			System.out.println("-\tnot during shift");
			System.out.println("???\tduring shift, but not scheduled");
			System.out.println("--------------");
			
			person.printSchedule();
			WATCH_ID = personID;
			printSchedule();
		} while (true);
	}
	
	// read input file and create Person objects
	// initializes and populates main data structures
	public static void createPersons() {
		people = new HashMap<Integer, Person>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			String line = br.readLine();
			String[] headers = line.split("\t");
			NUM_POSITIONS = headers.length - POSITION_COLUMN_START;
			// map position IDs to names
			for (int i = POSITION_COLUMN_START; i < headers.length; i++) {
				int positionID = i - POSITION_COLUMN_START; // position IDs start from 0
				positions.put(positionID, headers[i]);
			}
			// get iterator over 'positions' map keyset
			Set<Integer> positionIDs = positions.keySet();
			Iterator<Integer> positionIt = positionIDs.iterator();
			// initialize an empty isTrained list for each position
			while (positionIt.hasNext()) {
				int positionID = (Integer)positionIt.next();
				isTrainedList.put(positionID, new ArrayList<Integer>());
			}
			
			while ((line = br.readLine()) != null) {
				// reads from tab-separated text input file, saved from Excel
				String[] data = line.split("\t");
				int id = Integer.parseInt(data[0]);
				String name = data[1];
				String title = data[2];
				int age = Integer.parseInt(data[3]);
				double start = Double.parseDouble(data[4]); // beginning of shift
				double end = Double.parseDouble(data[5]); // end of shift
				Map<Integer, Boolean> positions = new HashMap<Integer, Boolean>();
				for (int i = POSITION_COLUMN_START; i < headers.length; i++) {
					boolean isTrained = Boolean.valueOf(data[i]);
					int positionID = i - POSITION_COLUMN_START;
					// store whether person is trained for curent positionID
					positions.put(positionID, isTrained);
					if (isTrained) {
						// add person to isTrained list of current positionID
						isTrainedList.get(positionID).add(id);
					}
				}
				// create person and add to people list
				people.put(id, new Person(id, name, title, age, start, end, positions));
				//System.out.println("Person added: ID=" + id + " " + name);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
		}
		// debugging
//		Set<Integer> keys = positions.keySet();
//		for(int key : keys) {
//			System.out.println(positions.get(key));
//		}
	}
	
	public static boolean createSchedule(int timeSlots) {
		// keeping track of program status
		scheduleTries++;
		//System.out.println("Try " + scheduleTries);
		if (scheduleTries % 1000 == 0) {
			System.out.println(scheduleTries + " tries...");
			//System.out.println("Error: Schedule cannot be created.");
			//System.exit(1);
		}
		// actual scheduling code
		Random random = new Random();
		schedule = new int[timeSlots][NUM_POSITIONS];
		
		for (int i = 0; i < timeSlots; i++) { // for each time slot
			schedule[i] = new int[NUM_POSITIONS]; // create new array for time slot
			for (int j = 0; j < NUM_POSITIONS; j++) { // for each position in time slot i
				//System.out.println("Scheduling time slot " + i);
				//System.out.println("Scheduling position " + j + ": " + positions.get(j));
				// get list of everyone trained for position with positionID j
				ArrayList<Integer> isTrained = new ArrayList<Integer>(isTrainedList.get(j));
				int personID;
				
				boolean valid = true; // whether a person can work this position, set to false as soon as a check fails
				do {
					valid = true;
					// check if anyone is available to work this shift
					if (isTrained.isEmpty()) {
						//System.out.println("Error: No one is available to work this position");
						personID = -1;
						break;
					}
					
					// chose random index from isTrained list
					int randomInt = random.nextInt(isTrained.size());
					personID = isTrained.get(randomInt); // get ID of person at random index
					//System.out.println("Checking ID=" + personID);
					
					// check if current time slot is during person's shift
					Person person = people.get(personID);
					int startTime = person.shiftStart();
					int endTime = person.shiftEnd();
					//System.out.println("Timeslot: " + startTime + " to " + endTime);
					if (i < startTime || i >= endTime) {
						valid = false;
						//System.out.println("Error: Person does not work at this time.");
					}
					
					// check if person is already scheduled this timeslot
					for (int k = 0; k < NUM_POSITIONS; k++) {
						if (schedule[i][k] == personID) {
							valid = false;
							//System.out.println("Error: Person already scheduled in this time slot.");
						}
					}
					// check if person worked this position last time slot
					if (i > 0 && schedule[i-1][j] == personID) {
						valid = false;
						//System.out.println("Error: Person was scheduled for this position in the last time slot.");
					}
					// check if person is scheduled for a break
					if (person.isOnBreak(i)) {
						valid = false;
						//System.out.println("Error: Person is scheduled for break at this time slot.");
					}
					
					// remove person from list
					if (!valid) {
						isTrained.remove(randomInt);
					}
				} while (!valid);
				// all tests passed, schedule this person
				schedule[i][j] = personID;
				if (personID >= 0) {
					people.get(personID).schedulePosition(i, j);
				}
				//System.out.println("Person scheduled: ID=" + personID);
				//System.out.println("-------------------\n");
			}			
		}
		return checkEssentialPositions();
	}
	
	public static boolean checkEssentialPositions() {
		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < 5; j++) {
				if (schedule[i][j] == -1) {
					return false;
				}
			}
		}
		return true;
	}
	
//	public static void createScheduleRecursive(int timeSlots) {
//		schedule = new int[timeSlots][NUM_POSITIONS];
//		Random random = new Random();
//		ArrayList<Integer> isAvailable = new ArrayList<Integer>(isTrainedList.get(0));
//		createScheduleRecursive(random, timeSlots, 0, 0, isAvailable);
//		System.out.println("----------------");
//		System.out.println("Schedule Created!");
//		printSchedule();
//	}
//	private static boolean createScheduleRecursive(Random random, int timeSlots, int timeSlot, int positionID, ArrayList<Integer> isAvailable) {
//		if (timeSlot >= timeSlots) return true;
//		if (positionID >= NUM_POSITIONS) {
//			positionID = 0;
//			timeSlot++;
//			return createScheduleRecursive(random, timeSlots, timeSlot, positionID, isAvailable);
//		}
//		
//		if (positionID == 0) {
//			schedule[timeSlot] = new int[NUM_POSITIONS];
//			isAvailable = new ArrayList<Integer>(isTrainedList.get(positionID));
//		}
//		// attempt to schedule a person
//		int personID;
//		boolean valid = true;
////		System.out.println("--------------------");
////		System.out.println("Scheduling time slot " + timeSlot);
////		System.out.println("Scheduling position " + positionID + ": " + positions.get(positionID));
//		if (isAvailable.isEmpty()) {
////			System.out.println("Error: No one is available to work this position");
//			personID = -1;
//			schedule[timeSlot][positionID] = -1;
//			positionID++;
//			return createScheduleRecursive(random, timeSlots, timeSlot, positionID, isAvailable);
//		}
//		int randomInt = random.nextInt(isAvailable.size());
//		personID = isAvailable.get(randomInt);
////		System.out.println("Checking ID=" + personID);
//		
//		// check if person is already scheduled this timeslot
//		for (int i = 0; i < NUM_POSITIONS; i++) {
//			if (schedule[timeSlot][i] == personID) {
//				valid = false;
////				System.out.println("Error: Person already scheduled in this time slot.");
//			}
//		}
//		// check if person worked this position last time slot
//		if (timeSlot > 0 && schedule[timeSlot-1][positionID] == personID) {
//				valid = false;
////				System.out.println("Error: Person was scheduled for this position in the last time slot.");
//		}
//		// check if current time slot is during person's shift
//		Person person = people.get(personID);
//		int startTime = toTimeSlot(person.shiftStart());
//		int endTime = toTimeSlot(person.shiftEnd());
////		System.out.println("Timeslot: " + timeSlot);
////		System.out.println("Timeslot: " + startTime + " to " + endTime);
//		if (timeSlot < startTime || timeSlot >= endTime) {
//			valid = false;
////			System.out.println("Error: Person does not work at this time.");
//		}
//		
//		// remove person from list
//		if (!valid) {
//			isAvailable.remove(randomInt);
//		} else {
//			schedule[timeSlot][positionID] = personID;
////			System.out.println("Person ID=" + personID + " scheduled!");
//			positionID++;
//		}
//		return createScheduleRecursive(random, timeSlots, timeSlot, positionID, isAvailable);
//	}
	
	public static void printSchedule() {
		System.out.println("--------------");
		// print column headers
		System.out.print("Slot\tTime\t");
		for (int i = 0; i < NUM_POSITIONS; i++) {
			String posName = positions.get(i);
			int stringEnd = Math.min(6, posName.length());
			System.out.print(posName.substring(0, stringEnd) + "\t");
		}
		System.out.println();
		// print schedule, line by line (time slot by time slot)
		for (int i = 0; i < schedule.length; i++) {
			System.out.print(i + "\t");
			System.out.print(toTime(i) + "\t");
			for (int j = 0; j < NUM_POSITIONS; j++) {
				int personID = schedule[i][j];
				if (personID == -1) { // nobody scheduled
					System.out.print("-\t");
				} else if (personID == WATCH_ID) { // marked watched ID; for debugging
					System.out.print("[" + schedule[i][j] + "]\t");
				} else {
					System.out.print(schedule[i][j] + "\t");
				}
			}
			System.out.println();
		}
	}
	
	// helper methods
	
	// convert from military hour to time slot
	public static int toTimeSlot(double time) {
		return (int)(time - 10) * 2;
	}
	// convert from time slot to military hour
	public static double toHour(int timeSlot) {
		return timeSlot / 2.0 + 10;
	}
	// convert from time slot to military hour
	public static String toTime(int timeSlot) {
		double milTime = toHour(timeSlot);
		String time = "";
		int hour = (int) milTime;
		String minute;
		if (hour == milTime) {
			minute = "00";
		} else {
			minute = "30";
		}
		if (hour > 12) hour -= 12;

		return hour + ":" + minute;
	}
}
