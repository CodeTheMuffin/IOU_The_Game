package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;
import static com.iou.IOU.print;

public class NextInstructionsScreen implements Screen {
    public final IOU game;
    private final Viewport viewport;
    private Stage stage;
    Label caffeineMechanic;
    Label prev;
    Label returnMenu;
    Label LevelComplete;
    private Sprite background;
    private Sprite caffeineBar;

    public NextInstructionsScreen(IOU game){
        this.game = game;
        viewport = new FitViewport(IOU.WIDTH,IOU.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);


        //below was in show()

        //find better background for instruction screens
        //background = new Sprite(new Texture("school-background.jpg"));
        //background.setPosition(0,0);
        //background.setSize(IOU.WIDTH, IOU.HEIGHT);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/Letters_for_Learners/Letters for Learners" +
                        ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;

        final BitmapFont font = generator.generateFont(parameter);

        caffeineBar = new Sprite(new Texture(Gdx.files.internal("JamesBar_small.png")));
        caffeineBar.setPosition(IOU.WIDTH/2 -150, IOU.HEIGHT/2+100);

        caffeineMechanic = new Label("The caffeine meter shows how much caffeine the player has left."+
                " To increase the\ncaffeine level you need to collect the coffee cups flying towards you." +
                " Once at \nmax caffeine level the player will have increase throwing and movement speed.", new Label.LabelStyle(font, Color.BLACK));
        caffeineMechanic.setPosition(IOU.WIDTH/8,IOU.HEIGHT/2);

        LevelComplete = new Label("Once a level is complete the total debit remaining will be calculated\n and the interest shall be applied for the next level."
                , new Label.LabelStyle(font, Color.BLACK));
        LevelComplete.setPosition(IOU.WIDTH/8, IOU.HEIGHT/2-100);

        prev = new Label("Previous", new Label.LabelStyle(font, Color.BLACK));
        prev.setPosition(IOU.WIDTH/10,50);
        prev.setTouchable(Touchable.enabled);
        prev.setBounds(IOU.WIDTH/10, 50, prev.getWidth(),prev.getHeight());
        prev.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //print("\tTrying to Return to Pre Menu");
                game.setScreen( IOU.get_InstructionScreen( game ) );
                //game.setScreen(new InstructionsScreen(game));
                //dispose();//since we are reusing this screen, don't dispose of anything!
            }
        });

        returnMenu = new Label("Return to Menu", new Label.LabelStyle(font,Color.BLACK));
        returnMenu.setPosition(IOU.WIDTH-200,50);
        returnMenu.setTouchable(Touchable.enabled);
        returnMenu.setBounds(IOU.WIDTH-200, 50, returnMenu.getWidth(),returnMenu.getHeight());
        returnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //print("\tTrying to Return to Main Menu");
                game.setScreen( IOU.get_StartScreen( game ) );//reuse the Start screen
                //game.setScreen(new StartScreen(game));
                //dispose();
            }
        });

        stage.addActor(caffeineMechanic);
        stage.addActor(LevelComplete);
        stage.addActor(prev);
        stage.addActor(returnMenu);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        // game.batch.draw(background,game.WIDTH,game.HEIGHT);
        //background.draw(game.batch);
        caffeineBar.draw(game.batch);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        prev.setTouchable(Touchable.disabled);
        returnMenu.setTouchable(Touchable.disabled);
    }
}
