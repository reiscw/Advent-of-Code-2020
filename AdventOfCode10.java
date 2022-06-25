import java.io.*;
import java.util.*;

public class AdventOfCode10 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input10.txt"));
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		while (in.hasNextInt()) numbers.add(in.nextInt());
		Collections.sort(numbers);
		// add outlet
		numbers.add(0, 0);
		// add device charger
		numbers.add(numbers.get(numbers.size()-1)+3);
		part1(numbers);
		part2(numbers);
	}
	
	public static void part1(ArrayList<Integer> numbers) {
		int oneCount = 0;
		int threeCount = 0;
		for (int i = 0; i < numbers.size() - 1; i++) {
			int a = numbers.get(i);
			int b = numbers.get(i+1);
			if (b - a == 1)
				oneCount++;
			else if (b - a == 3)
				threeCount++;
		}
		System.out.println(oneCount*threeCount);
		
	}
	public static void part2(ArrayList<Integer> numbers) {
		List<Partition> partitions = partitions(numbers);
		long result = 1;
		for (Partition partition : partitions) {
			result = result * partition.partitionCount();
		}
		System.out.println(result);
	}
	
	public static List<Partition> partitions(ArrayList<Integer> numbers) {
		List<Partition> result = new ArrayList<Partition>();
		int startIndex = 0;
		int index = 0;
		while (index < numbers.size()-1) {
			while (numbers.get(index+1)-numbers.get(index) < 3) {
				index++;
			}
			List<Integer> partition = numbers.subList(startIndex, index+1);
			result.add(new Partition(partition));
			startIndex = index+1;
			index++;
		}
		return result;
	}
	
}

class Partition {
	
	private List<Integer> elements;
	
	public Partition(List<Integer> list) {
		elements = list;
	}
	
	private boolean isValid(List<Integer> subset) {
		for (int i = 0; i < subset.size() - 1; i++) {
			if (subset.get(i+1) - subset.get(i) > 3)
				return false;
		}
		return true;
	}
	
	public int partitionCount() {
		int count = 0;
		if (elements.size() <= 2) {
			return 1;
		}
		ArrayList<Integer> removables = new ArrayList<Integer>();
		for (int i = 1; i < elements.size()-1; i++) 
			removables.add(elements.get(i));
		ArrayList<ArrayList<Integer>> powerset = powerSet(removables);
		for (int i = 0; i < powerset.size(); i++) {
			powerset.get(i).add(0, elements.get(0));
			powerset.get(i).add(elements.get(elements.size()-1));
			if (isValid(powerset.get(i)))
				count++;
		}
		return count;
	}

	// code to obtain subsets modified from 
	// https://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
	private ArrayList<ArrayList<Integer>> powerSet(List<Integer> original) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		if (original.isEmpty()) {
			result.add(new ArrayList<Integer>());
			return result;
		}
		ArrayList<Integer> list = new ArrayList<Integer>(original);
		Integer head = list.get(0);
		ArrayList<Integer> rest = new ArrayList<Integer>(list.subList(1, list.size())); 
		for (ArrayList<Integer> set : powerSet(rest)) {
			ArrayList<Integer> newSet = new ArrayList<Integer>();
			newSet.add(head);
			newSet.addAll(set);
			Collections.sort(newSet);
			result.add(newSet);
			Collections.sort(set);
			result.add(set);
		}       
		return result;
	}	
	
}
