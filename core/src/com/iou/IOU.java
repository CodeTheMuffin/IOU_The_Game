package com.iou;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.iou.Screens.InstructionsScreen;
import com.iou.Screens.NextInstructionsScreen;
import com.iou.Screens.PlayScreen;
import com.iou.Screens.StartScreen;

public class IOU extends Game{
    public SpriteBatch batch;

    public static final int WIDTH = 1000;//1000//1600
    public static final int HEIGHT = 800;//800
    public static final float PIXELS_PER_METER = 100;//32

    private static StartScreen main_startScreen;
    private static PlayScreen main_playScreen;
    private static InstructionsScreen main_instruction;
    private static NextInstructionsScreen main_next_instruction;


    public static AssetManager assetManager;

    public void create () {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        //assetManager.load("audio/music/pirateMusic.mp3", Music.class);
        //assetManager.load("audio/sounds/pirateJump.wav", Sound.class);
        assetManager.finishLoading();
        set_StartScreen( new StartScreen(this) );

        this.setScreen(main_startScreen);
    }

    public static final void print( String s )
    {
        System.out.println( s );
    }

    public void render(){
        super.render();
    }

    public static void set_StartScreen(StartScreen ss)
    {
        main_startScreen = ss;
    }

    public static void set_PlayScreen(PlayScreen ps)
    {
        main_playScreen = ps;
    }

    public static StartScreen get_StartScreen(IOU iou)
    {
        if(main_startScreen == null)
        {
            main_startScreen = new StartScreen( iou );
        }

        return main_startScreen;
    }

    public static PlayScreen get_PlayScreen(IOU iou)
    {
        if(main_playScreen == null)
        {
            main_playScreen = new PlayScreen( iou );
        }

        return main_playScreen;
    }

    public static InstructionsScreen get_InstructionScreen( IOU iou)
    {
        if(main_instruction == null)
        {
            main_instruction = new InstructionsScreen( iou );
        }

        return main_instruction;
    }

    public static NextInstructionsScreen get_NextInstructionScreen( IOU iou)
    {
        if(main_next_instruction == null)
        {
            main_next_instruction = new NextInstructionsScreen( iou );
        }

        return main_next_instruction;
    }

    // yes/true: it is created      no/false: not created
    public static boolean isPlayScreenCreated()
    {
        return main_playScreen != null;
    }

    public static void areEmpty()
    {
        print("                         Empty?");
        print("main_startScreen:        "+ (main_startScreen==null));
        print("main_playScreen:         "+ (main_playScreen==null));
        print("main_instruction:        "+ (main_instruction==null));
        print("main_next_instruction:   "+ (main_next_instruction==null));
        print("\n\n");
    }

}
