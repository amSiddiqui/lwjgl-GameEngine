package com.tks.learning.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL32.*;

public class ShaderProgram {
    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        uniforms = new HashMap<>();
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String code) throws Exception {
        vertexShaderId = createShader(code, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String code) throws Exception {
        fragmentShaderId = createShader(code, GL_FRAGMENT_SHADER);
    }

    private int createShader(String code, int type) throws Exception {
        int shaderId = glCreateShader(type);
        String shaderType = "Unknown";
        switch (type) {
            case GL_VERTEX_SHADER:
                shaderType = "Vertex Shader";
                break;
            case GL_FRAGMENT_SHADER:
                shaderType = "Fragment Shader";
                break;
        }
        if (shaderId == 0) {
            throw new Exception ("Error create shader. Type : "+shaderType);
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: "+glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);
        return shaderId;
    }


    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception ("Error Linking shader code: "+glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0){
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: "+glGetProgramInfoLog(programId, 1024));
        }
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformId = glGetUniformLocation(programId, uniformName);
        if (uniformId < 0) {
            throw new Exception("Could not find uniform: "+uniformName);
        }
        uniforms.put(uniformName, uniformId);
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName+".ambient");
        createUniform(uniformName+".diffuse");
        createUniform(uniformName+".specular");
        createUniform(uniformName+".hasTexture");
        createUniform(uniformName+".reflectance");
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }


    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
