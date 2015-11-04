using System;

namespace TileEngine
{
	[Serializable]
	public class Map
	{
		public MapSquare[,] mapSquares;
		public int background;
		public int[,] foreground;
		public int version;
		public int width;
		public int height;

		public Map (
			int backG, 
			MapSquare[,] 
			mapC, int[,] foreG, 
			int w,
			int h,
			int v)
		{
			mapSquares = mapC;
			background = backG;
			foreground = foreG;
			version = v;
			width = w;
			height = h;
		}
	}
}

