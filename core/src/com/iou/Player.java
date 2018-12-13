package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;


public class Player extends Sprite implements InputProcessor {
    World world;
    Body player_body;
    Sprite player_sprite;
    // speed and jump_force are being used with applyLinearImpulse, so they are both velocities, in m/s.
    float speed = 5;
    float jump_force = 2.5f;
    ArrayList< Integer > allkeysPressed = new ArrayList< Integer >( 3 );
    boolean keypressed = false, jumping = true, onGround = false;
    int left_center_right = 0;// -1 left    0 center   +1 right
    BodyDef bullet_bodyDef;
    ArrayList< Body > bullet_bodies;

    public Player(World w)
    {
        world= w;
        player_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));
    }

    //assuming that the batch has begun and ended before calling this method
    public void draw_me( Batch batch)
    {
        player_sprite.draw( batch );
    }

    public static void print( String s )
    {
        System.out.println( s );
    }

    //"Called when a key was pressed"
    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.SPACE && onGround)
        {
            //print("Trying to jump");
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

        return false;
    }

    public void create_bullet()
    {
        print("CHANGE ME");//TODO: change this
    }

    //"Called when a key was released"
    @Override
    public boolean keyUp(int keycode)
    {
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
		}*/

        print("\tAfter: jumping: "+ jumping +"\tOnGround: "+ onGround);
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
}
