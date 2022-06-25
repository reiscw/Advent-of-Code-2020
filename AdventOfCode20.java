import java.util.*;
import java.io.*;

public class AdventOfCode20 {
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("input20.txt"));
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		while (in.hasNextLine()) {
			String header = in.nextLine();
			char[][] image = new char[10][10];
			for (int i = 0; i < 10; i++) {
				image[i] = in.nextLine().toCharArray();
			}
			int number = Integer.parseInt(header.substring(5, 9));
			if (in.hasNextLine()) in.nextLine();
			tiles.add(new Tile(number, image));
		}
		part1(tiles);		
		part2(tiles);	
	}
	
	public static void part1(ArrayList<Tile> tiles) {
		long product = 1;
		for (Tile tile : tiles) {
			if (tile.edgeMatches(tiles) == 2) {
				product = product * tile.getNumber();
			}
		}
		System.out.println(product);
	}
	
	public static void part2(ArrayList<Tile> tiles) {
		Picture myPicture = new Picture(tiles);
		myPicture.arrangeTiles();
		myPicture.getPicture();
		System.out.println(myPicture.solve());
	}
}

class Picture {
	
	private Tile[][] tiles;
	private ArrayList<Tile> tileList;
	private char[][] picture;
	private int dim;
	
	public Picture(ArrayList<Tile> tileList) {
		this.tileList = tileList;
		dim = (int)(Math.sqrt(tileList.size()));
		tiles = new Tile[dim][dim];
		picture = new char[8*dim][8*dim];
	}
	
