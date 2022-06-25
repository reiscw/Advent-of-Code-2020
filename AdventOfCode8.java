import java.io.*;
import java.util.*;

public class AdventOfCode8 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input8.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while (in.hasNextLine())
			lines.add(in.nextLine());
		part1(lines);
		part2(lines);
	}
	
	public static void part1(ArrayList<String> lines) {
		Console myConsole = new Console(lines);
		while (!myConsole.isDone()) {
			myConsole.step();
		}
		System.out.println(myConsole.getAccumulator());
	}
	
	public static void part2(ArrayList<String> lines) {
		Console myConsole = new Console(lines);
		ArrayList<Instruction> instructions = myConsole.getInstructions();
		for (int i = 0; i < instructions.size(); i++) {
			String command = instructions.get(i).getCode();
			if (command.equals("jmp")) {
				instructions.get(i).setCode("nop");
				while (!myConsole.isDone() && !myConsole.isDoneCorrectly()) {
					myConsole.step();
				}
				if (myConsole.isDoneCorrectly()) {
					System.out.println(myConsole.getAccumulator());
					return;
				} else {
					instructions.get(i).setCode("jmp");
				}
			} else if (command.equals("nop")) {
				instructions.get(i).setCode("jmp");
				while (!myConsole.isDone() && !myConsole.isDoneCorrectly()) {
					myConsole.step();
				}
				if (myConsole.isDoneCorrectly()) {
					System.out.println(myConsole.getAccumulator());
					return;
				} else {
					instructions.get(i).setCode("nop");
				}
			}
			myConsole.reset();
		}
	}
	
}

class Console {
	
	private int[] instructionCounts;
	private ArrayList<Instruction> instructions;
	private boolean done;
	private int index;
	private int accumulator;
	
	public Console() {
		accumulator = 0;
		done = false;
		index = 0;
	}
	
	public Console(ArrayList<String> lines) {
		accumulator = 0;
		done = false;
		index = 0;
		instructions = new ArrayList<Instruction>();
		for (String line : lines) {
			String code = line.substring(0,3);
			int argument;
			if (line.charAt(4) == '+') {
				argument = Integer.parseInt(line.substring(5));
			} else {
				argument = Integer.parseInt(line.substring(4));
			}
			instructions.add(new Instruction(code, argument));
		}
		instructionCounts = new int[instructions.size()];
	}

	public void reset() {
		accumulator = 0;
		done = false;
		index = 0;
		instructionCounts = new int[instructions.size()];
	}
	
	public boolean isDone() {return done;}
	public boolean isDoneCorrectly() {return index == instructions.size();}
	
	public void step() {
		instructionCounts[index]++;
		if (instructionCounts[index] == 2) {
			done = true;
			return;
		} else {
			String command = instructions.get(index).getCode();
			int argument = instructions.get(index).getArgument();
			switch (command) {
				case "nop": 
					index++;
					break;
				case "acc":
					accumulator = accumulator + argument;
					index++;
					break;
				case "jmp": 
					index = index + argument;
					break;
			}
		}
	}
	
	public ArrayList<Instruction> getInstructions() {return instructions;}
	
	public int getAccumulator() {return accumulator;}
}

class Instruction {
	
	private String code;
	private int argument;
	
	public Instruction(String aCode, int anArgument) {
		code = aCode;
		argument = anArgument;
	}
	
	public String getCode() {return code;}
	public int getArgument() {return argument;}
	
	public void setCode(String aCode) {
		code = aCode;
	}
}
