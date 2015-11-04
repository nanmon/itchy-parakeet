using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace TileEngine
{
	static public class Camera
	{
		#region Declarations
		private static Vector2 position = Vector2.Zero;
		private static Vector2 viewPortSize = Vector2.Zero;
		public static Rectangle WorldRectangle = new Rectangle (0, 0, 0, 0);
		#endregion

		#region Properties
		public static Vector2 Position{
			get{ return position; }
			set{
				position = new Vector2 (
					MathHelper.Clamp (
						value.X,
				        WorldRectangle.X, 
				        WorldRectangle.Width-ViewPortWidth
					),
					MathHelper.Clamp (
						value.Y,
				        WorldRectangle.Y,
				        WorldRectangle.Height-ViewPortHeight
				    )
				);
			}
		}

		public static int ViewPortWidth{
			get { return (int)viewPortSize.X; }
			set { viewPortSize.X = value; }
		}

		public static int ViewPortHeight{
			get { return (int)viewPortSize.Y; }
			set { viewPortSize.Y = value; }
		}

		public static Rectangle ViewPort{
			get {
				return new Rectangle (
					(int)Position.X,
					(int)Position.Y,
					ViewPortWidth,
					ViewPortHeight);
			}
			set {
				position.X = value.X;
				position.Y = value.Y;
				viewPortSize.X = value.Width;
				viewPortSize.Y = value.Height;
			}
		}
		#endregion

		#region Public Methods
		public static void Move(Vector2 offset){
			Position += offset;
		}

		public static bool ObjectIsVisible(Rectangle bounds){
			return (ViewPort.Intersects (bounds));
		}

		public static Vector2 WorldToScreen(Vector2 worldLocation){
			return worldLocation - position;
		}

		public static Rectangle WorldToScreen(Rectangle worldRectangle){
			return new Rectangle (
				worldRectangle.Left - (int)position.X,
				worldRectangle.Top - (int)position.Y,
				worldRectangle.Width,
				worldRectangle.Height);
		}

		public static Vector2 ScreenToWorld(Vector2 screenLocation){
			return screenLocation + position;
		}

		public static Rectangle ScreenToWorld(Rectangle screenRectangle){
			return new Rectangle (
				screenRectangle.Left + (int)position.X,
				screenRectangle.Top + (int)position.Y,
				screenRectangle.Width,
				screenRectangle.Height);
		}
		#endregion
	}
}

