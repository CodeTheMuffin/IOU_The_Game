package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
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
    public static int totalDebtOwed = 10000;// starting at 10K

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
    private int scholarshipMoney = 0;
    private int eDrinks_collected = 0, eDrinks_allowed = 5;//can only collect up to 5 drinks a level
    private int level= 0;

    private int boost_amount= 0;

    private Timer boost_timer;

    private Sound throwing_sound;

    private ArrayList<Body > BodiesToBeDeleted;

    //FOR THE PENCILS being thrown
    public ArrayList< Pencil > Player_Pencils;

    //use these as oppose to animations to simplify code
    //yes its more memory expensive, but due to lack of time....
    private Sprite[] player_idle_sprites = new Sprite[3];
    private Sprite[] player_boost_sprites = new Sprite[3];
    private Sprite[] player_run_sprites = new Sprite[3];
    private Sprite player_jump_sprite;

    private Sprite player_sprite;//current sprite

    //used for the sprites
    private boolean isRunning =false;
    private boolean isInBoostMode =false;
    private boolean justChanged = false;
    private boolean justPressed = false;
    private int animate_index= 0;// 0,1,2 values only

    private final float SPRITE_SIZE = 108f/PIXELS_PER_METER;

    private Timer animation_timer;

    public Player(World world, Batch the_batch)
    {
        main_world= world;
        //player_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));

        //bullet_sprite = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));
        Player_Pencils = new ArrayList<>();

        boost_timer = new Timer(1f);//runs for 3 seconds

        //if(batch == null)
          //  batch = the_batch;

        setup();
    }

    //setup the pencil's box2D properties
    public void setup()
    {
        setup_sprites();

        throwing_sound = Gdx.audio.newSound( Gdx.files.internal( "SFX/swoosh.mp3" ) );

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

        //specifying the density, restitution(bouncyness), others. for the player
        FixtureDef player_fixtureDef = new FixtureDef();
        player_fixtureDef.shape = shape;
        player_fixtureDef.density = 0.08f;// JC//0.1f
        player_fixtureDef.friction = 1f;//.5f
        player_fixtureDef.restitution = 0f;//.1

        player_body.setUserData( this );//easier for collision detection
        player_body.createFixture( player_fixtureDef );
        relocate();

        shape.dispose();
    }

    private void setup_sprites()
    {
        for(int i=0; i< player_idle_sprites.length; i++)
        {
            player_idle_sprites[i] = new Sprite( new Texture(
                    Gdx.files.internal( "Sprites/Player/idle_"+(i+1)+".png") ) );
            //player_idle_sprites[i].setSize( SPRITE_SIZE, SPRITE_SIZE );
            player_idle_sprites[i].setSize( player_world_width,player_world_height );
            player_idle_sprites[i].setOrigin( player_idle_sprites[i].getWidth()/2,
                    player_idle_sprites[i].getHeight()/2 );
        }

        for(int i=0; i< player_boost_sprites.length; i++)
        {
            player_boost_sprites[i] = new Sprite( new Texture(
                    Gdx.files.internal( "Sprites/Player/boost_"+(i+1)+".png") ) );
            //player_boost_sprites[i].setSize( SPRITE_SIZE, SPRITE_SIZE );
            player_boost_sprites[i].setSize( player_world_width,player_world_height );
            player_boost_sprites[i].setOrigin( player_boost_sprites[i].getWidth()/2,
                    player_boost_sprites[i].getHeight()/2 );
        }

        for(int i=0; i< player_run_sprites.length; i++)
        {
            player_run_sprites[i] = new Sprite( new Texture(
                    Gdx.files.internal( "Sprites/Player/run_"+(i+1)+".png") ) );
            //player_run_sprites[i].setSize( SPRITE_SIZE, SPRITE_SIZE );
            player_run_sprites[i].setSize( player_world_width,player_world_height );
            player_run_sprites[i].setOrigin( player_run_sprites[i].getWidth()/2,
                    player_run_sprites[i].getHeight()/2 );
        }

        player_jump_sprite= new Sprite(new Texture( Gdx.files.internal( "Sprites/Player/jump.png" ) ));
        //player_jump_sprite.setSize( SPRITE_SIZE, SPRITE_SIZE );
        player_jump_sprite.setSize( player_world_width,player_world_height );
        player_jump_sprite.setOrigin( player_jump_sprite.getWidth()/2,
                player_jump_sprite.getHeight()/2 );

        player_sprite = player_idle_sprites[0];
        player_sprite.setSize( player_world_width,player_world_height );//this
        //player_sprite.setSize( SPRITE_SIZE, SPRITE_SIZE);

        //able to link sprite with body's scaling and rotations (even if they aren't being used in-game)
        //  really for 'just-in-case' a bug occurs, it won't look janky.
        player_sprite.setOrigin( player_sprite.getWidth()/2, player_sprite.getHeight()/2 );

        animation_timer = new Timer(.2f);//.2f
    }

    public void setup_level(int lvl)
    {
        level = lvl;
        reset_assignments_collected();
        reset_eDrinks_collected();

        /*float seconds= 10f;

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
        }*/

        //level_timer = new Timer(seconds);
    }

    public void check_sprite_animation()
    {
        if(justChanged)
        {
            animate_index = 0;
            justChanged =false;
        }

        if(!isInBoostMode)
        {
            if(onGround)
            {
                if(isRunning)
                {
                    player_sprite = player_run_sprites[animate_index];
                }
                else//then idle
                {
                    player_sprite = player_idle_sprites[animate_index];
                }
            }
            else if(jumping)
            {
                player_sprite = player_jump_sprite;
            }
        }
        else//in boost mode
        {
            player_sprite = player_boost_sprites[animate_index];
        }

        if(animation_timer.isTimeUp())
        {
            animate_index = (animate_index+1)%3;
            animation_timer.resetTimer();
        }

    }

    public int getBoost_amount(){return boost_amount;}

    public void resetBoost()
    {boost_amount = 0; isInBoostMode = false;}

    public void incrementBoost()
    {
        if(!isInBoostMode)
        {
            boost_amount +=10;
            if(boost_amount >=100)
            {
                boost_amount =100;
                isInBoostMode =true;
                boost_timer.resetTimer();
            }
        }
    }

    /*
    public void isLevelDone()
    {
        if(level_timer.isTimeUp())
        {
            //TODO: PAUSE, HAVE POP-UP, (once closed), increment level, reposition player,
            //reset spawner (in PlayScreen), reset level.
        }

    }*/

    //assuming that the batch has begun and ended before calling this method
    public void draw_me( Batch batch)
    {
        //player_sprite.setPosition( player_body.getPosition().x, player_body.getPosition().y );
        //player_sprite.setPosition( player_body.getPosition().x * PIXELS_PER_METER - (player_sprite.getWidth()/2),
               // player_body.getPosition().y * PIXELS_PER_METER - (player_sprite.getHeight()/2));

       // player_sprite.setPosition( getBody_X(), getBody_Y() );

        if(isInBoostMode)
            BOOST_MODE();

        check_sprite_animation();
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
        //draw valid pencils, remove unnecessary ones elsewhere
        for(int i= 0;i<Player_Pencils.size(); i++)//for(Pencil pencil: player.Player_Pencils)
        {
            Pencil pencil = Player_Pencils.get( i );

            if(!pencil.isTimerDone())
                pencil.draw_me(batch);
            else//if timer is done or if ready to die
            {
                BodiesToBeDeleted.add( pencil.getBody() );
                Player_Pencils.remove( i );
                i--;

                if(Player_Pencils.size() ==0)
                    break;
            }
        }
    }

    public void BOOST_MODE()
    {
        boost_amount -= boost_timer.getTimeLeft()/60;// divide by 60 for the frames

        //to prevent the GUI from being funky
        if(boost_amount <0)
            boost_amount = 0;

        if(!boost_timer.isTimeUp())
        {
            player_body.applyAngularImpulse( 0.8f,true );
            create_bullet();
        }
        else
        {
            resetBoost();
        }
    }

    //to be called in the draw_me(). really only for rendering purposes
    private void adjust_sprite_position_and_rotation()
    {
        float new_x = getBody_X() - player_sprite.getWidth()/2;
        float new_y = getBody_Y() - player_sprite.getHeight()/2-.1f;

        player_sprite.setPosition(new_x, new_y);

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

    public void relocate()
    {
        //resets the player's position and speed
        player_body.setTransform( ORGINAL_X, ORGINAL_Y,0 );
        player_body.setLinearVelocity( 0,0 );
        player_body.setAngularVelocity( 0 );
    }

    //only to be called for the next level
    public void nextLevel()
    {
        setup_level( level++ );
        scholarshipMoney = 0;
        assignments_collected = 0;
        total_grade= 0;

        for(Pencil pencil: Player_Pencils)
        {
            if(!BodiesToBeDeleted.contains( pencil.getBody() ))
            {
                pencil.set_to_DIE();
                //Player_Pencils.remove( pencil );
            }
        }
        Player_Pencils.clear();
        allkeysPressed.clear();
        keypressed =false;
    }

    // should be called in a render method
    // called when the user hits one or more keys
    public void movement()
    {
        if(!allkeysPressed.isEmpty() && !isInBoostMode)
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
                    reset_stats();//unnecessary but more for understanding purposes
                    //justChanged =true;
                    isRunning = true;
                }
                else if(keycode == Input.Keys.LEFT|| keycode == Input.Keys.A)
                {
                    //this works: player_body.setLinearVelocity(-speed,0f);
                    left_center_right = -1;
                    reset_stats();//unnecessary but more for understanding purposes
                    //justChanged =true;
                    isRunning = true;
                }
                player_body.applyForceToCenter( speed*left_center_right,0,true );
            }
        }
        else
        {
            if(onGround && justPressed)
            {
                reset_stats();//unnecessary but more for understanding purposes
                justChanged =true;
            }
        }
    }

    public void calculateFinalGrade( int totalGrade, int assignmentsAllowedGradeTotal){
        if(totalGrade <= assignmentsAllowedGradeTotal*4 && totalGrade >assignmentsAllowedGradeTotal*3){
            scholarshipMoney = 3000;
        }
        if(totalGrade <= assignmentsAllowedGradeTotal*3 && totalGrade >assignmentsAllowedGradeTotal*2){
            scholarshipMoney = 1500;
        }
        if(totalGrade <= assignmentsAllowedGradeTotal*2 && totalGrade >assignmentsAllowedGradeTotal*1){
            scholarshipMoney = 500;
        }
        else{
            scholarshipMoney = 0;
        }
    }
    public int getScholarshipMoney(){
        return scholarshipMoney;
    }

    public void setScholarshipMoney(int newscholaramount){
        scholarshipMoney = newscholaramount;
    }

    public int getTotalGrade(){
        return total_grade;
    }

    public int getAssignments_allowed(){
        return assignments_allowed;
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

    public int get_assigment_collected_count(){return assignments_collected;}

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
           //main_world.destroyBody( pen.getBody() );
        }
    }

    //"Called when a key was pressed"
    //@Override
    public boolean keyDown(int keycode, boolean isPaused) {
        if (isPaused) //prevent user from pressing buttons when game is paused
            return false;

        if(isInBoostMode)
            return false;

        //TODO: TO DELETE LATER
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            player_body.applyAngularImpulse( 0.8f,true );
        }

        if(keycode == Input.Keys.DOWN|| keycode == Input.Keys.S)
        {
            player_body.applyAngularImpulse( -0.8f,true );
        }

        if(keycode == Input.Keys.SPACE && onGround)
        {
            //print("Trying to jump");
            //player_body.setLinearVelocity(0f,80000f);
            //player_body.setLinearVelocity(speed*left_center_right,jump_force);
            //THIS WORKS: player_body.applyLinearImpulse( speed*left_center_right, jump_force,
                    //player_body.getPosition().x, player_body.getPosition().y, true);

            //player_body.applyForceToCenter( 0,jump_force,true );
            player_body.applyLinearImpulse( 0,jump_force, getBody_X(), getBody_Y(),true );

            reset_stats();
            justChanged = true;

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
        throwing_sound.play(.2f);
        Player_Pencils.add(new Pencil( this  ));
    }

    //used for animations
    private void reset_stats()
    {
        isRunning= false;
        justPressed = false;
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
        {
            allkeysPressed.remove( index );
            justPressed = true;
        }


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
    public boolean keyTyped(char character) {return false; }
    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning".
    //@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false; }
    //@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false; }
    //@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    //@Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    //@Override
    public boolean scrolled(int amount) { return false; }
}
