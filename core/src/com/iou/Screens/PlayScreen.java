package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;
import com.iou.Pencil;
import com.iou.Player;
import com.iou.Wall;

import static com.iou.IOU.print;

public class PlayScreen implements Screen, InputProcessor {
    public final IOU game;
    private Stage stage;
    private final Viewport viewport;
    PausePopUp pauseMenu;
    World main_world;
    //Body player_body;
    static Player player;
    OrthographicCamera camera;
    Wall floor, left_wall, right_wall, ceiling;
    public boolean isPaused = false;

    public PlayScreen( IOU the_game)
    {
        print("In the Playscreen");
        game= the_game;
        camera = new OrthographicCamera();
        this.viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // create a world
        main_world = new World( new Vector2( 0, -20f ), true );

        if(player == null)
            player = new Player(main_world, game.batch);

        //Gdx.input.setInputProcessor( player );
        Gdx.input.setInputProcessor( this ); // Required for implementing 'InputProcessor'
        floor       = new Wall(player, Wall.wall_position.DOWN);
        left_wall   = new Wall(player, Wall.wall_position.LEFT);
        right_wall  = new Wall(player, Wall.wall_position.RIGHT);
        ceiling     = new Wall(player, Wall.wall_position.UP);

        //print("screen width: "+ Gdx.graphics.getWidth()+"\theight: "+ Gdx.graphics.getHeight());
        CreateContactListener();
        IOU.set_PlayScreen( this );
    }

    @Override
    public void render(float value)
    {
        Gdx.gl.glClearColor(255,255,255,1);//white
        //Gdx.gl.glClearColor(.5f,.5f,.5f,0.1f);//black
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        //recommended default values (1/60, 6,2)
        if (!isPaused)//if NOT paused, then simulate physics (via run step/frame)
            main_world.step(1f/60f, 6, 2);

        stage.draw();
        game.batch.begin();

            floor.draw_me(game.batch);
            ceiling.draw_me(game.batch);
            left_wall.draw_me(game.batch);
            right_wall.draw_me(game.batch);

            player.draw_me(game.batch);
            //player.getSprite().draw( game.batch );

            for(Pencil pencil: player.Player_Pencils)
            {
                pencil.draw_me(game.batch);
                //pencil.get_Pencil_sprite().draw( game.batch );
            }
        game.batch.end();
    }

    /*
        this gets called when the game focuses on the play Screen (once per game refocus)
        So when the game goes from start screen to play screen
        typically this would be solved at the start of the game, however, after redirecting to from
        start -> play -> pause -> start -> play
        I was not able to press a button when in the play screen after the above steps
    */
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor( this );//refocus the game's input on this screen
        isPaused = false;// play the game
    }

    // to be called only in the constructor
    void CreateContactListener()
    {
        main_world.setContactListener( new ContactListener() {
            @Override
            public void beginContact( Contact contact )
            {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                //print("beginContact: between " + fixtureA.toString() + " and " + fixtureB.toString());

                Body bodyA = fixtureA.getBody();//.getUserData();
                Body bodyB = fixtureB.getBody();//.getUserData();

                /*
				if(bodyA != null && bodyB != null)
					print("\tObject A user data: "+ bodyA.toString()+"\tObject B user data: "+ bodyB.toString());
				else
					print(bodyA +"\t"+bodyB);
				*/
                Body player_body = player.getBody();
                Body floor_body = floor.getBody();

                print("is bodyA the player or ground?: "+ (bodyA==player_body)+"\t"+ (bodyA== floor_body));
                print("is bodyB the player or ground?: "+ (bodyB==player_body)+"\t"+ (bodyB== floor_body));

                if(bodyB.getUserData().getClass() == Wall.class)
                {
                    print("\tIt's a: "+ ((Wall)bodyB.getUserData()).curr_wall_position);
                }


                if((bodyA == player_body && bodyB == floor_body) || (bodyB == player_body && bodyA == floor_body))
                {
                    print("\t\tColliding with ground.");
                    player.jumping= false;
                    player.onGround = true;
                }

                //print("jumping: "+ jumping +"\tOnGround: "+ onGround);
                //print("\tx: "+ player_body.getPosition().x+"\ty: "+ player_body.getPosition().y);
                print("\n");
            }

            @Override
            public void endContact( Contact contact )
            {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Body bodyA = fixtureA.getBody();//.getUserData();
                Body bodyB = fixtureB.getBody();//.getUserData();

                Body player_body = player.getBody();
                Body floor_body = floor.getBody();

                if((bodyA == player_body && bodyB == floor_body) || (bodyB == player_body && bodyA == floor_body))
                {
                    player.jumping= true;
                }
            }

            @Override
            public void preSolve( Contact contact, Manifold oldManifold )
            {

            }

            @Override
            public void postSolve( Contact contact, ContactImpulse impulse )
            {

            }
        });
    }


    //"Called when a key was pressed"
    @Override
    public boolean keyDown(int keycode) {

        print("the keycode: "+ keycode);
        if(keycode == Input.Keys.ESCAPE)
        {
            print("Pressing ESC");
            //pauseMenu = new PausePopUp(stage,game, this);
            pause();
        }

        //TODO: to test changing back to other screen. delete later.
        if(keycode == Input.Keys.NUM_0 || keycode == Input.Keys.NUMPAD_0)
        {
            print("pressing 0");
            //game.setScreen(new StartScreen(game));
            //game.setScreen( IOU.main_startScreen );
            game.setScreen( IOU.get_StartScreen( game ) );
            dispose();
        }


        player.keyDown( keycode, isPaused );//needed for player's input

        return false;
    }


    //"Called when a key was released"
    @Override
    public boolean keyUp(int keycode)
    {
        //player.keyDown( keycode );//needed for player's input
        player.keyUp( keycode, isPaused );
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning".
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void resize(int width, int height){}

    //pauses when the user presses 'ESC' OR when the user clicks outside of the window.
    @Override
    public void pause() {
        isPaused = true;
        //gets rid of duplicate dialogs boxes, which could occur when pausing and clicking outside the window
        if(pauseMenu != null)
        {
            pauseMenu.RemoveDialog();
            pauseMenu = null;
        }

        pauseMenu = new PausePopUp(stage,game, this);
    }

    //resumes when the user click 'Resume' in dialog box OR when user clicks back inside the window
    @Override
    public void resume() {
        isPaused = false;
        //print("is popup up?: "+ (pauseMenu!=null));
        if(pauseMenu != null)
        {
            pauseMenu.RemoveDialog();
            pauseMenu = null;
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
    }

}
