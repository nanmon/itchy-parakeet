using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework.Storage;
using System.IO;
using System.Xml.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;
using HooHaaUtils;

namespace TileEngine
{
	public static class TileMap
	{
		#region Declarations
		public const int MAPVERSION = 1;
		public const int TileSize = 32;
		private static int mapWidth = 160;
		private static int mapHeight = 24;
		private static int DefaultWidth;
		private static int DefaultHeight;
		public static MapCellMan MapCells = new MapCellMan();
		public static MapSquareMan MapSquares = new MapSquareMan();
		private static BackgroundMan background = new BackgroundMan ();
		private static ForegroundMan foreground = new ForegroundMan ();
		public static BitFont spriteFont;
		static private Texture2D tileSheet;
		static private int drugoidal = 0;
		static public bool OnDrugs = false;
		public static bool EditorMode = false;
		#endregion

		#region Properties
		public static int MapWidth {
			get{
				return mapWidth;
			}
			set{
				if (!EditorMode)
					throw new EditModeException();
				MapSquares.Resize (width: value, height: MapHeight);
				foreground.Resize (width: value, height: MapHeight);
				mapWidth = value;
				Camera.WorldRectangle.Width = mapWidth * TileSize;
			}
		}
		public static int MapHeight{
			get{ return mapHeight; } 
			set{
				if (!EditorMode)
					throw new EditModeException ();
				MapSquares.Resize (height: value, width: MapWidth);
				foreground.Resize (height: value, width: MapWidth);
				mapHeight = value;
				Camera.WorldRectangle.Height = mapHeight * TileSize;
			}
		}

		public static BackgroundMan Background{
			get{
				if (!EditorMode)
					throw new EditModeException ();
				return background;
			}
		}

		public static ForegroundMan Foreground{
			get{ if (!EditorMode)
					throw new EditModeException ();
				return foreground;
			}
		}
		#endregion

		public static void Main(string[] args){

		}

		#region Initialization
		/// <summary>
		/// EditMode Initialization
		/// </summary>
		/// <param name="backgrounds">Backgrounds.</param>
		/// <param name="tileTexture">Tile texture.</param>
		/// <param name="width">Width.</param>
		/// <param name="height">Height.</param>
		static public void Initialize(
			Texture2D[] backgrounds, 
			Texture2D tileTexture, 
			int width, 
			int height)
		{
			EditorMode = true;
			MapHeight = DefaultHeight = height / TileSize;
			MapWidth = DefaultWidth = width / TileSize;
			Initialize (backgrounds, tileTexture);

		}

		public static void Initialize(
			Texture2D[] backgrounds,
			Texture2D tileTexture)
		{
			tileSheet = tileTexture;
			background.textures = backgrounds;
			MapSquares.squares = new MapSquare[MapWidth,MapHeight];
			foreground.f = new int[MapWidth,MapHeight];

			ClearMap ();
		}
		#endregion

		//Simple TileEngining

		#region MapCells
		///Informacion de la celda: ints & asi
		public class MapCellMan{

			public class SingleMapCell{
				public int X {
					get;
					internal set;
				}

				public int Y {
					get;
					internal set;
				}

				public Vector2 Center { 
					get{
						return new Vector2 (
							(int)(TileMap.TileSize * (X + 0.5f)),
							(int)(TileMap.TileSize * (Y + 0.5f)));
					}
				}

				public Vector2 CellWorldPosition {
					get {
						return new Vector2 (
							X * TileMap.TileSize,
							Y * TileMap.TileSize);
					}
				}

				public Rectangle CellWorldRectangle{
					get {
						return new Rectangle (
							X* TileMap.TileSize,
							Y * TileMap.TileSize,
							TileMap.TileSize,
							TileMap.TileSize);
					}
				}

				public Vector2 CellScreenPosition{
					get { return Camera.WorldToScreen (CellWorldPosition); }
				}

				public Rectangle CellScreenRectangle{ 
					get { return Camera.WorldToScreen (CellWorldRectangle); }
				}

			}

			SingleMapCell cell = new SingleMapCell();


			public SingleMapCell this[int x, int y]{
				get{
					cell.X = (int)MathHelper.Clamp (x, 0, TileMap.MapWidth-1);
					cell.Y = (int)MathHelper.Clamp (y, 0, TileMap.MapHeight-1);
					return cell;
				}
			}

			public SingleMapCell GetCellByPixel(Vector2 pixelLocation){ 
				return this[
					GetColumnByPixelX ((int)pixelLocation.X),
					GetRowByPixelY ((int)pixelLocation.Y)];
			}

