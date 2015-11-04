using System;
using Microsoft.Xna.Framework;

namespace Framework3D
{
	public static class Camera
	{
		//private static Vector3 position = new Vector3();
		private static Polygon plain = new Polygon(
			Vector3.UnitY*600, 
			Vector3.UnitY*600 + Vector3.UnitX*800, 
			Vector3.UnitX*800);
		//position = plain.U;
		//viewPort = plain.U -> plain.W  
		private static Vector3 eyeball = new Vector3();
		//private static Rectangle viewPort = new Rectangle();

		public static bool IsVisible(Polygon obj){
			Polygon proyected = new Polygon ();
			Vector3 lambda = obj.U - eyeball;

		}
	}
}

