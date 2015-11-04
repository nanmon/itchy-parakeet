using System;
using Microsoft.Xna.Framework;

namespace Framework3D
{
	public struct Rect3
	{
		public Vector3 A, B;
		public bool Infinite{
			get; set;
		}

		public double Length{
			get{ 
				return (B - A).Length ();
			}
		}

		public Rect3 (Vector3 a, Vector3 b, bool infinite = false)
		{
			this.A = a;
			this.B = b;
			this.Infinite = infinite;
		}

		public Vector3 Lambdian(){
			return B - A;
		}
		public Vector3 Intersection(Rect3 rect){
			if (Vector3.Normalize (Lambdian ()) == Vector3.Normalize (rect.Lambdian ()))
				return Vector3.;

		}

	}
}

