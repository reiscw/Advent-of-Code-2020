import java.io.*;
import java.util.*;

public class AdventOfCode2 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input2.txt"));
		ArrayList<Entry> list = new ArrayList<Entry>();
		while (in.hasNext()) list.add(new Entry(in.nextLine()));
		part1(list);
		part2(list);
	}
	
	public static void part1(ArrayList<Entry> list) {
		int count = 0;
		for (Entry element : list) {
			if (element.isValid1())
				count++;
		}
		System.out.println(count);
	}
	
	public static void part2(ArrayList<Entry> list)  {
		int count = 0;
		for (Entry element : list) {
			if (element.isValid2())
				count++;
		}
		System.out.println(count);
	}
}

class Entry {
	
	private int min;
	private int max;
	private char letter;
	private String password;
	
	public Entry(String text) {
		int dashLocation = text.indexOf("-");
		int colonLocation = text.indexOf(":");
		min = Integer.parseInt(text.substring(0, dashLocation));
		max = Integer.parseInt(text.substring(dashLocation+1, colonLocation-2));
		letter = text.charAt(colonLocation - 1);
		password = text.substring(colonLocation + 2);
	}

	public boolean isValid1() {
		int count = 0;
		for (int i = 0; i < password.length(); i++) {
			if (password.charAt(i) == letter) {
				count++;
			}
		}
		return min <= count && count <= max;
	}
	
	public boolean isValid2() {
		return password.charAt(min-1) == letter ^ password.charAt(max-1) == letter;
	}
	
}
