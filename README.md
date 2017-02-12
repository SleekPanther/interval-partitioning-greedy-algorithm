# Jave Interval Partitioning Greedy Algorithm
Java Implementation of the Interval Partitioning greedy algorithm  
**Given a set of lectures (jobs) with start & end times, schedule all lectures to use the fewest rooms (resources)**

## Usage
- Times are treated as integers, but converted to strings when printing output (`getTimeFromInt()` method of `Lecture` class)
- Times are integers. 3-digits for the morning (9AM = 900), 4-digits for afternoons (10:30 = 1030)
- Uses 24-hour clock (2:30 PM is 14:30)
- &nbsp;
- Enter the Lectures/Jobs in `IntvlPart` constructor
- Run the program to view optimal schedule

### Output
1. Prints the room where a lecture is schedued when it is scheduled
2. A List of all the rooms and the lectures in that room

<br>
## Pseudocode
![preudocode](https://cloud.githubusercontent.com/assets/15304528/22863520/a5cffba0-f10f-11e6-9400-30508bb51208.png)

<br>
## Pseudocode (detailed with untime)
![preudocode runtime](https://cloud.githubusercontent.com/assets/15304528/22863521/a731a570-f10f-11e6-99aa-001b80e5f5f1.png)

## Code Details / Notes
