import java.util.Scanner;
import java.util.Random;

class Ship {
	// the start of the ship
	Tile head;
	// is the orientation vertical or horizontal
	boolean is_vert;
	int len;
	int hits;
	boolean sunk;
	char c;
	String name;
	public boolean on_hit() {
		hits++;
		System.out.println("You hit the " + name);
		if (hits == len) {
			sunk = true;
			System.out.println("You sunk the " + name);
		}
		return sunk;
	}
	public Ship(String n, Tile h, boolean v, int l, char ic) {
		name = n;
		head = h;
		is_vert = v;
		len = l;
		c = ic;
		hits = 0;
		sunk = false;
	}
}
class Tile {
	int x;
	int y;
	Ship ship;
	boolean is_hit;
	public boolean on_hit() {
		is_hit = true;
		if (has_ship()) {
			return ship.on_hit();
		}
		return false;
	}
	public Tile(int ix, int iy) {
		x = ix;
		y = iy;
		ship = null;
		is_hit = false;
	}
	public boolean has_ship() {
		return ship != null;
	}
	public char get_hit_char() {
		return is_hit ? has_ship() ? 'x' : 'o' : '.';
	}
	public char get_ship_char(boolean hidden) {
		char c = has_ship() ? ship.c : '_';
		return !hidden ? c : is_hit ? c : '?';
	}
}
class Battleship {
	Random r;
	Scanner s;
	int width;
	int height;
	boolean show;
	Tile[][] tiles;
	Ship[] ships;
	int hits;
	int tries;
	public static void main(String[] args) {
		Battleship b = new Battleship();
	}
	public Battleship() {
		tries = 0;
		r = new Random();
		s = new Scanner(System.in);
		show = false;
		width = 10;
		height = 10;
		ships = new Ship[1];
		hits = 0;

		// tiles
		tiles = new Tile[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[y][x] = new Tile(x, y);
			}
		}

		// make ships
		int[] ship_sizes = new int[]{ 5, 4, 3, 3, 2 };
		char[] ship_chars = new char[]{'c', 'b', 'd', 's', 'p' };
		String[] ship_names = new String[]{"Carrier", "Battleship", "Destroyer", "Submarine", "Patrol" };

		for (int i = 0; i < ships.length; i++) {
			Ship s = new Ship(ship_names[i], get_rand_tile(), r.nextBoolean(), ship_sizes[i], ship_chars[i]);
			while (!try_add_ship(s)) {
				s = new Ship(ship_names[i], get_rand_tile(), r.nextBoolean(), ship_sizes[i], ship_chars[i]);
			}
		}

		while(!do_move());
		System.out.printf("You won! It took you %d tries to win!\n", tries);
	}
	// tiles
	public boolean in_bounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	public Tile get_tile(int x, int y) {
		return in_bounds(x, y) ? tiles[y][x] : null;
	}
	public Tile get_rand_tile() {
		return get_tile(r.nextInt(width), r.nextInt(height));
	}
	public Tile get_tile_in_dir(int x, int y, boolean is_vert, int len) {
		if (is_vert) {
			y += len;
		}
		else {
			x += len;
		}
		return get_tile(x, y);
	}
	// ships
	public boolean try_add_ship(Ship ship) {
		Tile[] tiles = new Tile[ship.len];
		int x = ship.head.x;
		int y = ship.head.y;

		for (int i = 0; i < ship.len; i++) {
			Tile t = get_tile_in_dir(x, y, ship.is_vert, i);
			tiles[i] = t;
			if (t == null || t.has_ship()) {
				return false;
			}
		}
		for (int i = 0; i < ship.len; i++) {
			tiles[i].ship = ship;
		}
		add_ship(ship);

		return true;
	}
	public void add_ship(Ship ship) {
		for (int i = 0; i < ships.length; i++) {
			if (ships[i] == null) {
				ships[i] = ship;
				return;
			}
		}
	}
	// io
	public boolean try_hit(int x, int y) {
		boolean sunk = tiles[y][x].on_hit();
		tries++;
		if (sunk) {
			hits++;
		}
		if (hits == ships.length) {
			return true;
		}
		return false;
	}
	public boolean do_move() {
		print_board();
		System.out.println("Enter the coordinate to hit in the form (x, y): ");
		int[] vals = new int[2];
		String in = s.nextLine();
		if (in.equals("/show")) {
			show = true;
			System.out.println("Revealing board");
			return false;
		}
		int icount = 0;
		boolean started = false;
		int start = -1;
		for (int i = 0; i < in.length(); i++) {
			if (!started && Character.isDigit(in.charAt(i))) {
				start = i;
				started = true;
			}
			else if (started && !Character.isDigit(in.charAt(i))) {
				vals[icount] = Integer.parseInt(in.substring(start, i));
				started = false;
				icount++;
			}
		}
		if (started) {
			vals[icount] = Integer.parseInt(in.substring(start, in.length()));
		}
		if (!in_bounds(vals[0], vals[1])) {
			System.out.println("Invalid coordinate, go again.");
			return false;
		}
		return try_hit(vals[0], vals[1]);
	}
	public void print_board() {
		for (int x = 0; x < width; x++) {
			System.out.printf("\t%d", x);
		}
		for (int y = 0; y < height; y++) {
			System.out.printf("\n%d", y);
			for (int x = 0; x < width; x++) {
				System.out.printf("\t%c", tiles[y][x].get_hit_char());
			}
			System.out.println("");
			for (int x = 0; x < width; x++) {
				 System.out.printf("\t%c", tiles[y][x].get_ship_char(!show));
				//System.out.printf("\t%c", 'f');
			}
		}
		System.out.printf("\n");
	}
}
