import java.io.*;
import java.util.*;

public class AdventOfCode4 {

	public static void main(String[] args) throws FileNotFoundException {
		part1();
		part2();
	}
	
	public static void part1() throws FileNotFoundException{
		ArrayList<String> passports = getInput();
		int count = 0;
		for (String passport : passports) {
			if (isValid(passport)) 
				count++;
		}
		System.out.println(count);
	}
	
	public static void part2() throws FileNotFoundException {
		ArrayList<String> inputs = getInput();
		ArrayList<Passport> passports = new ArrayList<Passport>();
		for (String element : inputs) {
			if (isValid(element)) 
				passports.add(new Passport(element));
		}
		int count = 0;
		for (Passport passport : passports) {
			if (passport.isValid())
				count++;
		}
		System.out.println(count);
	}
	
	public static ArrayList<String> getInput() throws FileNotFoundException {
		Scanner in = new Scanner(new File("input4.txt"));
		ArrayList<String> result = new ArrayList<String>();
		while (in.hasNextLine()) {
			String passport = "";
			while (in.hasNextLine()) {
				String element = in.nextLine();
				if (element.equals("")) 
					break;
				else
					passport = passport + element + " ";
			}
			result.add(passport);
		}
		return result;
	}
	
	public static boolean isValid(String passport) {
		int[] result = new int[7];
		result[0] = passport.indexOf("byr");
		result[1] = passport.indexOf("iyr");
		result[2] = passport.indexOf("eyr");
		result[3] = passport.indexOf("hgt");
		result[4] = passport.indexOf("hcl");
		result[5] = passport.indexOf("ecl");
		result[6] = passport.indexOf("pid");
		for (int element : result) {
			if (element == -1)
				return false;
		}
		return true;
	}
}

class Passport {

	private int byr;		// done, handled
	private int iyr;		// done, handled
	private int eyr;		// done, handled
	private String hgt;		// done, handled
	private String hcl;		// done, handled
	private String ecl;		// done, handled
	private String pid;		// done, handled
	
	public Passport(String passport) {
		String[] elements = passport.split(" ");
		Arrays.sort(elements);
		if (elements.length == 8) {
			String[] temp = new String[7];
			temp[0] = elements[0];
			temp[1] = elements[2];
			temp[2] = elements[3];
			temp[3] = elements[4];
			temp[4] = elements[5];
			temp[5] = elements[6];
			temp[6] = elements[7];
			elements = temp;
		}
		try {
			String text = elements[0].substring(elements[0].indexOf(":")+1);
			byr = Integer.parseInt(text);
		} catch(Exception e) {
			byr = 0;
		}
		try {
			String text = elements[1].substring(elements[1].indexOf(":")+1);
			ecl = text;
		} catch(Exception e) {
			ecl = "";
		}
		try {
			String text = elements[2].substring(elements[2].indexOf(":")+1);
			eyr = Integer.parseInt(text);
		} catch(Exception e) {
			eyr = 0;
		}
		try {
			String text = elements[3].substring(elements[3].indexOf(":")+1);
			hcl = text;
		} catch(Exception e) {
			hcl = "";
		}
		try {
			String text = elements[4].substring(elements[4].indexOf(":")+1);
			hgt = text;
		} catch(Exception e) {
			hgt = "";
		}
		try {
			String text = elements[5].substring(elements[5].indexOf(":")+1);
			iyr = Integer.parseInt(text);
		} catch(Exception e) {
			iyr = 0;
		}
		try {
			String text = elements[6].substring(elements[6].indexOf(":")+1);
			pid = text;
		} catch(Exception e) {
			pid = "";
		}
	}
	
	public boolean isValid() {
		if (byr < 1920 || byr > 2002) 
			return false;
		if (iyr < 2010 || iyr > 2020) 
			return false;
		if (eyr < 2020 || eyr > 2030) 
			return false;
		String heightUnits = hgt.substring(hgt.length() - 2);
		int height = 0;
		try {
			height = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
		} catch(Exception e) {}
		if (heightUnits.equals("cm") && (height < 150 || height > 193))
			return false;
		if (heightUnits.equals("in") && (height < 59 || height > 76))
			return false;
		if (!heightUnits.equals("cm") && !heightUnits.equals("in"))
			return false;
		if (hcl.charAt(0) != '#' || hcl.length() != 7)
			return false;
		for (int i = 1; i < 7; i++) {
			char letter = hcl.charAt(i);
			if (!(letter >= 48 && letter <= 57) && !(letter >= 97 && letter <= 102))
				return false;
		}
		switch (ecl) {
			case "amb": 
			case "blu":
			case "brn":
			case "gry":
			case "grn":
			case "hzl":
			case "oth": break;
			default: return false;
		}
		if (pid.length() != 9)
			return false;
		for (int i = 0; i < 9; i++) {
			if (!Character.isDigit(pid.charAt(i)))
				return false;
		}
		return true;
	}
	
	public String toString() {
		return "" + byr + " " + iyr + " " + eyr + " " + hgt + " " + hcl + " " + ecl + " " + pid;
	}
	
}
