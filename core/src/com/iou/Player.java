package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;

public class Player {//implements InputProcessor {
    public World main_world;//TODO: get rid of public for main_world
    Body player_body;
    final Sprite player_sprite;//, bullet_sprite;
    // speed and jump_force are being used with applyLinearImpulse, so they are both velocities, in m/s.
    float speed = 4f;//5
    float jump_force = 2f;//2.5
    final float ORGINAL_Y = 150F/PIXELS_PER_METER, ORGINAL_X = -300F/PIXELS_PER_METER;//50 arbitrary values in meters
    ArrayList< Integer > allkeysPressed = new ArrayList< Integer >( 3 );
    boolean keypressed = false;
    public boolean jumping =true,onGround = false;
    final float player_width_px = 100f, player_height_px = 200f;//in pixels also arbitrary
    final float player_world_width = player_width_px /PIXELS_PER_METER; // in meters
    final float player_world_height = player_height_px / PIXELS_PER_METER;
    int left_center_right = 0;// -1 left    0 center   +1 right

    private int assignments_collected= 0, assignments_needed = 10, assignments_allowed = 15;
    public int total_grade = 0;
    private int eDrinks_collected = 0, eDrinks_allowed = 5;//can only collect up to 5 drinks a level
    private int level= 0;
    private Timer level_timer;

    private ArrayList<Body > BodiesToBeDeleted;

    //FOR THE PENCILS being thrown
    public ArrayList< Pencil > Player_Pencils;




