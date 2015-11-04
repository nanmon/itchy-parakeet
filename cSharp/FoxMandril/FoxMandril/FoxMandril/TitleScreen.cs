using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using HooHaaUtils;

namespace Hi
{
	public class TitleScreen : Screen
	{
		bool helpMenu = false;
		enum MenuOptions { Play = 0, Instructions, Exit };
		MenuOptions menuOptions = MenuOptions.Play;
		Vector2[] menuPositions = new Vector2[] {
			new Vector2(350, 300),
			new Vector2(350, 320),
			new Vector2(350, 340)
		};

		public TitleScreen ()
		{
		}

		public override void Update (GameTime gameTime)
		{
			throw new NotImplementedException ();
		}

		public override void Draw (SpriteBatch spriteBatch)
		{
			throw new NotImplementedException ();
		}

	}
}

