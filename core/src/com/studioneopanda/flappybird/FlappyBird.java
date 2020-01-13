package com.studioneopanda.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    //---DATA---//
    private int score = 0;
    private int scoreTube = 0;
    private SpriteBatch batch;
    private Texture background;
    private Texture[] birds;
    private Texture topTube;
    private Texture bottomTube;
    private Texture gameOver;
    private BitmapFont font;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private int gameState = 0;
    private float gravity = 1;
    private float gap = 500;
    private float maxTubeOffset;
    private Random randomGenerator;
    private float tubeVelocity = 4;
    private int numberOfTubes = 4;
    private float[] tubeX = new float[numberOfTubes];
    private float[] tubeOffset = new float[numberOfTubes];
    private float distanceBetweenTubes;
    private Circle birdCircle;
    //private ShapeRenderer shapeRenderer;
    private Rectangle[] topTubeRectangles;
    private Rectangle[] bottomTubeRectangles;
    //---DATA---//

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameOver = new Texture("gameover.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeOffset = (float) Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();

        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();
    }

    private void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = (float) Gdx.graphics.getWidth() / 2 - (float) topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoreTube] < (float) Gdx.graphics.getWidth() / 2) {
                score++;

                Gdx.app.log("Score", String.valueOf(score));
                if (scoreTube < numberOfTubes - 1) {
                    scoreTube++;
                } else {
                    scoreTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -25;
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {

                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {
                    tubeX[i] -= tubeVelocity;
                }

                batch.draw(topTube, tubeX[i], (float) Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], (float) Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], (float) Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], (float) Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }


        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score = 0;
                scoreTube = 0;
                velocity = 0;
            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);


        font.draw(batch, String.valueOf(score), 100, 200);

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + (float) birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++) {
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                gameState = 2;
            }
        }
        batch.end();
        //shapeRenderer.end();
    }

    @Override
    public void dispose() { //dont touch this !
        batch.dispose();
    }
}
