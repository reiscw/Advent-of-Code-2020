import java.io.*;
import java.util.*;

public class AdventOfCode11 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input11.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while (in.hasNextLine()) lines.add(in.nextLine());
		char[][] grid = new char[lines.size()][lines.get(0).length()];
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				grid[i][j] = lines.get(i).charAt(j);
			}
		}
		part1(grid);
		part2(grid);
	}
	
	public static void part1(char[][] grid) {
		GridSimulation sim = new GridSimulation(grid);
		while (true) {
			if (sim.step() == 0)
				break;
		}
		System.out.println(sim.occupiedSeats());
	}
	
	public static void part2(char[][] grid) {
		RevisedGridSimulation sim = new RevisedGridSimulation(grid);
		while (true) {
			if (sim.step() == 0)
				break;
		}
		System.out.println(sim.occupiedSeats());
	}
	
}

class GridSimulation {
	
	private char[][] grid;
	
	public GridSimulation(char[][] aGrid) {
		grid = aGrid;
	}
	
	public int step() {
		int count = 0;
		char[][] temp = new char[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 'L' && adjacentCount(i,j) == 0) {
					temp[i][j] = '#';
					count++;
				} else if (grid[i][j] == '#' && adjacentCount(i,j) >= 4) {
					temp[i][j] = 'L';
					count++;
				} else {
					temp[i][j] = grid[i][j];
				}
			}
		}
		grid = temp;
		return count;
	}
	
	private int adjacentCount(int row, int column) {
		int count = 0;
		// upper left
		if (column >= 1 && row >= 1 && grid[row-1][column-1] == '#')
			count++;
		// upper
		if (row >= 1 && grid[row-1][column] == '#')
			count++;
		// upper right
		if (row >= 1 && column <= grid[0].length - 2 && grid[row-1][column+1] == '#')
			count++;
		// left
		if (column >= 1 && grid[row][column-1] == '#')
			count++;
		// right
		if (column <= grid[0].length - 2 && grid[row][column+1] == '#')
			count++;
		// lower left
		if (column >= 1 && row <= grid.length - 2 && grid[row+1][column-1] == '#')
			count++;
		// lower
		if (row <= grid.length - 2 && grid[row+1][column] == '#')
			count++;
		// lower right
		if (row <= grid.length - 2 && column <= grid[0].length - 2 && grid[row+1][column+1] == '#')
			count++;
		return count;
	}
	
	public int occupiedSeats() {
		int count = 0;
		for (char[] line : grid) {
			for (char ch : line) {
				if (ch == '#')
					count++;
			}
		}
		return count;
	}
	
}

class RevisedGridSimulation {
	
	private char[][] grid;
		
	public RevisedGridSimulation(char[][] aGrid) {
		grid = aGrid;
	}

	public int step() {
		int count = 0;
		char[][] temp = new char[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 'L' && adjacentCount(i,j) == 0) {
					temp[i][j] = '#';
					count++;
				} else if (grid[i][j] == '#' && adjacentCount(i,j) >= 5) {
					temp[i][j] = 'L';
					count++;
				} else {
					temp[i][j] = grid[i][j];
				}
			}
		}
		grid = temp;
		return count;
	}
	
	private boolean isValid(int row, int column) {
		return row >= 0 && row <= grid.length - 1 && column >= 0 && column <= grid[0].length - 1;
	}
	
	private int adjacentCount(int row, int column) {
		int count = 0;
		int temprow, tempcol;
		// upper left
		temprow = row;
		tempcol = column;
		while (true) {
			temprow--;
			tempcol--;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// upper
		temprow = row;
		tempcol = column;
		while (true) {
			temprow--;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// upper right
		temprow = row;
		tempcol = column;
		while (true) {
			temprow--;
			tempcol++;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// left
		temprow = row;
		tempcol = column;
		while (true) {
			tempcol--;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// right
		temprow = row;
		tempcol = column;
		while (true) {
			tempcol++;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// lower left
		temprow = row;
		tempcol = column;
		while (true) {
			temprow++;
			tempcol--;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// lower
		temprow = row;
		tempcol = column;
		while (true) {
			temprow++;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		// lower right
		temprow = row;
		tempcol = column;
		while (true) {
			temprow++;
			tempcol++;
			if (!isValid(temprow, tempcol)) {
				break;
			}
			if (grid[temprow][tempcol] == '#') {
				count++;
				break;
			} else if (grid[temprow][tempcol] == 'L') {
				break;
			}
		}
		return count;
	}
	
	public int occupiedSeats() {
		int count = 0;
		for (char[] line : grid) {
			for (char ch : line) {
				if (ch == '#')
					count++;
			}
		}
		return count;
	}
}
