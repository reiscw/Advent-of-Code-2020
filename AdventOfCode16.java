import java.io.*;
import java.util.*;

public class AdventOfCode16 {
	
	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> tickets = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> yourTicket = new ArrayList<Integer>();
		Scanner in = new Scanner(new File("input16.txt"));
		// read in the rules
		while (true) {
			String input = in.nextLine();
			if (input.equals("")) break;
			rules.add(input);
		}
		// read in your ticket
		in.nextLine();
		for (String number : in.nextLine().split(",")) {
			yourTicket.add(Integer.parseInt(number));
		}
		// read in the tickets
		in.nextLine();
		in.nextLine();
		while (in.hasNextLine()) {
			ArrayList<Integer> ticket = new ArrayList<Integer>();
			for (String number : in.nextLine().split(",")) {
				ticket.add(Integer.parseInt(number));
			}
			tickets.add(ticket);
		}
		// process the rules
		ArrayList<Rule> actualRules = new ArrayList<Rule>();
		for (String rule : rules) {
			actualRules.add(processStringRule(rule));
		}
		part1(actualRules, tickets);
		part2(actualRules, tickets, yourTicket);
	}
	
	public static Rule processStringRule(String rule) {
		String name = rule.substring(0,rule.indexOf(":"));
		String numbers = rule.substring(rule.indexOf(":") + 2);
		String[] parts = numbers.split(" ");
		String firstRule = parts[0];
		String secondRule = parts[2];
		int low1 = Integer.parseInt(firstRule.substring(0,firstRule.indexOf("-")));
		int high1 = Integer.parseInt(firstRule.substring(firstRule.indexOf("-")+1));
		int low2 = Integer.parseInt(secondRule.substring(0,secondRule.indexOf("-")));
		int high2 = Integer.parseInt(secondRule.substring(secondRule.indexOf("-")+1));
		return new Rule(low1, high1, low2, high2, name);
	}
	
	public static void part1(ArrayList<Rule> rules, ArrayList<ArrayList<Integer>> tickets) {
		int sum = 0;
		for (ArrayList<Integer> ticket : tickets) {
			for (Integer element : ticket) {
				boolean success = false;
				for (Rule rule : rules) {
					if (rule.isValid(element)) {
						success = true;
						break;
					}
				}
				if (!success) {
					sum += element;
				}
			}
		}
		System.out.println(sum);
	}
	
	public static void part2(ArrayList<Rule> rules, ArrayList<ArrayList<Integer>> tickets, ArrayList<Integer> yourTicket) {
		// remove invalid tickets
		for (int i = 0; i < tickets.size(); i++) {
			if (!validTicket(tickets.get(i), rules)) {
				tickets.remove(i);
				i--;
			}
		}
		// get the candidates
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		for (Rule rule : rules) {
			candidates.add(rule.checkRule(tickets));
		}
		Collections.sort(candidates);
		ArrayList<Integer> usedColumns = new ArrayList<Integer>();
		// process the candidates
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		for (Candidate candidate : candidates) {
			Solution solution = processCandidate(candidate, usedColumns);
			solutions.add(solution);
		}
		long product = 1;
		for (Solution solution : solutions) {
			if (solution.getName().contains("departure")) {
				product = product * yourTicket.get(solution.getColumn());
			}
		}
		System.out.println(product);
		
	}
	
	public static boolean validTicket(ArrayList<Integer> ticket, ArrayList<Rule> rules) {
		for (Integer element : ticket) {
			boolean success = false;
			for (Rule rule : rules) {
				if (rule.isValid(element)) {
					success = true;
					break;
				}
			}
			if (!success) {
				return false;
			}
		}
		return true;
	}
	
	
	public static Solution processCandidate(Candidate candidate, ArrayList<Integer> usedColumns) {
		for (Integer column : usedColumns) {
			candidate.getColumns().remove(new Integer(column));
		}
		usedColumns.add(candidate.getColumns().get(0));
		return new Solution(candidate.getName(), candidate.getColumns().get(0));
	}
	
}

class Rule {
	
	private int low1;
	private int high1;
	private int low2;
	private int high2;
	private String name;
	
	public Rule(int low1, int high1, int low2, int high2, String name) {
		this.low1 = low1;
		this.high1 = high1;
		this.low2 = low2;
		this.high2 = high2;
		this.name = name;
	}
	
	public String getName() {return name;}
	
	public boolean isValid(int number) {
		return (low1 <= number && number <= high1) || (low2 <= number && number <= high2);
	}
	
	public Candidate checkRule(ArrayList<ArrayList<Integer>> tickets) {
		ArrayList<Integer> columns = new ArrayList<Integer>();
		for (int i = 0; i < tickets.get(0).size(); i++) {
			boolean success = true;
			for (int j = 0; j < tickets.size(); j++) {
				if (!isValid(tickets.get(j).get(i))) {
					success = false;
					break;
				}
			}
			if (success) {
				columns.add(i);
			}
		}
		return new Candidate(name, columns);
	}
	
}

class Candidate implements Comparable {
	
	private String name;
	private ArrayList<Integer> columns;
	
	public Candidate(String name, ArrayList<Integer> columns) {
		this.name = name;
		this.columns = columns;
	}
	
	public String toString() {
		return name + " " + columns;
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Integer> getColumns() {
		return columns;
	}

	public int compareTo(Object other) {
		return columns.size() - ((Candidate)other).getColumns().size();
	}
	
}

class Solution {

	private String name;
	private int column;
	
	public Solution(String name, int column) {
		this.name = name;
		this.column = column;
	}
	
	public String getName() {return name;}
	public int getColumn() {return column;}
	
	public String toString() {
		return name + " " + column;
	}
	
}

