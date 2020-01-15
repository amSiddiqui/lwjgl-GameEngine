package com.tks.learning.game;

import com.tks.learning.engine.Window;
import com.tks.learning.engine.graph.ShaderProgram;
import com.tks.learning.engine.graph.Transformation;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL32.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;


    private ShaderProgram shaderProgram;


    private Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        System.out.println("Initializing Renderer");

        shaderProgram = new ShaderProgram();
        InputStream vShaderStream = getClass().getClassLoader().getResourceAsStream("shader/vertexShader.shader");
        assert vShaderStream != null;
        String vShader = IOUtils.toString(vShaderStream, StandardCharsets.UTF_8.name());
        InputStream fShaderStream = getClass().getClassLoader().getResourceAsStream("shader/fragmentShader.shader");
        assert fShaderStream != null;
        String fShader = IOUtils.toString(fShaderStream, StandardCharsets.UTF_8.name());
        shaderProgram.createVertexShader(vShader);
        shaderProgram.createFragmentShader(fShader);
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
       System.out.println("Finished Initializing Renderer");
    }

    public void render(Window window, GameItem[] items) {
        clear();
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        shaderProgram.setUniform("texture_sampler", 0);

        for (GameItem item : items) {
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    item.getPosition(),
                    item.getRotation(),
                    item.getScale()
            );
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            item.getMesh().render();
        }

        shaderProgram.unbind();
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

}
