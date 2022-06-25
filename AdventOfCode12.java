import java.io.*;
import java.util.*;

public class AdventOfCode12 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input12.txt"));
		ArrayList<String> input = new ArrayList<String>();
		while (in.hasNext()) input.add(in.next());
		ArrayList<Code> codes = new ArrayList<Code>();
		for (String word : input) {
			char command = word.charAt(0);
			int argument = Integer.parseInt(word.substring(1));
			codes.add(new Code(command, argument));
		}
		part1(codes);
		part2(codes);
	}
	
	public static void part1(ArrayList<Code> codes) {
		Simulation sim = new Simulation(codes);
		while (sim.hasNextStep()) sim.step();
		System.out.println(sim.manhattanDistance());
	}
	
	public static void part2(ArrayList<Code> codes) {
		WaypointSimulation sim = new WaypointSimulation(codes);
		while (sim.hasNextStep()) sim.step();
		System.out.println(sim.manhattanDistance());
	}
	
}

class Code {
	
	private char command;
	private int argument;
	
	public Code(char command, int argument) {
		this.command = command;
		this.argument = argument;
	}
	
	public char getCommand() {return command;}
	public int getArgument() {return argument;}
	
	public String toString() {
		return command + " " + argument;
	}
	
}

class Simulation {
	
	private int x = 0;
	private int y = 0;
	private int direction = 1;
	private ArrayList<Code> codes; 
	private int codeCount = 0;
	
	public Simulation(ArrayList<Code> codes) {
		this.codes = codes;
	}
	
	public boolean hasNextStep() {return codeCount < codes.size();}
	
	public void step() {
		char command = codes.get(codeCount).getCommand();
		int argument = codes.get(codeCount).getArgument();
		switch (command) {
			case 'N': y = y + argument; break;
			case 'S': y = y - argument; break;
			case 'E': x = x + argument; break;
			case 'W': x = x - argument; break;
			case 'F': switch (direction) {
						case 0: y = y + argument; break;
						case 1: x = x + argument; break;
						case 2: y = y - argument; break;
						case 3: x = x - argument; break;
					  }
					  break;
			case 'R': 
				direction = (direction + (argument / 90)) % 4; 
				if (direction < 0) direction = direction + 4;
				break;
			case 'L': 
				direction = (direction - (argument / 90)) % 4; 
				if (direction < 0) direction = direction + 4;
				break;
		}
		codeCount++;
	}
	
	public int manhattanDistance() {
		return (int)Math.abs(x)+ (int)Math.abs(y);
	}
	
}

class WaypointSimulation {
	
	private int shipX = 0;
	private int shipY = 0;
	private int waypointX = 10;
	private int waypointY = 1;
	private ArrayList<Code> codes; 
	private int codeCount = 0;
	
	public WaypointSimulation(ArrayList<Code> codes) {
		this.codes = codes;
	}	
	
	public boolean hasNextStep() {return codeCount < codes.size();}
	
	public void step() {
		char command = codes.get(codeCount).getCommand();
		int argument = codes.get(codeCount).getArgument();
		switch (command) {
			case 'N': waypointY = waypointY + argument; break;
			case 'S': waypointY = waypointY - argument; break;
			case 'E': waypointX = waypointX + argument; break;
			case 'W': waypointX = waypointX - argument; break;
			case 'F': 
				shipX = shipX + waypointX * argument;
				shipY = shipY + waypointY * argument;
			case 'R': 
			case 'L': 
				if ((command == 'L' && argument == 90) || (command == 'R' && argument == 270)) {
					int temp = waypointX;
					waypointX = -1*waypointY;
					waypointY = temp;
				} else if ((command == 'R' && argument == 90) || (command == 'L' && argument == 270)) {
					int temp = waypointX;
					waypointX = waypointY;
					waypointY = -1*temp;
				} else if (argument == 180) {
					waypointX = -1*waypointX;
					waypointY = -1*waypointY;
				}
				break;
		}
		codeCount++;
	}
	
	public int manhattanDistance() {
		return (int)Math.abs(shipX)+ (int)Math.abs(shipY);
	}	
}