			public int GetColumnByPixelX(int pixelX){
				return (int)MathHelper.Clamp (
					pixelX / TileSize,
					0, 
					MapWidth-1);
			}

			public int GetRowByPixelY(int pixelY){
				return (int)MathHelper.Clamp (
					pixelY / TileSize,
					0,
					MapHeight-1); 
			}


		}


		#endregion

		#region MapSquares
		///Tangible
		public class MapSquareMan{

			internal MapSquare[,] squares = null;

			internal void Resize(
				int width, 
				int height)
			{
				if (width == TileMap.MapWidth && height == TileMap.MapHeight)
					return;
				MapSquare[,] aux = squares;
				squares = new MapSquare[width, height];
				if (aux == null)
					return;
				int w = (int)MathHelper.Min (aux.GetLength (0), width);
				int h = (int)MathHelper.Min (aux.GetLength (1), height);
				for (int x=0; x<w; ++x)
					for (int y=0; y<h; ++y)
						squares [x, y] = aux [x, y];
			}

			public MapSquare this[int tileX, int tileY]{
				get{
					if (tileX >= 0 && tileX < MapWidth && tileY >= 0 && tileY < MapHeight) {
						if (TileMap.EditorMode && squares [tileX, tileY] == null)
							squares [tileX, tileY] = new MapSquare ();
						return squares [tileX, tileY];
					} else
						return null;
				}
				set{
					if (!TileMap.EditorMode)
						throw new EditModeException ();
					if (tileX >= 0 && tileX < MapWidth && tileY >= 0 && tileY < MapHeight) {
						if(value == null){
							squares [tileX, tileY] = null;
							return;
						}
						if (squares [tileX, tileY] == null)
							squares [tileX, tileY] = new MapSquare ();
						squares [tileX, tileY].Copy (value);
					}
				}
			}

			public MapSquare this[Vector2 pos]{
				get{ return this [(int)pos.X, (int)pos.Y]; }
				set{ this [(int)pos.X, (int)pos.Y] = value; }
			}

			public MapSquare AtPixel(int pixelX, int pixelY){
				return this[
					TileMap.MapCells.GetColumnByPixelX (pixelX),
					TileMap.MapCells.GetRowByPixelY (pixelY)];
			}

			public MapSquare AtPixel(Vector2 pixelLocation){
				return AtPixel (
					(int)pixelLocation.X,
					(int)pixelLocation.Y);
			}

			public bool IsRectanglePassable(Rectangle rect){
				int startX = TileMap.MapCells.GetColumnByPixelX (rect.X);
				int startY = TileMap.MapCells.GetRowByPixelY (rect.Y);
				int endX = TileMap.MapCells.GetColumnByPixelX (rect.X + rect.Width - 1);
				int endY = TileMap.MapCells.GetRowByPixelY (rect.Y + rect.Height - 1);
				for (int x = startX; x <= endX; ++x)
					for (int y = startY; y <= endY; ++y)
						if (!this[x,y].IsPassable())
							return false;
				return true;
			}

		}
		#endregion

		#region Tile and Tile Sheet Handling
		public static int TilesPerRow{
			get{ return tileSheet.Width / TileSize; }
		}

		public static Rectangle TileSourceRectangle(int tileIndex){
			return new Rectangle (
				(tileIndex % TilesPerRow) * TileSize,
				(tileIndex / TilesPerRow) * TileSize,
				TileSize,
				TileSize
			);
		}
		#endregion

		#region Drawing
		static public void Begin(SpriteBatch spriteBatch)//, bool drugged)
		{
			int startX = (int)(Camera.Position.X/2)%background.Width, endX=0, startY, endY;

			while (endX < Camera.ViewPortWidth) {
				spriteBatch.Draw (
					background.Texture,
					new Rectangle (
					endX, 
					0, 
					(int)MathHelper.Min(
					(background.Width - startX)*Camera.ViewPortHeight/background.Height, 
					Camera.ViewPortWidth - endX), 
					Camera.ViewPortHeight),
					new Rectangle (
					startX, 
					0, 
					(int)MathHelper.Min(
					background.Width - startX, 
					(Camera.ViewPortWidth - endX)*background.Height/Camera.ViewPortHeight), 
					background.Height),
					Color.White);
				endX += (background.Width - startX)*Camera.ViewPortHeight/background.Height;
				startX = 0;
			}

			startX = MapCells.GetColumnByPixelX ((int)Camera.Position.X);
			endX = MapCells.GetColumnByPixelX ((int)Camera.Position.X + Camera.ViewPortWidth);
			startY = MapCells.GetRowByPixelY ((int)Camera.Position.Y);
			endY = MapCells.GetRowByPixelY ((int)Camera.Position.Y + Camera.ViewPortHeight);
			for (int x = startX; x <= endX; x++)
				for (int y = startY; y <= endY; y++) {
					if (MapSquares[x,y] == null || !MapSquares[x,y].HasToDraw())
						continue;
					spriteBatch.Draw (
						tileSheet, 
						MapCells[x,y].CellScreenRectangle, 
						TileSourceRectangle (MapSquares [x, y][OnDrugs?1:0]),
						Color.White
					);
				}
		}

