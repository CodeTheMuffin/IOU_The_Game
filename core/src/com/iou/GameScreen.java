package com.iou;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntSet;

import java.util.ArrayList;

import javax.swing.DebugGraphics;

public class GameScreen extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    Sprite player_sprite, ground_sprite, block_sprite, bullet_sprite;
    World main_world;
    Body player_body, ground_body;
    OrthographicCamera camera;
    //IntSet all_keys_pressed = new IntSet(3);
    ArrayList< Integer > allkeysPressed = new ArrayList< Integer >( 3 );
    boolean keypressed = false, jumping = true, onGround = false;
    int left_center_right = 0;// -1 left    0 center   +1 right
    BodyDef bullet_bodyDef;
    ArrayList< Body > bullet_bodies;


    // JC
    // float speed = 200, jump_force= 12;//800000;
    // speed and jump_force are being used with applyLinearImpulse, so they are both velocities, in m/s.
    float speed = 5;
    float jump_force = 2.5f;

    // JC
    // final float PIXELS_PER_METER = 32;
    final float PIXELS_PER_METER = 100;

    public static void print( String s )
    {
        System.out.println( s );
    }

    @Override
    public void create( )
    {
        Gdx.graphics.setWindowedMode( 1200, 800 );
        batch = new SpriteBatch();
        img = new Texture( "badlogic.jpg" );
        player_sprite = new Sprite( img );
        player_sprite.setSize( 100, 200 );

        ground_sprite = new Sprite( img );
        ground_sprite.setSize( 1000, 50 );
        ground_sprite.setColor( Color.GREEN );

        block_sprite = new Sprite( img );
        block_sprite.setSize( 64, 64 );
        block_sprite.setColor( Color.CYAN );


        //set coords for sprite
        player_sprite.setPosition( 200, 400 );
        ground_sprite.setPosition( 0, 0 );
        block_sprite.setPosition( 800, 100 );

        // create a world
        main_world = new World( new Vector2( 0, -20f ), true );

        //create a body definition for the player
        //it needs to be dynamic, because we expect the player to be moving
        BodyDef player_bodyDef = new BodyDef();
        player_bodyDef.type = BodyDef.BodyType.DynamicBody;


        //using 1 unit as 1 pixel

        // JC
        // Don't use 1 unit as 1 pixel. That makes the physics world impossibly huge.

        // JC
        // player_bodyDef.position.set(player_sprite.getX(), player_sprite.getY());
        // Need to convert between physics world and pixel world, as done in the Box2D demos (https://bit.ly/2Q0OWp7).
        player_bodyDef.position.set( ( player_sprite.getX() + player_sprite.getWidth() / 2 ) / PIXELS_PER_METER,
                ( player_sprite.getY() + player_sprite.getHeight() / 2 ) / PIXELS_PER_METER );
        print( "player pos: x: " + player_sprite.getX() + "\ty: " + player_sprite.getY() );

        BodyDef ground_body_def = new BodyDef();
        ground_body_def.type = BodyDef.BodyType.StaticBody;
        //ground_body_def.position.set( 0.0f,10.0f );

        // JC
        // Need to convert between physics world and pixel world, as done in the Box2D demos (https://bit.ly/2Q0OWp7).
        // ground_body_def.position.set( ground_sprite.getX(),ground_sprite.getY() );
        ground_body_def.position.set( ( ground_sprite.getX() + ground_sprite.getWidth() / 2 ) / PIXELS_PER_METER,
                ( ground_sprite.getY() + ground_sprite.getHeight() / 2 ) / PIXELS_PER_METER );
        print( "ground pos: x: " + ground_sprite.getX() + "\ty: " + ground_sprite.getY() );

        // Create a body in the world using our definition
        player_body = main_world.createBody( player_bodyDef );
        ground_body = main_world.createBody( ground_body_def );


        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();

        // JC
        // shape.setAsBox( player_sprite.getWidth()/PIXELS_PER_METER/2,player_sprite.getHeight()/PIXELS_PER_METER/2 );
        shape.setAsBox( player_sprite.getWidth() / 2 / PIXELS_PER_METER, player_sprite.getHeight() / 2 / PIXELS_PER_METER );
        // print("player shape pos: x: "+ (player_sprite.getWidth()/PIXELS_PER_METER/2) +"\ty: "+ player_sprite.getHeight()/PIXELS_PER_METER/2);

        PolygonShape ground_shape = new PolygonShape();

        // JC
        // ground_shape.setAsBox( ground_sprite.getWidth(), ground_sprite.getHeight()*3);
        ground_shape.setAsBox( ( ground_sprite.getWidth() / 2 / PIXELS_PER_METER ),
                ( ground_sprite.getHeight() / 2 / PIXELS_PER_METER ) );
        // print("ground shape pos: x: "+ (ground_sprite.getWidth()/PIXELS_PER_METER) +"\ty: "+
        // 		ground_sprite.getHeight()/PIXELS_PER_METER);

        //specifying the density, restitution(bouncyness), others. for the player
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        // JC
        // fixtureDef.density = 1f;
        fixtureDef.density = 0.1f;

        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0.1f;

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = ground_shape;
        //groundFixture.density = 1f;
        groundFixture.friction = 1f;

        player_body.createFixture( fixtureDef );
        ground_body.createFixture( groundFixture );
        player_body.setFixedRotation( true );

        //bullet
        img = new Texture( "badlogic.jpg" );
        bullet_sprite = new Sprite( img );
        bullet_sprite.setSize( 20, 10 );
        bullet_sprite.setColor( Color.PINK );

        //create a body definition for the player
        //it needs to be dynamic, because the bullet/pencil is moving
        bullet_bodyDef = new BodyDef();
        bullet_bodyDef.type = BodyDef.BodyType.DynamicBody;

        // -negative: bodies go up		+ positives: bodies goes down
        // higher the number: the faster the body goes, vice versa.
        //player_body.setGravityScale( 5f );//sets gravity separate from the world's gravity

        Gdx.input.setInputProcessor( this );  // I guess required for implementing 'InputProcessor'

        camera = new OrthographicCamera( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

        CreateContactListener(); // for the main_world



        //set debug mode ON
        //DebugGraphics dd = new DebugGraphics();
        //dd.setDebugMode();
        //main_world.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE );
        // Shape is the only disposable of the lot, so get rid of it.
        shape.dispose();
        ground_shape.dispose();
    }

    public void create_bullet( )
    {
        bullet_bodyDef.position.set( ( player_sprite.getX() + player_sprite.getWidth()+50  ) / PIXELS_PER_METER,
                ( player_sprite.getY() + player_sprite.getHeight() / 2 ) / PIXELS_PER_METER );
        Body bullet_body = main_world.createBody( bullet_bodyDef );

        bullet_body.setLinearVelocity( 2,0 );
        //bullet_bodies.add(bullet_body);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( bullet_sprite.getWidth() /2/ PIXELS_PER_METER,
                bullet_sprite.getHeight()/2/PIXELS_PER_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0.1f;

        bullet_body.createFixture( fixtureDef );

        bullet_sprite.setPosition( bullet_body.getPosition().x, bullet_body.getPosition().y );
    }

    class Bullet{
        Body bullet_body;
        Sprite bullet_sprite;

        Bullet(World world)
        {

        }

    }


    @Override
    public void render () {
        camera.update();
        main_world.step(1f/60f, 6, 2);

        if(keypressed)
            movement();


        //updating player's motion

        // JC
        // Need to convert between physics world and pixel world, as done in the Box2D demos (https://bit.ly/2Q0OWp7).
        // player_sprite.setPosition( player_body.getPosition().x - (player_sprite.getWidth()/2),
        //		player_body.getPosition().y - (player_sprite.getHeight()/2));
        player_sprite.setPosition( player_body.getPosition().x * PIXELS_PER_METER - (player_sprite.getWidth()/2),
                player_body.getPosition().y * PIXELS_PER_METER - (player_sprite.getHeight()/2));

        player_sprite.setRotation((float)Math.toDegrees(player_body.getAngle()));




        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.draw(player_sprite, 100, 0);
        player_sprite.draw( batch );
        ground_sprite.draw( batch );
        block_sprite.draw(  batch );

		/*for(Body b: bullet_bodies)
        {

        }*/
        bullet_sprite.draw( batch );

        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
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

                /*
				if(bodyA != null && bodyB != null)
					print("\tObject A user data: "+ bodyA.toString()+"\tObject B user data: "+ bodyB.toString());
				else
					print(bodyA +"\t"+bodyB);
				*/

                //print("is bodyA the player or ground?: "+ (bodyA==player_body)+"\t"+ (bodyA== ground_body));
                //print("is bodyB the player or ground?: "+ (bodyB==player_body)+"\t"+ (bodyB== ground_body));

                if((bodyA == player_body && bodyB == ground_body) || (bodyB == player_body && bodyA == ground_body))
                {
                    print("\t\tColliding with ground.");
                    jumping= false;
                    onGround = true;
                }

                print("jumping: "+ jumping +"\tOnGround: "+ onGround);
                print("\tx: "+ player_body.getPosition().x+"\ty: "+ player_body.getPosition().y);

            }

            @Override
            public void endContact( Contact contact )
            {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Body bodyA = fixtureA.getBody();//.getUserData();
                Body bodyB = fixtureB.getBody();//.getUserData();

                if((bodyA == player_body && bodyB == ground_body) || (bodyB == player_body && bodyA == ground_body))
                {
                    jumping= true;
                }
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

    // INPUT PROCESSOR REQUIRED METHODS

    //"Called when a key was pressed"
    @Override
    public boolean keyDown(int keycode) {

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

        return false;
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


    // called when the user hits one or more keys
    void movement()
    {

        if(!allkeysPressed.isEmpty())
        {
            for(int i=0;i<3 && i< allkeysPressed.size(); i++)
            {
                //print(i+"\t"+allkeysPressed.size());
                int keycode = allkeysPressed.get( i );

                if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
                {
                    player_body.setLinearVelocity(speed, 0f);
                    //player_body.applyLinearImpulse( new Vector2( 0,550 ), player_body.getPosition(),true );
                    left_center_right = -1;
                }

                if(keycode == Input.Keys.LEFT|| keycode == Input.Keys.A)
                {
                    player_body.setLinearVelocity(-speed,0f);
                    left_center_right = 1;
                }

                //if(keycode == Input.Keys.SPACE)
                //player_body.setLinearVelocity(0f,200f);

            }
            //print("===============================");
        }



















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
