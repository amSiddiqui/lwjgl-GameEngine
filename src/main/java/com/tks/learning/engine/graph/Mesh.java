package com.tks.learning.engine.graph;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vaoId;
    private List<Integer> vboIdList;

    private int vertexCount;

    private final Texture texture;

    public Mesh(float[] positions, int[] indices, float[] txtCoords, Texture texture) {
        vertexCount = indices.length;
        FloatBuffer verticesBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        vboIdList = new ArrayList<>();

        try {
            this.texture = texture;
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();


            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);


            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);


//            Texture Coordinates
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(txtCoords.length);
            textCoordsBuffer.put(txtCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);


            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        }finally {
            if (verticesBuffer != null ){
                MemoryUtil.memFree(verticesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if (textCoordsBuffer != null ){
                MemoryUtil.memFree(textCoordsBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public void render() {

        glActiveTexture(GL_TEXTURE0);

        texture.bind();

        glBindVertexArray(this.getVaoId());


        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);


        glBindVertexArray(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (Integer vboId : vboIdList) {
            if (vboId != 0) {
                glDeleteBuffers(vboId);
            }
        }

        texture.cleanup();
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);


    }
}
