package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;


public class Pencil {
    Body bullet_body;
    Sprite bullet_sprite;
    final static Texture bullet_txt= new Texture( Gdx.files.internal("badlogic.jpg"));
    //static Batch batch;
    static Player player;
    BodyDef bullet_bodyDef;
    final float bullet_width_px = 20f, bullet_height_px = 10f; //in pixels
    final float bullet_world_width = bullet_width_px/PIXELS_PER_METER;//in meters
    final float bullet_world_height = bullet_height_px/PIXELS_PER_METER;//in meters

    //stuff for setting up stuff

    public Pencil(Player the_player)//public Pencil( Batch the_batch, Player the_player)
    {
        bullet_sprite = new Sprite(bullet_txt);

        //if(batch == null)
        //{   batch = the_batch;}

        if(player == null)
        {    player= the_player;}

        setup();

    }

    //setup the pencil's box2D properties
    private void setup()
    {
        bullet_sprite = new Sprite(bullet_txt);
        bullet_sprite.setSize( bullet_width_px,bullet_height_px );
        bullet_sprite.setColor( Color.ORANGE );

        //print("\tbullet height: "+ bullet_sprite.getHeight() +"\twidth: "+ bullet_sprite.getWidth()+"\tColor: "+
              //  bullet_sprite.getColor());

        //create a body definition for the player
        //it needs to be dynamic, because the bullet/pencil is moving
        bullet_bodyDef = new BodyDef();
        bullet_bodyDef.type = BodyDef.BodyType.DynamicBody;

        //bullet_bodyDef.position.set( ( player.getSprite().getX() + player.getSprite().getWidth()+50  ) ,
                //( player.getSprite().getY() + player.getSprite().getHeight() / 2 ) );

        player.print_position();
        float new_x = player.getBody_X()+ (player.getSprite().getWidth()/2/PIXELS_PER_METER);
        new_x = player.getBody_X() +0.51f;

        print("trying to set bullet x: "+ new_x);
        bullet_bodyDef.position.set(new_x,
                player.getBody_Y());

        bullet_body = player.getWorld().createBody( bullet_bodyDef );

        PolygonShape shape = new PolygonShape();
        //shape.setAsBox( bullet_sprite.getWidth() /2/ PIXELS_PER_METER,
               // bullet_sprite.getHeight()/2/PIXELS_PER_METER);
        shape.setAsBox( bullet_world_width/2, bullet_world_height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.friction = .1f;
        fixtureDef.restitution = 0.1f;

        bullet_body.createFixture( fixtureDef );

        //Sprite b_sprite= new Sprite(bullet_sprite);//create clone of the sprite
        bullet_sprite.setPosition( bullet_body.getPosition().x, bullet_body.getPosition().y );

        bullet_sprite.setSize( bullet_world_width, bullet_world_height);

        //bullet_sprite.setOrigin( bullet_width/2/PIXELS_PER_METER, bullet_height/2/PIXELS_PER_METER );
        //the above is essentially the same as below, since we just set the size
        bullet_sprite.setOrigin( bullet_sprite.getWidth()/2, bullet_sprite.getHeight()/2 );

        //bullet_body.setLinearVelocity( 50*PIXELS_PER_METER,0 );
        //bullet_body.setGravityScale(0.01f  );//.3
        //bullet_body.applyForceToCenter( 800f,0f,true );

        //TODO: THIS WAS USED
        //bullet_body.applyLinearImpulse( 10*PIXELS_PER_METER,0,0,0,false );


        //testing
        //bullet_body.applyForceToCenter( 0.1f,0.0f, true );//0.3
        //bullet_body.setLinearVelocity( .1f,0f);
        bullet_body.setLinearVelocity( 50,0 );

        bullet_body.setUserData( this );
        //bullet_bodies.add(new Bullet( bullet_body, b_sprite ));
        shape.dispose();
    }

    public Body get_pencil_body()
    {return bullet_body;}

    public Sprite get_Pencil_sprite()
    {return bullet_sprite;}

    //to be called within a batch.begin and batch.end.
    public void draw_me(Batch batch)
    {
        //bullet_body.applyLinearImpulse( 50*PIXELS_PER_METER,0,0,0,true );
        //bullet_sprite.setPosition( bullet_body.getPosition().x, bullet_body.getPosition().y );
        bullet_sprite.setPosition( getX() - (bullet_world_width/2),
                getY() - (bullet_world_height/2));

        bullet_sprite.setRotation( bullet_body.getAngle() * MathUtils.radiansToDegrees );
        bullet_sprite.draw( batch );
    }

    public float getX()
    {
        return bullet_body.getPosition().x;
    }

    public float getY()
    {
        return bullet_body.getPosition().y;
    }

    public Body getBody()
    {return bullet_body;}
}
