package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Random;
import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;



public class Assignments extends GAME_OBJECT{
    //something to hold all the sprite animations
    public final static Sprite[] sprite_animations = new Sprite[7];

    public final int MAX_GRADE =4;
    public int grade= 0;
    /*grades:
       * 0: F : 0%
       * 1: D : 25%
       * 2: C : 50%
       * 3: B : 75%
       * 4: A : 100%
       * */

    public int x, y;//position
    public float vx = -3f;//horizontal speed in meters (x velocity)
    public static World world;
    public Body assignment_body;
    public Sprite assignment_sprite;//the current sprite
    public final float assignment_width_px = 32, assignment_height_px =32;//in pixels
    public final float assignment_world_width = assignment_width_px/PIXELS_PER_METER;//in meters
    public final float assignment_world_height = assignment_height_px/PIXELS_PER_METER;//in meters

    //startX=(IOU.WIDTH/2) + Wall.extra_space
    private float startX=(IOU.WIDTH /2) + assignment_width_px*.75f;//, startY= IOU.HEIGHT/2;//in pixels
    private float startY = IOU.HEIGHT*.125f;

    /*
    public final static float MAX_START_Y = IOU.HEIGHT*0.6f;//in pixels
    public final static float MIN_START_Y = IOU.HEIGHT*0.125f;//in pixels

    public final static float MAX_SPEED = IOU.HEIGHT*0.6f;//in meters
    public final static float MIN_SPEED = IOU.HEIGHT*0.125f;//in meters
    */

    public boolean isBad = false; //some assignments are always bad, regardless how much work you put into them!
    public int bad_chance = 50; // used for generating the chance of getting a bad assignment
    // bad assignments should be NOT considered 'late' if it hits the Left wall. They are move faster.

    public boolean isAwesome = false;//some assignments are always good (easy A), regardless of how much work you put
    // into them!
    public int good_chance =50; //used for generating the chance of getting an awesome assignment.
    public boolean isReadyToDie= false;

    //timer to be destroyed
    //destroy Assignment after

    Timer test_timer;

    public Assignments(World w)
    {
        pre_setup( w );
        test_timer= new Timer(5f);//default is 5 seconds
    }

    public Assignments(World w, float speed)
    {
        if( speed <0 && speed >= -15)
            vx= speed;
        pre_setup( w );
        test_timer= new Timer(5f);//default is 5 seconds
    }

    public Assignments(World w, float speed, float seconds)
    {
        if( speed <0f && speed >= -15f)
            vx= speed;

        if(seconds > 0f && seconds<= 10f)
            test_timer= new Timer(seconds);
        else
            test_timer= new Timer(5f);//default is 5 seconds

        pre_setup( w );
    }

    public Assignments(World w, float speed, float seconds, float starting_Y)
    {
        if( speed <0f && speed >= -15f)
            vx= speed;
        print("ASS: speed: "+ speed +"\tvx: "+ vx);

        if(seconds > 0f && seconds<= 10f)
            test_timer= new Timer(seconds);
        else
            test_timer= new Timer(5f);//default is 5 seconds

        if(starting_Y>= MIN_START_Y && starting_Y <= MAX_START_Y)
            startY = starting_Y;

        print("\tthe starting Y: "+ starting_Y);

        pre_setup( w );
    }


    //created pre_setup just in case there is time to make Classes related (ie. have classes extend other classes)
    private void pre_setup(World w)
    {
        if(world == null)
        {
            world =w;
        }

        setup();
    }

    private int getArraySize(Sprite[] A)
    {
        int q=0;//quanity or size
        for(Sprite a: A)
        {
            if(a != null)
                q++;
        }
        return q;
    }

