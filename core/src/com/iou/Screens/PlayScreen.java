package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;
import com.iou.Player;

public class PlayScreen implements Screen, InputProcessor {
    final IOU game;
    private Stage stage;
    private final Viewport viewport;
    World main_world;
    Body player_body;
    static Player player;
    OrthographicCamera camera;

    public PlayScreen( IOU the_game)
    {
        game= the_game;
        camera = new OrthographicCamera();
        this.viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        // create a world
        main_world = new World( new Vector2( 0, -20f ), true );

        if(player == null)
            player = new Player(main_world);

        Gdx.input.setInputProcessor( this );  // Required for implementing 'InputProcessor'
        print("Inside playscreen");
    }

    public static void print( String s )
    {
        System.out.println( s );
    }


    @Override
    public void render(float value)
    {
        Gdx.gl.glClearColor(255,255,255,1);//white
        //Gdx.gl.glClearColor(.5f,.5f,.5f,0.1f);//black
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        main_world.step(1f/60f, 6, 2);

        stage.draw();
        game.batch.begin();
            player.draw_me( game.batch );
            //TODO: JUST FOR TESTING. DELETE LATER
        game.batch.end();


    }

    @Override
    public void show()
    {}

    //"Called when a key was pressed"
    @Override
    public boolean keyDown(int keycode) {

        print("the keycode: "+ keycode);
        if(keycode == Input.Keys.ESCAPE)
        {
            pause();
        }

        //TODO: to test changing back to other screen. delete later.
        if(keycode == Input.Keys.NUM_0 || keycode == Input.Keys.NUMPAD_0)
        {
            print("pressing 0");
            game.setScreen(new StartScreen(game));
            dispose();
        }


        /*
        if(keycode == Input.Keys.SPACE && onGround)
        {
            print("Trying to jump");
            //player_body.setLinearVelocity(0f,80000f);
            //player_body.setLinearVelocity(speed*left_center_right,jump_force);
            player_body.applyLinearImpulse( speed*left_center_right, jump_force,
                    player_body.getPosition().x, player_body.getPosition().y, true);
            jumping= true;
            onGround = false;

            //player_body.applyLinearImpulse( new Vector2( 0,2000 ), player_body.getPosition(),true );
            //player_body.applyForce( new Vector2( 0,5000 ),player_body.getWorldCenter(), false );
            //player_body.applyTorque( 50,false );  //messing with values

            if(allkeysPressed.isEmpty())
                keypressed = false;
            else
                keypressed= true;
            return false;
        }


        if(!allkeysPressed.contains( keycode ) && allkeysPressed.size()+1 <= 3)
        {
            allkeysPressed.add( keycode );
            keypressed = true;
        }

        if(keycode == Input.Keys.ENTER)
        {
            create_bullet();
        }
        */
        return false;
    }


    //"Called when a key was released"
    @Override
    public boolean keyUp(int keycode)
    {
        /*
        print("jumping: "+ jumping +"\tOnGround: "+ onGround);
        int index = allkeysPressed.indexOf( keycode );
        if(index != -1)
            allkeysPressed.remove( index );

        left_center_right= 0;

        if(allkeysPressed.size() == 0)
            keypressed = false;

	    /*
	    if(keycode == Input.Keys.SPACE && onGround)
		{
			jumping= true;
			onGround = false;
		}* /

        print("\tAfter: jumping: "+ jumping +"\tOnGround: "+ onGround);*/
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

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

}