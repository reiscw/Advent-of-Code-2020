import java.util.*;
import java.io.*;

public class AdventOfCode3 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input3.txt"));
		ArrayList<String> fileRead = new ArrayList<String>();
		while (in.hasNext()) fileRead.add(in.nextLine());
		TobogganHill myHill = new TobogganHill(fileRead);
		int[] results = new int[5];
		results[0] = simulation(myHill, 1, 1);
		myHill.reset();
		results[1] = simulation(myHill, 3, 1);
		myHill.reset();
		results[2] = simulation(myHill, 5, 1);
		myHill.reset();
		results[3] = simulation(myHill, 7, 1);
		myHill.reset();
		results[4] = simulation(myHill, 1, 2);
		System.out.println(results[1]);
		int product = 1;
		for (int result : results)
			product *= result;
		System.out.println(product);
	}
	
	public static int simulation(TobogganHill hill, int right, int down) {
		int count = 0;
		while (hill.canMove(down)) {
			if (hill.isTree())
				count++;
			hill.move(right, down);
		}
		// necessary to handle last time
		if (hill.isTree())
			count++;
		return count;
	}
}

class TobogganHill {
	
	private int row;
	private int column;
	private ArrayList<String> hill;
	private int width;
	
	public TobogganHill(ArrayList<String> input) {
		row = 0;
		column = 0;
		hill = input;
		width = hill.get(0).length();
	}
	
	public boolean canMove(int downAmount) {
		return row + downAmount < hill.size();
	}
	
	private char getLetter() {
		return hill.get(row).charAt(column % width);
	}
	
	public boolean isTree() {
		return getLetter() == '#';
	}
	
	public void move(int right, int down) {
		row = row + down;
		column = column + right;
	}
	
	public void reset() {
		row = 0;
		column = 0;
	}
}
