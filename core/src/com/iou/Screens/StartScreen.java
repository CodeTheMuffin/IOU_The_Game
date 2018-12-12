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


public class StartScreen implements Screen {
    public final IOU game;
    private Viewport viewport;
    private Stage stage;
    Label lbl_example;
    Sprite img_delte_me;

    public StartScreen(IOU the_game)
    {
        game= the_game;
        viewport = new FitViewport( IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera() );
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
        lbl_example = new Label("This is an example", new Label.LabelStyle(font, Color.BLACK));

        lbl_example.setPosition(50,30);
        lbl_example.setTouchable( Touchable.enabled);
        lbl_example.setBounds(50,30,lbl_example.getWidth(),lbl_example.getHeight());

        //to set background color
        //based on: https://stackoverflow.com/questions/18166556/how-to-change-the-color-of-the-background-in-libgdx-labels
        Pixmap labelColor =new Pixmap((int)lbl_example.getWidth(), (int)lbl_example.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.WHITE);
        labelColor.fill();
        lbl_example.getStyle().background = new Image(new Texture(labelColor)).getDrawable();

        lbl_example.addListener(new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y) {
                print("Example label clicked!");
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        stage.addActor( lbl_example );
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
        lbl_example.setTouchable( Touchable.disabled);
    }
}