		public static void End(SpriteBatch spriteBatch){
				int startX = MapCells.GetColumnByPixelX ((int)Camera.Position.X);
				int endX = MapCells.GetColumnByPixelX ((int)Camera.Position.X + Camera.ViewPortWidth);
				int startY = MapCells.GetRowByPixelY ((int)Camera.Position.Y);
				int endY = MapCells.GetRowByPixelY ((int)Camera.Position.Y + Camera.ViewPortHeight);
				for (int x = startX; x <= endX; x++)
					for (int y = startY; y <= endY; y++) {
					if (foreground[x,y] <=0 )
						continue;
					spriteBatch.Draw (
						tileSheet, 
						MapCells[x,y].CellScreenRectangle, 
						TileSourceRectangle (foreground[x, y]),
						Color.White
						); 
				}
			}

		#endregion

		#region Loading and Saving Maps

        public static void SaveMap(FileStream fileStream)
        {
            BinaryFormatter formatter = new BinaryFormatter();
			Map mapita = new Map (
				background.Index, 
				MapSquares.squares, 
				foreground.f,
				MapWidth,
				MapHeight,
				MAPVERSION);
            formatter.Serialize(fileStream, mapita);
            fileStream.Close();
        }

        public static void LoadMap(FileStream fileStream)
        {
            try
            {
                BinaryFormatter formatter = new BinaryFormatter();
                Map aux = (Map)formatter.Deserialize(fileStream);
				if(aux.version != MAPVERSION){
					Console.Write("ERROR: Mapa incompatible");
					throw new MapIncompatible ();
				}
				MapWidth = aux.width;
				MapHeight = aux.height;
                MapSquares.squares = aux.mapSquares;
				background.Index = aux.background;
				foreground.f = aux.foreground;
            }
            catch
            {
                ClearMap();
				Console.Write ("ERROR: no se puedo read archivo .MAP");
            } finally{
				fileStream.Close ();
			}
        }
        public static void ClearMap()
        {
			background.Index = 0;
			MapWidth = DefaultWidth;
			MapHeight = DefaultHeight;
			for (int x=0; x<MapWidth; ++x)
				for (int y=0; y<MapHeight; ++y)
					MapSquares [x, y] = null;

			for (int x = 0; x < MapWidth; x++)
				for (int y = 0; y < MapHeight; y++)
					foreground[x, y] = 0;
        }
		#endregion

		//Editing Mode Functions

		#region Background

		public class BackgroundMan{
			private int index = 0;

			internal Texture2D[] textures = null;

			public int Width{
				get{ return textures [index].Width;}
			}

			public int Height{
				get{ return textures [index].Height; }
			}

			public Texture2D Texture{
				get{ return textures [index]; }
			}

			public int Index{
				get { return index; }
				set { 
					index = value >= textures.Length ? 0 : value < 0 ? textures.Length - 1 : value; }
			}

		}


		#endregion

		#region Foreground

		public class ForegroundMan{
			internal int[,] f = null;

			internal void Resize(
				int width, 
				int height)
			{
				if (width == TileMap.MapWidth && height == TileMap.MapHeight)
					return;
				int[,] aux = f;
				f = new int[width, height];
				if (aux == null)
					return;
				int w = (int)MathHelper.Min (aux.GetLength (0), width);
				int h = (int)MathHelper.Min (aux.GetLength (1), height);
				for (int x=0; x<w; ++x)
					for (int y=0; y<h; ++y)
						f [x, y] = aux [x, y];
			}

			public int this[int x, int y]{
				get{
					if (x >= 0 && x < MapWidth && y >= 0 && y < MapHeight)
						return f [x, y];
					else
						return -1;
				}
				set{
					if (x >= 0 && x < MapWidth && y >= 0 && y < MapHeight)
						f [x, y] = value;
				}
			}

			public int this[Vector2 tile]{
				get{ return this [(int)tile.X, (int)tile.Y]; }
				set{ this [(int)tile.X, (int)tile.Y] = value; }
			}

