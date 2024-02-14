package com.xmdev.sfs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.objects.Fighter;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.resources.GlobalVariables;

import java.util.Locale;

public class GameScreen implements Screen, InputProcessor {

    private final SFS game;
    private final ExtendViewport viewport;

    // game
    private enum GameState {
        RUNNING, PAUSED, GAME_OVER
    }
    private GameState gameState;
    private GlobalVariables.Difficulty difficulty = GlobalVariables.Difficulty.EASY;

    // rounds
    private enum RoundState {
        STARTING, IN_PROGRESS, ENDING
    }
    private RoundState roundState;
    private float roundStateTime;
    private static final float START_ROUND_DELAY = 2f;
    private static final float END_ROUND_DELAY = 2f;
    private int currentRound;
    private static final int MAX_ROUNDS = 3;
    private int roundsWon = 0, roundsLost = 0;
    private static final float MAX_ROUND_TIME = 99.99f;
    private float roundTimer = MAX_ROUND_TIME;
    private static final float CRITICAL_ROUND_TIME = 10f;
    private static final Color CRITICAL_ROUND_TIME_COLOR = Color.RED;

    // fonts
    private BitmapFont smallFont, mediumFont, largeFont;
    private static final Color DEFAULT_FONT_COLOR = Color.WHITE;

    // HUD
    private static final Color HEALTH_BAR_COLOR = Color.RED;
    private static final Color HEALTH_BAR_BACKGROUND_COLOR = GlobalVariables.GOLD;

    // background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;
    private static final float RING_MIN_X = 7f;
    private static final float RING_MAX_X = 60f;
    private static final float RING_MIN_Y = 4f;
    private static final float RING_MAX_Y = 22f;
    private static final float RING_SLOPE = 3.16f;

    // fighters
    private static final float PLAYER_START_POSITION_X = 16f;
    private static final float OPPONENT_START_POSITION_X = 51f;
    private static final float FIGHTER_START_POSITION_Y = 15f;
    private static final float FIGHTER_CONTACT_DISTANCE_X = 7.5f;
    private static final float FIGHTER_CONTACT_DISTANCE_Y = 1.5f;

    // buttons
    private Sprite playAgainButtonSprite;
    private Sprite mainMenuButtonSprite;

    public GameScreen(SFS game) {
        this.game = game;

        // set up the viewport
        viewport = new ExtendViewport(
                GlobalVariables.WORLD_WIDTH,
                GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH,
                0
        );

        // create the game area
        createGameArea();

        // setup the fonts
        setUpFonts();

        // create buttons
        createButtons();
    }