    public void setup()
    {
        if(getArraySize( sprite_animations ) == 0)
        {
            //sprite_animations = new Sprite[5];
            String path = "";//"/Sprites/Assignments/";
            sprite_animations[0] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg" ) ));//F
            sprite_animations[0].setColor( Color.BLACK );//F for testing

            sprite_animations[1] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//D
            sprite_animations[1].setColor( Color.FIREBRICK );//D

            sprite_animations[2] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//C
            sprite_animations[2].setColor( Color.PINK );//D

            sprite_animations[3] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//B
            sprite_animations[3].setColor( Color.BLUE );//D

            sprite_animations[4] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//A
            sprite_animations[4].setColor( Color.WHITE );//D

            sprite_animations[5] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//BAD
            sprite_animations[5].setColor( Color.RED );//D

            sprite_animations[6] = new Sprite(new Texture( Gdx.files.internal( path+"badlogic.jpg"  ) ));//AWESOME
            sprite_animations[6].setColor( Color.GREEN );//D

            //resize all of them
            for(int i=0; i< sprite_animations.length;i++)
            {
                //sprite_animations[i].setSize( 10,10 );//TODO: change this later
                sprite_animations[i].setSize( assignment_world_width, assignment_world_height);
                sprite_animations[i].setOrigin( assignment_world_width/2, assignment_world_height/2);
            }
        }

        assignment_sprite = sprite_animations[0];//set default sprite to this

        if(new Random().nextInt(bad_chance) == 1)// 1/X chance to spawn a bad assignment
        {
            isBad = true;
            grade = 5;
            assignment_sprite = sprite_animations[grade];//set default sprite to this
        }

        if(!isBad && new Random().nextInt(good_chance) == 1) // 1_Y chance to spawn an awesome assignment
        {
            isAwesome = true;
            grade = 6;
            assignment_sprite = sprite_animations[grade];//set default sprite to this
        }

        // Creating the Assignment BODY PROCESS

        BodyDef assignment_body_def = new BodyDef();
        assignment_body_def.type = BodyDef.BodyType.KinematicBody;

        assignment_body_def.position.set(startX/PIXELS_PER_METER, startY/PIXELS_PER_METER);

        assignment_body = world.createBody( assignment_body_def );

        PolygonShape assignment_shape = new PolygonShape();
        assignment_shape.setAsBox( assignment_world_width/2, assignment_world_height/2 );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = assignment_shape;
        fixtureDef.density = 0.05f;

        assignment_body.createFixture( fixtureDef );
        //assignment_sprite.setSize( assignment_world_width, assignment_world_height);
        //assignment_sprite.setOrigin( assignment_world_width/2, assignment_world_height/2);
        //essentially setting the Origin to the sprite's width and height

        assignment_sprite.setPosition( getBody_X(), getBody_Y());

        assignment_body.setLinearVelocity( vx,0 );

        assignment_body.setUserData( this );

        assignment_shape.dispose();
    }

    //to be called in a render() method
    public void draw_me(Batch batch)
    {
        if(!isReadyToDie)
        {
            assignment_sprite.setPosition( getBody_X() - (assignment_world_height/2),
                    getBody_Y() - (assignment_world_height/2));

            assignment_sprite.setRotation( assignment_body.getAngle() * MathUtils.radiansToDegrees );
            assignment_sprite.draw( batch );

            check_out_of_bounds();

            if(test_timer.isTimeUp() )
            {
                isReadyToDie = true;//destroy();//print("time is up");
            }
        }

    }

    //just in case something goes bonkers...
    //Pencil and Assignment have same method
    public void check_out_of_bounds()
    {
        float body_x = getBody_X();
        float body_y = getBody_Y();

        float max_y = (IOU.HEIGHT+(Wall.extra_space))/PIXELS_PER_METER;
        float min_y = -max_y;
        float max_x = (IOU.WIDTH+(Wall.extra_space))/PIXELS_PER_METER;
        float min_x = -max_x;

        //if(body_y <0 || body_y > IOU.HEIGHT || body_x<0 || body_x >IOU.WIDTH/2/PIXELS_PER_METER)
        if(body_y < min_y || body_y >max_y || body_x < min_x || body_x > max_x)
        {
            isReadyToDie = true;
            //destroy();
        }
    }

    public int getGrade()
    {
        if(isBad)
            return 0;
        else if (isAwesome)
            return MAX_GRADE;

        return grade;
    }

    /*
    public void destroy()
    {
        isReadyToDie = true;
        if(assignment_body != null)
        {
            world.destroyBody( assignment_body );
        }
    }

    public void destroy(World the_world)
    {
        isReadyToDie = true;
        if(assignment_body != null)
        {
            the_world.destroyBody( assignment_body );
        }
    }*/

    //called when a pencil hit the assignment
    public void pencil_hit()
    {
        if(!isAwesome && !isBad)
        {
            if(grade +1 <= MAX_GRADE)
            {
                grade++;
                assignment_sprite = sprite_animations[grade];
            }
        }
    }

    //called when assignment hit the left wall (will be coded that way in PlayScreen)
    public void wall_hit()
    {
        if(!isAwesome && !isBad)
        {
            if(grade -1 >= 0)
            {
                grade--;
                assignment_sprite = sprite_animations[grade];
                isReadyToDie = true;
            }
        }
    }

    //called when the player hit the assignment
    public void player_hit()
    {
        //if(!isAwesome && !isBad)
        {
            isReadyToDie = true;
            //print("Assignment collected!");
        }
    }



    public Body getBody()
    { return assignment_body;}

    public float getBody_X()
    {return assignment_body.getPosition().x;}

    public float getBody_Y()
    {return assignment_body.getPosition().y;}

    public boolean isBad(){return  isBad;}

    public boolean isAwesome(){return isAwesome;}

    //asks if is is ready to die
    public boolean isTimerDone()
    {return isReadyToDie;}//test_timer.isTimeUp();}
}
