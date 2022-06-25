import java.io.*;
import java.util.*;

public class AdventOfCode19 {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input19.txt"));
		HashMap<Integer, String> rules = new HashMap<Integer, String>();
		ArrayList<String> messages = new ArrayList<String>();
		String ruleZero = "";
		int aloc = -1;
		int bloc = -1;
		while (in.hasNextLine()) {
			String input = in.nextLine();
			if (input.equals("")) {
				continue;
			} else if (Character.isDigit(input.charAt(0))) {
				int colon = input.indexOf(":");
				int key = Integer.parseInt(input.substring(0, colon));
				String rule = input.substring(colon + 2);
				if (key == 0) {
					ruleZero = rule;
				} else if (rule.contains("a")) {
					aloc = key;
				} else if (rule.contains("b")) {
					bloc = key;
				} else {
					rules.put(key, rule);
				}
			} else {
				messages.add(input);
			}
		}
		int maxLength = 0;
		for (String message : messages) {
			if (message.length() > maxLength) {
				maxLength = message.length();
			}
		}
		RuleSet ruleSet = new RuleSet(aloc, bloc, messages, rules, maxLength, ruleZero);
		ModifiedRuleSet modifiedRuleSet = new ModifiedRuleSet(aloc, bloc, messages, rules, maxLength, ruleZero);
		part1(ruleSet, messages);
		part2(modifiedRuleSet, messages);
	}

	public static void part1(RuleSet ruleSet, ArrayList<String> messages) {
		ruleSet.getValidMessages();
		int count = 0;
		for (String message : messages) {
			if (ruleSet.validMessage(message)) {
				count++;
			}
		}
		System.out.println(count);
	}
	
	public static void part2(ModifiedRuleSet ruleSet, ArrayList<String> messages) {
		ruleSet.getValidMessages();
		int count = 0;
		for (String message : messages) {
			if (ruleSet.validMessage(message)) {
				count++;
			}
		}
		System.out.println(count);
	}
}

class RuleSet {
	
	protected int aloc;
	protected int bloc;
	protected ArrayList<String> messages;
	protected HashMap<Integer, String> rules;
	protected HashMap<String, Integer> dictionary;
	protected int maxLength;
	protected String ruleZero;
	protected ArrayList<ArrayList<Integer>> candidates;
	
	public RuleSet(int aloc, int bloc, ArrayList<String> messages, HashMap<Integer, String> rules, int maxLength, String ruleZero) {
		this.aloc = aloc;
		this.bloc = bloc;
		this.messages = messages;
		this.rules = rules;
		this.maxLength = maxLength;
		this.ruleZero = ruleZero;
		dictionary = new HashMap<String, Integer>();
		candidates = new ArrayList<ArrayList<Integer>>();
	}
	
	public void getValidMessages() {
		// break apart ruleZero
		ArrayList<Integer> zero = new ArrayList<Integer>();
		for (String element : ruleZero.split(" ")) {
			zero.add(Integer.parseInt(element));
		}
		candidates.add(zero);
		while (candidates.size() > 0) {
			ArrayList<Integer> candidate = candidates.remove(0);
			// find a number that is not aloc or bloc
			int i = 0;
			while (true) {
				if (candidate.get(i) != aloc && candidate.get(i) != bloc) {
					break;
				} else {
					i++;
				}
			}
			// get the replacement rule
			String replacementRule = rules.get(candidate.get(i));
			if (replacementRule.contains("|")) {
				String[] splits = replacementRule.split(" ");
				if (splits.length == 5) {
					int element1 = Integer.parseInt(replacementRule.split(" ")[0]);
					int element2 = Integer.parseInt(replacementRule.split(" ")[1]);
					int element3 = Integer.parseInt(replacementRule.split(" ")[3]);
					int element4 = Integer.parseInt(replacementRule.split(" ")[4]);
					ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
					ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
					candidate1.set(i, element1);
					candidate1.add(i+1, element2);
					candidate2.set(i, element3);
					candidate2.add(i+1, element4);
					if (isValidMessage(candidate1)) {
						dictionary.put(candidateToString(candidate1), 1);
					} else if (isValidCandidate(candidate1)) {
						candidates.add(candidate1);
					}
					if (isValidMessage(candidate2)) {
						dictionary.put(candidateToString(candidate2), 1);
					} else if (isValidCandidate(candidate2)){
						candidates.add(candidate2);
					}
				} else {
					int element1 = Integer.parseInt(replacementRule.split(" ")[0]);
					int element2 = Integer.parseInt(replacementRule.split(" ")[2]);
					ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
					ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
					candidate1.set(i, element1);
					candidate2.set(i, element2);
					if (isValidMessage(candidate1)) {
						dictionary.put(candidateToString(candidate1), 1);
					} else if (isValidCandidate(candidate1)) {
						candidates.add(candidate1);
					}
					if (isValidMessage(candidate2)) {
						dictionary.put(candidateToString(candidate2), 1);
					} else if (isValidCandidate(candidate2)) {
						candidates.add(candidate2);
					}
				}
			} else {
				String[] splits = replacementRule.split(" ");
				if (splits.length == 1) {
					int element = Integer.parseInt(splits[0]);
					candidate.set(i, element);
					if (isValidMessage(candidate)) {
						dictionary.put(candidateToString(candidate), 1);
					} else if (isValidCandidate(candidate)) {
						candidates.add(candidate);
					}
				} else {
					int element1 = Integer.parseInt(splits[0]);
					int element2 = Integer.parseInt(splits[1]);
					candidate.set(i, element1);
					candidate.add(i+1, element2);
					if (isValidMessage(candidate)) {
						dictionary.put(candidateToString(candidate), 1);
					} else if (isValidCandidate(candidate)){
						candidates.add(candidate);
					}
				}
			}
		}
	}
	
