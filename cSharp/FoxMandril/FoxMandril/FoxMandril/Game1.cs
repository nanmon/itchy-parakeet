using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using HooHaaUtils;
using TileEngine;

namespace Hi
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
		#region Declarations
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
		Screen screen;
        Player player;
        SpriteFont pericles8;
		float sec = 0.0f;
		Boolean entro = true; 
        Vector2 scorePosition = new Vector2(20, 10);
        enum GameState {Playing, PlayerDead, GameOver, Paused };
        GameState gameState;
        Vector2 gameOverPosition = new Vector2(350, 300);
        Vector2 livesPosition = new Vector2(20, 30);
        Vector2 inyeccionPosition = new Vector2(20, 20);

		Vector2 framesPosition = new Vector2 (750, 10);
		Vector2 pausePosition = new Vector2 (370, 140);
		Rectangle screenRect;
        int helpIndex = 0;
        
        float deathTimer = 0.0f;
        float deathDelay = 2.0f;
		KeyboardState lastState;
		public static bool OnDrugs = false;
		#endregion

		#region Constructor
        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            //Content.RootDirectory = "Content";
            Content.RootDirectory = "HiContent";
        }
		#endregion


		#region Overrided Methods
        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here
            this.graphics.PreferredBackBufferWidth = 800;
            this.graphics.PreferredBackBufferHeight = 480;
            this.graphics.ApplyChanges();
			screenRect = new Rectangle (0, 0, graphics.PreferredBackBufferWidth, graphics.PreferredBackBufferHeight);
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
			try{
				Assets.normal = Content.Load<Song> (@"sounds/normal.wav");
				Assets.high = Content.Load<Song> (@"sounds/High.wav");
				Assets.gettingHi = Content.Load<Song> (@"sounds/gettinHi.wav");
				Assets.gettingNormal = Content.Load<Song> (@"sounds/gettingBack.wav");
				Assets.title = Content.Load<Song> (@"sounds/titlescreen.wav");

				Assets.rip = Content.Load<Song> (@"sounds/dead.wav");
			}catch{
				Assets.normal = Content.Load<Song> (@"sounds/normal");
				Assets.high = Content.Load<Song> (@"sounds/High");
				Assets.gettingHi = Content.Load<Song> (@"sounds/gettinHi");
				Assets.gettingNormal = Content.Load<Song> (@"sounds/gettingBack");
				Assets.title = Content.Load<Song> (@"sounds/titlescreen");
				Assets.rip = Content.Load<Song> (@"sounds/dead");
			}


            spriteBatch = new SpriteBatch(GraphicsDevice);
            TileMap.Initialize(Content.Load<Texture2D>(@"Textures\PlatformTiles"), 800, 480);
			try{
	            pericles8 = Content.Load<SpriteFont>(@"Fonts\Pericles8");
			}catch{
				Assets.myFont = new BitFont (Content);
			}
			TileMap.spriteFont = Assets.myFont;
            Assets.titleScreen = Content.Load<Texture2D>(@"Textures\TitleScreen");
            Camera.WorldRectangle = new Rectangle(0, 0, TileMap.MapWidth * TileMap.TileSize, TileMap.MapHeight *
            TileMap.TileSize);
            Camera.Position = Vector2.Zero;
            Camera.ViewPortWidth = 800;
            Camera.ViewPortHeight = 480;
            player = new Player(Content);
            LevelManager.Initialize(Content, player);

			screen = new TitleScreen ();
            // TODO: use this.Content to load your game content here*/
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {

            // Allows the game to exit
            KeyboardState keyState = Keyboard.GetState();
            GamePadState gamepadState = GamePad.GetState(PlayerIndex.One);

            float elapsed = (float)gameTime.ElapsedGameTime.TotalSeconds;

			switch(gameState){
			case GameState.TitleScreen:
				if (entro) {
					MediaPlayer.Stop ();
					MediaPlayer.Play (title);
					entro = false;
				}
				if (keyState.IsKeyDown (Keys.Enter) || gamepadState.Buttons.A == ButtonState.Pressed) {
					switch (menuOptions) {
						case MenuOptions.Play:
							StartNewGame ();
							gameState = GameState.Playing;
							MediaPlayer.Play (normal);
							break;
						case MenuOptions.Instructions:
							gameState = GameState.Help;
							helpIndex = 0;
							break;
						case MenuOptions.Exit:
							Exit ();
							break;
					}
				} else if (keyState.IsKeyDown (Keys.Down) && lastState.IsKeyUp(Keys.Down) || 
				           keyState.IsKeyDown (Keys.S) && lastState.IsKeyUp(Keys.S)) {
					if (menuOptions != MenuOptions.Exit)
						++menuOptions;
				}	else if(keyState.IsKeyDown (Keys.Up) && lastState.IsKeyUp(Keys.Up)
				         || keyState.IsKeyDown (Keys.W) && lastState.IsKeyUp(Keys.W))
					if(menuOptions != MenuOptions.Play) --menuOptions;
				break;
			case GameState.Help:
				if (keyState.IsKeyDown(Keys.D) && lastState.IsKeyUp(Keys.D) ||
				    keyState.IsKeyDown(Keys.Right) && lastState.IsKeyUp(Keys.Right) )
				{
					helpIndex++;
					if (helpIndex == 3) gameState = GameState.TitleScreen;

				}
				else if (keyState.IsKeyDown(Keys.A) && lastState.IsKeyUp(Keys.A) ||
				    keyState.IsKeyDown(Keys.Left) && lastState.IsKeyUp(Keys.Left) )
				{
					helpIndex--;
					if (helpIndex == -1) gameState = GameState.TitleScreen;

				}
				else if (keyState.IsKeyDown(Keys.Escape) && lastState.IsKeyUp(Keys.Escape)){
					gameState = GameState.TitleScreen;
				}
				break;
			case GameState.Playing:
				player.Update (gameTime);
				LevelManager.Update (gameTime);
				sec += (float)gameTime.ElapsedGameTime.TotalSeconds;
				if (player.Dead) {
					MediaPlayer.Pause ();
					MediaPlayer.Play (rip);
					MediaPlayer.IsRepeating = false;
					if (player.LivesRemaining > 0) {
						gameState = GameState.PlayerDead;
						deathTimer = 0.0f;
					} else {
						gameState = GameState.GameOver;
						deathTimer = 0.0f;
					}
				} if (OnDrugs) {
					if (!entro && sec > 4.2f) {
						MediaPlayer.Pause ();
						MediaPlayer.Play (high);
						MediaPlayer.IsRepeating = true;
						entro = true;
					}
					if (!TileMap.OnDrugs) {
						MediaPlayer.Pause ();
						MediaPlayer.Play (gettingHi);
						sec = 0.0f;
						entro = false;
						MediaPlayer.IsRepeating = false;
						TileMap.OnDrugs = true;
						if(!TileMap.isRectanglePassable(player.CollisionRectangle)){
							player.Kill ();
						}
					}
				} else{
					if (!entro && sec > 2.0f) {
						MediaPlayer.Pause ();
						MediaPlayer.Play (normal);
						MediaPlayer.IsRepeating = true;
						entro = true;
					}
					if (TileMap.OnDrugs) {
						MediaPlayer.Pause ();
						MediaPlayer.Play (gettingNormal);
						sec = 0.0f;
						entro = false;
						MediaPlayer.IsRepeating = false;
						TileMap.OnDrugs = false;
						if(!TileMap.isRectanglePassable(player.CollisionRectangle)){
							player.GoSafe ();
						}
					}
				}

				if (keyState.IsKeyDown(Keys.Escape) && lastState.IsKeyUp(Keys.Escape))
				{
					MediaPlayer.Volume = 0.3f;
					gameState = GameState.Paused;
				}
				break;
			case GameState.Paused:
				sec += (float)gameTime.ElapsedGameTime.TotalSeconds;
				if (OnDrugs && (!entro && sec > 4.2f)) {
					MediaPlayer.Pause ();
					MediaPlayer.Play (high);
					MediaPlayer.IsRepeating = true;
					entro = true;
				}else if (!OnDrugs && (!entro && sec > 2.0f)) {
					MediaPlayer.Pause ();
					MediaPlayer.Play (normal);
					MediaPlayer.IsRepeating = true;
					entro = true;
				}
				if (keyState.IsKeyDown(Keys.Escape) && lastState.IsKeyUp(Keys.Escape))
				{
					MediaPlayer.Volume = 1.0f;
					gameState = GameState.Playing;
				}
				break;
			case GameState.PlayerDead:
				player.Update(gameTime);
				LevelManager.Update(gameTime);

				deathTimer += elapsed;
				if (deathTimer > deathDelay){
					player.WorldLocation = Vector2.Zero;
					LevelManager.ReloadLevel();
					player.Revive();
					entro = false;
					gameState = GameState.Playing;
				}
				break;
			case GameState.GameOver:
				player.Update(gameTime);
				deathTimer += elapsed;
				if (deathTimer > deathDelay){
					gameState = GameState.TitleScreen;
					player.WorldLocation = Vector2.Zero;
				}
				break;
			}
			lastState = keyState;


			screen.Update (gameTime);
            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.Black);
            spriteBatch.Begin(
	            SpriteSortMode.Immediate,
	            BlendState.AlphaBlend);

			switch(gameState){
			case GameState.TitleScreen:
				//spritebatch.Draw(titleScreen);
				myFont.DrawText (spriteBatch, menuPositions [0], "Jugar");
				myFont.DrawText (spriteBatch, menuPositions [1], "Instrucciones");
				myFont.DrawText (spriteBatch, menuPositions [2], "Salir");
				myFont.DrawText (spriteBatch, menuPositions [(int)menuOptions] - new Vector2 (10, 0), ">");
				break;
			case GameState.Help:
				switch(helpIndex){
					case 0:
						spriteBatch.Draw(Content.Load<Texture2D>(@"Textures\HelpMenus\HelpMenu1"), Vector2.Zero, Color.White);
						break;
					case 1:
						spriteBatch.Draw(Content.Load<Texture2D>(@"Textures\HelpMenus\HelpMenu2"), Vector2.Zero, Color.White);
						break;
					case 2:
						spriteBatch.Draw(Content.Load<Texture2D>(@"Textures\HelpMenus\HelpMenu3"), Vector2.Zero, Color.White);
						break;
				}
				break;
			case GameState.Playing:
			case GameState.Paused:
			case GameState.PlayerDead:
			case GameState.GameOver:
				TileMap.Begin(spriteBatch);
				player.Draw(spriteBatch);
				LevelManager.Draw(spriteBatch);
				TileMap.End (spriteBatch);
				myFont.DrawText (spriteBatch, scorePosition, "Drogas: " + player.drugCount.ToString ());
				myFont.DrawText (spriteBatch, inyeccionPosition, "Inyecciones: " + player.inyecciones.ToString ());
				myFont.DrawText (spriteBatch, livesPosition, "Vidas: " + player.LivesRemaining.ToString ());
				myFont.DrawText (spriteBatch, framesPosition, (1/(float)(gameTime.ElapsedGameTime.TotalSeconds)).ToString ());
				if (gameState == GameState.GameOver)
					myFont.DrawText (spriteBatch, gameOverPosition, "G A M E O V E R !");
				else if (gameState == GameState.Paused) {
					spriteBatch.Draw(
						Content.Load<Texture2D>(@"Textures\greenTexture"), 
					    Camera.ViewPort,
						Color.White);
					myFont.DrawText (spriteBatch, pausePosition, "PAUSA");
				}
				break;
			}
            
			screen.Draw (spriteBatch);
            spriteBatch.End();
            base.Draw(gameTime);
        }
		#endregion

		public void setScreen(Screen screen){
			this.screen = screen;
		}

        #region Helper Methods
        private void StartNewGame()
        {
            player.Revive();
            player.LivesRemaining = 3;
            player.WorldLocation = Vector2.Zero;
            LevelManager.LoadLevel(0);
        }
        #endregion


    }
}
