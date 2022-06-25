import java.io.*;
import java.util.*;

public class AdventOfCode6 {
	
	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String> anyyeses = anyYesQuestions();
		ArrayList<String> allyeses = allYesQuestions();
		part1(anyyeses);
		part2(allyeses);
	}
	
	public static ArrayList<String> anyYesQuestions() throws FileNotFoundException {
		ArrayList<String> result = new ArrayList<String>();
		Scanner in = new Scanner(new File("input6.txt"));
		while (in.hasNextLine()) {
			String group = "";
			while (in.hasNextLine()) {
				String input = in.nextLine();
				if (input.equals("")) break;
				for (int i = 0; i < input.length(); i++) {
					if (group.indexOf(input.charAt(i)) == -1)
						group = group + input.charAt(i);
				}
			}
			result.add(group);
		}
		return result;
	}
	
	public static ArrayList<String> allYesQuestions() throws FileNotFoundException {
		ArrayList<String> result = new ArrayList<String>();
		Scanner in = new Scanner(new File("input6.txt"));
		while (in.hasNextLine()) {
			String temp = "";
			String group = "";
			int lineCount = 0;
			while (in.hasNextLine()) {
				String input = in.nextLine();
				if (input.equals("")) break;
				for (int i = 0; i < input.length(); i++) {
					temp = temp + input.charAt(i);
				}
				lineCount++;
			}
			for (char letter = 'a'; letter <= 'z'; letter++) {
				if (letterCount(temp, letter) == lineCount) {
					group = group + letter;
				}
			}
			result.add(group);
		}
		return result;
	}
	
	public static int letterCount(String word, char letter) {
		int count = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == letter) 
				count++;
		}
		return count;
	}
	
	public static void part1(ArrayList<String> yeses) {
		int count = 0;
		for (String yes : yeses) 
			count = count + yes.length();
		System.out.println(count);
	}
	
	public static void part2(ArrayList<String> yeses) {
		part1(yeses);
	}
	
}
