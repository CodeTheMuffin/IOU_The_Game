package com.iou.Screens;

import com.badlogic.gdx.Screen;
import com.iou.IOU;

public class PlayScreen implements Screen {
    final IOU game;

    public PlayScreen( IOU the_game)
    {
        game= the_game;
        print("Inside playscreen");
    }

    public static void print( String s )
    {
        System.out.println( s );
    }


    @Override
    public void render(float value)
    {}

    @Override
    public void show()
    {}

    @Override
    public void resize(int width, int height){}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

}
