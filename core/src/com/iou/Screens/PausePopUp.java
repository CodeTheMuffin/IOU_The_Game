package com.iou.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iou.IOU;


public class PausePopUp {

    //don't really need these in the field, since it isn't being used outside of the constructor
    final IOU game;
    private Dialog pauseDialog;
    private Skin skin;
    private TextButton resume;
    private TextButton mainMenu;
    private PlayScreen the_playScreen;

    public Stage stage;

    public Stage getStage(){
        return stage;
    }

    public PausePopUp(Stage stage,IOU game, PlayScreen the_playScreen){
        this.game = game;
        this.stage = stage;
        this.the_playScreen = the_playScreen;

        skin = new Skin(Gdx.files.internal("Ui Skins/uiskin.json"));
        pauseDialog = new Dialog("Pause",skin);
        //stage = new Stage();
        resume = new TextButton("Resume", skin);
        mainMenu = new TextButton("Main Menu", skin);

        pauseDialog.text("Pause");
        pauseDialog.button(resume);
        pauseDialog.button(mainMenu);
        pauseDialog.show(stage);

        resume.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //stage.unfocusAll();
                //game.setScreen(screen);
                //stage.unfocus( pauseDialog );

                //based on this:
                //https://stackoverflow.com/questions/29144352/libgdx-alert-dialog/29171391
                //pauseDialog.hide();
                //pauseDialog.cancel();
                RemoveDialog();

                the_playScreen.resume();
            }
        });

        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                pauseDialog.remove();//to get rid of the dialog box
                //game.setScreen(new StartScreen(game));//refocus the screen to a new start screen
                game.setScreen( IOU.get_StartScreen( game ) );
            }
        });
        Gdx.input.setInputProcessor(stage);


    }

    //mainly to be called in PlayScreen when user clicks outside of screen, which pauses the game
    //this will get rid of the dialog box
    public void RemoveDialog()
    {
        //get removed from the stage
        pauseDialog.remove();//wouldn't this hide and cancel it, then delete? why bother with other methods?

        //typically you would refocus the stage, but that didn't work, but this did.
        Gdx.input.setInputProcessor(the_playScreen);//sets the input focused back on the screen
    }
}
