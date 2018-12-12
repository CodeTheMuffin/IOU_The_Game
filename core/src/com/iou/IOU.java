package com.iou;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.iou.Screens.StartScreen;

public class IOU extends Game{
    public SpriteBatch batch;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 32;//pixels per meter

    public static AssetManager assetManager;

    public void create () {

        batch = new SpriteBatch();
        assetManager = new AssetManager();
        //assetManager.load("audio/music/pirateMusic.mp3", Music.class);
        //assetManager.load("audio/sounds/pirateJump.wav", Sound.class);
        assetManager.finishLoading();
        this.setScreen(new StartScreen(this));

    }

    public void render(){
        super.render();
    }
}
