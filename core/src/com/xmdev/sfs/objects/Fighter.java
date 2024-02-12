package com.xmdev.sfs.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.Assets;

public class Fighter {
    // number of frame rows and columns in each animation sprite sheet
    private static final int FRAME_ROWS = 2, FRAME_COLS = 3;

    // how fast a fighter can move
    public static final float MOVEMENT_SPEED = 10f;

    // maximum life a fighter can have
    public static final float MAX_LIFE = 100f;

    // amount of damage a fighter's hit will inflict
    public static final float HIT_DAMAGE = 5f;

    // factor to decrease damage if a fighter gets hit while blocking
    public static final float BLOCK_DAMAGE_FACTOR = 0.2f;

    // distinguishing details
    private String name;
    private Color color;

    // states
    public enum State {
        BLOCK,
        HURT,
        IDLE,
        KICK,
        LOSE,
        PUNCH,
        WALK,
        WIN
    }

    private State state;
    private float stateTime;
    private State renderState;
    private float renderStateTime;
    private final Vector2 position = new Vector2();
    private final Vector2 movementDirection = new Vector2();
    private float life;
    private int facing;
    private boolean madeContact;

    // animations
    private Animation<TextureRegion> blockAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> kickAnimation;
    private Animation<TextureRegion> loseAnimation;
    private Animation<TextureRegion> punchAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> winAnimation;

    public Fighter(SFS game, String name, Color color) {
        this.name = name;
        this.color = color;

        // initialize animations
        initializeBlockAnimation(game.assets.manager);
        initializeHurtAnimation(game.assets.manager);
        initializeIdleAnimation(game.assets.manager);
        initializeKickAnimation(game.assets.manager);
        initializeLoseAnimation(game.assets.manager);
        initializePunchAnimation(game.assets.manager);
        initializeWalkAnimation(game.assets.manager);
        initializeWinAnimation(game.assets.manager);
    }

    private void initializeBlockAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        blockAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeHurtAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.HURT_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        hurtAnimation = new Animation<>(0.03f, frames);
    }

    private void initializeIdleAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        idleAnimation = new Animation<>(0.01f, frames);
    }

    private void initializeKickAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        kickAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeLoseAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        loseAnimation = new Animation<>(0.05f, frames);
    }

    private void initializePunchAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        punchAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeWalkAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        walkAnimation = new Animation<>(0.08f, frames);
    }

    private void initializeWinAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        winAnimation = new Animation<>(0.05f, frames);
    }

    private TextureRegion[] getAnimationFrames(Texture spriteSheet) {
        TextureRegion[][] tmp = TextureRegion.split(
                spriteSheet,
                spriteSheet.getWidth() / FRAME_COLS,
                spriteSheet.getHeight() / FRAME_ROWS
        );

        TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;

        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return frames;
    }
}
