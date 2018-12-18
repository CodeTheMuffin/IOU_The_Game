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
import com.iou.HUD.HUD;
import com.iou.IOU;
import com.iou.Player;

public class GameOverScreen implements Screen {
    final IOU game;
    private static Player player;
    private HUD hud;
    private final Viewport viewport;
    private int finalDebt = Player.totalDebtOwed;//hud.getDebtOwed();
    private Stage stage;
    Label gameOver;
    Label finalScore;
    Label returnMain;
    Label quit;
    Label scholarshipEarned;
    private Texture background;
    private boolean readyToQuit =false;

    public GameOverScreen(IOU game, Player player, boolean quitME){
        this.game = game;
        this.player = player;
        readyToQuit = quitME;
        viewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());
        stage= new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        player.calculateFinalGrade(player.getTotalGrade(),player.getAssignments_allowed());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/Letters_for_Learners/Letters for Learners.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        final BitmapFont font = generator.generateFont(parameter);


        returnMain = new Label("Return to Main Menu", new Label.LabelStyle(font, Color.BLACK));
        returnMain.setPosition(IOU.WIDTH/2-100,IOU.HEIGHT/2-50);
        returnMain.setTouchable(Touchable.enabled);
        returnMain.setBounds(IOU.WIDTH/2-100,IOU.HEIGHT/2-50,returnMain.getWidth(),returnMain.getHeight());
        returnMain.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen( IOU.get_StartScreen( game ) );
                //game.setScreen(new StartScreen(game));
                //dispose();
            }
        });

        String quitMsg = "Quit";

        if(!readyToQuit)
        {
            quitMsg="Resume to next Level.";
        }

        quit = new Label(quitMsg, new Label.LabelStyle(font, Color.BLACK));
        quit.setPosition((IOU.WIDTH-quit.getWidth())/2,IOU.HEIGHT/2-100);
        quit.setTouchable(Touchable.enabled);
        quit.setBounds((IOU.WIDTH-quit.getWidth())/2,IOU.HEIGHT/2-100,quit.getWidth(),quit.getHeight());
        quit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(readyToQuit)
                    Gdx.app.exit();
                else
                    game.setScreen( IOU.get_PlayScreen( game ) );
            }
        });

        if(readyToQuit)
        {
            gameOver = new Label("Game Over", new Label.LabelStyle(font,Color.BLACK));
            gameOver.setPosition(IOU.WIDTH/2-50, IOU.HEIGHT/2+100);
            stage.addActor(gameOver);
        }

        Integer total_debt = player.totalDebtOwed - player.getScholarshipMoney();

        finalScore = new Label("Total Debt amount is $ "+
                String.format("%,06d", total_debt),
                new Label.LabelStyle(font, Color.BLACK));
        finalScore.setPosition(IOU.WIDTH/2-100, IOU.HEIGHT/2+50);

        scholarshipEarned = new Label("Scholarship: $"+player.getScholarshipMoney(), new Label.LabelStyle(font, Color.BLACK));
        scholarshipEarned.setPosition(IOU.WIDTH/2-50, IOU.HEIGHT/2);

        stage.addActor(returnMain);
        stage.addActor(quit);

        stage.addActor(finalScore);
        stage.addActor(scholarshipEarned);

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

    }
}