    private void createGameArea() {
        // get ring textures from asset manager
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    private void setUpFonts() {
        smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setColor(DEFAULT_FONT_COLOR);
        smallFont.setUseIntegerPositions(false);

        mediumFont = game.assets.manager.get(Assets.MEDIUM_FONT);
        mediumFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        mediumFont.setColor(DEFAULT_FONT_COLOR);
        mediumFont.setUseIntegerPositions(false);

        largeFont = game.assets.manager.get(Assets.LARGE_FONT);
        largeFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        largeFont.setColor(DEFAULT_FONT_COLOR);
        largeFont.setUseIntegerPositions(false);
    }

    private void createButtons() {
        // get the gameplay button texture atlas from the asset manager
        TextureAtlas buttonTextureAtlas = game.assets.manager.get(Assets.GAMEPLAY_BUTTONS_ATLAS);

        // create the play again button sprite
        playAgainButtonSprite = new Sprite(buttonTextureAtlas.findRegion("PlayAgainButton"));
        playAgainButtonSprite.setSize(
                playAgainButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE,
                playAgainButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // create main menu button sprite
        mainMenuButtonSprite = new Sprite(buttonTextureAtlas.findRegion("MainMenuButton"));
        mainMenuButtonSprite.setSize(
                mainMenuButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE,
                mainMenuButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE
        );
    }

    @Override
    public void show() {
        // process user input
        Gdx.input.setInputProcessor(this);

        // start the game
        startGame();
    }

    private void startGame() {
        gameState = GameState.RUNNING;
        roundsWon = roundsLost = 0;

        // start the first round
        currentRound = 1;
        startRound();
    }

    private void startRound() {
        // get the fighters ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONENT_START_POSITION_X, FIGHTER_START_POSITION_Y);

        // start the round
        roundState = RoundState.STARTING;
        roundStateTime = 0f;
        roundTimer = MAX_ROUND_TIME;
    }

    private void endRound() {
        // end the round
        roundState = RoundState.ENDING;
        roundStateTime = 0f;
    }

    private void winRound() {
        // players wins the round and opponent loses
        game.player.win();
        game.opponent.lose();
        roundsWon++;

        // end the round
        endRound();
    }

    private void loseRound() {
        // player loses the round and opponent wins
        game.player.lose();
        game.opponent.win();
        roundsLost++;

        // end the round
        endRound();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // update the game
        update(delta);

        // set the sprite batch and the shape renderer to use the viewport camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        // set the sprite batch to use the camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        game.batch.begin();

        // draw the background
        game.batch.draw(
                backgroundTexture, 0, 0,
                backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
                backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // draw the fighters
        renderFighters();

        // draw the front ropes
        game.batch.draw(
                frontRopesTexture, 0, 0,
                frontRopesTexture.getWidth() * GlobalVariables.WORLD_SCALE,
                frontRopesTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // draw the HUD
        renderHUD();

        // if the game is over draw the game over overlay
        if (gameState == GameState.GAME_OVER) {
            renderGameOverOverlay();
        } else {
            // if round is starting, draw the start around the text
            if (roundState == RoundState.STARTING) {
                renderStartRoundText();
            }
        }

        // end drawing
        game.batch.end();
    }

    private void renderFighters() {
        // use the y coordinates of the fighter's position to determine which fighter to draw first
        if (game.player.getPosition().y > game.opponent.getPosition().y) {
            // draw player
            game.player.render(game.batch);

            // draw opponent
            game.opponent.render(game.batch);
        } else {
            // draw opponent
            game.opponent.render(game.batch);

            // draw player
            game.player.render(game.batch);
        }
    }

    private void renderHUD() {
        float HUDMargin = 1f;

        // draw the rounds won to lost ratio
        smallFont.draw(
                game.batch,
                "WINS: " + roundsWon + " - " + roundsLost,
                HUDMargin, viewport.getWorldHeight() - HUDMargin
        );

        // draw the difficulty setting
        String text = "DIFFICULTY: ";
        switch (difficulty) {
            case EASY:
                text += "EASY";
                break;
            case MEDIUM:
                text += "MEDIUM";
                break;
            default:
                text += "HARD";
        }

        smallFont.draw(
                game.batch,
                text,
                viewport.getWorldWidth() - HUDMargin,
                viewport.getWorldHeight() - HUDMargin,
                0,
                Align.right,
                false
        );

        // setup layout sizes and positioning
        float healthBarPadding = 0.5f;
        float healthBarHeight = smallFont.getCapHeight() + healthBarPadding * 2f;
        float healthBarMaxWidth = 32f;
        float healthBarBackgroundPadding = 0.2f;
        float healthBarBackgroundHeight = healthBarHeight + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundWidth = healthBarMaxWidth + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundMarginTop = 0.8f;
        float healthBarBackgroundPositionY = viewport.getWorldHeight() - HUDMargin -
                smallFont.getCapHeight() - healthBarBackgroundMarginTop - healthBarBackgroundHeight;
        float healthBarPositionY = healthBarBackgroundPositionY + healthBarBackgroundPadding;
        float fighterNamePositionY = healthBarPositionY + healthBarHeight - healthBarPadding;

        game.batch.end();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw the fighter health bar background rectangles
        game.shapeRenderer.setColor(HEALTH_BAR_BACKGROUND_COLOR);
        game.shapeRenderer.rect(
                HUDMargin, healthBarBackgroundPositionY,
                healthBarBackgroundWidth, healthBarBackgroundHeight
        );
        game.shapeRenderer.rect(
                viewport.getWorldWidth() - HUDMargin - healthBarBackgroundWidth,
                healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight
        );

        // draw the fighter's red health bar rectangles
        game.shapeRenderer.setColor(HEALTH_BAR_COLOR);
        float healthBarWidth = healthBarMaxWidth * game.player.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(
                HUDMargin + healthBarBackgroundPadding, healthBarPositionY,
                healthBarWidth, healthBarHeight
        );
        healthBarWidth = healthBarMaxWidth * game.opponent.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(
                viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarWidth,
                healthBarPositionY, healthBarWidth, healthBarHeight
        );

        game.shapeRenderer.end();
        game.batch.begin();

        // draw the fighter's names
        smallFont.draw(
                game.batch, game.player.getName(),
                HUDMargin + healthBarBackgroundPadding + healthBarPadding,
                fighterNamePositionY
        );
        smallFont.draw(
                game.batch, game.opponent.getName(),
                viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarPadding,
                fighterNamePositionY,
                0, Align.right, false
        );

        // draw the round timer
        // check if round timer dropped below critical round time, if so, change the color of the text
        if (roundTimer < CRITICAL_ROUND_TIME) {
            mediumFont.setColor(CRITICAL_ROUND_TIME_COLOR);
        }
        mediumFont.draw(
                game.batch, String.format(Locale.getDefault(), "%02d", (int) roundTimer),
                viewport.getWorldWidth() / 2f - mediumFont.getSpaceXadvance() * 2.3f,
                viewport.getWorldHeight() - HUDMargin
        );
        // set medium font to default color to prevent subsequent color change after color changes
        mediumFont.setColor(DEFAULT_FONT_COLOR);
    }

    // add text at the start of the round
    private void renderStartRoundText() {
        String text;
        // check if round state time < half of start of delay, if so, display round number text
        if (roundStateTime < START_ROUND_DELAY * 0.5f) {
            text = "ROUND " + currentRound;
        } else {
            // if round state time > half of start of delay, display fight text
            text = "FIGHT!";
            mediumFont.setColor(Color.RED);
        }

        mediumFont.draw(
                game.batch, text,
                viewport.getWorldWidth() / 2f,
                viewport.getWorldHeight() / 2f,
                0, Align.center, false
        );

        mediumFont.setColor(DEFAULT_FONT_COLOR);
    }

    private void renderGameOverOverlay() {
        // cover game area with a partially transparent black overlay
        game.batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 0, 0.7f);
        game.shapeRenderer.rect(
                0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight()
        );
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.batch.begin();

        // calculate the layout dimensions
        float textMarginBottom = 2f; // spacing between text and button below it
        float buttonSpacing = 0.5f; // spacing between buttons

        // calculate the height of the layout
        float layoutHeight = largeFont.getCapHeight() + textMarginBottom +
                playAgainButtonSprite.getHeight() + buttonSpacing +
                mainMenuButtonSprite.getHeight();

        // calculate y position of the layout
        float layoutPositionY = viewport.getWorldHeight() / 2f - layoutHeight / 2f;

        // draw and set positions of layout bottom up
        mainMenuButtonSprite.setPosition(
                viewport.getWorldWidth() / 2f - mainMenuButtonSprite.getWidth() / 2f,
                layoutPositionY
        );
        mainMenuButtonSprite.draw(game.batch);
        playAgainButtonSprite.setPosition(
                viewport.getWorldWidth() / 2f - playAgainButtonSprite.getWidth() / 2f,
                layoutPositionY + mainMenuButtonSprite.getHeight() + buttonSpacing
        );
        playAgainButtonSprite.draw(game.batch);

        // draw the text
        String text = roundsWon > roundsLost ? "YOU WIN!" : "YOU LOSE!";
        largeFont.draw(
                game.batch, text,
                viewport.getWorldWidth() / 2f,
                playAgainButtonSprite.getY() + playAgainButtonSprite.getHeight() +
                        textMarginBottom + largeFont.getCapHeight(),
                0, Align.center, false
        );
    }

    private void update(float deltaTime) {
        // update the round state
        if (roundState == RoundState.STARTING && roundStateTime >= START_ROUND_DELAY) {
            // if the start of the round delay has been reached, start the fight
            roundState = RoundState.IN_PROGRESS;
            roundStateTime = 0f;
        } else if (roundState == RoundState.ENDING && roundStateTime >= END_ROUND_DELAY) {
            // if the end round delay has been reached and player has won or lost more than half of the max rounds,
            // end the game, otherwise, start the next round
            if (roundsWon > MAX_ROUNDS / 2 || roundsLost > MAX_ROUNDS / 2) {
                gameState = GameState.GAME_OVER;
            } else {
                currentRound++;
                startRound();
            }
        } else {
            // implement the rounds state time by delta time
            roundStateTime += deltaTime;
        }

        game.player.update(deltaTime);
        game.opponent.update(deltaTime);

        // make sure fighters are facing each other
        if (game.player.getPosition().x <= game.opponent.getPosition().x) {
            game.player.faceRight();
            game.opponent.faceLeft();
        } else {
            game.player.faceLeft();
            game.opponent.faceRight();
        }

        // keep the fighters within the bounds of the ring
        keepWithinRingBounds(game.player.getPosition());
        keepWithinRingBounds(game.opponent.getPosition());

        // check if round state is in progress, if so decrease round timer by delta time
        if (roundState == RoundState.IN_PROGRESS) {
            // if round is in progress, decrease the round timer by delta time
            roundTimer -= deltaTime;

            // check if round timer is finished, if so, neither of the fighters has won yet
            // fighter with most life wins and other loses
            // if both fighters have the same life, the player wins
            if (roundTimer <= 0) {
                if (game.player.getLife() >= game.opponent.getLife()) {
                    winRound();
                } else {
                    loseRound();
                }
            }

            // check if the fighters are within contact distance
            if (areWithingContactDistance(game.player.getPosition(), game.opponent.getPosition())) {
                // check if the fighters are attacking
                if (game.player.isAttackActive()) {
                    // if the fighters are within contact distance and player is actively attacking, opponent hit
                    game.opponent.getHit(Fighter.HIT_STRENGTH);

                    // deactivate player's attack
                    game.player.makeContact();

                    // check if opponent has lost
                    if (game.opponent.hasLost()) {
                        // if opponent has lost, player wins the round
                        winRound();
                    }
                }
            }
        }
    }

    private void keepWithinRingBounds(Vector2 position) {
        if (position.y < RING_MIN_Y) {
            position.y = RING_MIN_Y;
        } else if (position.y > RING_MAX_Y) {
            position.y = RING_MAX_Y;
        }

        if (position.x < position.y / RING_SLOPE + RING_MIN_X) {
            position.x = position.y / RING_SLOPE + RING_MIN_X;
        } else if (position.x > position.y / -RING_SLOPE + RING_MAX_X) {
            position.x = position.y / -RING_SLOPE + RING_MAX_X;
        }
    }

    private boolean areWithingContactDistance(Vector2 position1, Vector2 position2) {
        // determine if the positions are within the distance in which contact is possible
        return Math.abs(position1.x - position2.x) <= FIGHTER_CONTACT_DISTANCE_X
                && Math.abs(position1.y - position2.y) <= FIGHTER_CONTACT_DISTANCE_Y;
    }

    @Override
    public void resize(int width, int height) {
        // update viewport with new screen size
        viewport.update(width, height, true);
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

    @Override
    public boolean keyDown(int keycode) {
        // check if spacebar has been pressed
        if (keycode == Input.Keys.SPACE) {
            // if the game is in running state, check round state is starting or ending, if so, end delay
            // if game is in the game over state, restart the game
            if (gameState == GameState.RUNNING) {
                // if game is running and space key has been pressed, skip any round delays
                if (roundState == RoundState.STARTING) {
                    roundStateTime = START_ROUND_DELAY;
                } else if (roundState == RoundState.ENDING) {
                    roundStateTime = END_ROUND_DELAY;
                }
            } else if (gameState == GameState.GAME_OVER) {
                // if game is over and space key has been pressed, restart the game
                startGame();
            }
        } else {
            if (roundState == RoundState.IN_PROGRESS) {
                // check if player has pressed a movement key
                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    game.player.moveLeft();
                } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    game.player.moveRight();
                }

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    game.player.moveUp();
                } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    game.player.moveDown();
                }
            }

            // check if the player has pressed a block or attack key
            if (keycode == Input.Keys.L) {
                game.player.block();
            } else if (keycode == Input.Keys.J) {
                game.player.punch();
            } else if (keycode == Input.Keys.K) {
                game.player.kick();
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // if player has release a movement key, stop moving in that direction
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            game.player.stopMovingLeft();
        } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            game.player.stopMovingRight();
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            game.player.stopMovingUp();
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            game.player.stopMovingDown();
        }

        // if player has released a block key, stop attacking
        if (keycode == Input.Keys.L) {
            game.player.stopBlocking();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // convert the screen coordinates of the touch/click to world coordinates
        Vector3 position = new Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(
                position, viewport.getScreenX(),
                viewport.getScreenY(), viewport.getScreenWidth(),
                viewport.getScreenHeight()
        );

        // check if the game is in running state
        if (gameState == GameState.RUNNING) {
            if (roundState == RoundState.STARTING) {
                // if the round is starting and the screen has been touched, skip the round delay
                roundStateTime = START_ROUND_DELAY;
            } else if (roundState == RoundState.ENDING) {
                // if the round is ending and the screen has been touched, skip the round delay
                roundStateTime = END_ROUND_DELAY;
            }
        } else {
            // if game over and player has clicked our touch play again button
            if (gameState == GameState.GAME_OVER && playAgainButtonSprite.getBoundingRectangle().contains(position.x, position.y)) {
                // start over from the beginning
                startGame();
            } else if (gameState == GameState.GAME_OVER && mainMenuButtonSprite.getBoundingRectangle().contains(position.x, position.y)) {
                // if game over and player has clicked our touch main menu button
                game.setScreen(game.gameScreen);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
