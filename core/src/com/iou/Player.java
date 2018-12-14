package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;

public class Player {//implements InputProcessor {
    World main_world;
    Body player_body;
    final Sprite player_sprite;//, bullet_sprite;
    // speed and jump_force are being used with applyLinearImpulse, so they are both velocities, in m/s.
    float speed = 5;
    float jump_force = 25f;//2.5
    final float ORGINAL_Y = 50F, ORGINAL_X = 50F;
    ArrayList< Integer > allkeysPressed = new ArrayList< Integer >( 3 );
    boolean keypressed = false;
    public boolean jumping =true,onGround = false;
    int left_center_right = 0;// -1 left    0 center   +1 right
    //static Batch batch;

    Sprite debug;


    //FOR THE PENCILS being thrown
    BodyDef bullet_bodyDef;
    public ArrayList< Pencil > Player_Pencils;

    public Player(World world, Batch the_batch)
    {
        main_world= world;
        player_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));


        //bullet_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));
        Player_Pencils = new ArrayList<>();

        //if(batch == null)
          //  batch = the_batch;

        debug = new Sprite(player_sprite);
        debug.setColor( Color.CYAN );
        setup();
    }

    //setup the pencil's box2D properties
    public void setup()
    {
        player_sprite.setSize( 100,200 );
        player_sprite.setColor( Color.BLUE );

        print("Are the world's the same?: "+ main_world + "\t"+getWorld());

        //create a body definition for the player
        //it needs to be dynamic, because we expect the player to be moving
        BodyDef player_bodyDef = new BodyDef();
        player_bodyDef.type = BodyDef.BodyType.DynamicBody;

        player_bodyDef.position.set( ( player_sprite.getX() + player_sprite.getWidth() / 2 ) / PIXELS_PER_METER,
                ( player_sprite.getY() + player_sprite.getHeight() / 2 ) / PIXELS_PER_METER );
        //print( "player pos: x: " + player_sprite.getX() + "\ty: " + player_sprite.getY() );

        player_body = main_world.createBody( player_bodyDef );

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();

        // JC
        // shape.setAsBox( player_sprite.getWidth()/PIXELS_PER_METER/2,player_sprite.getHeight()/PIXELS_PER_METER/2 );
        shape.setAsBox( player_sprite.getWidth() / 2 / PIXELS_PER_METER,
                player_sprite.getHeight() / 2 / PIXELS_PER_METER );

        debug.setSize( player_sprite.getWidth() / 2 / PIXELS_PER_METER,
                player_sprite.getHeight() / 2 / PIXELS_PER_METER );



        //specifying the density, restitution(bouncyness), others. for the player
        FixtureDef player_fixtureDef = new FixtureDef();
        player_fixtureDef.shape = shape;
        player_fixtureDef.density = 0.1f;// JC
        player_fixtureDef.friction = 1f;//.5f
        player_fixtureDef.restitution = 0f;//.1

        player_body.setUserData( this );//easier for collision detection
        player_body.createFixture( player_fixtureDef );
        relocate();

        shape.dispose();
    }

    //assuming that the batch has begun and ended before calling this method
    public void draw_me( Batch batch)
    {
        debug.setPosition( player_body.getPosition().x , player_body.getPosition().y);
        player_sprite.setPosition( player_body.getPosition().x, player_body.getPosition().y );
        check_out_of_bounds();

        player_sprite.draw( batch );
        debug.draw( batch );
        //print("debug  pos: "+ debug.getWidth()+"\t" +debug.getHeight());
        //print("player pos: "+ player_sprite.getWidth()+"\t" +player_sprite.getHeight()+"\n");
    }

    //just in case something goes bonkers...
    public void check_out_of_bounds()
    {
        float body_x = player_body.getPosition().x;
        float body_y = player_body.getPosition().y;

        if(body_y <0 || body_y > IOU.HEIGHT || body_x<0 || body_x >IOU.WIDTH)
        {
            //resets the player's position and speed
            //player_body.setTransform( IOU.WIDTH- player_sprite.getWidth(),
                    //IOU.HEIGHT - player_sprite.getHeight(),0 );
            relocate();
        }

    }

    void relocate()
    {
        //resets the player's position and speed
        player_body.setTransform( ORGINAL_X, ORGINAL_Y,0 );
        player_body.setLinearVelocity( 0,0 );


    }


    public Sprite getSprite()
    {return player_sprite;}

    public World getWorld()
    {return this.main_world;}

    public Body getBody()
    {return player_body;}

    public void removePencil(Pencil pen)
    {
        if(Player_Pencils.contains( pen ))
        {
            Player_Pencils.remove( pen );
            main_world.destroyBody( pen.getBody() );

        }
    }

    //"Called when a key was pressed"
    //@Override
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

            //TODO: DELETE LATER. TESTING SPRITE SETTINGS
            Sprite dummy = Player_Pencils.get( 0 ).bullet_sprite;
            //print("Color of Orange: "+ Color.ORANGE);
            //print("bullet height: "+ dummy.getHeight() +"\twidth: "+ dummy.getWidth()+"\tColor: "+ dummy.getColor());
        }
        return false;
    }

    public void create_bullet()
    {
        /*bullet_bodyDef.position.set( ( player_sprite.getX() + player_sprite.getWidth()+50  ) ,
                ( player_sprite.getY() + player_sprite.getHeight() / 2 ) );

        Body bullet_body = main_world.createBody( bullet_bodyDef );

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( bullet_sprite.getWidth() /2/ PIXELS_PER_METER, bullet_sprite.getHeight()/2/PIXELS_PER_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = .1f;
        fixtureDef.restitution = 0.1f;

        bullet_body.createFixture( fixtureDef );

        Sprite b_sprite= new Sprite(bullet_sprite);//create clone of the sprite
        b_sprite.setPosition( bullet_body.getPosition().x, bullet_body.getPosition().y );
        //bullet_body.setLinearVelocity( 50*PIXELS_PER_METER,0 );
        bullet_body.setGravityScale(0.5f  );
        //bullet_body.applyForceToCenter( 800f,0f,true );
        bullet_body.applyLinearImpulse( 10*PIXELS_PER_METER,0,0,0,false );
        */

        //bullet_bodies.add(new Pencil( bullet_body, batch,this  ));
        Player_Pencils.add(new Pencil( this  ));
        //shape.dispose();

    }



    //"Called when a key was released"
    //@Override
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

    //@Override
    public boolean keyTyped(char character) {
        return false;
    }

    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning".
    //@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    //@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    //@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    //@Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //@Override
    public boolean scrolled(int amount) {
        return false;
    }
}
