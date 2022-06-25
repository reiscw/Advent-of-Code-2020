import java.util.*;
import java.io.*;

public class AdventOfCode1 {
	
	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Integer> list = getList();
		part1(list);
		part2(list);
	}

	public static void part1(ArrayList<Integer> list) {
		for (int i = 0; i < list.size(); i++) 
			for (int j = i+1; j < list.size(); j++) 
				if (list.get(i)+list.get(j) == 2020) {
					System.out.println(list.get(i)*list.get(j)); 
					return;
				}
	}
	
	public static void part2(ArrayList<Integer> list) {
		for (int i = 0; i < list.size(); i++) 
			for (int j = i+1; j < list.size(); j++) 
				for (int k = j+1; k < list.size(); k++) 
					if (list.get(i)+list.get(j)+list.get(k) == 2020) {
						System.out.println(list.get(i)*list.get(j)*list.get(k)); 
						return;
					}
	}

	public static ArrayList<Integer> getList() throws FileNotFoundException {
		Scanner in = new Scanner(new File("input1.txt"));
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (in.hasNextInt()) list.add(in.nextInt());
		return list;
	}
	
}
