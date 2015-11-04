#region Using Statements
using System;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Storage;
using Microsoft.Xna.Framework.Input;

#endregion

namespace HooHaaUtils
{
	public abstract class Screen
	{

		abstract public void Update (GameTime gameTime);
		abstract public void Draw (SpriteBatch spriteBatch);
	}

}

