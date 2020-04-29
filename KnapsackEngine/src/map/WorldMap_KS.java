package map;

import gfx.DrawGraphics;

public abstract class WorldMap_KS {

	int mapLayers;
	
	public WorldMap_KS(int mapLayers) {
		this.mapLayers = mapLayers;
	}
	
	public abstract BaseTile_KS getTile(int x, int y, int map);
	
	public abstract void render(DrawGraphics g);
	
	public abstract void update();
	
	
	public int getLayers() {
		return mapLayers;
	}
	
}