	public void arrangeTiles() {
		// find a corner tile
		Tile corner = null;
		for (Tile tile : tileList) {
			if (tile.edgeMatches(tileList) == 2) {
				corner = tile;
				break;
			}
		}		
		// arrange it in the right configuration
		corner.cornerConfig(tileList);
		
		// for each slot in the tile array
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				// if it's 0, 0, put the corner tile in place
				if (i == 0 && j == 0) {
					tiles[i][j] = corner;
				} else if (j == 0) {
					// if it's 0th element in a row, move it so it matches with the tile above
					// Get tile above
					Tile above = tiles[i-1][0];
					// Get bottom edge
					char[] bottomEdge = above.edges()[2];
					// search the rest of the tiles
					int match = -1;
					Tile next = null;
					for (Tile tile : tileList) {
						char[][] edges = tile.edges();
						for (int k = 0; k < 8; k++) {
							if (tile.getNumber() != above.getNumber() && Tile.edgeEquals(bottomEdge, edges[k])) {
								next = tile;
								match = k;
							}
						} 
					}
					// perform the necessary transformations
					switch (match) {
						case 0: break; // top
						case 1: next.flip(); break; // top, reversed
						case 2: next.flip(); next.rotateRight(); next.rotateRight(); break; // bottom
						case 3: next.rotateRight(); next.rotateRight(); break; // bottom, reversed
						case 4: next.rotateRight(); next.flip(); break; // left
						case 5: next.rotateRight(); break; // left, reversed
						case 6: next.rotateRight(); next.rotateRight(); next.rotateRight(); break; // right
						case 7: next.flip(); next.rotateRight(); break; // right, reversed
					}
					// insert the tile in the array
					tiles[i][j] = next;
				} else {
					// otherwise, move it so it matches with the tile to the left
					// Get tile to the left
					Tile left = tiles[i][j-1];
					// Get right edge
					char[] rightEdge = left.edges()[6];
					// search the rest of the tiles
					int match = -1;
					Tile next = null;
					for (Tile tile : tileList) {
						char[][] edges = tile.edges();
						for (int k = 0; k < 8; k++) {
							if (tile.getNumber() != left.getNumber() && Tile.edgeEquals(rightEdge, edges[k])) {
								next = tile;
								match = k;
							}
						} 
					}
					// perform the necessary transformations
					switch (match) {
						case 0: next.rotateRight(); next.flip(); break; // top
						case 1: next.rotateRight(); next.rotateRight(); next.rotateRight(); break; // top, reversed
						case 2: next.rotateRight(); break; // bottom
						case 3: next.rotateRight(); next.rotateRight(); next.rotateRight(); next.flip(); break; // bottom, reversed
						case 4: break; // left
						case 5: next.rotateRight(); next.rotateRight(); next.flip(); break; // left, reversed
						case 6: next.flip(); break; // right
						case 7: next.rotateRight(); next.rotateRight(); break; // right, reversed
					}
					// insert the tile in the array
					tiles[i][j] = next;
				}
				tileList.remove(tiles[i][j]);
			}
		}
	}
	
	public void getPicture() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				for (int k = 1; k <= 8; k++) {
					for (int l = 1; l <= 8; l++) {
						picture[i*8+k-1][j*8+l-1] = (tiles[i][j].getImage())[k][l];
					}
				}
			}
		}
	}
	
	public void displayNumbers() {
		for (Tile[] row : tiles) {
			for (Tile tile : row) {
				if (tile == null) System.out.print("null ");
				else System.out.print(tile.getNumber() + " ");
			}
			System.out.println();
		}
	}
	
	public int solve() {
		// for each reflection status
		for (int reflection = 0; reflection < 2; reflection++) {
			// for each rotation status
			for (int rotation = 0; rotation < 4; rotation++) {
				// traverse the image
				for (int i = 0; i < picture.length; i++) {
					for (int j = 0; j < picture.length; j++) {
						// at each position, look for the pattern
						// if found, replace the pattern with Os
						if (patternExists(i, j)) {
							patternReplace(i, j);
						}
					}
				}
				rotate();
			}
			reflect();
		}
		// count the number of # signs remaining
		int count = 0;
		for (char[] row : picture) {
			for (char letter : row) {
				if (letter == '#') {
					count++;
				}
			}
		}
		return count;
	}
	
	public boolean patternExists(int i, int j) {
		if (i + 2 >= picture.length || j + 19 >= picture.length)
			return false;
		else {
			int count = 0;
			if (picture[i+0][j+18] == '#') count++;
			if (picture[i+1][j+0] == '#') count++;
			if (picture[i+1][j+5] == '#') count++;
			if (picture[i+1][j+6] == '#') count++;
			if (picture[i+1][j+11] == '#') count++;
			if (picture[i+1][j+12] == '#') count++;
			if (picture[i+1][j+17] == '#') count++;
			if (picture[i+1][j+18] == '#') count++;
			if (picture[i+1][j+19] == '#') count++;
			if (picture[i+2][j+1] == '#') count++;
			if (picture[i+2][j+4] == '#') count++;
			if (picture[i+2][j+7] == '#') count++;
			if (picture[i+2][j+10] == '#') count++;
			if (picture[i+2][j+13] == '#') count++;
			if (picture[i+2][j+16] == '#') count++;
			return count == 15;
		}
	}
	
	public void patternReplace(int i, int j) {
		picture[i+0][j+18] = 'O';
		picture[i+1][j+0] = 'O';
		picture[i+1][j+5] = 'O';
		picture[i+1][j+6] = 'O';
		picture[i+1][j+11] = 'O';
		picture[i+1][j+12] = 'O';
		picture[i+1][j+17] = 'O';
		picture[i+1][j+18] = 'O';
		picture[i+1][j+19] = 'O';
		picture[i+2][j+1] = 'O';
		picture[i+2][j+4] = 'O';
		picture[i+2][j+7] = 'O';
		picture[i+2][j+10] = 'O';
		picture[i+2][j+13] = 'O';
		picture[i+2][j+16] = 'O';
	}
	
	public void rotate() {
		char[][] newPicture = new char[picture.length][picture.length];
		for (int i = 0; i < newPicture.length; i++) {
			for (int j = 0; j < newPicture[0].length; j++) {
				newPicture[j][picture.length - 1 - i] = picture[i][j];
			}
		}
		picture = newPicture;
	}
	
	public void reflect() {
		char[][] newPicture = new char[picture.length][picture.length];
		for (int i = 0; i < newPicture.length; i++) {
			for (int j = 0; j < newPicture[0].length; j++) {
				newPicture[i][j] = picture[i][picture.length - 1 - j];
			}
		}
		picture = newPicture;
	}
	
	public void displayPicture() {
		for (char[] row : picture) {
			System.out.println(new String(row));
		}
	}
	
}

