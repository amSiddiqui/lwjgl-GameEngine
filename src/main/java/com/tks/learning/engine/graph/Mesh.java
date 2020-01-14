package com.tks.learning.engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vaoId;
    private int vboId;
    private int idxVboId;
    private int colorVboId;

    private int vertexCount;

    public Mesh(float[] positions, int[] indices, float[] colors) {
        vertexCount = indices.length;
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer colorBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);


            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);


            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        }finally {
            if (verticesBuffer != null ){
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public void render() {
        glBindVertexArray(this.getVaoId());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (vboId != 0) {
            glDeleteBuffers(vboId);
        }
        if (idxVboId != 0) {
            glDeleteBuffers(idxVboId);
        }
        if (colorVboId != 0) {
            glDeleteBuffers(colorVboId);
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);


    }
}
