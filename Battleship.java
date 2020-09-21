class Ship {
	Tile head;
	int len;
	char c;
	public Ship(Tile h, int l, char ic) {
		head = h;
		len = l;
		c = c;
	}
}
class Tile {
	int x;
	int y;
	Ship ship;
	boolean is_hit;
	public Tile(int ix, int iy) {
		x = ix;
		y = iy;
	}
}
class Battleship {
	int width;
	int height;
	Tile[][] tiles;
	Ship[] ships;
	public static void main(String[] args) {
		Battleship b = new Battleship();
	}
	public Battleship() {
		width = 10;
		height = 10;
		tiles = new Tile[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[y][x] = new Tile(x, y);
			}
		}
	}
}
