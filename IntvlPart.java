import java.io.*;
import java.util.*;

public class IntvlPart {
	ArrayList<Lecture> lectures = new ArrayList<Lecture>();		//list of lectures to be scheduled

	public static void main(String[] args){
		IntvlPart program = new IntvlPart();
		program.intervalPartition();
	}

	public IntvlPart(){		//constructor creates jobs from the problem. Sorted later, can be in any order
		lectures.add(new Lecture(900, 1030, "A"));
		lectures.add(new Lecture(900, 1230, "B"));
		lectures.add(new Lecture(1100, 1230, "D"));
		lectures.add(new Lecture(900, 1030, "C"));
		lectures.add(new Lecture(1100, 1400, "E"));
		lectures.add(new Lecture(1300, 1430, "F"));
		lectures.add(new Lecture(1300, 1430, "G"));
		lectures.add(new Lecture(1400, 1630, "H"));
		lectures.add(new Lecture(1500, 1630, "I"));
		lectures.add(new Lecture(1500, 1630, "J"));
	}

	public void intervalPartition(){
		Collections.sort(lectures);		//sort lectures/jobs by start time

		ArrayList<Room> allRooms = new ArrayList<Room>();	//list of all rooms & their respective lectures
		PriorityQueue<Room> roomsQueue = new PriorityQueue<Room>();		//priority Queue holds Rooms, ordered by the finish time of the last lecture added
		String outputMessage = "";		//build output message to show steps of the algorithm (printed at the end)

		int roomCount =0;	//running total of how many rooms exist

		Room firstRoom = new Room(lectures.get(0), ++roomCount);		//Create 1st room & add 1st lecture (earliest start time)
		roomsQueue.add(firstRoom);	//add to queue
		allRooms.add(firstRoom);	//add to list of rooms & their contents
		outputMessage+=lectures.get(0)+"\tadded in room "+firstRoom.getId();

		for(int i=1; i<lectures.size(); i++){		//loop over all lectures (skipping 1st since already added)
			Room currentRoom = roomsQueue.peek();		//get temporary room (earliest finishTime from top of Priority Queue)
			Lecture currentLecture = lectures.get(i);	//current lecture from the position in the loop

			if(currentLecture.getStartTime() >= currentRoom.getLastFinished()){		//Compatible if it starts AFTER or @ the same time as the old lecture Finished the last thing finished.  MUST BE >= OR JOBS CAN'T BE SCHEDULED RIGHT AFTER EACH OTHER (h after e)
				currentRoom.addLecture(currentLecture);		//adds another to current room. Increases the room's finish time behind the scenes
				//these 2 lines do "increaseKey". Since I added a new lecture to the room, the finish time for the room changed. I really want to update it, but I do this in the Queue by removing the old value & adding it back in which forces the Queue to resort itself
				roomsQueue.remove();
				roomsQueue.add(currentRoom);	//looks redundant,  but actually forces the heap to reorder with a new lastFinished for the same room
				outputMessage+="\n"+currentLecture+"\tadded in room "+currentRoom.getId();
			}
			else{
				Room newRoom = new Room(currentLecture, ++roomCount);	//create a new room
				roomsQueue.add(newRoom);	//add to queue
				allRooms.add(newRoom);		//add new room to the list of all rooms
				outputMessage+="\n" + currentLecture + "\tadded in room " + newRoom.getId();
			}
		}
		
		outputMessage+="\n\nContents of Rooms";
		for(Room room: allRooms){
			outputMessage+= "\nRoom " + room.getId() + ": " + room.getLectures();
		}
		displayMessage(outputMessage);
		
	}

	
	private void displayMessage(String outputMessage) {		
		System.out.println(outputMessage);
		
		try (PrintWriter outputFile = new PrintWriter( new File("output.txt") ); ){
			outputFile.println(outputMessage);
		} catch (FileNotFoundException e) {
			System.out.println("Error! Couldn't create file");
		}
	}


	private class Room implements Comparable<Room>{
		private LinkedList<Lecture> lectures = new LinkedList<Lecture>();	//lectures that the room holds
		private int lastFinished;	//time that the last lecture currently in the room finishes
		private int id;				//to distinguish rooms (e.g. Room 1, Room 3,  etc.)

		public Room(Lecture lecture, int id){
			addLecture(lecture);		//adding lecture updates lastFinished
			this.id = id;
		}

		public int getLastFinished(){
			return lastFinished;
		}

		public int getId(){
			return id;
		}

		public LinkedList<Lecture> getLectures(){	//return list of lectures in the room
			return lectures;
		}

		public Lecture getLastLecture(){
			return lectures.getLast();
		}

		//Add lecture to the room & update lastFinished for the room to match the new lecture added
		public void addLecture(Lecture lecture){
			lectures.add(lecture);
			lastFinished = (lecture.getFinishTime());	//very important to update, or the room won't update
		}

		@Override
		//Compare based on finish time (for the priority queue)
		public int compareTo(Room otherRoom) {
			return this.getLastFinished() - otherRoom.getLastFinished();
		}
	}


	private class Lecture implements Comparable<Lecture>{
		private int startTime;
		private int finishTime;
		private String id;

		public Lecture(int startTime, int finishTime, String id){
			this.startTime = startTime;
			this.finishTime = finishTime;
			this.id = id;
		}

		public int getStartTime(){
			return startTime;
		}

		public int getFinishTime(){
			return finishTime;
		}
		
		public String getId(){
			return id;
		}

		//Convert from int time like 900 to string "9:00"
		public String getTimeFromInt(int intTime){
			String stringTime = intTime + "";		//create string representation of time
			//use substrings to "insert" a ":" character to separate hours & minutes
			if(stringTime.length() == 3){		//3-digit times like 900 or 830 ("9:00" & "8:30" respectively)
				stringTime = stringTime.substring(0, 1) + ":" + stringTime.substring(1, 3);
			}
			else if(stringTime.length() == 4){	//4 digit times like 1030 or 1430 ("10:30" & "14:30" respectively)
				stringTime = stringTime.substring(0, 2) + ":" + stringTime.substring(2, 4);
			}
			return stringTime;
		}

		//Convert from time like "10:30" to int 1030
		public int getIntFromTime(String stringTime){
			stringTime = stringTime.replace(":", "");
			return Integer.parseInt(stringTime);
		}


		@Override
		//Compare based on finish time (for initial sorting)
		public int compareTo(Lecture otherLecture) {
			return this.getFinishTime() - otherLecture.getFinishTime();
		}

		@Override
		public String toString() {
			String strStartTime = getTimeFromInt( this.getStartTime() );
			String strFinishTime = getTimeFromInt( this.getFinishTime() );
			return "(\""+ getId() + "\"  " + strStartTime+" - "+strFinishTime+")";
		}
	}

}