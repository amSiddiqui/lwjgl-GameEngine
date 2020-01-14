package com.tks.learning.game;

import com.tks.learning.engine.IGameLogic;
import com.tks.learning.engine.Window;
import com.tks.learning.engine.graph.Mesh;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private int displayXInc = 0;

    private int displayYInc = 0;

    private int displayZInc = 0;

    private int scaleInc = 0;

    private float color = 0.f;

    private final Renderer renderer;

    private GameItem[] gameItems;
    private Mesh mesh;

    public DummyGame() {
        this.renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float[] vertices = new float[] {
                -0.5f,  0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f,  0.5f, 0.5f,
        };

        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        mesh = new Mesh(vertices, indices, colors);
        GameItem item = new GameItem(mesh);
        item.setPosition(0, 0, -2);
        gameItems = new GameItem[]{item};

    }

    @Override
    public void input(Window window) {
        displayXInc = 0;
        displayYInc = 0;
        displayZInc = 0;
        scaleInc = 0;

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displayYInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displayYInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)){
            displayXInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)){
            displayXInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)){
            displayZInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_W)) {
            displayZInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            Vector3f itemPosition = gameItem.getPosition();
            float posX = itemPosition.x + displayXInc * 0.01f;
            float posY = itemPosition.y + displayYInc * 0.01f;
            float posZ = itemPosition.z + displayZInc * 0.01f;
            gameItem.setPosition(posX, posY, posZ);

            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);

            float rotation = gameItem.getRotation().z + 1.5f;
            if ( rotation > 360 ) {
                rotation = 0;
            }
            gameItem.setRotation(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanup();
    }
}
