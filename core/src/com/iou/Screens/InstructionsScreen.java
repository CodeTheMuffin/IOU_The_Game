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


public class InstructionsScreen implements Screen {
    final IOU game;
    private final Viewport viewport;
    private Stage stage;
    Label greetings;
    Label intro;
    Label returnMain;
    Label next;
    Label jumpControls;
    Label attackControls;
    Label collectProjects;
    Label pauseKey;
    private Sprite background;
    private Sprite spaceBar;
    private Sprite eKey;

    public InstructionsScreen(IOU game){
        this.game = game;
        viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //below was in show()
        //find better background for instruction screens
        //background = new Sprite(new Texture("school-background.jpg"));
        //background.setPosition(0,0);
        //background.setSize(IOU.WIDTH, IOU.HEIGHT);

        //enter key sprite
        eKey = new Sprite(new Texture("enterKey.png"));
        eKey.setPosition(IOU.WIDTH/4,IOU.HEIGHT/2-75);
        eKey.setSize(60,50);

        //spacebar sprite
        spaceBar = new Sprite(new Texture("spaceBar.png"));
        spaceBar.setPosition(IOU.WIDTH/5, IOU.HEIGHT/2);
        spaceBar.setSize(125, 50);

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
                game.setScreen( IOU.get_StartScreen( game ) );
                //game.setScreen( IOU.main_startScreen );
                //game.setScreen(new StartScreen(game));
                //dispose();
            }
        });

        next = new Label("Next", new Label.LabelStyle(font, Color.BLACK));
        next.setPosition(IOU.WIDTH-200, 50);
        next.setTouchable((Touchable.enabled));
        next.setBounds(IOU.WIDTH-200,50, next.getWidth(),next.getHeight());
        next.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen( IOU.get_NextInstructionScreen( game ) );
                //game.setScreen(new NextInstructionsScreen(game));
                //dispose();//since we are reusing this screen, don't dispose of anything!
            }
        });

        greetings = new Label("Hello and welcome to IOU.", new Label.LabelStyle(font, Color.BLACK));
        greetings.setPosition(IOU.WIDTH/2-150,IOU.HEIGHT/2+ 250);

        intro = new Label("The goal of the game is experience the college lifestyle of debt and schoolwork\n" +
                "In order to succeed you must finish the projects thrown at you as best you can.\n When enough projects are complete then the gpa is calculated " +
                "and any eligible \nscholarships will be awarded to the player's debt.", new Label.LabelStyle(font, Color.BLACK));
        intro.setPosition(IOU.WIDTH/10,IOU.HEIGHT/2+100);

        jumpControls = new Label("Press  space bar to jump.", new Label.LabelStyle(font, Color.BLACK));
        jumpControls.setPosition(IOU.WIDTH/3,IOU.HEIGHT/2);

        attackControls = new Label("Press E or Enter to throw pencils to\n increase project grade.", new Label.LabelStyle(font, Color.BLACK));
        attackControls.setPosition(IOU.WIDTH/3,IOU.HEIGHT/2-75);

        collectProjects = new Label("Collect the projects by touching them with the\n player", new Label.LabelStyle(font, Color.BLACK));
        collectProjects.setPosition(IOU.WIDTH/3,IOU.HEIGHT/2-150);

        pauseKey = new Label("Press Esc to pause the game.", new Label.LabelStyle(font, Color.BLACK));
        pauseKey.setPosition(IOU.WIDTH/3, IOU.HEIGHT/2-200);

        stage.addActor(pauseKey);
        stage.addActor(greetings);
        stage.addActor(intro);
        stage.addActor(attackControls);
        stage.addActor(jumpControls);
        stage.addActor(returnMain);
        stage.addActor(collectProjects);
        stage.addActor(next);
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
        eKey.draw(game.batch);
        spaceBar.draw(game.batch);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        returnMain.setTouchable(Touchable.disabled);
        next.setTouchable(Touchable.disabled);
    }
}

