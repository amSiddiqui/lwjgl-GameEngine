//package com.tks.learning.game;
//
//import com.tks.learning.engine.IGameLogic;
//import com.tks.learning.engine.MouseInput;
//import com.tks.learning.engine.Window;
//import com.tks.learning.engine.graph.Camera;
//import com.tks.learning.engine.graph.Mesh;
//import com.tks.learning.engine.graph.Texture;
//import org.joml.Vector2f;
//import org.joml.Vector3f;
//
//import static org.lwjgl.glfw.GLFW.*;
//
//public class DummyGame implements IGameLogic {
//
//    private int displayXInc = 0;
//
//    private int displayYInc = 0;
//
//    private int displayZInc = 0;
//
//    private int scaleInc = 0;
//
//    private static final float MOUSE_SENSITIVITY = 0.2f;
//
//    private final Camera camera;
//
//    private static final float CAMERA_POS_STEP = 0.05f;
//
//    private final Vector3f cameraInc;
//
//    private final Renderer renderer;
//
//    private GameItem[] gameItems;
//    private Mesh mesh;
//
//    public DummyGame() {
//        this.renderer = new Renderer();
//        camera = new Camera();
//        cameraInc = new Vector3f();
//    }
//
//    @Override
//    public void init(Window window) throws Exception {
//        renderer.init(window);
//
//        float[] positions = new float[]{
//                // V0
//                -0.5f, 0.5f, 0.5f,
//                // V1
//                -0.5f, -0.5f, 0.5f,
//                // V2
//                0.5f, -0.5f, 0.5f,
//                // V3
//                0.5f, 0.5f, 0.5f,
//                // V4
//                -0.5f, 0.5f, -0.5f,
//                // V5
//                0.5f, 0.5f, -0.5f,
//                // V6
//                -0.5f, -0.5f, -0.5f,
//                // V7
//                0.5f, -0.5f, -0.5f,
//                // For text coords in top face
//                // V8: V4 repeated
//                -0.5f, 0.5f, -0.5f,
//                // V9: V5 repeated
//                0.5f, 0.5f, -0.5f,
//                // V10: V0 repeated
//                -0.5f, 0.5f, 0.5f,
//                // V11: V3 repeated
//                0.5f, 0.5f, 0.5f,
//                // For text coords in right face
//                // V12: V3 repeated
//                0.5f, 0.5f, 0.5f,
//                // V13: V2 repeated
//                0.5f, -0.5f, 0.5f,
//                // For text coords in left face
//                // V14: V0 repeated
//                -0.5f, 0.5f, 0.5f,
//                // V15: V1 repeated
//                -0.5f, -0.5f, 0.5f,
//                // For text coords in bottom face
//                // V16: V6 repeated
//                -0.5f, -0.5f, -0.5f,
//                // V17: V7 repeated
//                0.5f, -0.5f, -0.5f,
//                // V18: V1 repeated
//                -0.5f, -0.5f, 0.5f,
//                // V19: V2 repeated
//                0.5f, -0.5f, 0.5f,};
//        float[] textCoords = new float[]{
//                0.0f, 0.0f,
//                0.0f, 0.5f,
//                0.5f, 0.5f,
//                0.5f, 0.0f,
//                0.0f, 0.0f,
//                0.5f, 0.0f,
//                0.0f, 0.5f,
//                0.5f, 0.5f,
//                // For text coords in top face
//                0.0f, 0.5f,
//                0.5f, 0.5f,
//                0.0f, 1.0f,
//                0.5f, 1.0f,
//                // For text coords in right face
//                0.0f, 0.0f,
//                0.0f, 0.5f,
//                // For text coords in left face
//                0.5f, 0.0f,
//                0.5f, 0.5f,
//                // For text coords in bottom face
//                0.5f, 0.0f,
//                1.0f, 0.0f,
//                0.5f, 0.5f,
//                1.0f, 0.5f,};
//        int[] indices = new int[]{
//                // Front face
//                0, 1, 3, 3, 1, 2,
//                // Top Face
//                8, 10, 11, 9, 8, 11,
//                // Right face
//                12, 13, 7, 5, 12, 7,
//                // Left face
//                14, 15, 6, 4, 14, 6,
//                // Bottom face
//                16, 18, 19, 17, 16, 19,
//                // Back face
//                4, 6, 7, 5, 4, 7,};
//
//        Texture texture = new Texture("C:\\Users\\aamir\\Documents\\projects\\lwjgl-learning\\src\\texture\\grassblock.png");
//
//        mesh = new Mesh(positions, indices, textCoords, texture);
//        GameItem gameItem1 = new GameItem(mesh);
//        gameItem1.setScale(0.5f);
//        gameItem1.setPosition(0, 0, -2);
//        gameItems = new GameItem[]{gameItem1};
//
//    }
//
//    @Override
//    public void input(Window window, MouseInput mouseInput) {
//
////        Camera controls
//
//        cameraInc.set(0, 0, 0);
//        if (window.isKeyPressed(GLFW_KEY_I)) {
//            cameraInc.z = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_K)) {
//            cameraInc.z = 1;
//        }
//        if (window.isKeyPressed(GLFW_KEY_J)) {
//            cameraInc.x = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_L)) {
//            cameraInc.x = 1;
//        }
//        if (window.isKeyPressed(GLFW_KEY_N)) {
//            cameraInc.y = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_M)) {
//            cameraInc.y = 1;
//        }
//
////        Object controls
//
//        displayXInc = 0;
//        displayYInc = 0;
//        displayZInc = 0;
//        scaleInc = 0;
//
//        if (window.isKeyPressed(GLFW_KEY_UP)) {
//            displayYInc = 1;
//        } if (window.isKeyPressed(GLFW_KEY_DOWN)) {
//            displayYInc = -1;
//        } if (window.isKeyPressed(GLFW_KEY_LEFT)){
//            displayXInc = -1;
//        } if (window.isKeyPressed(GLFW_KEY_RIGHT)){
//            displayXInc = 1;
//        } if (window.isKeyPressed(GLFW_KEY_Q)){
//            displayZInc = -1;
//        } if (window.isKeyPressed(GLFW_KEY_W)) {
//            displayZInc = 1;
//        } if (window.isKeyPressed(GLFW_KEY_A)) {
//            scaleInc = -1;
//        } if (window.isKeyPressed(GLFW_KEY_S)) {
//            scaleInc = 1;
//        }
//    }
//
//    @Override
//    public void update(float interval, MouseInput mouseInput) {
//
//        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
//
//        if (mouseInput.isLeftButtonPressed()) {
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//        }
//
//        for (GameItem gameItem : gameItems) {
//            Vector3f itemPosition = gameItem.getPosition();
//            float posX = itemPosition.x + displayXInc * 0.01f;
//            float posY = itemPosition.y + displayYInc * 0.01f;
//            float posZ = itemPosition.z + displayZInc * 0.01f;
//            gameItem.setPosition(posX, posY, posZ);
//
//            float scale = gameItem.getScale();
//            scale += scaleInc * 0.01f;
//            if (scale < 0) {
//                scale = 0;
//            }
//            gameItem.setScale(scale);
//
//            float rotation = gameItem.getRotation().x + 1.5f;
//            if ( rotation > 360 ) {
//                rotation = 0;
//            }
//            gameItem.setRotation(rotation, rotation, rotation);
//        }
//    }
//
//    @Override
//    public void render(Window window) {
//        float color = 0.15f;
//        window.setClearColor(color, color, color, 1.0f);
//        renderer.render(window, camera, gameItems);
//    }
//
//    @Override
//    public void cleanup() {
//        renderer.cleanup();
//        mesh.cleanup();
//    }
//}
