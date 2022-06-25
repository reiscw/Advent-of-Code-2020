import java.util.*;
import java.io.*;

public class AdventOfCode18 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input18.txt"));
		ArrayList<Expression> expressions = new ArrayList<Expression>();
		ArrayList<RevisedExpression> revisedExpressions = new ArrayList<RevisedExpression>();
		while (in.hasNextLine()) {
			String text = in.nextLine();
			expressions.add(new Expression(text));
			revisedExpressions.add(new RevisedExpression(text));
		} 
		part1(expressions);		
		part2(revisedExpressions);		
	}
	
	public static void part1(ArrayList<Expression> expressions) {
		long sum = 0;
		for (Expression expression : expressions) {
			// remove all of the parentheses
			expression.removeParentheses();
			// add the evaluated value to sum
			sum = sum + expression.evaluate();
		}
		System.out.println(sum);
	}
	
	public static void part2(ArrayList<RevisedExpression> revisedExpressions) {
		long sum = 0;
		for (RevisedExpression expression : revisedExpressions) {
			// remove all of the parentheses
			expression.removeParentheses();
			// add the evaluated value to sum
			sum = sum + expression.evaluate();
		}
		System.out.println(sum);
	}
}
	
class Expression {
	
	protected String text;
	
	public Expression(String text) {
		this.text = text;
	}
	
	public boolean hasParentheses() {
		return text.indexOf("(") != -1;
	}
	
	public void removeParentheses() {
		while (hasParentheses()) {
			// locate a matching set of parentheses
			int start = text.indexOf("(");
			int end = start + 1;
			while (true) {
				if (text.charAt(end) == '(') {
					start = end;
					end = start + 1;
				} else if (text.charAt(end) == ')') {
					break;
				}
				end++;
			}
			// break apart the string
			String first = text.substring(0, start);
			String last = text.substring(end+1);
			// evaluate the middle
			Expression temp = new Expression(text.substring(start+1, end));
			String middle = "" + temp.evaluate();
			// reform the string
			text = first + middle + last;
		}
	}
		
	public long evaluate() {
		String temp = text;
		while (!containsNoOperators(temp)) {
			// split up temp
			String[] elements = temp.split(" ");
			// grab the first instruction
			long arg1 = Long.parseLong(elements[0]);
			char operator = (elements[1]).charAt(0);
			long arg2 = Long.parseLong(elements[2]);
			long result = 0;
			switch (operator) {
				case '+': result = arg1 + arg2; break;
				case '*': result = arg1 * arg2; break;
			}
			// reform temp
			temp = "" + result + " ";
			for (int i = 3; i < elements.length; i++) {
				temp = temp + elements[i] + " ";
			}
		}
		return Long.parseLong(temp.substring(0, temp.length()-1));
	}
	
	public boolean containsNoOperators(String text) {
		return text.split(" ").length == 1;
	}
	
}

class RevisedExpression extends Expression {

	public RevisedExpression(String text) {
		super(text);
	}

	public void removeParentheses() {
		while (hasParentheses()) {
			// locate a matching set of parentheses
			int start = text.indexOf("(");
			int end = start + 1;
			while (true) {
				if (text.charAt(end) == '(') {
					start = end;
					end = start + 1;
				} else if (text.charAt(end) == ')') {
					break;
				}
				end++;
			}
			// break apart the string
			String first = text.substring(0, start);
			String last = text.substring(end+1);
			// evaluate the middle
			RevisedExpression temp = new RevisedExpression(text.substring(start+1, end));
			String middle = "" + temp.evaluate();
			// reform the string
			text = first + middle + last;
		}
	}

	public long evaluate() {
		String temp = text;
		while (!containsNoOperators(temp)) {
			// check first for addition
			if (temp.contains("+")) {
				// split up temp
				String[] elements = temp.split(" ");
				// find the first addition operator
				int op1index = -1;
				int op2index = -1;
				for (int i = 1; i < elements.length; i++) {
					if (elements[i].equals("+")) {
						op1index = i - 1;
						op2index = i + 1;
						break;
					}
				}
				long arg1 = Long.parseLong(elements[op1index]);
				long arg2 = Long.parseLong(elements[op2index]);
				long result = arg1 + arg2;
				// reform temp
				temp = "";
				for (int i = 0; i < op1index; i++) {
					temp = temp + elements[i] + " ";
				}
				temp = temp + result + " ";
				for (int i = op2index + 1; i < elements.length; i++) {
					temp = temp + elements[i] + " ";
				}
			} else {
				// split up temp
				String[] elements = temp.split(" ");
				// grab the first instruction
				long arg1 = Long.parseLong(elements[0]);
				long arg2 = Long.parseLong(elements[2]);
				long result = arg1 * arg2;
				// reform temp
				temp = "" + result + " ";
				for (int i = 3; i < elements.length; i++) {
					temp = temp + elements[i] + " ";
				}
			}
		}
		return Long.parseLong(temp.substring(0, temp.length()-1));
	}
	
}
