package com.tks.learning.engine.graph;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OBJLoader {
    public static Mesh loadMesh(String filename)throws Exception {
        List<String> lines = IOUtils.readLines(Objects.requireNonNull(OBJLoader.class.getClassLoader().getResourceAsStream(filename)), StandardCharsets.UTF_8);
        if (lines.size() == 0) throw new Exception("Empty file");

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> texture = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    Vector3f vector3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(vector3f);
                    break;
                case "vt":
                    Vector2f vecText2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    texture.add(vecText2f);
                    break;
                case "vn":
                    Vector3f vn3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(vn3f);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    break;
            }
        }
        return reorderLists(vertices, texture, normals, faces);
    }

    private static Mesh reorderLists(List<Vector3f> vertices, List<Vector2f> texture, List<Vector3f> normals, List<Face> faces) {
        List<Integer> indices = new ArrayList<>();
        float[] posArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f vertex : vertices) {
            posArr[i*3] = vertex.x;
            posArr[i*3 + 1] = vertex.y;
            posArr[i*3 + 2] = vertex.z;
            i++;
        }
        float[] textCoordArr = new float[vertices.size() * 2];
        float[] normalArr = new float[vertices.size() * 3];

        for (Face face : faces) {
            IdxGroup[] faceVertices = face.getIdxGroups();
            for (IdxGroup faceVertex : faceVertices) {
                processFaceVertex(faceVertex, texture, normals, indices, textCoordArr, normalArr);
            }
        }

        int[] indicesArr;
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        return new Mesh(posArr, textCoordArr, normalArr, indicesArr);
    }

    private static void processFaceVertex(IdxGroup faceVertex, List<Vector2f> texture, List<Vector3f> normals,
                                          List<Integer> indices, float[] textCoordArr, float[] normalArr) {
        int posIndex = faceVertex.idxPos;
        indices.add(posIndex);
        if (faceVertex.idxTextCoord >= 0) {
            Vector2f textCoord = texture.get(faceVertex.idxTextCoord);
            textCoordArr[posIndex * 2] = textCoord.x;
            textCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        if (faceVertex.idxVecNormal >= 0) {
            Vector3f normalCoord = normals.get(faceVertex.idxVecNormal);
            normalArr[posIndex * 3] = normalCoord.x;
            normalArr[posIndex * 3 + 1] = normalCoord.y;
            normalArr[posIndex * 3 + 2] = normalCoord.z;
        }
    }

    protected static class Face {
        private IdxGroup[] idxGroups;
        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();
            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                String textCoord = lineTokens[1];
                if (textCoord.trim().length() > 0) {
                    idxGroup.idxTextCoord = Integer.parseInt(textCoord) - 1;
                }
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }
            return idxGroup;
        }

        public IdxGroup[] getIdxGroups() {
            return idxGroups;
        }
    }

    protected static class IdxGroup {
        public static final int NO_VALUE = -1;
        public int idxPos;
        public int idxTextCoord;
        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }
}
