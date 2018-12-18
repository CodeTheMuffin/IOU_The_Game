package com.iou.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
    private static Integer debtOwed = 10000;//starting at 10k
    private Float curr_GPA = 3.5f;//just for giggles

    public Label Debt, GPA;
    public static Label debt_amount, GPA_amount, timer_amount;
    //public Label gameTitle;
    public Label timer_msg;//first for displaying what numbers represent,
        // second is for actually displaying time
    public Label label_level, label_boost;
    Image back_boost_img, boost_amount;

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

        //DEBT
        Debt = new Label("Debt: ", new Label.LabelStyle(font, Color.BLACK));
        Debt.setPosition(IOU.WIDTH*.74f, IOU.HEIGHT-45);

        debt_amount = new Label("$ "+String.format("%,06d",debtOwed), new Label.LabelStyle(font, Color.BLACK));
        debt_amount.setPosition(Debt.getX()+Debt.getWidth(), Debt.getY());

        //GPA
        GPA = new Label("GPA: ", new Label.LabelStyle(font, Color.BLACK));
        GPA.setPosition(IOU.WIDTH*.74f, IOU.HEIGHT-80);

        GPA_amount = new Label(" "+String.format("%.1f",curr_GPA), new Label.LabelStyle(font, Color.BLACK));
        GPA_amount.setPosition(GPA.getX()+GPA.getWidth(), GPA.getY()-2);

        //gameTitle = new Label("IOU", new Label.LabelStyle(font, Color.BLACK));
        //gameTitle.setPosition(IOU.WIDTH/2-25,IOU.HEIGHT-45);

        // TIMER
        timer_msg = new Label( "Time Left", new Label.LabelStyle(font, Color.BLACK) );
        timer_msg.setPosition(IOU.WIDTH*.43f, IOU.HEIGHT- (timer_msg.getHeight()));

        timer_amount = new Label( "00.00 s", new Label.LabelStyle(font, Color.BLACK) );
        timer_amount.setPosition(IOU.WIDTH*.45f, timer_msg.getY()- timer_amount.getHeight());

        /*
        Drawable textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("JamesBar_small.png"))));
        Drawable textureKnob = new TextureRegionDrawable(new TextureRegion((new Texture(Gdx.files.internal("JamesKnobbar.png")))));
        //textureKnob.setMinWidth( 100f );
        //textureKnob.setLeftWidth( 100f );
        textureBar.setMinHeight( textureBar.getMinHeight());
        //barstyle = new ProgressBar.ProgressBarStyle(textureKnob,textureBar);
        */

        /*
        Slider test_slider = new Slider(0,100,1,false,skin);
        test_slider.setPosition( IOU.WIDTH/2,IOU.HEIGHT/2 );
        test_slider.setValue( 50f );*/

        /*
        barstyle = new ProgressBar.ProgressBarStyle();
        barstyle.background = textureBar;
        barstyle.knobBefore = textureKnob;
        barstyle.knob = textureKnob;

        //skin.newDrawable("white", Color.BLACK)
        //caffeineMeter = new ProgressBar(0,5,caffeineMeterProgress,false,barstyle);
        caffeineMeter = new ProgressBar(0,10,0.5f,false,barstyle);//this

        //caffeineMeter = new ProgressBar(0,10,0.5f,false,
               // new ProgressBar.ProgressBarStyle(skin.newDrawable("white",Color.DARK_GRAY),textureBar));
        caffeineMeter.setPosition(10, IOU.HEIGHT-caffeineMeter.getHeight()-10);
        caffeineMeter.setSize(IOU.WIDTH/4, 50);//200,5
        caffeineMeter.setValue( 5f );
        caffeineMeter.setRound( true );
        */

        label_boost = new Label( "ENERGY", new Label.LabelStyle( font, Color.BLACK ));
        label_boost.setPosition( 10,IOU.HEIGHT -label_boost.getHeight()-3 );

        back_boost_img= new Image(new Texture( Gdx.files.internal( "blue_background_bar.png" ) ));
        back_boost_img.setPosition( label_boost.getX() + label_boost.getWidth()+10,
                label_boost.getY() );
        back_boost_img.setSize( 200,50 );

        boost_amount= new Image(new Texture( Gdx.files.internal( "blue_bar.png" ) ));
        boost_amount.setPosition( back_boost_img.getX()+1, back_boost_img.getY()+5 );
        boost_amount.setSize( 195,42 );// 0%: width of 10,   100%: width of 195

        label_level = new Label( "LVL: "+ the_playScreen.level, new Label.LabelStyle( font, Color.BLACK ));
        label_level.setPosition( 10,label_boost.getY()-label_level.getHeight()-5 );

        stage.addActor(Debt);
        stage.addActor(debt_amount);

        stage.addActor( timer_msg );
        stage.addActor( timer_amount );

        stage.addActor( GPA );
        stage.addActor( GPA_amount );

        //stage.addActor(gameTitle);
        //stage.addActor(caffeineMeter);

        stage.addActor( label_boost );
        stage.addActor( boost_amount );
        stage.addActor( back_boost_img );

        stage.addActor( label_level );

        //stage.addActor( test_slider );
    }

    public static Integer getDebtOwed(){
        return debtOwed;
    }
    public static void setDebtOwed(int newDebtOwed){
        HUD.debtOwed = newDebtOwed;
    }
    public static void updateDebtOwed(int newDebtOwned){
        debt_amount.setText("$ "+String.format("%,06d", newDebtOwned));
    }

    public void updateGPA(int total_grades, int total_collected)
    {
        if(total_collected == 0)
        {
            curr_GPA = 0.0f;
        }
        else
        {
            curr_GPA = (float)total_grades/(float)total_collected;
        }

        GPA_amount.setText(" "+String.format("%.1f",curr_GPA) );
    }

    public void updateTimer(float seconds)
    {
        String buffer= "";

        if(seconds<10)
        {buffer = " ";}

        timer_amount.setText( buffer+ String.format( "%.1f", (Float)seconds ));
    }


    public void updateCaffeineMeter(){
        caffeineMeterProgress += 1;
        //float previousKnobPosX = caffeineMeter.getX();
        //float previousKnobPosY = caffeineMeter.getY();
        //barstyle.knob.draw(game.batch, caffeineMeter.getX()+5,caffeineMeter.getY()+5,20, 10);
    }

    //boost should be a value between 0-100
    public void updateBoostAmount(float boost)
    {
        boost/= 100;
        int min_width = 10;//of the image. This represents 0% boost
        int max_width = 195;//of the image. This is 100% boost

        int value= (int)((max_width- min_width)*boost)+min_width;
        boost_amount.setSize( value,42 );
    }

    public void update_lvl_label()
    {
        label_level.setText( "LVL: "+the_playScreen.level);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
