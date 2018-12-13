package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;


public class InstructionsScreen implements Screen {
    final IOU game;
    private final Viewport viewport;
    private Stage stage;
    Label returnMain;
    Label next;
    Label jumpControls;
    Label attackControls;
    Label collectProjects;
    private Texture background;

    public InstructionsScreen(IOU game){
        this.game = game;
        viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        background = new Texture("badlogic.jpg");


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/Letters_for_Learners/Letters for Learners.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        final BitmapFont font = generator.generateFont(parameter);
        //generator.dispose();



        returnMain = new Label("Return to Main Menu", new Label.LabelStyle(font, Color.BLACK));
        returnMain.setPosition(IOU.WIDTH/10,50);
        returnMain.setTouchable(Touchable.enabled);
        returnMain.setBounds(IOU.WIDTH/10,50,returnMain.getWidth(),returnMain.getHeight());
        returnMain.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new StartScreen(game));
                dispose();
            }
        });

        next = new Label("Next", new Label.LabelStyle(font, Color.BLACK));
        next.setPosition(IOU.WIDTH-200, 50);
        next.setTouchable((Touchable.enabled));
        next.setBounds(IOU.WIDTH-200,50, next.getWidth(),next.getHeight());
        next.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new NextInstructionsScreen(game));
                dispose();
            }
        });

        //intro = new Label("Hello and welcome to IOU.\n The goal of the game is to collect projects");

        jumpControls = new Label("Press  space bar to jump.", new Label.LabelStyle(font, Color.BLACK));
        jumpControls.setPosition(300,350);

        attackControls = new Label("Press E or Enter to throw pencils to\n increase project grade.", new Label.LabelStyle(font, Color.BLACK));
        attackControls.setPosition(300,275);

        collectProjects = new Label("Collect the projects by touching them with the\n player", new Label.LabelStyle(font, Color.BLACK));
        collectProjects.setPosition(300,225);



        stage.addActor(attackControls);
        stage.addActor(jumpControls);
        stage.addActor(returnMain);
        stage.addActor(collectProjects);
        stage.addActor(next);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        // game.batch.draw(background,game.WIDTH,game.HEIGHT);
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
        returnMain.setTouchable(Touchable.disabled);
        next.setTouchable(Touchable.disabled);
    }
}