class Tile {
	
	protected int number;
	protected char[][] image;
	
	public Tile(int number, char[][] image) {
		this.number = number;
		this.image = image;
	}

	public int getNumber() {return number;}

	public char[][] getImage() {return image;}

	public char[][] edges() {
		char[][] result = new char[8][10];
		// top
		for (int i = 0; i < 10; i++) {
			result[0][i] = image[0][i];
		}
		// top, reversed
		for (int i = 0; i < 10; i++) {
			result[1][i] = image[0][9-i];
		}
		// bottom
		for (int i = 0; i < 10; i++) {
			result[2][i] = image[9][i];
		}
		// bottom, reversed
		for (int i = 0; i < 10; i++) {
			result[3][i] = image[9][9-i];
		}
		// left
		for (int i = 0; i < 10; i++) {
			result[4][i] = image[i][0];
		}
		// left, reversed
		for (int i = 0; i < 10; i++) {
			result[5][i] = image[9-i][0];
		}
		// right
		for (int i = 0; i < 10; i++) {
			result[6][i] = image[i][9];
		}
		// right, reversed
		for (int i = 0; i < 10; i++) {
			result[7][i] = image[9-i][9];
		}
		return result;
	}
	
	public int edgeMatches(ArrayList<Tile> tiles) {
		int count = 0;
		for (Tile tile : tiles) {
			if (tile.getNumber() != number) {
				char[][] oneEdges = this.edges();
				char[][] twoEdges = tile.edges();
				boolean success = false;
				for (int i = 0; i < 8 && !success; i++) {
					for (int j = 0; j < 8 && !success; j++) {
						if (Tile.edgeEquals(oneEdges[i], twoEdges[j])) {
							count++;
							success = true;
						}
					}
				}
			}
		}
		return count;
	}
	
	public static boolean edgeEquals(char[] array1, char[] array2) {
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != array2[i]) 
				return false;
		}
		return true;
	}
	
	public void flip() {
		char[][] newImage = new char[image.length][image.length];
		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[0].length; j++) {
				newImage[i][j] = image[i][image.length - 1 - j];
			}
		}
		image = newImage;
	}
	
	public void rotateRight() {
		char[][] newImage = new char[image.length][image.length];
		for (int i = 0; i < newImage.length; i++) {
			for (int j = 0; j < newImage[0].length; j++) {
				newImage[j][image.length - 1 - i] = image[i][j];
			}
		}
		image = newImage;
	}

	public void printTile() {
		for (char[] row : image) {
			for (char letter : row) {
				System.out.print(letter);
			}
			System.out.println();
		}
	}
	
	public void cornerConfig(ArrayList<Tile> tiles) {
		int count = 0;
		boolean top = false;
		boolean right = false;
		boolean bottom = false;
		boolean left = false;
		char[][] edges = edges();
		for (int i = 0; i < edges.length; i = i + 2) {
			for (Tile tile : tiles) {
				for (char[] otherEdge : tile.edges()) {
					if (getNumber() != tile.getNumber() && edgeEquals(edges[i], otherEdge)) {
						count++;
						if (i == 0 || i == 1) top = true;
						if (i == 2 || i == 3) bottom = true;
						if (i == 4 || i == 5) left = true;
						if (i == 6 || i == 7) right = true;
					}
					if (count == 2) break;
				}
				if (count == 2) break;
			}
			if (count == 2) break;
		}
		if (top && right) {
			rotateRight();
		} else if (top && left) {
			rotateRight();
			rotateRight();
		} else if (left && bottom) {
			rotateRight();
			rotateRight();
			rotateRight();
		} else if (bottom && right) {
			// nothing to do
		} 
	}
	
}
