using System;
using Microsoft.Xna.Framework;

namespace Framework3D
{
	public struct Polygon
	{
		public Vector3 U, V, W;
		public Microsoft.Xna.Framework.Quaternion q = new Quaternion ();

		public bool Infinite {
			get;
			set;
		}

		public Polygon(){
			this.U = new Vector3 ();
			this.V = new Vector3 ();
			this.W = new Vector3 ();
			Infinite = false;

		}

		public Polygon (Vector3 u, Vector3 v, Vector3 w)
		{
			this.U = u;
			this.V = v;
			this.W = w;
			Infinite = false;
		}

		public float Area(){
			return (V - U).Length () * (W - (V + U) / 2).Length () / 2;
		}

		public Vector3 Normal() {
			return new Vector3(
				(V.Y - V.X) * (W.Z - W.X) - (V.Z - V.X) * (W.Y - W.X), //*-u.x
			    -(U.Y - U.X) * (W.Z - W.X) + (U.Z - U.X) * (W.Y - W.X), //*-v.X
			    (U.Y - U.X) * (V.Z - V.X) - (U.Z - U.X) * (V.Y - V.X)) ;//*-w.x)
		}

		public bool Intersects(Polygon obj){
			Vector3 n1 = Normal (), n2 = obj.Normal ();
			if (Vector3.Dot (n1,n2) == 0)
				//if(this.Contains(obj.U))
				return false;
			double d1 = -(n1.X * U.X + n1.Y * V.X + n1.Z * W.X);
			double d2 = -(n2.X * obj.U.X + n2.Y * obj.V.X + n2.Z * obj.W.X);
			double div1 = Vector3.Dot (n1, n1);
			double div2 = Vector3.Dot (n1, n2);
			double k2 = (-d1 / div1 - d2 / (div1 * div2)) * (div1 * div2 / (div1 * div2 + Vector3.Dot (n2, n2)));
			double k1 = (-d1 - k2 * div2) / div1;
			Rect3 intersection = new Rect3 (k1 * n1 + k2 * n2, Vector3.Cross (n1, n2), true);

		}

		public bool Intersects(Rect3 rect){
			//Determinante shit
			//double a = ( (V.Y - V.X) * (W.Z - W.X) - (V.Z - V.X) * (W.Y - W.X)); //*-u.x
			//double b = (-(U.Y - U.X) * (W.Z - W.X) + (U.Z - U.X) * (W.Y - W.X)); //*-v.X
			//double c = ( (U.Y - U.X) * (V.Z - V.X) - (U.Z - U.X) * (V.Y - V.X)); //*-w.x
			Vector3 Normal = Normal ();
			//la recta no toca el plano
			if (Normal.X * rect.Lambdian.X + Normal.Y *rect.Lambdian.Y + Normal.Z * rect.Lambdian.Z == 0)
				return false;
			double d = - Normal.X * U.X - Normal.Y * V.X - Normal.Z * W.X;
			double lambda = (-d - Normal.X * rect.A.X - Normal.Y * rect.A.Y - Normal.Z * rect.A.Z) 
				/ (Normal.X * rect.Lambdian.X + Normal.Y * rect.Lambdian.Y + Normal.Z * rect.Lambdian.Z);
			if(!rect.Infinite){
				//el punto que toca el plano no se encuentra en el rango de la recta
				if (lambda < 0 || lambda > 1)
					return false;
			}
			if (!Infinite) {
				//el punto que toca al plano
				Vector3 p = rect.Lambdian * lambda;
				//los triangulos formados por U,V,W y p
				Polygon uvp = new Polygon (U, V, p), 
				vwp = new Polygon (V, W, p), 
				uwp = new Polygon (U, W, p);
				//la suma de las areas de los nuevos triangulos es mayor que la de el triangulo original
				if (uvp.Area () + vwp.Area () + uwp.Area () > this.Area ())
					return false;
			}

			return true;
		}
		
	}
}

