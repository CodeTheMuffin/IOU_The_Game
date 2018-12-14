package com.iou;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.iou.Screens.StartScreen;

public class IOU extends Game{
    public SpriteBatch batch;

    public static final int WIDTH = 1000;//800
    public static final int HEIGHT = 800;//480
    public static final float PIXELS_PER_METER = 100;//32

    public static AssetManager assetManager;

    public void create () {

        batch = new SpriteBatch();
        assetManager = new AssetManager();
        //assetManager.load("audio/music/pirateMusic.mp3", Music.class);
        //assetManager.load("audio/sounds/pirateJump.wav", Sound.class);
        assetManager.finishLoading();
        this.setScreen(new StartScreen(this));

    }

    public static final void print( String s )
    {
        System.out.println( s );
    }

    public void render(){
        super.render();
    }
}
