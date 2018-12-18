package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.Assignments;
import com.iou.Energy_Drink;
import com.iou.GAME_OBJECT;
import com.iou.IOU;
import com.iou.Pencil;
import com.iou.Player;
import com.iou.SpawnManager;
import com.iou.Wall;
import com.iou.HUD.HUD;

import java.util.ArrayList;

import jdk.nashorn.internal.ir.Assignment;

import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;

public class PlayScreen implements Screen, InputProcessor {
    public final IOU game;
    private Stage stage;
    public final Viewport play_viewport;
    PausePopUp pauseMenu;
    World main_world;
    //Body player_body;
    static Player player;
    OrthographicCamera camera;
    Wall floor, left_wall, right_wall, ceiling;
    public boolean isPaused = false;
    public static float global_timer = 10*60;//representing 10 minutes in seconds

    //BOX2D DEBUGGING
    public final static boolean DEBUG_MODE = true;
    private Box2DDebugRenderer box2DDR;
    private Matrix4 debugMatrix;
    private boolean hitCtrl = false; //if user is hitting CTRL (used for faster scrolling in debug mode)
    public int level = 1;

	private HUD hud;
	
    public Assignments debugAssignment;//TODO: to delete later;

    public SpawnManager Spawner;

    public PlayScreen( IOU the_game)
    {
        game= the_game;
        camera = new OrthographicCamera();
        //this.viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        this.play_viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, camera);
        stage = new Stage(play_viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        //camera.position.set( -5f,0,0 );
        camera.viewportWidth = play_viewport.getScreenWidth()/PIXELS_PER_METER;
        camera.viewportHeight = play_viewport.getScreenHeight()/PIXELS_PER_METER;

        camera.position.set( 0,IOU.HEIGHT/2/PIXELS_PER_METER,0 );//in meters
        //camera.position(new Vector2( 0,0 ));

		//creating the hud
		hud = new HUD(game.batch, this);
		
        // create a world
        main_world = new World( new Vector2( 0, -20f ), true );

        if(player == null)
            player = new Player(main_world, game.batch);


        debugMatrix=new Matrix4(camera.combined);

        //BoxObjectManager.BOX_TO_WORLD = 100f
        //Scale it by 100 as our box physics bodies are scaled down by 100
        //debugMatrix.scale(PIXELS_PER_METER, PIXELS_PER_METER, 1f);
        //NOT THIS//debugMatrix.scale(1,1,1f);

        //DEBUGGING
        this.box2DDR = new Box2DDebugRenderer();

        //Gdx.input.setInputProcessor( player );
        Gdx.input.setInputProcessor( this ); // Required for implementing 'InputProcessor'
        floor       = new Wall(player, Wall.wall_position.DOWN);
        left_wall   = new Wall(player, Wall.wall_position.LEFT);
        right_wall  = new Wall(player, Wall.wall_position.RIGHT);
        ceiling     = new Wall(player, Wall.wall_position.UP);

        //debugAssignment  = new Assignments( main_world );
        debugAssignment  = new Assignments( main_world , -1,100, GAME_OBJECT.generate_starting_Y());

        Spawner = new SpawnManager( level, game.batch, main_world );

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

        //TODO: DELETE LATER
        //camera.position.set( player.getBody_X(), player.getBody_Y(),0 );//this follows the player

        camera.update();

        //recommended default values (1/60, 6,2)
        if (!isPaused)//if NOT paused, then simulate physics (via run step/frame)
		{
			main_world.step(1f/60f, 6, 2);
			//updating the debt owed by the player
            hud.setDebtOwed(HUD.getDebtOwed()+1);
            hud.updateDebtOwed(HUD.getDebtOwed());
		}

        //draws things in the same way as debug and self scales
        //game.batch.setProjectionMatrix( camera.combined );

        if(DEBUG_MODE)
        {
            player.check_out_of_bounds();
            //player.print_position();
            //DEBUGGING
            box2DDR.render( main_world, camera.combined );
            //box2DDR.render( main_world, debugMatrix );
        }

        //include player control movements
        player.movement();

        stage.draw();
        game.batch.begin();
            //floor.draw_me(game.batch);
            //left_wall.draw_me(game.batch);
            //ceiling.draw_me(game.batch);
            //right_wall.draw_me(game.batch);

            player.draw_me(game.batch);
            //player.getSprite().draw( game.batch );

            /*
            if(debugAssignment != null)
            {
                if(!debugAssignment.isTimerDone())
                    debugAssignment.draw_me( game.batch );
                else
                    debugAssignment = null;
            }*/


            player.draw_pencils( game.batch );
            Spawner.draw_spawns( game.batch, main_world );


        game.batch.end();



		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

		hud.stage.draw();

		/*
		* Delete bodies
		* */
        player.DeleteBodies(main_world);
        Spawner.DeleteBodies( main_world );

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

                Object objA = bodyA.getUserData();
                Object objB = bodyB.getUserData();

                //There is only one player so I don't need to add it to anything, besides PlayScreen has a reference
                //  to the only Player object!
                ArrayList<Wall> contactWall = new ArrayList<Wall>();
                ArrayList<Assignments> contactAssignment = new ArrayList<Assignments>();
                ArrayList<Pencil> contactPencil = new ArrayList<Pencil>();
                ArrayList<Energy_Drink> contactE_Dirnk = new ArrayList<Energy_Drink>();

                //ONLY CARE ABOUT WHEN:
                /*
                * Player and Assignment Hit:
                *   - Collect Assignment AS IS, apply grade, and get rid of assignment.
                *
                * Player and Bottom Wall (floor):
                *   - Active jumping (set isGround to true)
                *
                * Player and E-Drink:
                *   - Apply boost, get rid of E-Drink
                *
                *  Assignment and Wall:
                *   - if LEFT wall:
                *       - Collect assignment, apply 'late' grade, and get rid of assignment
                *
                *  Assignment and Pencil:
                *   - if allowed, increase grade of assignment.
                * */

                boolean isPencil =false, isWall = false, isPlayer = false, isAssignment = false, isEDrink = false;
                //for wall, only care about the left wall with assignments

                //print("objA: "+ objA.getClass()+"\tobjB: "+ objB.getClass());

                //OBJECT A
                if(objA instanceof  Pencil)//if(objA instanceof Pencil || objB instanceof Pencil)
                {
                    isPencil = true;
                    contactPencil.add( (Pencil)objA );
                }
                else if(objA instanceof Wall)
                {
                    isWall = true;
                    contactWall.add( (Wall)objA );
                }
                else if(objA instanceof Assignments)
                {
                    isAssignment = true;
                    contactAssignment.add( (Assignments) objA );
                }
                else if(objA instanceof Player)
                {
                    isPlayer = true;
                }
                else if(objA instanceof Energy_Drink)
                {
                    isEDrink = true;
                    contactE_Dirnk.add( (Energy_Drink) objA );
                }

                //OBJECT B
                if(objB instanceof  Pencil)//if(objA instanceof Pencil || objB instanceof Pencil)
                {
                    isPencil = true;
                    contactPencil.add( (Pencil)objB );
                }
                else if(objB instanceof Wall)
                {
                    isWall = true;
                    contactWall.add( (Wall)objB );
                }
                else if(objB instanceof Assignments)
                {
                    isAssignment = true;
                    contactAssignment.add( (Assignments) objB );
                }
                else if(objB instanceof Player)
                {
                    isPlayer = true;
                }
                else if(objB instanceof Energy_Drink)
                {
                    isEDrink = true;
                    contactE_Dirnk.add( (Energy_Drink) objB );
                }

                ////////////////////
                // CONTACT CHECKS //
                ////////////////////
                if(isPlayer)//if objA or objB is the player
                {
                    if(isAssignment)//if objA or objB is an Assignments object
                    {
                        player.total_grade+= contactAssignment.get(0).getGrade();
                        contactAssignment.get(0).player_hit();//decrement assignment's grade, if allowed
                        player.assignment_collected();
                    }
                    else if(isWall)//if objA or objB is an Assignments object
                    {
                        if(contactWall.get( 0 ).curr_wall_position == Wall.wall_position.DOWN)
                        {
                            player.jumping= false;
                            player.onGround = true;
                        }
                    }
                    else if(isEDrink)//if objA or objB is an Energy_Drink object
                    {

                    }
                }
                else if(isAssignment)//if objA or objB is an Assignments object
                {
                    if(isWall)//if objA or objB is a Wall object
                    {
                        //if the wall is in the LEFT position
                        //Assume that there will be at least one Wall object in the array list.
                        if(contactWall.get(0).curr_wall_position == Wall.wall_position.LEFT)
                        {
                            //TODO: Take some points off for 'lateness' and apply grading
                            //      Get rid of Assignment
                            contactAssignment.get(0).wall_hit();
                        }
                    }
                    else if(isPencil)//if objA or objB is a Pencil object
                    {
                        contactAssignment.get(0).pencil_hit();//increment assignment's grade, if allowed
                        //contactPencil.get(0).set_to_DIE();

                        //contactPencil.get(0).destroy();//get rid of pencil
                    }
                }



                /*
				if(bodyA != null && bodyB != null)
					print("\tObject A user data: "+ bodyA.toString()+"\tObject B user data: "+ bodyB.toString());
				else
					print(bodyA +"\t"+bodyB);
				*/
                Body player_body = player.getBody();
                Body floor_body = floor.getBody();

                //print("is bodyA the player or ground?: "+ (bodyA==player_body)+"\t"+ (bodyA== floor_body));
                //print("is bodyB the player or ground?: "+ (bodyB==player_body)+"\t"+ (bodyB== floor_body));


                if((bodyA == player_body && bodyB == floor_body) || (bodyB == player_body && bodyA == floor_body))
                {
                    //print("\t\tColliding with ground.");
                    //player.jumping= false;
                    //player.onGround = true;
                }

                //print("jumping: "+ jumping +"\tOnGround: "+ onGround);
                //print("\tx: "+ player_body.getPosition().x+"\ty: "+ player_body.getPosition().y);
                //print("\n");
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

                //print("Collision ended");
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

        if(DEBUG_MODE)
        {
            float cam_speed = 0.1f;

            switch (keycode){
                case Input.Keys.NUMPAD_8:{
                    camera.position.y += cam_speed;
                    break;
                }
                //to get to gameOverScreen
                case Input.Keys.Q:{
                    game.setScreen(new GameOverScreen(game, player));
                    System.out.println(player.total_grade);
                    break;
                }
                case Input.Keys.NUMPAD_2:{
                    camera.position.y -= cam_speed;
                    break;
                }
                case Input.Keys.NUMPAD_4:{
                    camera.position.x -= cam_speed;
                    break;
                }
                case Input.Keys.NUMPAD_6:{
                    camera.position.x += cam_speed;
                    break;
                }
                case Input.Keys.NUMPAD_5:{//reset camera's original position
                    camera.position.set( 0,IOU.HEIGHT/2/PIXELS_PER_METER,0 );//in meters
                    break;
                }
                case Input.Keys.NUMPAD_9:{//position at player's body's origin
                    camera.position.set( player.getBody_X(),player.getBody_Y(),0 );//in meters
                    break;
                }
                case Input.Keys.CONTROL_LEFT:{
                    hitCtrl = true;
                    break;
                }
                case Input.Keys.CONTROL_RIGHT:{
                    camera.viewportWidth = 1300;
                    camera.viewportHeight = 1300;
                    break;
                }

            }


        }



        //("the keycode: "+ keycode);
        if(keycode == Input.Keys.ESCAPE)
        {
            //print("Pressing ESC");
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

        if(DEBUG_MODE)
        {
            if(keycode == Input.Keys.CONTROL_LEFT)
            {
                hitCtrl = false;
            }
        }


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
        // to be used with debug renderer
        if(DEBUG_MODE)
        {
            if(hitCtrl)
                amount*=10;

            camera.viewportWidth += amount;
            camera.viewportHeight += amount;

            /*
            print("[Camera ViewPort & Position]");
            print("\tWidth : " + camera.viewportWidth);
            print("\tHeight: "+ camera.viewportHeight);
            print("\tX: "+ camera.position.x + "\tY: "+ camera.position.y);
            print("==============");
            */

        }

        return false;
    }

    @Override
    public void resize(int width, int height)
    {
        //camera.viewportWidth = width/ PIXELS_PER_METER;
        //camera.viewportHeight = height/PIXELS_PER_METER;
    }

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