    public Player(World world, Batch the_batch)
    {
        main_world= world;
        player_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));

        //bullet_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));
        Player_Pencils = new ArrayList<>();

        //if(batch == null)
          //  batch = the_batch;

        setup();
    }

    //setup the pencil's box2D properties
    public void setup()
    {
        player_sprite.setColor( Color.BLUE );
        BodiesToBeDeleted = new ArrayList<Body>();
        //print("Are the world's the same?: "+ main_world + "\t"+getWorld());

        //create a body definition for the player
        //it needs to be dynamic, because we expect the player to be moving
        BodyDef player_bodyDef = new BodyDef();
        player_bodyDef.type = BodyDef.BodyType.DynamicBody;

        //this works
        //player_bodyDef.position.set( ( player_sprite.getX() + player_sprite.getWidth() / 2 ) / PIXELS_PER_METER,
           //     ( player_sprite.getY() + player_sprite.getHeight() / 2 ) / PIXELS_PER_METER );


        //print( "player pos: x: " + player_sprite.getX() + "\ty: " + player_sprite.getY() );

        player_body = main_world.createBody( player_bodyDef );

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();

        // JC
        // shape.setAsBox( player_sprite.getWidth()/PIXELS_PER_METER/2,player_sprite.getHeight()/PIXELS_PER_METER/2 );
        //center the box to be the center of the sprite
        //this worked
        //shape.setAsBox( player_sprite.getWidth() / 2 / PIXELS_PER_METER,
          //      player_sprite.getHeight() / 2 / PIXELS_PER_METER );

        shape.setAsBox( player_world_width/2, player_world_height/2 );//20,40

        player_sprite.setSize( player_world_width,player_world_height );
        //able to link sprite with body's scaling and rotations (even if they aren't being used in-game)
        //  really for 'just-in-case' a bug occurs, it won't look janky.
        player_sprite.setOrigin( player_sprite.getWidth()/2, player_sprite.getHeight()/2 );

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

    public void setup_level(int lvl)
    {
        level = lvl;
        reset_assignments_collected();
        reset_eDrinks_collected();
        float seconds= 10f;

        //the levels are the years
        if(level == 1)
        {
            //REQ_hw = 5;REQ_projects = 2;
            assignments_needed = 5;
            assignments_allowed = 10;
            seconds = 10f;
        }
        else if(level == 2)
        {
            //REQ_hw = 8;REQ_projects = 3;
            assignments_needed = 8;
            assignments_allowed = 10;
            seconds = 20f;
        }
        else if (level == 3)
        {
            //REQ_hw = 10;REQ_projects = 4;
            assignments_needed = 10;
            assignments_allowed = 15;
            seconds = 30f;
        }
        else if (level == 4)
        {
            //REQ_hw = 12;REQ_projects = 6;
            assignments_needed = 12;
            assignments_allowed = 15;
            seconds = 40f;
        }

        level_timer = new Timer(seconds);
    }

    public void isLevelDone()
    {
        if(level_timer.isTimeUp())
        {
            //TODO: PAUSE, HAVE POP-UP, (once closed), increment level, reposition player,
            //reset spawner (in PlayScreen), reset level.
        }

    }

    //assuming that the batch has begun and ended before calling this method
    public void draw_me( Batch batch)
    {
        //player_sprite.setPosition( player_body.getPosition().x, player_body.getPosition().y );
        //player_sprite.setPosition( player_body.getPosition().x * PIXELS_PER_METER - (player_sprite.getWidth()/2),
               // player_body.getPosition().y * PIXELS_PER_METER - (player_sprite.getHeight()/2));

       // player_sprite.setPosition( getBody_X(), getBody_Y() );
        adjust_sprite_position_and_rotation();


        //player_sprite.setPosition( player_body.getPosition().x * PIXELS_PER_METER*2,
                //player_body.getPosition().y * PIXELS_PER_METER*2);

        check_out_of_bounds();

        player_sprite.draw( batch );
        //print("player pos: "+ player_sprite.getWidth()+"\t" +player_sprite.getHeight()+"\n");
    }

    //draw pencils if they can
    public void draw_pencils(Batch batch)
    {
        //print("drawing pencils: "+ Player_Pencils.size());
        //draw valid pencils, remove unnecessary ones
        for(int i= 0;i<Player_Pencils.size(); i++)//for(Pencil pencil: player.Player_Pencils)
        {
            Pencil pencil = Player_Pencils.get( i );

            if(!pencil.isTimerDone())
                pencil.draw_me(batch);
            else//if timer is done or if ready to die
            {
                /* This broke the program
                    if(pencil.get_pencil_body() != null)//if there is still a body, get rid of it in the world!
                    {
                        //pencil.destroy();
                    }*/

                BodiesToBeDeleted.add( pencil.getBody() );
                Player_Pencils.remove( i );
                i--;

                if(Player_Pencils.size() ==0)
                    break;
                //player.Player_Pencils.remove( pencil );
            }
            //pencil.get_Pencil_sprite().draw( game.batch );
        }
    }

    //to be called in the draw_me(). really only for rendering purposes
    private void adjust_sprite_position_and_rotation()
    {
        player_sprite.setPosition( getBody_X() - player_sprite.getWidth()/2,
                getBody_Y() - player_sprite.getHeight()/2 );

        player_sprite.setRotation( player_body.getAngle() * MathUtils.radiansToDegrees );
        //player_sprite.rotate( player_body.getAngle() );
        //print("Body angle: "+ player_body.getAngle());
        //print("Sprite angle: "+ player_sprite.getRotation());

    }

    //just in case something goes bonkers...
    public void check_out_of_bounds()
    {
        float body_x = getBody_X();
        float body_y = getBody_Y();

        float max_y = IOU.HEIGHT/PIXELS_PER_METER;
        float min_y = -max_y;
        float max_x = IOU.WIDTH/PIXELS_PER_METER;
        float min_x = -max_x;


        //if(body_y <0 || body_y > IOU.HEIGHT || body_x<0 || body_x >IOU.WIDTH/2/PIXELS_PER_METER)
        if(body_y < min_y || body_y >max_y || body_x < min_x || body_x > max_x)
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
        player_body.setAngularVelocity( 0 );
    }

    // should be called in a render method
    // called when the user hits one or more keys
    public void movement()
    {
        if(!allkeysPressed.isEmpty())
        {
            for(int i=0;i<3 && i< allkeysPressed.size(); i++)
            {
                //print(i+"\t"+allkeysPressed.size());
                int keycode = allkeysPressed.get( i );
                Vector2 moving_vec = new Vector2( 0,0 );
                left_center_right = 0;//meaning the center or neither left nor right

                if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
                {
                    //THIS works: player_body.setLinearVelocity(speed, 0f);

                    //player_body.applyLinearImpulse( new Vector2( 0,550 ), player_body.getPosition(),true );
                    left_center_right = 1;
                }

                if(keycode == Input.Keys.LEFT|| keycode == Input.Keys.A)
                {
                    //this works: player_body.setLinearVelocity(-speed,0f);
                    left_center_right = -1;
                }

                player_body.applyForceToCenter( speed*left_center_right,0,true );
                //if(keycode == Input.Keys.SPACE)
                //player_body.setLinearVelocity(0f,200f);

            }
            //print("===============================");
        }
    }

    public void increment_assignments_collected()
    {assignments_collected++;}

    public void increment_eDrinks_collected()
    {eDrinks_collected++; }

    public void reset_assignments_collected()
    {assignments_collected=0;}

    public void reset_eDrinks_collected()
    {eDrinks_collected=0;}

    //delete bodies that are ready to be dead
    public void DeleteBodies(World world)
    {
        for(Body body: BodiesToBeDeleted)
        {
            world.destroyBody( body );
        }
        BodiesToBeDeleted.clear();
    }

    public void assignment_collected()
    {assignments_collected++;}

    public Sprite getSprite()
    {return player_sprite;}

    public World getWorld()
    {return this.main_world;}

    public Body getBody()
    {return player_body;}

    public float getBody_X(){return player_body.getPosition().x;}
    public float getBody_Y(){return player_body.getPosition().y;}
    public Vector2 getBody_POS(){return player_body.getPosition();}//get position



    public void print_position()
    {
        print(this.getClass()+"\n\tx: "+getBody_X()+"\ty: "+ getBody_Y());
    }

    //called if we want to remove something from the collection of pencils
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
    public boolean keyDown(int keycode, boolean isPaused) {
        if (isPaused) //prevent user from pressing buttons when game is paused
            return false;

        //TODO: TO DELETE LATER
        if(keycode == Input.Keys.UP)
        {
            player_body.applyAngularImpulse( 1,true );
        }

        if(keycode == Input.Keys.DOWN)
        {
            player_body.applyAngularImpulse( -1,true );
        }

        if(keycode == Input.Keys.SPACE && onGround)
        {
            print("Trying to jump");
            //player_body.setLinearVelocity(0f,80000f);
            //player_body.setLinearVelocity(speed*left_center_right,jump_force);
            //THIS WORKS: player_body.applyLinearImpulse( speed*left_center_right, jump_force,
                    //player_body.getPosition().x, player_body.getPosition().y, true);

            //player_body.applyForceToCenter( 0,jump_force,true );
            player_body.applyLinearImpulse( 0,jump_force, getBody_X(), getBody_Y(),true );

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
            //Sprite dummy = Player_Pencils.get( 0 ).bullet_sprite;
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
    public boolean keyUp(int keycode, boolean isPaused)
    {
        if(isPaused)
            return false;

        //print("jumping: "+ jumping +"\tOnGround: "+ onGround);
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

        //print("\tAfter: jumping: "+ jumping +"\tOnGround: "+ onGround);
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
