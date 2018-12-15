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
    final IOU game;
    private Dialog pauseDialog;
    private Skin skin;
    private TextButton resume;
    private TextButton mainMenu;
    private PlayScreen screen;

    public Stage stage;

    public Stage getStage(){
        return stage;
    }

    public PausePopUp(Stage stage,IOU game){
        this.game = game;
        this.stage = stage;
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
                System.out.println("Resume button is clicked");
                pauseDialog.hide();
            }
        });

        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new StartScreen(game));
                System.out.println("Main Menu button clicked.");
            }
        });
        Gdx.input.setInputProcessor(stage);

    }
}