	public boolean isValidMessage(ArrayList<Integer> candidate) {
		for (int element : candidate) {
			if (element != aloc && element != bloc) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidCandidate(ArrayList<Integer> candidate) {
		if (candidate.size() > maxLength) return false;
		int i = 0;
		while (candidate.get(i) == aloc || candidate.get(i) == bloc) {
			i++;
		}
		String start = "";
		for (int j = 0; j < i; j++) {
			if (candidate.get(j) == aloc) {
				start = start + "a";
			} else {
				start = start + "b";
			}
		}
		for (String message : messages) {
			if (message.startsWith(start))
				return true;
		}
		return false;
	}
	
	public String candidateToString(ArrayList<Integer> candidate) {
		String result = "";
		for (int element : candidate) {
			if (element == aloc) {
				result = result + "a";
			} else {
				result = result + "b";
			}
		}
		return result;
	}
	
	public boolean validMessage(String message) {
		return dictionary.containsKey(message);
	}
}

class ModifiedRuleSet extends RuleSet {
	
	public ModifiedRuleSet(int aloc, int bloc, ArrayList<String> messages, HashMap<Integer, String> rules, int maxLength, String ruleZero) {
		super(aloc, bloc, messages, rules, maxLength, ruleZero);
	}
	
	public void getValidMessages() {
		// break apart ruleZero
		ArrayList<Integer> zero = new ArrayList<Integer>();
		for (String element : ruleZero.split(" ")) {
			zero.add(Integer.parseInt(element));
		}
		candidates.add(zero);
		while (candidates.size() > 0) {
			ArrayList<Integer> candidate = candidates.remove(0);
			// find a number that is not aloc or bloc
			int i = 0;
			while (true) {
				if (candidate.get(i) != aloc && candidate.get(i) != bloc) {
					break;
				} else {
					i++;
				}
			}
			// get the replacement rule
			String replacementRule = rules.get(candidate.get(i));
			if (candidate.get(i) == 8) {
				ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
				ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
				candidate1.set(i, 42);
				candidate2.set(i, 42);
				candidate2.add(i+1, 8);
				if (isValidCandidate(candidate1))
					candidates.add(candidate1);
				if (isValidCandidate(candidate2))
					candidates.add(candidate2);
			} else if (candidate.get(i) == 11) {
				ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
				ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
				candidate1.set(i, 42);
				candidate1.add(i+1, 31);
				candidate2.set(i, 42);
				candidate2.add(i+1, 11);
				candidate2.add(i+2, 31);
				if (isValidCandidate(candidate1))
					candidates.add(candidate1);
				if (isValidCandidate(candidate2))
					candidates.add(candidate2);
			} else if (replacementRule.contains("|")) {
				String[] splits = replacementRule.split(" ");
				if (splits.length == 5) {
					int element1 = Integer.parseInt(replacementRule.split(" ")[0]);
					int element2 = Integer.parseInt(replacementRule.split(" ")[1]);
					int element3 = Integer.parseInt(replacementRule.split(" ")[3]);
					int element4 = Integer.parseInt(replacementRule.split(" ")[4]);
					ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
					ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
					candidate1.set(i, element1);
					candidate1.add(i+1, element2);
					candidate2.set(i, element3);
					candidate2.add(i+1, element4);
					if (isValidMessage(candidate1)) {
						dictionary.put(candidateToString(candidate1), 1);
					} else if (isValidCandidate(candidate1)) {
						candidates.add(candidate1);
					}
					if (isValidMessage(candidate2)) {
						dictionary.put(candidateToString(candidate2), 1);
					} else if (isValidCandidate(candidate2)){
						candidates.add(candidate2);
					}
				} else {
					int element1 = Integer.parseInt(replacementRule.split(" ")[0]);
					int element2 = Integer.parseInt(replacementRule.split(" ")[2]);
					ArrayList<Integer> candidate1 = new ArrayList<Integer>(candidate);
					ArrayList<Integer> candidate2 = new ArrayList<Integer>(candidate);
					candidate1.set(i, element1);
					candidate2.set(i, element2);
					if (isValidMessage(candidate1)) {
						dictionary.put(candidateToString(candidate1), 1);
					} else if (isValidCandidate(candidate1)) {
						candidates.add(candidate1);
					}
					if (isValidMessage(candidate2)) {
						dictionary.put(candidateToString(candidate2), 1);
					} else if (isValidCandidate(candidate2)) {
						candidates.add(candidate2);
					}
				}
			} else {
				String[] splits = replacementRule.split(" ");
				if (splits.length == 1) {
					int element = Integer.parseInt(splits[0]);
					candidate.set(i, element);
					if (isValidMessage(candidate)) {
						dictionary.put(candidateToString(candidate), 1);
					} else if (isValidCandidate(candidate)) {
						candidates.add(candidate);
					}
				} else {
					int element1 = Integer.parseInt(splits[0]);
					int element2 = Integer.parseInt(splits[1]);
					candidate.set(i, element1);
					candidate.add(i+1, element2);
					if (isValidMessage(candidate)) {
						dictionary.put(candidateToString(candidate), 1);
					} else if (isValidCandidate(candidate)){
						candidates.add(candidate);
					}
				}
			}
		}
	}
	
}
