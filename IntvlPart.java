import java.util.*;

//times are stored as integers and in 24-hr time. 9AM is 900 and 2:30PM is 1430
public class IntvlPart {
	ArrayList<Lecture> lectures = new ArrayList<Lecture>();

	public static void main(String[] args){
		IntvlPart program = new IntvlPart();
		program.intervalPartition();
		System.out.println(0);
	}

	public IntvlPart(){		//constructor creates jobs from the problem
		lectures.add(new Lecture(900, 1030, "A"));
		lectures.add(new Lecture(900, 1230, "B"));
		lectures.add(new Lecture(900, 1030, "C"));
		lectures.add(new Lecture(1100, 1230, "D"));
		lectures.add(new Lecture(1100, 1400, "E"));
		lectures.add(new Lecture(1300, 1430, "F"));
		lectures.add(new Lecture(1300, 1430, "G"));
		lectures.add(new Lecture(1400, 1630, "H"));
		lectures.add(new Lecture(1500, 1630, "I"));
		lectures.add(new Lecture(1500, 1630, "J"));
	}

	public void intervalPartition(){
		Collections.sort(lectures);		//sort jobs by start time

		ArrayList<Room> allRooms = new ArrayList<Room>();	//list of all lectures in each room. Printed at the end to show exactly which job went in which room
		PriorityQueue<Room> roomsQueue = new PriorityQueue<Room>();	//priority Queue holds Rooms, ordered by the finish time of the last lecture added to the room
		String outputMessage = "";	//build outout message to show steps of the algorithm

		int roomCount =0;	//running total of how many rooms exist

		Room firstRoom = new Room(lectures.get(0), ++roomCount);		//get the 1st room (with earliest finish time)
		roomsQueue.add(firstRoom);	//add to quque
		allRooms.add(firstRoom);	//also add to list of rooms to keep track at the end
		outputMessage+=lectures.get(0)+"\tadded in room "+firstRoom.getId();

		for(int i=1; i<lectures.size(); i++){
			Room oldRoom = roomsQueue.peek();			//get temporary room (earliest finishTime from top of Priority Queue)
			Lecture currentLecture = lectures.get(i);	//current in the sense of the loop

			if(currentLecture.getStartTime() >= oldRoom.getLastFinished()){		// if it's compatible & starts after the last thing finished.   MUST BE >= OR JOBS CAN'T BE SCHEDULED RIGHT AFTER EACH OTHER (h after e)
				// System.out.println("start "+currentLecture.getStartTime()+" 0 new lectures "+oldRoom.getLectures() + " lastFinished "+oldRoom.getLastFinished());
				oldRoom.addLecture(currentLecture);	//adds another lecture & increases the room's finish time behind the scenes
				//these 2 lines do increaseKey. Since I added a new lecture to the room, the finish time for the room changed. I really want to update it,  but I do this in the Queue by removing the old value & adding it back in since it's lastFinished has now changed
				roomsQueue.remove();
				roomsQueue.add(oldRoom);	//looks redundant,  but actually forces the heap to reorder with a new lastFinished for the same room
				// System.out.println("oldRoom lastFin "+oldRoom.getLastFinished());
				// System.out.println("new lectures "+oldRoom.getLectures() + " lastFinished "+oldRoom.getLastFinished());
				outputMessage+="\n"+currentLecture+"\tadded in room "+oldRoom.getId();
			}else{
				Room newRoom = new Room(currentLecture, ++roomCount);	//add a new room
				// System.out.println("  new room "+newRoom.getId());
				roomsQueue.add(newRoom);	//add to queue
				// System.out.println("\t||start "+currentLecture.getStartTime()+"  new lectures "+newRoom.getLectures() + " lastFinished "+newRoom.getLastFinished());
				allRooms.add(newRoom);		//add new room to the list of all rooms
				outputMessage+="\n"+currentLecture+"\tadded in room "+newRoom.getId();
			}
		}
		System.out.println(outputMessage);
		System.out.println("\nContents of Rooms");
		for(Room room: allRooms){
			System.out.println(room.getId() +" "+ room.getLectures());
		}
	}


	private class Room implements Comparable<Room>{
		private LinkedList<Lecture> lectures = new LinkedList<Lecture>();
		private int lastFinished;
		private int id;

		public Room(Lecture lecture, int id){
			addLecture(lecture);
			this.id = id;
		}

		public int getLastFinished(){
			return lastFinished;
		}

		public int getId(){
			return id;
		}

		public LinkedList<Lecture> getLectures(){
			return lectures;
		}

		public Lecture getLastLecture(){
			return lectures.getLast();
		}

		public void addLecture(Lecture lecture){
			lectures.add(lecture);
			setLastFinished(lecture.getFinishTime());
		}

		private void setLastFinished(int newLastFinished){
			lastFinished = newLastFinished;
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

		@Override
		//Compare based on finish time (for initial sorting)
		public int compareTo(Lecture otherLecture) {
			return this.getFinishTime() - otherLecture.getFinishTime();
		}

		@Override
		public String toString() {
			return "\""+ getId() + "\": [" + getStartTime()+" - "+getFinishTime()+"]";
		}
	}

}