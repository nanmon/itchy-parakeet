using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace HooHaaUtils
{
	public class Animations <T>
	{

		public class AnimationStrip
		{
			private Animations<T> father;
			private Texture2D texture;
			private int width, currentFrame = 0, nextStop = -1;
			private int[] stops = null;
			private float timer = 0;

			public bool Repeat {
				get;
				set;
			}

			public T NextAnimation{
				get; set;
			}

			public float Velocity {
				get;
				set;
			}

			public float StartSpeed{
				get; set;
			}

			public float MaxSpeed {
				get;
				set;
			}

			public float Aceleration {
				get;
				set;
			}

			public bool Flipped{
				get; set;
			}

			public int[] Stops {
				get{ return stops; }
				set{
					if (value != null)
						nextStop = 0;
					else
						nextStop = -1;
					stops = value;
				}
			}

			internal AnimationStrip(Animations<T> father, Texture2D texture, 
			                        int width, float velocity){
				this.father = father;
				this.texture = texture;
				this.width = width;
				Velocity = StartSpeed = velocity;
				Aceleration = 0;
				MaxSpeed = 1f/60;
				Repeat = true;
				Flipped = false;
			}

			

			public void Reset(){ 
				currentFrame = 0; 
				timer = 0;
				Velocity = StartSpeed;
			}

			public void Procede(){
				if (stops == null)
					return;
				if (++nextStop >= stops.Length)
					nextStop = 0;
			}

			internal void Update(GameTime gameTime){
				timer += (float)gameTime.ElapsedGameTime.TotalSeconds;
				if(stops != null && currentFrame == stops[nextStop]){
					return;
				}
				if (Velocity > MaxSpeed)
					Velocity -= Aceleration * (float)gameTime.ElapsedGameTime.TotalSeconds;
				else
					Velocity = MaxSpeed;
				if(timer >= Velocity){
					timer -= Velocity;
					++currentFrame;
					if(currentFrame == texture.Width/width){
						if (Repeat)
							currentFrame = 0;
						else if (NextAnimation != null) 
							father.CurrentKey = NextAnimation;
					}
				}
			}

			internal void Draw(SpriteBatch spriteBatch){
				spriteBatch.Draw (
					texture, 
					father.Position, 
					new Rectangle (width * currentFrame, 0, width, texture.Height),
					Color.White,
					0,
					Vector2.Zero,
					1,
					Flipped ? SpriteEffects.FlipHorizontally : SpriteEffects.None,
					0);
			}

		}

		private Dictionary<T, AnimationStrip> dict = new Dictionary<T, AnimationStrip> ();
		private T currentKey;
		private Vector2 position;

		public T CurrentKey{
			get{ return currentKey; }
			set{
				currentKey = value;
				if (!dict.ContainsKey (value))
					throw new NullEnumException ("No associated animation to enum " + value.ToString ());
				dict [value].Reset ();
			}
		}

		public AnimationStrip CurrentAnimation{
			get{ return dict [currentKey]; }
		}

		public Vector2 Position{
			get{ return position; }
			set{ position = value; }
		}

		public AnimationStrip this[T key]{
			get { return dict[key]; }
		}

		public Animations (){}

		public void Add(T key, Texture2D texture, int width, float velocity = 0.2f){
			AnimationStrip animation = new AnimationStrip (this, texture, width, velocity);
			dict.Add (key, animation);
		}

		public AnimationStrip Remove(T key){
			AnimationStrip r = dict [key];
			dict.Remove (key);
			return r;
		}

		public void Update(GameTime gameTime){
			dict [currentKey].Update (gameTime);
		}

		public void Draw(SpriteBatch spriteBatch){
			dict [currentKey].Draw (spriteBatch);
		}

	}


	[Serializable]
	public class NullEnumException : Exception
	{
		/// <summary>
		/// Initializes a new instance of the <see cref="T:NullEnumException"/> class
		/// </summary>
		public NullEnumException ()
		{
		}

		/// <summary>
		/// Initializes a new instance of the <see cref="T:NullEnumException"/> class
		/// </summary>
		/// <param name="message">A <see cref="T:System.String"/> that describes the exception. </param>
		public NullEnumException (string message) : base (message)
		{
		}

		/// <summary>
		/// Initializes a new instance of the <see cref="T:NullEnumException"/> class
		/// </summary>
		/// <param name="message">A <see cref="T:System.String"/> that describes the exception. </param>
		/// <param name="inner">The exception that is the cause of the current exception. </param>
		public NullEnumException (string message, Exception inner) : base (message, inner)
		{
		}

		/// <summary>
		/// Initializes a new instance of the <see cref="T:NullEnumException"/> class
		/// </summary>
		/// <param name="context">The contextual information about the source or destination.</param>
		/// <param name="info">The object that holds the serialized object data.</param>
		protected NullEnumException (System.Runtime.Serialization.SerializationInfo info, System.Runtime.Serialization.StreamingContext context) : base (info, context)
		{
		}
	}
}

