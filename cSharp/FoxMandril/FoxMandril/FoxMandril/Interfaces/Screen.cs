using System;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace Hi
{
	namespace Interfaces
	{
		public interface Screen
		{
			void Update(GameTime gameTime);
			void Draw (SpriteBatch spriteBatch);
		}
	}
}

