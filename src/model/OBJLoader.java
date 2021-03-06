package model;

import buffers.VAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OBJLoader {
    public static Mesh loadObj(String path) {
        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<int[]> faces = new ArrayList<>();
        ArrayList<int[]> uvPositions = new ArrayList<>();
        ArrayList<float[]> uvCoords = new ArrayList<>();
        ArrayList<float[]> normals = new ArrayList<>();
        ArrayList<int[]> normalPositions = new ArrayList<>();
        boolean hasUv = true;
        boolean hasNormals = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) continue;
                switch (line.charAt(0)) {
                    case 'v': {
                        if (line.charAt(1) == ' ') {
                            String[] values = line.split(" ");
                            int idx = 0;
                            for (String value : values) {
                                if (value.length() == 0) {
                                    values = removeItem(values, idx);
                                }
                                idx++;
                            }
                            float[] vertex = new float[values.length - 1];
                            for (int i = 0; i < values.length - 1; i++) {

                                vertex[i] = Float.parseFloat(values[i + 1]);
                            }
                            vertices.add(vertex);
                        } else if (line.charAt(1) == 't') {
                            String[] values = line.split(" ");
                            int idx = 0;
                            for (String value : values) {
                                if (value.length() == 0) {
                                    values = removeItem(values, idx);
                                }
                                idx++;
                            }
                            float[] coord = {
                                    Float.parseFloat(values[1]),
                                    Float.parseFloat(values[2])
                            };
                            uvCoords.add(coord);

                        } else if (line.charAt(1) == 'n') {
                            String[] values = line.split(" ");
                            int idx = 0;
                            for (String value : values) {
                                if (value.length() == 0) {
                                    values = removeItem(values, idx);
                                }
                                idx++;
                            }
                            float[] normal = {
                                    Float.parseFloat(values[1]),
                                    Float.parseFloat(values[2]),
                                    Float.parseFloat(values[3])
                            };
                            normals.add(normal);
                        }
                    }
                    break;
                    case 'f': {
                        String[] values = line.split(" ");
                        int idx = 0;
                        for (String value : values) {
                            if (value.length() == 0) {
                                values = removeItem(values, idx);
                            }
                            idx++;
                        }
                        int[] verts = new int[values.length - 1];
                        int[] uvPos = new int[values.length - 1];
                        int[] normalPos = new int[values.length - 1];
                        for (int i = 0; i < values.length - 1; i++) {

                            verts[i] = Integer.parseInt(values[i + 1].split("/")[0]);
                            if (hasUv) {
                                if (values[i + 1].split("/")[1].isEmpty()) {
                                    hasUv = false;
                                } else {
                                    uvPos[i] = Integer.parseInt(values[i + 1].split("/")[1]);
                                }
                            }
                            if (hasNormals) {
                                if (values[i + 1].split("/").length < 3 || values[i + 1].split("/")[2].isEmpty()) {
                                    hasNormals = false;
                                } else {
                                    normalPos[i] = Integer.parseInt(values[i + 1].split("/")[2]);
                                }
                            }
                        }
                        faces.add(verts);
                        uvPositions.add(uvPos);
                        normalPositions.add(normalPos);
                    }
                    break;
                    default: break;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        float[] coords = new float[uvCoords.size() * 2];
        int t = 0;
        for (float[] coord : uvCoords) {
            coords[t] = coord[0];
            coords[t + 1] = coord[1];
            t += 2;
        }

        int totalVerticesSize = 0;
        for (float[] vertex : vertices) {
            totalVerticesSize += vertex.length;
        }

        float[] verts = new float[totalVerticesSize];
        int j = 0;

        for (float[] vertex : vertices) {
            for (float v : vertex) {
                verts[j] = v;
                j++;
            }
        }

        float[] colors = new float[totalVerticesSize / 3 * 4];
        for (int i = 0; i < colors.length; i++) {
            if (i % 4 == 0) colors[i] = 0.8f;
            else if (i % 4 == 1) colors[i] = 0.8f;
            else if (i % 4 == 2) colors[i] = 0f;
            else colors[i] = 1f;
        }

        for (int[] face : faces) {
            if (face.length != 3) {
                System.err.println("Error: faces must have 3 vertices");
                System.exit(-3);
            }
        }

        int totalFacesSize = faces.size() * 3;


        int[] facesArray = new int[totalFacesSize];
        j = 0;

        for (int[] face : faces) {
            for (int f : face) {
                facesArray[j] = f - 1;
                j++;
            }
        }

        for (int[] face : faces) {
            if (face.length != 3) {
                System.err.println("Error: faces must have 3 vertices");
                System.exit(-3);
            }
        }
        VAO vao = null;
        if (hasUv && hasNormals) {
            int totalUvPosSize = uvPositions.size() * 3;


            int[] uvPosArray = new int[totalUvPosSize];
            j = 0;

            for (int[] uvPos : uvPositions) {
                for (int uv : uvPos) {
                    uvPosArray[j] = uv - 1;
                    j++;
                }
            }

            float[] uvs = sortUVCoords(facesArray, uvPosArray, uvCoords);

            int totalNormalPosSize = normalPositions.size() * 3;

            int[] normalPosArray = new int[totalNormalPosSize];
            j = 0;

            for (int[] normalPos : normalPositions) {
                for (int norm : normalPos) {
                    normalPosArray[j] = norm - 1;
                    j++;
                }
            }
            float[] norms = sortNormals(facesArray, normalPosArray, normals);

            vao = new VAO()
                    .addAttribArray(verts, 0, 3)
                    .addAttribArray(colors, 1, 4)
                    .addAttribArray(uvs, 2, 2)
                    .addAttribArray(norms, 3, 3)
                    .end(facesArray);
        } else if (hasUv) {
            int totalUvPosSize = uvPositions.size() * 3;


            int[] uvPosArray = new int[totalUvPosSize];
            j = 0;

            for (int[] uvPos : uvPositions) {
                for (int uv : uvPos) {
                    uvPosArray[j] = uv - 1;
                    j++;
                }
            }

            float[] uvs = sortUVCoords(facesArray, uvPosArray, uvCoords);
            vao = new VAO()
                    .addAttribArray(verts, 0, 3)
                    .addAttribArray(colors, 1, 4)
                    .addAttribArray(uvs, 2, 2)
                    .end(facesArray);
        } else if (hasNormals) {
            int totalNormalPosSize = normalPositions.size() * 3;

            int[] normalPosArray = new int[totalNormalPosSize];
            j = 0;

            for (int[] normalPos : normalPositions) {
                for (int norm : normalPos) {
                    normalPosArray[j] = norm - 1;
                    j++;
                }
            }
            float[] norms = sortNormals(facesArray, normalPosArray, normals);

            vao = new VAO()
                    .addAttribArray(verts, 0, 3)
                    .addAttribArray(colors, 1, 4)
                    .addAttribArray(norms, 2, 3)
                    .end(facesArray);
        } else {
            vao = new VAO()
                    .addAttribArray(verts, 0, 3)
                    .addAttribArray(colors, 1, 4)
                    .end(facesArray);
        }


        return new Mesh(vao, hasUv, hasNormals);
    }

    private static String[] removeItem(String[] data, int index) {
        String[] newData = new String[data.length - 1];

        if (index >= 0) System.arraycopy(data, 0, newData, 0, index);
        System.arraycopy(data, index + 1, newData, index, newData.length - index);
        return newData;
    }

    private static float[] sortUVCoords(int[] faces, int[] uvPositions, ArrayList<float[]> uvs) {
        float[] sortedUvs = new float[faces.length * 2];
        for (int i = 0; i < faces.length; i++) {
            int index = faces[i];
            sortedUvs[index * 2] = uvs.get(uvPositions[i])[0];

            sortedUvs[index * 2 + 1] = uvs.get(uvPositions[i])[1];
        }

        return sortedUvs;
    }

    private static float[] sortNormals(int[] faces, int[] normalPositions, ArrayList<float[]> normals) {
        float[] sortedNormals = new float[faces.length * 3];
        for (int i = 0; i < faces.length; i++) {
            int index = faces[i];
            sortedNormals[index * 3] = normals.get(normalPositions[i])[0];
            sortedNormals[index * 3 + 1] = normals.get(normalPositions[i])[1];
            sortedNormals[index * 3 + 2] = normals.get(normalPositions[i])[2];
        }

        return sortedNormals;
    }
}
