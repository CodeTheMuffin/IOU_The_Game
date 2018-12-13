package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;
import com.iou.The_Main_Game_Class;


public class StartScreen implements Screen {
    public final IOU game;
    private Viewport viewport;
    private Stage stage;
    Label lbl_example;
    Label startLabel;
    Label instructionsLabel;
    Label quitLabel;
    Sprite img_delte_me;
    OrthographicCamera camera;

    public StartScreen(IOU the_game)
    {
        game= the_game;
        //Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        camera = new OrthographicCamera(IOU.WIDTH, IOU.HEIGHT);
        viewport = new FitViewport( IOU.WIDTH, IOU.HEIGHT, camera );
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor( stage );
        img_delte_me = new Sprite(new Texture( Gdx.files.internal( "badlogic.jpg" ) ));
        img_delte_me.setPosition( 50,30 );
    }

    @Override
    public void render(float value)
    {

        Gdx.gl.glClearColor(255,255,255,1);//white
        //Gdx.gl.glClearColor(.5f,.5f,.5f,0.1f);//black
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        game.batch.begin();
        //game.batch.draw(imgNinja,10,10,300,470);
        //img_delte_me.draw( game.batch );

        //gam
        game.batch.end();
    }

    public static void print( String s )
    {
        System.out.println( s );
    }


    @Override
    public void show()
    {
        print("is this working?");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/Letters_for_Learners/Letters for Learners" +
                ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;

        final BitmapFont font = generator.generateFont(parameter);


        startLabel = new Label("Start Game", new Label.LabelStyle(font, Color.BLACK));
        startLabel.setPosition(325,300);
        startLabel.setTouchable(Touchable.enabled);
        startLabel.setBounds(325,300,startLabel.getWidth(),startLabel.getHeight());
        startLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y ){
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        instructionsLabel = new Label("Instructions", new Label.LabelStyle(font, Color.BLACK));
        instructionsLabel.setPosition(315,250);
        instructionsLabel.setTouchable(Touchable.enabled);
        instructionsLabel.setBounds(315 ,250, instructionsLabel.getWidth(),instructionsLabel.getHeight());
        instructionsLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new InstructionsScreen(game));
                dispose();
            }
        });

        quitLabel = new Label("Quit", new Label.LabelStyle(font, Color.BLACK));
        quitLabel.setPosition(383,200);
        quitLabel.setTouchable(Touchable.enabled);
        quitLabel.setBounds(383,200,quitLabel.getWidth(),quitLabel.getHeight());
        quitLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        stage.addActor(startLabel);
        stage.addActor(instructionsLabel);
        stage.addActor(quitLabel);
        print("this is the end");
    }

    @Override
    public void resize(int width, int height){}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        startLabel.setTouchable(Touchable.disabled);
        instructionsLabel.setTouchable(Touchable.disabled);
        quitLabel.setTouchable(Touchable.disabled);
    }
}
