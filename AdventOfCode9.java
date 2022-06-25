import java.io.*;
import java.util.*;

public class AdventOfCode9 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input9.txt"));
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		while (in.hasNextInt()) numbers.add(in.nextInt());
		int result = part1(numbers);
		part2(result, numbers);
	}
	
	public static int part1(ArrayList<Integer> numbers) {
		int index = 0;
		while (true) {
			boolean found = false;
			int target = numbers.get(index + 25);
			for (int i = index; i < index + 25 && !found; i++) {
				for (int j = index; j < index + 25 && !found; j++) {
					if (numbers.get(i) + numbers.get(j) == target)
						found = true;
				}
			}
			if (!found) {
				System.out.println(target);
				return target;
			}
			index++;
		}
	}
	
	public static void part2(int result, ArrayList<Integer> numbers) {
		// start at first position
		int startIndex = 0;
		// keep adding until you hit the target, or you pass the target
		int sum = 0;
		int index = startIndex;
		while (startIndex < numbers.size()) {
			while (sum < result) {
				sum = sum + numbers.get(index);
				index++;
			}
			// if you hit the target, get the min and max in that range
			if (sum == result) {
				int min = numbers.get(startIndex);
				int max = numbers.get(startIndex);
				for (int i = startIndex; i < index; i++) {
					if (numbers.get(i) > max) 
						max = numbers.get(i);
					if (numbers.get(i) < min)
						min = numbers.get(i);
				}
				System.out.println(min + max);
				return;
			// otherwise move to next position
			} else {
				startIndex++;
				index = startIndex;
				sum = 0;
			}
		}
	}
	
}
