import java.util.*;

public class AdventOfCode15 {
	
	public static void main(String[] args) {
		String input = "17,1,3,16,19,0";
		String[] nums = input.split(",");
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (String num : nums) 
			numbers.add(Integer.parseInt(num));
		part1(numbers, 2020);
		part1(numbers, 30000000);
	}
	
	public static void part1(ArrayList<Integer> numbers, int target) {
		HashMap<Integer, Information> dictionary = new HashMap<Integer, Information>();
		int turnCount = 0;
		int lastNumber = 0;
		for (Integer number : numbers) {
			dictionary.put(number, new Information(true, turnCount, turnCount));
			turnCount++;
			lastNumber = number;
		}
		while (true) {
			// check if the last number was spoken for the first time
			if (dictionary.containsKey(lastNumber) && dictionary.get(lastNumber).getFirstTime() == true) {
				int previous = dictionary.get(0).getCurrentTurnCount();
				dictionary.put(0, new Information(false, previous, turnCount));
				turnCount++;
				lastNumber = 0;
			} else {
				int number = dictionary.get(lastNumber).delta();
				if (dictionary.containsKey(number)) {
					int previous = dictionary.get(number).getCurrentTurnCount();
					dictionary.put(number, new Information(false, previous, turnCount));
				} else {
					dictionary.put(number, new Information(true, turnCount, turnCount));
				}
				turnCount++;
				lastNumber = number;
			}
			if (turnCount == target) {
				System.out.println(lastNumber);
				break;
			}
		}
	}
	
}

class Information {
	
	private boolean firstTime;
	private int previousTurnCount;
	private int currentTurnCount;
	
	public Information(boolean firstTime, int previousTurnCount, int currentTurnCount) {
		this.firstTime = firstTime;
		this.previousTurnCount = previousTurnCount;
		this.currentTurnCount = currentTurnCount;
	}
	
	public boolean getFirstTime() {return firstTime;}
	public int getPreviousTurnCount() {return previousTurnCount;}
	public int getCurrentTurnCount() {return currentTurnCount;}
	public int delta() {return currentTurnCount - previousTurnCount;}
	
	public String toString() {
		return "" + firstTime + " " + previousTurnCount + " " + currentTurnCount;
	}
}
