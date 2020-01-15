package com.tks.learning.engine.graph;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL32.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private  final int id;

    public Texture(String filename) throws Exception {
        this(loadTexture(filename));
    }

    public Texture(int id) {
        this.id = id;
    }

    private static int loadTexture(String texture) throws Exception {
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channel = stack.mallocInt(1);

            buffer = stbi_load(texture, w, h, channel, 4);
            if (buffer == null) {
                throw new Exception("Image file: "+texture+" could not be loaded: "+stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(buffer);
        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;    }
}
