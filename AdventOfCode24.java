import java.io.*;
import java.util.*;

public class AdventOfCode24 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input24.txt"));
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		while (in.hasNextLine()) lines.add(processLine(in.nextLine()));
		int[][] hexarray = part1(lines);
		part2(hexarray);
	}
	
	public static ArrayList<String> processLine(String line) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == 'n' || line.charAt(i) == 's') {
				String next = line.substring(i, i + 2);
				result.add(next);
				i++;
			} else {
				result.add("" + line.charAt(i));
			}
		}
		return result;
	}
	
	public static int[][] part1(ArrayList<ArrayList<String>> lines) {
		int[][] hexarray = new int[1000][1000];
		int refi = 500;
		int refj = 500;
		int i = refi;
		int j = refj;
		for (ArrayList<String> line : lines) {
			i = refi;
			j = refj;
			for (String element : line) {
				switch (element) {
					case "nw": i = i - 1; j = j - 1; break;
					case "ne": i = i - 1; j = j + 1; break;
					case "w": j = j - 2; break;
					case "e": j = j + 2; break;
					case "sw": i = i + 1; j = j - 1; break;
					case "se": i = i + 1; j = j + 1; break;
				}
			}
			if (hexarray[i][j] == 0) hexarray[i][j] = 1;
			else hexarray[i][j] = 0;
		}
		int sum = 0;
		for (int[] row : hexarray) {
			for (int element : row) {
				sum += element;
			}
		}
		System.out.println(sum);
		return hexarray;
	}

	public static void part2(int[][] hexarray) {
		for (int t = 1; t <= 100; t++) {
			int[][] newarray = new int[1000][1000];
			for (int i = 0; i < 1000; i++) {
				for (int j = 0; j < 1000; j++) {
					int n = blackNeighbors(hexarray, i, j);
					if (hexarray[i][j] == 1 && (n == 0 || n > 2)) {
						newarray[i][j] = 0;
					} else if (hexarray[i][j] == 0 && n == 2) {
						newarray[i][j] = 1;
					} else {
						newarray[i][j] = hexarray[i][j];
					}
				}
			}
			hexarray = newarray;
		}
		int sum = 0;
		for (int[] row : hexarray) {
			for (int element : row) {
				sum += element;
			}
		}
		System.out.println(sum);
	}

	public static int blackNeighbors(int[][] hexarray, int i, int j) {
		int result = 0;
		// NW
		if (i >= 1 && j >= 1) result += hexarray[i-1][j-1];
		// NE
		if (i >= 1 && j < hexarray[0].length - 1) result += hexarray[i-1][j+1];
		// W
		if (j >= 2) result += hexarray[i][j-2];
		// E
		if (j < hexarray[0].length - 2) result += hexarray[i][j+2];
		// SW
		if (i < hexarray.length - 1 && j >= 1) result += hexarray[i+1][j-1];
		// SE
		if (i < hexarray.length - 1 && j < hexarray[0].length - 1) result += hexarray[i+1][j+1];
		return result;
	}

}
