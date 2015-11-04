using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace HooHaaUtils
{
	public abstract class GameObject
	{

		protected Vector2 position;
		protected Rectangle hitbox;

		public GameObject ()
		{

		}

		public abstract void Update(GameTime gameTime);
		public abstract void Draw (SpriteBatch spriteBatch);

	}
}

