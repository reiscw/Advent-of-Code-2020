import java.io.*;
import java.util.*;

public class AdventOfCode21 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input21.txt"));
		ArrayList<Food> foods = new ArrayList<Food>();
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<String> allergens = new ArrayList<String>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			String first = line.substring(0, line.indexOf("("));
			String second = line.substring(line.indexOf("("));
			second = second.substring(second.indexOf("contains")+9, second.length()-1);
			String[] i = first.split(" ");
			String[] a = second.split(", ");
			for (String element : i) {
				if (!ingredients.contains(element)) 
					ingredients.add(element);
			}
			for (String element : a) {
				if (!allergens.contains(element))
					allergens.add(element);
			}
			foods.add(new Food(i, a));
		}
		ArrayList<String> inerts = part1(ingredients, allergens, foods);
		part2(ingredients, allergens, foods, inerts);
	}
		
	public static ArrayList<String> part1(ArrayList<String> ingredients, ArrayList<String> allergens, ArrayList<Food> foods) {
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < ingredients.size(); i++) {
			for (int j = 0; j < allergens.size(); j++) {
				boolean match = true;
				for (Food food : foods) {
					if (!food.containsIngredient(ingredients.get(i)) && food.containsAllergen(allergens.get(j))) {
						match = false;
					}
					if (!match) 
						break;
				}
				if (match) {
					result.put(ingredients.get(i), allergens.get(j));
				}
			}
		}
		int count = 0;
		ArrayList<String> inerts = new ArrayList<String>();
		for (String i : ingredients) {
			if (!result.containsKey(i)) {
				inerts.add(i);
				for (Food food : foods) {
					if (food.containsIngredient(i)) 
						count++;
				}
			}
		}
		System.out.println(count);
		return inerts;
	}
	
	public static void part2(ArrayList<String> ingredients, ArrayList<String> allergens, ArrayList<Food> foods, ArrayList<String> inerts) {
		for (int i = 0; i < inerts.size(); i++) {
			ingredients.remove(inerts.get(i));
		}
		ArrayList<Allergen> solution = new ArrayList<Allergen>();
		for (int i = 0; i < allergens.size(); i++) {
			ArrayList<String> possibles = new ArrayList<String>();
			for (int j = 0; j < ingredients.size(); j++) {
				boolean candidate = true;
				for (Food food : foods) {
					if (food.containsAllergen(allergens.get(i))) {
						if (!food.containsIngredient(ingredients.get(j))) {
							candidate = false;
							break;
						}
					}
				}
				if (candidate) {
					possibles.add(ingredients.get(j));
				}
			}
			solution.add(new Allergen(allergens.get(i), possibles));
		}
		Collections.sort(solution);
		//System.out.println(solution);
		for (int i = 0; i < solution.size(); i++) {
			Allergen current = solution.get(i);
			String ingredient = current.getPossibles().get(0);
			for (int j = i + 1; j < solution.size(); j++) {
				solution.get(j).getPossibles().remove(ingredient);
			}
			Collections.sort(solution);
		}
		//System.out.println(solution);
		ArrayList<SolutionElement> elements = new ArrayList<SolutionElement>();
		for (Allergen a : solution) {
			elements.add(new SolutionElement(a.getName(), a.getPossibles().get(0)));
		}
		Collections.sort(elements);
		for (SolutionElement e : elements) {
			System.out.print(e.getIngredient()+",");
		}
	}

}

class Food {
	
	private String[] ingredients;
	private String[] allergens;
	
	public Food(String[] ingredients, String[] allergens) {
		this.ingredients = ingredients;
		this.allergens = allergens;
	}
	
	public String[] getIngredients() {return ingredients;}
	public String[] getAllergens() {return allergens;}
	
	public boolean containsIngredient(String ingredient) {
		for (String i : ingredients) {
			if (i.equals(ingredient))
				return true;
		}
		return false;	
	}
	
	public boolean containsAllergen(String allergen) {
		for (String a : allergens) {
			if (a.equals(allergen))
				return true;
		}
		return false;
	}
	
}

class Allergen implements Comparable {
	
	private String name;
	private ArrayList<String> possibles;
	
	public Allergen(String name, ArrayList<String> possibles) {
		this.name = name;
		this.possibles = possibles;
	}
	 
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getPossibles() {
		return possibles;
	}
	
	public String toString() {return name + " " + possibles;}
	
	public int compareTo(Object other) {
		return possibles.size() - ((Allergen)other).getPossibles().size();
	}
}

class SolutionElement implements Comparable {
	
	private String allergen;
	private String ingredient;
	
	public SolutionElement(String allergen, String ingredient) {
		this.allergen = allergen;
		this.ingredient = ingredient;
	}
	
	public String getAllergen() {return allergen;}
	public String getIngredient() {return ingredient;}
	
	public int compareTo(Object other) {
		return allergen.compareTo(((SolutionElement)other).getAllergen());
	}
	
}
