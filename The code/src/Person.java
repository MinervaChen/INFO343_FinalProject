import java.util.*;


public class Person implements Comparable<Person> {
	private int id;
	private String name;
	private String title;
	//private Level level;
	private int age;
	private int shiftStart;
	private int shiftEnd;
	//private ArrayList<Map<Integer, Boolean>> positions;
	private Map<Integer, Boolean> positions;
	private int numPositions;
	private int lunchID;
	private int breakID;
	
	private int[] schedule;
	
	public Person (int id, String name, String title, int age, double start, double end, Map<Integer, Boolean> positions) {
		this.id = id;
		this.title = title;
		this.age = age;
		this.shiftStart = PSCScheduler.toTimeSlot(start);
		this.shiftEnd = PSCScheduler.toTimeSlot(end);
		this.positions = positions;
		Iterator it = positions.keySet().iterator();
		numPositions = 0;
		while (it.hasNext()) {
			if (positions.get(it.next())) {
				numPositions++;
			}
		}
		// assign lunch and break IDs that don't conflict with existing position IDs
		lunchID = numPositions + 1;
		breakID = numPositions + 2;
	}
	
	public void scheduleBreaks(int timeSlots) {
		schedule = new int[timeSlots];
		// initialize all cells to -1, because 0 is a positionID
		for (int i = 0; i < schedule.length; i++) {
			schedule[i] = -1;
		}
		
		int maxSlotsUntilLunch = 0; // the maximum number of hours a person may work until a 30min lunch break is required (which is really one hour)
		int minSlotsBetweenBreaks = 0; // the maximum number of hours a person may work until a break is required
		int lunchPadding = 0; // extra padding so lunch isn't at ridiculous hours
		
		if (age <= 15) { // 14 & 15
			maxSlotsUntilLunch = 8; // 4 hours
			minSlotsBetweenBreaks = 4; // 2 hours
			lunchPadding = 0;
		} else { // 16 & 17 and adults
			maxSlotsUntilLunch = 10; // 5 hours
			minSlotsBetweenBreaks = 6; // 3 hours
			lunchPadding = 2; // 1 hour
		}
		scheduleBreaks(timeSlots, numPositions, maxSlotsUntilLunch, minSlotsBetweenBreaks, lunchPadding);
	}
	
	private void scheduleBreaks(int timeSlots, int numPositions, int maxSlotsUntilLunch, int minSlotsBetweenBreaks, int lunchPadding) {
		// copy shift times so they can be modified during break calculations
		int shiftStart = this.shiftStart;
		int shiftEnd = this.shiftEnd;
		
		Random random = new Random();
		// if shift is longer than the maximum number of hours before lunch is required
		if (shiftEnd - shiftStart > maxSlotsUntilLunch) {
			// schedule lunch
			int lunchEarliest = shiftEnd - maxSlotsUntilLunch - 2;
			int lunchLatest = shiftStart + maxSlotsUntilLunch - 1 - lunchPadding;
			int lunchStart = random.nextInt(lunchLatest - lunchEarliest) + lunchEarliest;
			schedule[lunchStart] = lunchID;
			schedule[lunchStart + 1] = lunchID; // lunch is an hour long
			//System.out.println("Lunch Window: " + lunchEarliest + " - " + lunchLatest);
			
			// marks the last time slot of the most recent break
			// to be set in the following code and used by the code for scheduling second break
			int lastBreakEnd;
			// only schedule a break before lunch if there's enough time
			if (lunchStart - shiftStart > 3) {
				int firstRoveEarliest = Math.max(lunchStart - minSlotsBetweenBreaks - 1, shiftStart + 1);
				int firstRoveLatest = Math.min(shiftStart + minSlotsBetweenBreaks, lunchStart - 2);
				int firstRoveStart = 
						random.nextInt(firstRoveLatest - firstRoveEarliest) + firstRoveEarliest;
				schedule[firstRoveStart] = breakID;
				//System.out.println("First Rove Window: " + firstRoveEarliest + " - " + firstRoveLatest);
				lastBreakEnd = lunchStart + 1;
			} else { // otherwise schedule the first break after lunch
				int firstRoveEarliest = lunchStart + 4;
				int firstRoveLatest = lunchStart + 7;
				int firstRoveStart = 
						random.nextInt(firstRoveLatest - firstRoveEarliest) + firstRoveEarliest;
				schedule[firstRoveStart] = breakID;
				//System.out.println("First Rove Window: " + firstRoveEarliest + " - " + firstRoveLatest);
				lastBreakEnd = firstRoveStart;
			}
			
			// schedule second break
			int secondRoveEarliest = Math.max(shiftEnd - minSlotsBetweenBreaks - 1, lastBreakEnd + 3);
			int secondRoveLatest = Math.min(lastBreakEnd + 1 + minSlotsBetweenBreaks, shiftEnd - 1);
			int secondRoveStart= 
					random.nextInt(secondRoveLatest - secondRoveEarliest) + secondRoveEarliest;
			schedule[secondRoveStart] = breakID; 
			
			//System.out.println("Second Rove Window: " + secondRoveEarliest + " - " + secondRoveLatest);
		} else { // shift is too short, no lunch
			int shiftMid = (shiftEnd + shiftStart) / 2 - 1;
			int firstRoveStart = random.nextInt(shiftMid - shiftStart - 1) + shiftStart + 1;
			schedule[firstRoveStart] = numPositions + 2;
			//System.out.println("Early Rove Window: " + shiftStart + " - " + shiftMid);
			
			shiftMid = Math.max(shiftMid, firstRoveStart + 2);
			shiftEnd = Math.min(shiftEnd, timeSlots - 1);
			int secondRoveStart = random.nextInt(shiftEnd - shiftMid) + shiftMid;
			schedule[secondRoveStart] = numPositions + 2;
			//System.out.println("Late Rove Window: " + shiftMid + " - " + shiftEnd);
		}
	}
	
	public void schedulePosition(int timeSlot, int positionID) {
		schedule[timeSlot] = positionID;
	}
	
	public void printSchedule() {
		System.out.println("Slot\tTime\tPosition"); // column headers
		for (int i = 0; i < schedule.length; i++) {
			System.out.print(i + "\t" + PSCScheduler.toTime(i) + "\t");
			if (schedule[i] == -1) { // not scheduled
				if (i < shiftStart || i >= shiftEnd) { // time not during shift
					System.out.println("-");
				} else { // time during shift, but not scheduled
					System.out.println("???");
				}
			} else if (schedule[i] == lunchID) {
				System.out.println("[Lunch]");
			} else if (schedule[i] == breakID) {
				System.out.println("[Rove & Report]");
			} else { // print position name
				System.out.println(PSCScheduler.positions.get(schedule[i]));
			}
		}
	}
	
	// getter methods
	public int id() {
		return id;
	}
	public String name() {
		return name;
	}
	public String title() {
		return title;
	}
	public int age() {
		return age;
	}
	public int shiftStart() {
		return shiftStart;
	}
	public int shiftEnd() {
		return shiftEnd;
	}
	// returns whether person is trained for positionID
	public boolean isTrained(int positionID) {
		return positions.get(positionID);
	}
	// returns whether person is scheduled for break during timeSlot
	public boolean isOnBreak(int timeSlot) {
		return schedule[timeSlot] == lunchID || schedule[timeSlot] == breakID;
	}

	@Override
	public int compareTo(Person other) {		
		return this.numPositions - other.numPositions;
	}
}
