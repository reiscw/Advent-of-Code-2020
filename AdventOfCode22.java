import java.util.*;
import java.io.*;

public class AdventOfCode22 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input22.txt"));
		in.nextLine();
		ArrayList<Integer> deck1 = new ArrayList<Integer>();
		ArrayList<Integer> deck2 = new ArrayList<Integer>();
		for (int i = 1; i <= 25; i++) {
			deck1.add(Integer.parseInt(in.nextLine()));
		}
		in.nextLine();
		in.nextLine();
		for (int i = 1; i <= 25; i++) {
			deck2.add(Integer.parseInt(in.nextLine()));
		}
		part1(deck1, deck2);
		part2(deck1, deck2);
	}
	
	public static void part1(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		Game game = new Game(deck1, deck2);
		while (game.hasNextMove()) game.move();
		System.out.println(game.solution());
	}
	
	public static void part2(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		RecursiveGame game = new RecursiveGame(deck1, deck2);
		int result = game.play();
		if (result == 1) {
			System.out.println(game.player1score());
		} else {
			System.out.println(game.player2score());
		}
	}
	
	
}

class Game {
	
	public ArrayList<Integer> deck1;
	public ArrayList<Integer> deck2;
	
	public Game(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		this.deck1 = new ArrayList<Integer>();
		this.deck2 = new ArrayList<Integer>();
		for (int element : deck1) this.deck1.add(element);
		for (int element : deck2) this.deck2.add(element);
	}
	
	public boolean hasNextMove() {
		return !deck1.isEmpty() && !deck2.isEmpty();
	}
	
	public void move() {
		int player1 = deck1.remove(0);
		int player2 = deck2.remove(0);
		if (player1 > player2) {
			deck1.add(player1);
			deck1.add(player2);
		} else {
			deck2.add(player2);
			deck2.add(player1);
		}
	}
	
	public long solution() {
		ArrayList<Integer> winner;
		if (deck1.isEmpty()) {
			winner = deck2;
		} else {
			winner = deck1;
		}
		long result = 0;
		for (int i = winner.size() - 1, j = 1; i >= 0; i--, j++) {
			result = result + winner.get(i) * j;
		}
		return result;
	}

}

class RecursiveGame {
	
	public ArrayList<Integer> deck1;
	public ArrayList<Integer> deck2;
	public ArrayList<PriorRound> priorRounds;
	
	public RecursiveGame(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		this.deck1 = new ArrayList<Integer>();
		this.deck2 = new ArrayList<Integer>();
		for (int element : deck1) this.deck1.add(element);
		for (int element : deck2) this.deck2.add(element);
		priorRounds = new ArrayList<PriorRound>();
	}
	
	public RecursiveGame(ArrayList<Integer> deck1, ArrayList<Integer> deck2, ArrayList<PriorRound> priorRounds) {
		this(deck1, deck2);
		this.priorRounds = priorRounds;
	}
	
	public int play() {
		Scanner in = new Scanner(System.in);
		while (true) {
			// check if the decks have already occured, if so, player one wins
			for (PriorRound priorRound : priorRounds) {
				if (priorRound.check(deck1, deck2)) {
					return 1;
				}
			}
			priorRounds.add(new PriorRound(deck1, deck2));
			// double check for empty decks
			if (deck1.isEmpty()) return 2;
			if (deck2.isEmpty()) return 1;
			// otherwise, perform a draw
			int player1 = deck1.remove(0);
			int player2 = deck2.remove(0);
			// determine if recursion is necessary
			if (player1 <= deck1.size() && player2 <= deck2.size()) {
				ArrayList<Integer> new1 = new ArrayList<Integer>();
				ArrayList<Integer> new2 = new ArrayList<Integer>();
				for (int i = 0; i < player1; i++) new1.add(deck1.get(i));
				for (int i = 0; i < player2; i++) new2.add(deck2.get(i));
				int result = new RecursiveGame(new1, new2).play();
				if (result == 1) {
					deck1.add(player1);
					deck1.add(player2);
				} else {
					deck2.add(player2);
					deck2.add(player1);
				}
			} else {
				if (player1 > player2) {
					deck1.add(player1);
					deck1.add(player2);
				} else {
					deck2.add(player2);
					deck2.add(player1);
				}
			}
		}
	}
	
	public long player1score() {
		long result = 0;
		for (int i = deck1.size() - 1, j = 1; i >= 0; i--, j++) {
			result = result + deck1.get(i) * j;
		}
		return result;
	}
	
	public long player2score() {
		long result = 0;
		for (int i = deck2.size() - 1, j = 1; i >= 0; i--, j++) {
			result = result + deck2.get(i) * j;
		}
		return result;
	}
}

class PriorRound {
	
	private ArrayList<Integer> deck1;
	private ArrayList<Integer> deck2;
	
	public PriorRound(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		this.deck1 = new ArrayList<Integer>();
		this.deck2 = new ArrayList<Integer>();
		for (int element : deck1) {
			this.deck1.add(element);
		} 
		for (int element : deck2) {
			this.deck2.add(element);
		}
	}
	
	public boolean check(ArrayList<Integer> deck1, ArrayList<Integer> deck2) {
		return this.deck1.equals(deck1) && this.deck2.equals(deck2);
	}
}
