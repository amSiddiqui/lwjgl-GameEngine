package com.tks.learning.engine.graph;

import org.joml.Vector3f;
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

    private Material material;

    public Mesh(float[] positions, float[] txtCoords, float[] normals, int[] indices) {
        FloatBuffer verticesBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer normalBuffer = null;
        IntBuffer indicesBuffer = null;
        vboIdList = new ArrayList<>();

        try {
            vertexCount = indices.length;

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


            vboId = glGenBuffers();
            vboIdList.add(vboId);
            normalBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);


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
            if (normalBuffer != null) {
                MemoryUtil.memFree(normalBuffer);
            }
        }
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


    public int getVaoId() {
        return vaoId;
    }

    public void render() {

        Texture texture = material.getTexture();

        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        glBindVertexArray(this.getVaoId());


        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);


        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
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
        if (material.getTexture() != null) {
            this.material.getTexture().cleanup();
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);


    }
}
