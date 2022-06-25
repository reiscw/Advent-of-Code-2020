import java.util.*;
import java.io.*;

public class AdventOfCode5 {
	
	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String> codes = getCodes();
		part1(codes);
		part2(codes);
	}
	
	public static void part1(ArrayList<String> codes) {
		int max = 0;
		for (String code : codes) {
			int seatid = new BoardingPass(code).getSeatid();
			if (seatid > max)
				max = seatid;
		}
		System.out.println(max);
	}
	
	public static void part2(ArrayList<String> codes) {
		boolean[] seats = new boolean[1024];
		for (String code : codes) {
			seats[new BoardingPass(code).getSeatid()] = true;
		}
		ArrayList<Integer> missingSeats = new ArrayList<Integer>();
		for (int i = 0; i < 1024; i++) {
			if (!seats[i])
				missingSeats.add(i);
		}
		for (int seat : missingSeats) {
			if (!missingSeats.contains(seat-1) && !missingSeats.contains(seat+1)) {
				System.out.println(seat);
				break;
			}
		}
	}
	
	public static ArrayList<String> getCodes() throws FileNotFoundException {
		ArrayList<String> codes = new ArrayList<String>();
		Scanner in = new Scanner(new File("input5.txt"));
		while (in.hasNext()) {
			codes.add(in.next());
		}
		return codes;
	}
	
}

class BoardingPass {
	
	private int row;
	private int col;
	private int seatid;
	
	public BoardingPass(String code) {
		for (int i = 6; i >=0; i--) {
			if (code.charAt(i) == 'B') {
				row = row + (int)Math.pow(2,6-i);
			}
		}
		for (int i = 9; i >=7; i--) {
			if (code.charAt(i) == 'R') {
				col = col + (int)Math.pow(2,9-i);
			}
		}
		seatid = row * 8 + col;
	}
	
	public int getRow() {return row;}
	public int getCol() {return col;}
	public int getSeatid() {return seatid;}
	
}
