import java.util.*;
import java.io.*;

public class AdventOfCode17 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input17.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while (in.hasNext()) lines.add(in.next());
		ArrayList<ActiveCube> cubes = new ArrayList<ActiveCube>();
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (lines.get(i).charAt(j) == '#') {
					cubes.add(new ActiveCube(i, j, 0));
				}
			}
		}
		ArrayList<ActiveHyperCube> hypercubes = new ArrayList<ActiveHyperCube>();
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(0).length(); j++) {
				if (lines.get(i).charAt(j) == '#') {
					hypercubes.add(new ActiveHyperCube(i, j, 0, 0));
				}
			}
		}
		part1(cubes);
		part2(hypercubes);
	}
	
	public static void part1(ArrayList<ActiveCube> cubes) {
		for (int i = 0; i < 6; i++) {
			// determine cubes to inactivate
			ArrayList<ActiveCube> toBeRemoved = new ArrayList<ActiveCube>();
			for (ActiveCube cube: cubes) {
				int count = 0;
				for (int[] neighbor : cube.neighbors()) {
					int x = neighbor[0];
					int y = neighbor[1];
					int z = neighbor[2];
					ActiveCube candidate = new ActiveCube(x, y, z);
					if (cubes.contains(candidate)) {
						count++;
					}
				}
				if (count != 2 && count != 3) {
					toBeRemoved.add(cube);
				}
			}
			// determine cubes to activate
			ArrayList<PotentialCube> toBeAdded = new ArrayList<PotentialCube>();
			for (ActiveCube cube: cubes) {
				int[][] neighbors = cube.neighbors();
				for (int[] neighbor : neighbors) {
					int x = neighbor[0];
					int y = neighbor[1];
					int z = neighbor[2];
					PotentialCube candidate = new PotentialCube(x, y, z);
					int index = toBeAdded.indexOf(candidate);
					if (index != -1) {
						toBeAdded.get(index).increment();
					} else {
						toBeAdded.add(candidate);
					}
				}
			}
			// add cubes to be added
			for (PotentialCube cube : toBeAdded) {
				if (cube.getCount() == 3 && !cubes.contains(cube)) {
					int x = cube.getX();
					int y = cube.getY();
					int z = cube.getZ();
					cubes.add(new ActiveCube(x, y, z));
				}
			}
			// remove cubes to be removed
			for (ActiveCube cube : toBeRemoved) {
				cubes.remove(cube);
			}
		}
		System.out.println(cubes.size());
		
	}
	
	public static void part2(ArrayList<ActiveHyperCube> hypercubes) {
		for (int i = 0; i < 6; i++) {
			// determine cubes to inactivate
			ArrayList<ActiveHyperCube> toBeRemoved = new ArrayList<ActiveHyperCube>();
			for (ActiveHyperCube cube: hypercubes) {
				int count = 0;
				for (int[] neighbor : cube.neighbors()) {
					int x = neighbor[0];
					int y = neighbor[1];
					int z = neighbor[2];
					int w = neighbor[3];
					ActiveHyperCube candidate = new ActiveHyperCube(x, y, z, w);
					if (hypercubes.contains(candidate)) {
						count++;
					}
				}
				if (count != 2 && count != 3) {
					toBeRemoved.add(cube);
				}
			}
			// determine cubes to activate
			ArrayList<PotentialHyperCube> toBeAdded = new ArrayList<PotentialHyperCube>();
			for (ActiveHyperCube cube: hypercubes) {
				int[][] neighbors = cube.neighbors();
				for (int[] neighbor : neighbors) {
					int x = neighbor[0];
					int y = neighbor[1];
					int z = neighbor[2];
					int w = neighbor[3];
					PotentialHyperCube candidate = new PotentialHyperCube(x, y, z, w);
					int index = toBeAdded.indexOf(candidate);
					if (index != -1) {
						toBeAdded.get(index).increment();
					} else {
						toBeAdded.add(candidate);
					}
				}
			}
			// add cubes to be added
			for (PotentialHyperCube cube : toBeAdded) {
				if (cube.getCount() == 3 && !hypercubes.contains(cube)) {
					int x = cube.getX();
					int y = cube.getY();
					int z = cube.getZ();
					int w = cube.getW();
					hypercubes.add(new ActiveHyperCube(x, y, z, w));
				}
			}
			// remove cubes to be removed
			for (ActiveHyperCube cube : toBeRemoved) {
				hypercubes.remove(cube);
			}
		}
		System.out.println(hypercubes.size());
	}
}

class ActiveCube {
	
	protected int x;
	protected int y;
	protected int z;
	
	public ActiveCube(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getZ() {return z;}
	
	public boolean equals(Object other) {
		ActiveCube oth = (ActiveCube)other;
		return x == oth.getX() && y == oth.getY() && z == oth.getZ();
	}

	public String toString() {return "" + x + " " + y + " " + z;}
	
	public int[][] neighbors() {
		int[][] result = new int[26][3];
		int index = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				for (int k = z - 1; k <= z + 1; k++) {
					if (!(x == i && y == j && z == k)) {
						result[index][0] = i;
						result[index][1] = j;
						result[index][2] = k;
						index++;
					}
				}
			}
		}
		return result;
	}
	
}

class PotentialCube extends ActiveCube {
	
	private int count;
	
	public PotentialCube(int x, int y, int z) {
		super(x, y, z);
		count = 1;
	}
	
	public void increment() {count++;}
	
	public int getCount() {return count;}
	
}

class ActiveHyperCube extends ActiveCube {
	
	protected int w;
	
	public ActiveHyperCube(int x, int y, int z, int w) {
		super(x, y, z);
		this.w = w;
	}
	
	
	public int getW() {return w;}
	
	public boolean equals(Object other) {
		ActiveHyperCube oth = (ActiveHyperCube)other;
		return x == oth.getX() && y == oth.getY() && z == oth.getZ() && w == oth.getW();
	}

	public String toString() {return super.toString() + " " + w;}
	
	public int[][] neighbors() {
		int[][] result = new int[80][4];
		int index = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				for (int k = z - 1; k <= z + 1; k++) {
					for (int l = w - 1; l <= w + 1; l++) {	
						if (!(x == i && y == j && z == k && l == w)) {
							result[index][0] = i;
							result[index][1] = j;
							result[index][2] = k;
							result[index][3] = l;
							index++;
						}
					}
				}
			}
		}
		return result;
	}
}

class PotentialHyperCube extends ActiveHyperCube {

	private int count;
	
	public PotentialHyperCube(int x, int y, int z, int w) {
		super(x, y, z, w);
		count = 1;
	}
	
	public void increment() {count++;}
	
	public int getCount() {return count;}
	
}
