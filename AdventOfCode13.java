import java.util.*;
import java.io.*;

public class AdventOfCode13 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input13.txt"));
		long target = in.nextLong();
		String input = in.next();
		String[] elements = input.split(",");
		ArrayList<Long> busIDs = new ArrayList<Long>();
		ArrayList<Long> offsets = new ArrayList<Long>();
		for (int i = 0; i < elements.length; i++) {
			if (!elements[i].equals("x")) {
				busIDs.add(Long.parseLong(elements[i]));
				offsets.add((long)i);
			}
		}
		part1(target, busIDs);
		part2(target, busIDs, offsets);
		
	}
	
	public static void part1(long target, ArrayList<Long> busIDs) {
		ArrayList<Bus> buses = new ArrayList<Bus>();
		for (Long busid : busIDs) {
			buses.add(new Bus(busid, target));
		}
		long minSol = buses.get(0).solution();
		long minid = buses.get(0).getID();
		for (int i = 1; i < buses.size(); i++) {
			long sol = buses.get(i).solution();
			if (sol < minSol) {
				minid = buses.get(i).getID();
				minSol = sol;
			}
		}
		System.out.println(minSol * minid);
		
	}
	
	public static void part2(long target, ArrayList<Long> busIDs, ArrayList<Long> offsets) {
		ArrayList<BetterBus> buses = new ArrayList<BetterBus>();
		for (int i = 0; i < busIDs.size(); i++) {
			buses.add(new BetterBus(busIDs.get(i), target, offsets.get(i)));
		}
		Collections.sort(buses);
		int index = 0;
		long timestamp = buses.get(index).getID();
		long delta = 1;
		while (true) {
			if (buses.get(index).isValid(timestamp)) {
				delta = delta * buses.get(index).getID();
				index++;
				if (index == buses.size())
					break;
			}
			timestamp = timestamp + delta;
		}
		System.out.println(timestamp);
	}
	
}

class Bus {
	
	protected long id;
	protected long target;
	
	public Bus(long id, long target) {
		this.id = id;
		this.target = target;
	}
	
	public long getID() {return id;}
	public long getTarget() {return target;}
	
	public long solution() {
		long count = 0;
		while (count < target) {
			count += id;
		}
		return count - target;
	}
	
}

class BetterBus extends Bus implements Comparable {
	
	protected long offset;
	
	public BetterBus(long id, long target, long offset) {
		super(id, target);
		if (offset > id) {
			this.offset = offset % id;
		} else {
			this.offset = offset;
		}
	}
	
	public long getOffset() {return offset;}
	
	public boolean isValid(long timestamp) {
		if (offset == 0) {
			return timestamp % id == 0;
		} else {
			return id - (timestamp % id) == offset;
		}
	}
	
	public int compareTo(Object other) {
		return (int)(((Bus)(other)).getID() - this.getID());
	}
	
	public String toString() {
		return "" + id + " " + offset;
	}
	
}

