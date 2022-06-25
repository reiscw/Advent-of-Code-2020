import java.util.*;

public class AdventOfCode23 {
	
	public static void main(String[] args) {
		String input = "538914762";
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (char digit : input.toCharArray()) {
			list.add(Integer.parseInt("" + digit));
		}
		part1(list);
		part2(list);
	}
	
	public static void part1(ArrayList<Integer> list) {
		Simulation test = new Simulation(list);
		test.process();
	}
	
	public static void part2(ArrayList<Integer> list) {
		RevisedSimulation test = new RevisedSimulation(list);
		test.process();
	}
	
	
}

class Simulation {
	
	private Node start;
	private final int size = 9;
	
	public Simulation(ArrayList<Integer> list) {
		// create the first node;
		start = new Node(list.get(0));
		Node current = start;
		// add nodes for the remaining elements in the list
		for (int i = 1; i < list.size(); i++) {
			Node newNode = new Node(list.get(i));
			current.next = newNode;
			current = newNode;
		}
		// form a circle
		current.next = start;
	}
	
	public void displayNodes() {
		Node current = start;
		current = current.next;
		while (current != start) {
			System.out.print(current.value);
			current = current.next;
		}
		System.out.println();
	}
	
	public void process() {
		Node current = start;
		for (int i = 1; i <= 100; i++) {
			// picked up cups
			Node removed = current.next;
			Node afterRemoved = current.next.next.next.next;
			int cup1 = removed.value;
			int cup2 = removed.next.value;
			int cup3 = removed.next.next.value;
			// reform the chain
			current.next = afterRemoved;
			// find the destination cup
			int destination = current.value - 1;
			if (destination == 0) destination = size;
			while (destination == cup1 || destination == cup2 || destination == cup3) {
				destination--;
				if (destination <= 0) destination = size;
			}
			Node destinationNode = nodeWithValue(destination);
			// move the picked up cups
			Node afterAdded = destinationNode.next;
			destinationNode.next = removed;
			destinationNode.next.next.next.next = afterAdded;
			// move to the next position
			current = current.next;
			start = current;
		}
		Node one = nodeWithValue(1);
		for (int i = 0; i < 8; i++) {
			one = one.next;
			System.out.print(one.value);
		}
		System.out.println();
	}
	
	public Node nodeWithValue(int value) {
		Node current = start;
		if (current.value == value) {
			 return current;
		} else {
			current = current.next;
			while (current != start) {
				if (current.value == value) {
					return current;
				}
				current = current.next;
			}
			return null;
		}
	}
	
	class Node {
	
		int value;
		Node next;
	
		public Node(int value) {
			this.value = value;
			next = null;
		}
	
	}
	
}

class RevisedSimulation {
	
	private Node start;
	private final int size = 1000000;
	private Node[] nodes = new Node[1000001];
	
	public RevisedSimulation(ArrayList<Integer> list) {
		// create the first node;
		start = new Node(list.get(0));
		nodes[list.get(0)] = start;
		Node current = start;
		// add nodes for the remaining elements in the list
		for (int i = 1; i < list.size(); i++) {
			Node newNode = new Node(list.get(i));
			nodes[list.get(i)] = newNode;
			current.next = newNode;
			current = newNode;
		}
		// add the remaining nodes
		for (int i = 10; i <= size; i++) {
			Node newNode = new Node(i);
			nodes[i] = newNode;
			current.next = newNode;
			current = newNode;
		}
		// form a circle
		current.next = start;
	}
	
	public void process() {
		Node current = start;
		for (int i = 1; i <= 10000000; i++) {
			// picked up cups
			Node removed = current.next;
			Node afterRemoved = current.next.next.next.next;
			int cup1 = removed.value;
			int cup2 = removed.next.value;
			int cup3 = removed.next.next.value;
			// reform the chain
			current.next = afterRemoved;
			// find the destination cup
			int destination = current.value - 1;
			if (destination == 0) destination = size;
			while (destination == cup1 || destination == cup2 || destination == cup3) {
				destination--;
				if (destination <= 0) destination = size;
			}
			Node destinationNode = nodes[destination];
			// move the picked up cups
			Node afterAdded = destinationNode.next;
			destinationNode.next = removed;
			destinationNode.next.next.next.next = afterAdded;
			// move to the next position
			current = current.next;
			start = current;
		}
		Node one = nodeWithValue(1);
		Node two = one.next;
		Node three = two.next;
		long result = (long)two.value * (long)three.value;
		System.out.println(result);
	}
	
	public Node nodeWithValue(int value) {
		Node current = start;
		if (current.value == value) {
			 return current;
		} else {
			current = current.next;
			while (current != start) {
				if (current.value == value) {
					return current;
				}
				current = current.next;
			}
			return null;
		}
	}
	
	class Node {
	
		int value;
		Node next;
	
		public Node(int value) {
			this.value = value;
			next = null;
		}
	
	}
	
}
