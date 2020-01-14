package com.tks.learning.game;

import com.tks.learning.engine.Window;
import com.tks.learning.engine.graph.Mesh;
import com.tks.learning.engine.graph.ShaderProgram;
import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL32.*;

public class Renderer {


    private ShaderProgram shaderProgram;

    private Mesh mesh;

    public void init() throws Exception {
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

       float[] vertices = new float[] {
               -0.5f,  0.5f, 0.0f,
               -0.5f, -0.5f, 0.0f,
               0.5f,  0.5f, 0.0f,
               0.5f,  0.5f, 0.0f,
               -0.5f, -0.5f, 0.0f,
               0.5f, -0.5f, 0.0f,
       };
       mesh = new Mesh(vertices);
       System.out.println("Finished Initializing Renderer");
    }

    public void render(Window window) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        glBindVertexArray(mesh.getVaoId());
        glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());

        glBindVertexArray(0);
        shaderProgram.unbind();
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        mesh.cleanup();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

}