			public int AtPixel(int pixelX, int pixelY){
				return this[
					TileMap.MapCells.GetColumnByPixelX (pixelX),
					TileMap.MapCells.GetRowByPixelY (pixelY)];
			}

			public int AtPixel(Vector2 pixelLocation){
				return AtPixel (
					(int)pixelLocation.X,
					(int)pixelLocation.Y);
			}
		}

		#endregion

		#region Edit Mode Drawing

		// Drawing
		/// <summary>
		/// Draws the edit mode.
		/// </summary>
		/// <param name="spriteBatch">Sprite batch.</param>
		/// <param name="currentState">
		/// Current state { background=0, clean, drugged, foreground, both, codevalue, options }
		/// </param>
		static public void DrawEditMode(SpriteBatch spriteBatch, int currentState)//, bool drugged)
		{
			int startX = (int)(Camera.Position.X/2)%background.Width, endX=0, startY, endY;

			while (endX < Camera.ViewPortWidth) {
				spriteBatch.Draw (
					background.Texture,
					new Rectangle (
						endX, 
						0, 
						(int)MathHelper.Min(
							(background.Width - startX)*Camera.ViewPortHeight/background.Height, 
							Camera.ViewPortWidth - endX), 
						Camera.ViewPortHeight),
					new Rectangle (
						startX, 
						0, 
						(int)MathHelper.Min(
							background.Width - startX, 
							(Camera.ViewPortWidth - endX)*background.Height/Camera.ViewPortHeight), 
						background.Height),
					Color.White);
				endX += (background.Width - startX)*Camera.ViewPortHeight/background.Height;
				startX = 0;
			}

			startX = MapCells.GetColumnByPixelX ((int)Camera.Position.X);
			endX = MapCells.GetColumnByPixelX ((int)Camera.Position.X + Camera.ViewPortWidth);
			startY = MapCells.GetRowByPixelY ((int)Camera.Position.Y);
			endY = MapCells.GetRowByPixelY ((int)Camera.Position.Y + Camera.ViewPortHeight);
			for (int x = startX; x <= endX; x++)
				for (int y = startY; y <= endY; y++) {
					if (MapSquares.squares[x,y] == null)
						continue;
					if (MapSquares.squares[x, y].IsActiveOnEditor (currentState)) {
						spriteBatch.Draw (
							tileSheet, 
							MapCells[x,y].CellScreenRectangle, 
							TileSourceRectangle (MapSquares[x, y] [currentState >= 3 ? 0 : currentState - 1]),
							Color.White
						);
					}
					DrawEditModeItems (spriteBatch, x, y);
				}
			for (int x = startX; x <= endX; x++)
				for (int y = startY; y <= endY; y++) {
				if (foreground[x,y]==0)
					continue;
				spriteBatch.Draw (
					tileSheet, 
					MapCells[x,y].CellScreenRectangle, 
					TileSourceRectangle (foreground[x, y]),
					Color.White*(currentState == 3 ? 1.0f: 0.2f)
					); 
			}
			if (currentState > 4)
				spriteBatch.Draw (
					tileSheet,
					new Rectangle (0, 0, Camera.ViewPortWidth, Camera.ViewPortHeight),
					TileSourceRectangle (1),
					Color.Black * 0.5f);
		}

		public static void DrawEditModeItems(SpriteBatch spriteBatch, int x, int y){

			spriteBatch.Draw (
				tileSheet,
				MapCells[x,y].CellScreenRectangle,
				TileSourceRectangle (1),
				new Color (
					MapSquares[x,y].IsPassable (0) ? 0 : 255, 
					MapSquares[x,y].IsPassable (1) ? 0 : 255, 0)*0.5f);
			if(MapSquares[x, y].CodeValue.Length != 0){
				spriteFont.DrawText (
					spriteBatch, 
					MapCells [x,y].CellScreenPosition, 
					MapSquares [x, y].CodeValue);
			}
		}
		#endregion


	}

	#region Exceptions
	public class MapIncompatible : System.ApplicationException {
		public MapIncompatible(string message = null, System.Exception inner = null): base(message, inner){
		}
		protected MapIncompatible(
			System.Runtime.Serialization.SerializationInfo info,
		    System.Runtime.Serialization.StreamingContext context):
		base(info, context){}
	}

	public class EditModeException : System.ApplicationException {
		public EditModeException(string message = null, System.Exception inner = null): base(message, inner){
		}
		protected EditModeException(
			System.Runtime.Serialization.SerializationInfo info,
			System.Runtime.Serialization.StreamingContext context):
			base(info, context){}
	}
	#endregion
}

