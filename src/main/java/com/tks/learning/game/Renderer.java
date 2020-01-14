package com.tks.learning.game;

import com.tks.learning.engine.Window;
import com.tks.learning.engine.graph.ShaderProgram;
import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL32.*;

public class Renderer {

    private int vboId;
    private int vaoId;

    private ShaderProgram shaderProgram;

    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        InputStream vShaderStream = getClass().getClassLoader().getResourceAsStream("vertexShader.shader");
        assert vShaderStream != null;
        String vShader = IOUtils.toString(vShaderStream, StandardCharsets.UTF_8.name());
        InputStream fShaderStream = getClass().getClassLoader().getResourceAsStream("fragmentShader.shader");
        assert fShaderStream != null;
        String fShader = IOUtils.toString(fShaderStream, StandardCharsets.UTF_8.name());
        shaderProgram.createVertexShader(vShader);
        shaderProgram.createFragmentShader(fShader);
        shaderProgram.link();

       float[] vertices = new float[] {
            0.0f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f
       };

        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
        }

    }

    public void render(Window window) {

    }

    public void cleanup() {

    }

}
