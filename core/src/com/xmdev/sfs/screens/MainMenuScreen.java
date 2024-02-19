package com.xmdev.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.resources.GlobalVariables;

public class MainMenuScreen implements Screen {
    private final SFS game;
    private final Stage stage;
    private final TextureAtlas menuItemsAtlas;

    // image widgets
    private Image logoImage;
    private Image fighterDisplayBackgroundImage;
    private Image fighterDisplayImage;

    // button widgets
    private Button playGameButton;
    private Button settingsButton;
    private Button quitGameButton;
    private Button previousFighterButton;
    private Button nextFighterButton;

    // label widgets
    private Label fighterDisplayNameLabel;

    // fighter choice
    private int currentFighterChoiceIndex;

    public MainMenuScreen(final SFS game) {
        this.game = game;

        // set up the stage
        stage = new Stage();
        stage.setViewport(new ExtendViewport(
                GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH, 0, stage.getCamera()
        ));

        // get the menu items texture atlas from the asset manager
        menuItemsAtlas = game.assets.manager.get(Assets.MENU_ITEMS_ATLAS);

        // create the widgets
        createImages();
        createButtons();
        createLabels();
    }

    private void createImages() {
        // create the logo image
        logoImage = new Image(menuItemsAtlas.findRegion("Logo"));
        logoImage.setSize(
                logoImage.getWidth() * GlobalVariables.WORLD_SCALE,
                logoImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create the fighter display background image
        fighterDisplayBackgroundImage = new Image(menuItemsAtlas.findRegion("FighterDisplayBackground"));
        fighterDisplayBackgroundImage.setSize(
                fighterDisplayBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
                fighterDisplayBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create the fighter display image
        fighterDisplayImage = new Image(menuItemsAtlas.findRegion("FighterDisplay"));
        fighterDisplayImage.setSize(
                fighterDisplayImage.getWidth() * GlobalVariables.WORLD_SCALE,
                fighterDisplayImage.getHeight() * GlobalVariables.WORLD_SCALE
        );
    }

    private void createButtons() {
        // create the play game button
        Button.ButtonStyle playGameButtonStyle = new Button.ButtonStyle();
        playGameButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("PlayGameButton"));
        playGameButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("PlayGameButtonDown"));
        playGameButton = new Button(playGameButtonStyle);
        playGameButton.setSize(
                playGameButton.getWidth() * GlobalVariables.WORLD_SCALE,
                playGameButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create the settings button
        Button.ButtonStyle settingsButtonStyle = new Button.ButtonStyle();
        settingsButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("SettingsButton"));
        settingsButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("SettingsButtonDown"));
        settingsButton = new Button(settingsButtonStyle);
        settingsButton.setSize(
                settingsButton.getWidth() * GlobalVariables.WORLD_SCALE,
                settingsButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create the quit game button
        Button.ButtonStyle quitGameButtonStyle = new Button.ButtonStyle();
        quitGameButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("QuitGameButton"));
        quitGameButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("QuitGameButtonDown"));
        quitGameButton = new Button(quitGameButtonStyle);
        quitGameButton.setSize(
                quitGameButton.getWidth() * GlobalVariables.WORLD_SCALE,
                quitGameButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create the triangle button style
        Button.ButtonStyle triangleButtonStyle = new Button.ButtonStyle();
        triangleButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButton"));
        triangleButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButtonDown"));

        // create the previous fighter button
        previousFighterButton = new Button(triangleButtonStyle);
        previousFighterButton.setSize(
                previousFighterButton.getWidth() * GlobalVariables.WORLD_SCALE,
                previousFighterButton.getHeight() * GlobalVariables.WORLD_SCALE
        );
        previousFighterButton.setTransform(true);
        previousFighterButton.setOrigin(
                previousFighterButton.getWidth() / 2f,
                previousFighterButton.getHeight() / 2
        );
        previousFighterButton.setScaleX(-1);

        // create the next fighter button
        nextFighterButton = new Button(triangleButtonStyle);
        nextFighterButton.setSize(
                nextFighterButton.getWidth() * GlobalVariables.WORLD_SCALE,
                nextFighterButton.getHeight() * GlobalVariables.WORLD_SCALE
        );
    }

    private void createLabels() {
        // create the small font from the asset manager
        BitmapFont smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setUseIntegerPositions(false);

        // create label style
        Label.LabelStyle fighterDisplayNameLabelStyle = new Label.LabelStyle();
        fighterDisplayNameLabelStyle.font = smallFont;
        fighterDisplayNameLabelStyle.fontColor = Color.BLACK;

        // create the fighter display name label
        fighterDisplayNameLabel = new Label("", fighterDisplayNameLabelStyle);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
