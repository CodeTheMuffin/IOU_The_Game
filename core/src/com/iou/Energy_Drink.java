package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.iou.IOU.PIXELS_PER_METER;

public class Energy_Drink extends GAME_OBJECT{
    static World world;
    Sound e_player_hit_sound;
    Sprite e_drink_spr;
    public Body e_body;
    public float vx = -3f;//horizontal speed in meters (x velocity)

    public final float e_width_px = 32, e_height_px =32;//in pixels
    public final float e_world_width = e_width_px/PIXELS_PER_METER;//in meters
    public final float e_world_height = e_height_px/PIXELS_PER_METER;//in meters

    private float startX=(IOU.WIDTH /2) + e_width_px*.75f;//in pixels
    private float startY = IOU.HEIGHT*.125f;
    public boolean isReadyToDie= false;
    public boolean player_hit_me =false;

    Timer life_span_timer;

    public Energy_Drink( World w, float speed, float seconds, float starting_Y)
    {
        if( speed <0f && speed >= -15f)
            vx= speed;

        if(seconds > 0f && seconds<= 10f)
            life_span_timer= new Timer(seconds);
        else
            life_span_timer= new Timer(5f);//default is 5 seconds

        if(world == null)
            world = w;

        if(starting_Y>= MIN_START_Y && starting_Y <= MAX_START_Y)
            startY = starting_Y;

        setup();
    }

    private void setup()
    {
        e_drink_spr = new Sprite( new Texture( Gdx.files.internal( "Sprites/e_drink.png" ) ) );

        e_drink_spr.setSize( e_world_width, e_world_height);
        e_drink_spr.setOrigin( e_world_width/2, e_world_height/2);

        e_player_hit_sound = Gdx.audio.newSound( Gdx.files.internal( "SFX/slurp.mp3" ) );

        // Creating the Assignment BODY PROCESS
        BodyDef assignment_body_def = new BodyDef();
        assignment_body_def.type = BodyDef.BodyType.KinematicBody;

        assignment_body_def.position.set(startX/PIXELS_PER_METER, startY/PIXELS_PER_METER);

        e_body = world.createBody( assignment_body_def );

        PolygonShape assignment_shape = new PolygonShape();
        assignment_shape.setAsBox( e_world_width/2, e_world_height/2 );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = assignment_shape;
        fixtureDef.density = 0.05f;

        e_body.createFixture( fixtureDef );
        e_drink_spr.setPosition( getBody_X(), getBody_Y());

        e_body.setLinearVelocity( vx,0 );

        e_body.setUserData( this );

        assignment_shape.dispose();
    }

    //to be called in a render() method
    public void draw_me(Batch batch)
    {
        if(!isReadyToDie)
        {
            e_drink_spr.setPosition( getBody_X() - (e_world_height/2),
                    getBody_Y() - (e_world_height/2));

            e_drink_spr.setRotation( e_body.getAngle() * MathUtils.radiansToDegrees );
            e_drink_spr.draw( batch );

            check_out_of_bounds();

            if(life_span_timer.isTimeUp() )
            {
                isReadyToDie = true;
            }
        }
    }

    public void player_hit()
    {
        isReadyToDie = true;
        player_hit_me= true;
        e_player_hit_sound.play(0.15f);
    }

    //just in case something goes bonkers...
    public void check_out_of_bounds()
    {
        float body_x = getBody_X();
        float body_y = getBody_Y();

        float max_y = (IOU.HEIGHT+(Wall.extra_space))/PIXELS_PER_METER;
        float min_y = -max_y;
        float max_x = (IOU.WIDTH+(Wall.extra_space))/PIXELS_PER_METER;
        float min_x = -max_x;

        if(body_y < min_y || body_y >max_y || body_x < min_x || body_x > max_x)
        {
            isReadyToDie = true;
        }
    }

    public void pauseTimer()
    { life_span_timer.pauseTimer(); }

    public Body getBody()
    { return e_body;}

    public float getBody_X()
    {return e_body.getPosition().x;}

    public float getBody_Y()
    {return e_body.getPosition().y;}

    //asks if is is ready to die
    public boolean isTimerDone()
    {return isReadyToDie;}
}
