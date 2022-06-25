import java.util.*;
import java.io.*;

public class AdventOfCode14 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input14.txt"));
		ArrayList<String> lines = new ArrayList<String>();
		while (in.hasNextLine()) lines.add(in.nextLine());
		ArrayList<InstructionSet> sets = new ArrayList<InstructionSet>();
		int startIndex = 0;
		for (int i = 1; i < lines.size(); i++) {
			if (lines.get(i).startsWith("mask")) {
				sets.add(processSet(lines.subList(startIndex, i)));
				startIndex = i;
			} else if (i == lines.size() - 1) {
				sets.add(processSet(lines.subList(startIndex, lines.size())));
			}
		}
		part1(sets);
		part2(sets);
	}
	
	public static InstructionSet processSet(List<String> lines) {
		String mask = lines.get(0).substring(lines.get(0).indexOf("=")+2);
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		for (int i = 1; i < lines.size(); i++) {
			int open = lines.get(i).indexOf("[");
			int close = lines.get(i).indexOf("]");
			int equal = lines.get(i).indexOf("=");
			int location = Integer.parseInt(lines.get(i).substring(open+1,close));
			int value = Integer.parseInt(lines.get(i).substring(equal+2));
			instructions.add(new Instruction(location, value));
		}
		return new InstructionSet(mask, instructions);
	}
	
	public static void part1(ArrayList<InstructionSet> sets) {
		long[] array = new long[100000];
		long result = 0;
		for (InstructionSet set : sets) {
			set.processArray(array);
		}
		for (long element : array) {
			result += element;
		}
		System.out.println(result);
	}
	
	public static void part2(ArrayList<InstructionSet> sets) {
		HashMap<Long, Integer> locations = new HashMap<Long, Integer>();
		for (InstructionSet set : sets) {
			for (Instruction instruction : set.getInstructions()) {
				int value = instruction.getValue();
				ArrayList<Long> addresses = instruction.memoryAddresses(instruction.processLocation(set.getMask()));
				for (Long address : addresses) {
					if (locations.containsKey(address)) {
						locations.replace(address, value);
					} else {
						locations.put(address, value);
					}
				}
			}
		}
		long result = 0;
		for (Integer value : locations.values())
			result = result + value;
		System.out.println(result);
	}
	
}

class InstructionSet {
	
	private String mask;
	private ArrayList<Instruction> instructions;
	
	public InstructionSet(String mask, ArrayList<Instruction> instructions) {
		this.mask = mask;
		this.instructions = instructions;
	}
	
	public void processArray(long[] array) {
		for (Instruction instruction : instructions) {
			array[instruction.getLocation()] = instruction.getAdjustedValue(mask);
		}
	}
	
	public String toString() {
		return mask + " " + instructions;
	}
	
	public String getMask() {return mask;}
	public ArrayList<Instruction> getInstructions() {return instructions;}
	
}

class Instruction {
	
	private int location;
	private int value;
	
	public Instruction(int location, int value) {
		this.location = location;
		this.value = value;
	}
	
	public int getLocation() {return location;}
	public int getValue() {return value;}
	
	public long getAdjustedValue(String mask) {
		String number = Integer.toBinaryString(value);
		int numZeros = 36 - number.length();
		for (int i = 0; i < numZeros; i++) {
			number = "0" + number;
		}
		String result = "";
		for (int i = 0; i < mask.length(); i++) {
			if (mask.charAt(i) == 'X')
				result = result + number.charAt(i);
			else 
				result = result + mask.charAt(i);
		}
		return Long.parseLong(result, 2);
	}
	
	public String toString() {
		return "" + location + ":" + value;
	}
	
	public String processLocation(String mask) {
		String number = Integer.toBinaryString(location);
		int numZeros = 36 - number.length();
		for (int i = 0; i < numZeros; i++) {
			number = "0" + number;
		}
		String result = "";
		for (int i = 0; i < mask.length(); i++) {
			if (mask.charAt(i) == 'X')
				result = result + 'X';
			else if (mask.charAt(i) == '1')
				result = result + '1';
			else 
				result = result + number.charAt(i);
		}
		return result;
	}
	
	public ArrayList<Long> memoryAddresses(String result) {
		ArrayList<Long> addresses = new ArrayList<Long>();
		if (!result.contains("X")) {
			addresses.add(Long.parseLong(result, 2));
			return addresses;
		} else {
			String one = result.replaceFirst("X", "0");
			String two = result.replaceFirst("X", "1");
			for (Long address : memoryAddresses(one)) 
				addresses.add(address);
			for (Long address : memoryAddresses(two))
				addresses.add(address);
			return addresses;
		}
	}
	
}
