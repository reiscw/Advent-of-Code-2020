import java.util.*;
import java.io.*;

public class AdventOfCode7 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input7.txt"));
		ArrayList<Bag> bags = new ArrayList<Bag>();
		while (in.hasNextLine()) {
			Bag bag = new Bag(in.nextLine());
			bags.add(bag);
		}
		part1(bags);
		part2(bags);
	}
	
	public static void part1(ArrayList<Bag> bags) {
		ArrayList<String> solution = new ArrayList<String>();
		while (true) {
			int check = solution.size();
			for (Bag bag : bags) {
				// check if the bag contains shiny gold
				if (!solution.contains(bag.getBagName()) && bag.contains("shiny gold")) {
					solution.add(bag.getBagName());
				} else { // check if the bag contains any bags already known
					for (int i = 0; i < solution.size(); i++) {
						if (!solution.contains(bag.getBagName()) && bag.contains(solution.get(i))) {
							solution.add(bag.getBagName());
						}
					}
				}
			}	
			// check if any bags have been added
			if (solution.size() == check)	
				break;
		}
		System.out.println(solution.size());
	}	
	
	public static void part2(ArrayList<Bag> bags) {
		ArrayList<String> allbags = new ArrayList<String>();
		allbags.add("shiny gold");
		for (int i = 0; i < allbags.size(); i++) {
			// find the bag
			int j;
			for (j = 0; j < bags.size(); j++) {
				if (bags.get(j).getBagName().equals(allbags.get(i))) {
					break;
				}
			}
			// add the descendent bags if it has them
			if (bags.get(j).getContainedBags().size() > 0) {
				for (Entry bag : bags.get(j).getContainedBags()) {
					for (int k = 0; k < bag.getNumber(); k++) {
						allbags.add(bag.getBag());
					}
				}
			}
		}
		System.out.println(allbags.size()-1);
	}
}

class Bag {

	String bagName;
	private ArrayList<Entry> containedBags;

	public Bag(String line) {
		containedBags = new ArrayList<Entry>();
		int bagNameLocation = line.indexOf("bags");
		bagName = line.substring(0, bagNameLocation - 1);
		int index = line.indexOf("contain") + 8;
		String sub = line.substring(index);
		String[] separate = sub.split(" ");
		if (!sub.equals("no other bags.")) {
			for (int i = 0; i < separate.length; i = i + 4) {
				containedBags.add(new Entry(separate[i+1] + " " + separate[i+2], Integer.parseInt(separate[i])));
			}
		}
	}
	
	public String toString() {
		return bagName + ": " + containedBags.toString();
	}
	
	public String getBagName() {
		return bagName;
	}
	
	public ArrayList<Entry> getContainedBags() {
		return containedBags;
	}
	
	public boolean contains(String aBagName) {
		for (Entry containedBag : containedBags) {
			if (containedBag.getBag().equals(aBagName)) {
				return true;
			}
		}
		return false;
	}
	
}

class Entry {
	
	private String bag;
	private int number;
	
	public Entry(String aBag, int aNumber) {
		bag = aBag;
		number = aNumber;
	}
	
	public String toString() {
		return bag + "["+number+"]";
	}
	
	public String getBag() {return bag;}
	public int getNumber() {return number;}
	
}

class BagNode {
	
	public String name;
	public int number;
	public ArrayList<BagNode> children;
	
	public BagNode(String aName, int aNumber) {
		name = aName;
		number = aNumber;
	}
	
}
