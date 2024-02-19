package com.xmdev.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
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

        // create the tables
        createTables();
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

    private void createTables() {
        // remove line later
        stage.setDebugAll(true);

        // create the main table and add it to the stage
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setRound(false);
        stage.addActor(mainTable);

        // create the left side table
        Table leftSideTable = new Table();
        leftSideTable.setRound(false);

        // add the logo to the left side table
        leftSideTable.add(logoImage).size(logoImage.getWidth(), logoImage.getHeight());
        leftSideTable.row().padTop(1.5f);

        // create the fighter display table and set its background to the fighter display background image
        Table fighterDisplayTable = new Table();
        fighterDisplayTable.setRound(false);
        fighterDisplayTable.setBackground(fighterDisplayBackgroundImage.getDrawable());
        fighterDisplayTable.setSize(
                fighterDisplayBackgroundImage.getWidth(),
                fighterDisplayBackgroundImage.getHeight()
        );

        // create the fighter display inner table and add the previous and next fighter buttons and the
        // fighter display image to it
        Table fighterDisplayInnerTable = new Table();
        fighterDisplayInnerTable.setRound(false);
        fighterDisplayInnerTable.add(previousFighterButton).size(
                previousFighterButton.getWidth(),
                previousFighterButton.getHeight()
        );
        fighterDisplayInnerTable.add(fighterDisplayImage).size(
                fighterDisplayImage.getWidth(),
                fighterDisplayImage.getHeight()
        ).padLeft(0.5f).padRight(0.5f);
        fighterDisplayInnerTable.add(nextFighterButton).size(
                nextFighterButton.getWidth(),
                nextFighterButton.getHeight()
        );
        fighterDisplayInnerTable.pack();

        // fill in the fighter display table with an empty top (for alignment), the fighter display inner table
        // in the center, and the fighter display name label at the bottom
        fighterDisplayTable.add().height(
                fighterDisplayBackgroundImage.getHeight() / 2f - fighterDisplayImage.getHeight() / 2f - 0.5f
        );
        fighterDisplayTable.row();
        fighterDisplayTable.add(fighterDisplayInnerTable).size(
                fighterDisplayInnerTable.getWidth(),
                fighterDisplayInnerTable.getHeight()
        );
        fighterDisplayTable.row();
        fighterDisplayTable.add(fighterDisplayNameLabel).height(
                fighterDisplayBackgroundImage.getHeight() / 2f - fighterDisplayImage.getHeight() / 2f - 0.5f
        );

        // add the fighter display table to the left side table
        leftSideTable.add(fighterDisplayTable).size(
                fighterDisplayBackgroundImage.getWidth(),
                fighterDisplayBackgroundImage.getHeight()
        );

        // add the left side table to the main table
        mainTable.add(leftSideTable);

        // create the right side table
        Table rightSideTable = new Table();
        rightSideTable.setRound(false);

        // add the play game, settings, and quit game buttons to the right side table
        rightSideTable.add(playGameButton).size(
                playGameButton.getWidth(),
                playGameButton.getHeight()
        );
        rightSideTable.row().padTop(1f);
        rightSideTable.add(settingsButton).size(
                settingsButton.getWidth(),
                settingsButton.getHeight()
        );
        rightSideTable.row().padTop(1f);
        rightSideTable.add(quitGameButton).size(
                quitGameButton.getWidth(),
                quitGameButton.getHeight()
        );

        // add the right side table to the main table
        mainTable.add(rightSideTable).padLeft(2f);
    }

    @Override
    public void show() {
        // set the fighter display name label's text to the name of the player's fighter
        fighterDisplayNameLabel.setText(game.player.getName().toUpperCase());

        // set the fighter display image's color to the color of the player's fighter
        fighterDisplayImage.setColor(game.player.getColor());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GlobalVariables.BLUE_BACKGROUND);

        // tell the stage to do actions and draw itself
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // update the stage's viewport with the new screen size
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
