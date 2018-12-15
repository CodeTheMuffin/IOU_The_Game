package com.iou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.iou.IOU.PIXELS_PER_METER;
import static com.iou.IOU.print;

public class Wall {
    //determines if its left, right, up or down
    public static enum wall_position{LEFT, RIGHT, UP, DOWN};
    wall_position curr_wall_position;

    Body wall_body;
    Sprite wall_sprite;
    final static Texture wall_txt= new Texture( Gdx.files.internal("badlogic.jpg"));
    //static Batch batch;
    static Player player;
    BodyDef wall_bodyDef;
    float wall_width=0, wall_height=0;
    float wall_x = 0, wall_y =0;

    public Wall(Player the_player, wall_position the_position)
    {
        if(player == null)
            player = the_player;

        curr_wall_position = the_position;

        setup();
    }

    //setup the pencil's box2D properties
    void setup()
    {
        wall_sprite = new Sprite(wall_txt);

        //create a body definition for the player
        //it needs to be dynamic, because the bullet/pencil is moving
        wall_bodyDef = new BodyDef();
        wall_bodyDef.type = BodyDef.BodyType.StaticBody;

        switch(curr_wall_position)
        {
            case UP://ceiling
            {
                wall_width= IOU.WIDTH;
                wall_height = IOU.HEIGHT*.05f;//making height 5% of the max viewport height
                //wall_x = (0- wall_height/2)/PIXELS_PER_METER;
                wall_x=0;
                //wall_y= (IOU.HEIGHT- wall_height)/PIXELS_PER_METER;
                wall_y= ((IOU.HEIGHT-wall_height)/2)/PIXELS_PER_METER;
                //wall_y= (IOU.HEIGHT)/PIXELS_PER_METER;
                wall_sprite.setColor( Color.GREEN );//for debugging
                break;
            }
            case DOWN://floor
            {
                wall_width= IOU.WIDTH;
                wall_height = IOU.HEIGHT*.05f;//making height 5% of the max viewport height
                //wall_x = (0 + wall_width/2)/PIXELS_PER_METER ;// keep 0 for better understanding
                wall_x = 0;
                //wall_y = (0 - wall_height/2)/PIXELS_PER_METER ;
                //wall_y = wall_height;//wall_height/PIXELS_PER_METER;
                //wall_y = wall_height/2/PIXELS_PER_METER;
                wall_y=0;
                wall_sprite.setColor( Color.PINK );//for debugging
                break;
            }
            case LEFT://left wall
            {
                wall_height= IOU.HEIGHT;//IOU.WIDTH;
                wall_width = IOU.WIDTH*0.05f;//IOU.HEIGHT*.05f;//making height 5% of the max viewport height
                //wall_x = (0- wall_width/2)/PIXELS_PER_METER;
                wall_x= 0;
                //wall_y= (0+ wall_height/2)/PIXELS_PER_METER;
                wall_y = 0;
                wall_sprite.setColor( Color.RED );//for debugging
                break;
            }
            case RIGHT://right wall
            {
                wall_height= IOU.HEIGHT;//IOU.WIDTH;
                wall_width = IOU.WIDTH*0.05f;//IOU.HEIGHT*.05f;//making height 5% of the max viewport height
                //wall_x = IOU.WIDTH - IOU.HEIGHT*.05f;
                //wall_x = (IOU.WIDTH - wall_width/2)/PIXELS_PER_METER;
                //wall_x = (IOU.WIDTH/2/PIXELS_PER_METER);
                //wall_x = wall_width/2/PIXELS_PER_METER;
                //wall_x = (IOU.WIDTH - (wall_width))/PIXELS_PER_METER;
                //wall_x = IOU.WIDTH*.095f/PIXELS_PER_METER;
                wall_x = (IOU.WIDTH -wall_width)/2/PIXELS_PER_METER;
                wall_y= 0;
                wall_sprite.setColor( Color.YELLOW );//for debugging
                break;
            }
        }


        //wall_sprite.setSize( wall_width,wall_height );


        //wall_bodyDef.position.set( wall_x,wall_y );//bottom left?
        wall_bodyDef.position.set( wall_x,wall_y );

        //wall_bodyDef.position.set( ( player.getSprite().getX() + player.getSprite().getWidth()+50  ) ,
           //     ( player.getSprite().getY() + player.getSprite().getHeight() / 2 ) );

        wall_body = player.getWorld().createBody( wall_bodyDef );

        //PolygonShape shape = new PolygonShape();
        //shape.setAsBox( wall_sprite.getWidth() /2/ PIXELS_PER_METER,
                //wall_sprite.getHeight()/2/PIXELS_PER_METER);

        EdgeShape edgeShape = new EdgeShape();
        //edgeShape.set(-wall_width/2, -wall_height/2, wall_width/2, -wall_height/2);
        edgeShape.set(0, wall_height, wall_width, 2);//this works
        //edgeShape.set(wall_x, wall_y-wall_height, wall_width, wall_height);//this works
        //wall_height*.1f

        PolygonShape wall_shape = new PolygonShape();
        wall_shape.setAsBox( wall_width,
                wall_height);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = wall_shape;//edgeShape;
        //fixtureDef.density = 0.01f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;//0.1f

        wall_body.createFixture( fixtureDef );
        wall_sprite.setSize( wall_width,wall_height );
        //wall_sprite.setPosition( wall_body.getPosition().x, wall_body.getPosition().y );

        //print("Wall h: "+ wall_height+ "\tw: "+wall_width+"\tx: "+wall_x +"\ty: "+ wall_y);

        //set the wall's object to the body's UserData to make it easier to determine if its a wall object
        // in collision detection
        wall_body.setUserData( this );

        //wall_body.applyLinearImpulse( 10*PIXELS_PER_METER,0,0,0,false );

        edgeShape.dispose();

    }


    //mainly used for debugging the walls
    public void setColor(Color c)
    {
        wall_sprite.setColor( c );
    }

    //to be called within a batch.begin and batch.end.
    public void draw_me(Batch batch)
    {
        //bullet_body.applyLinearImpulse( 50*PIXELS_PER_METER,0,0,0,true );
        //wall_sprite.setPosition( wall_body.getPosition().x, wall_body.getPosition().y );
        wall_sprite.setPosition( wall_x*PIXELS_PER_METER*2,wall_y*PIXELS_PER_METER*2 );
        wall_sprite.draw( batch );
    }

    public Body getBody()
    {return wall_body;}
}
