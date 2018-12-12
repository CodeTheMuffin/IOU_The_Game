package com.iou;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Assignments {
    //something to hold all the sprite animations
    public final int MAX_HP =100;
    public int hp= MAX_HP;

    public int x, y;//position
    public int vx = 10;//speed
    public static World world;
    public Body Assignment_Body;
    public FixtureDef Assignment_Fix_Def;
    public Texture Assignment_Texture;
    public Sprite Assignment_Sprite;

    public Assignments(World w)
    {
        if(world == null)
        {
            world =w;
        }
    }

    public void destroy()
    {
        world.destroyBody( Assignment_Body );
    }

    public void define()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    public float getAssignmnetBody_X()
    {return Assignment_Body.getPosition().x;}

    public float getAssignmnetBody_Y()
    {return Assignment_Body.getPosition().y;}
}
