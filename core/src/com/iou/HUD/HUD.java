package com.iou.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;
import com.iou.Screens.PlayScreen;



public class HUD implements Disposable {

    public Stage stage;
    public IOU game;

    private Viewport hudViewport;
    private PlayScreen the_playScreen;
    private static Integer debtOwed = 10000;

    public Label Debt;
    public static Label debtAmount;
    public Label gameTitle;

    //need to find texture for the bar before finish testing
    public float caffeineMeterProgress =1;
    public ProgressBar caffeineMeter;
    public ProgressBar.ProgressBarStyle barstyle;


    public HUD(SpriteBatch spriteBatch, PlayScreen the_playScreen){
        this.the_playScreen = the_playScreen;
        //debtOwed = 10000;

        Skin skin = new Skin(Gdx.files.internal("Ui Skins/uiskin.json"));

        hudViewport = new FitViewport(IOU.WIDTH, IOU.HEIGHT, new OrthographicCamera());

        stage = new Stage(hudViewport);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
                ("fonts/Letters_for_Learners/Letters for Learners.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 45;
        final BitmapFont font = generator.generateFont(parameter);

        Debt = new Label("Current Debt:", new Label.LabelStyle(font, Color.BLACK));
        Debt.setPosition(IOU.WIDTH/2+150, IOU.HEIGHT-45);

        debtAmount = new Label(String.format("%06d",debtOwed), new Label.LabelStyle(font, Color.BLACK));
        debtAmount.setPosition(Debt.getX()+200, Debt.getY());

        gameTitle = new Label("IOU", new Label.LabelStyle(font, Color.BLACK));
        gameTitle.setPosition(IOU.WIDTH/2-25,IOU.HEIGHT-45);

        Drawable textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("JamesBar_small.png"))));
        Drawable textureKnob = new TextureRegionDrawable(new TextureRegion((new Texture(Gdx.files.internal("JamesKnobbar.png")))));
        //barstyle = new ProgressBar.ProgressBarStyle(textureKnob,textureBar);

        barstyle = new ProgressBar.ProgressBarStyle();
        barstyle.background = textureBar;
        barstyle.knob = textureKnob;

        //skin.newDrawable("white", Color.BLACK)
        caffeineMeter = new ProgressBar(0,5,caffeineMeterProgress,false,barstyle);
        caffeineMeter.setPosition(10, IOU.HEIGHT-43);
        caffeineMeter.setSize(200, 5);

        stage.addActor(Debt);
        stage.addActor(debtAmount);
        stage.addActor(gameTitle);
        stage.addActor(caffeineMeter);

    }
    public static Integer getDebtOwed(){
        return debtOwed;
    }
    public static void setDebtOwed(int newDebtOwed){
        HUD.debtOwed = newDebtOwed;
    }
    public static void updateDebtOwed(int newDebtOwned){
        debtAmount.setText(String.format("%06d", newDebtOwned));
    }
    public void updateCaffeineMeter(){
        caffeineMeterProgress += 1;
        //float previousKnobPosX = caffeineMeter.getX();
        //float previousKnobPosY = caffeineMeter.getY();
        //barstyle.knob.draw(game.batch, caffeineMeter.getX()+5,caffeineMeter.getY()+5,20, 10);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
