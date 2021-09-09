package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.*;

public class Shader {
    private final int ID;
    private static final int SHADER_COMPILATION_FAILURE = -1;

    public Shader(String sourcePath, int type) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
            String string;
            while ((string = reader.readLine()) != null) {
                stringBuilder.append(string + "\n");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        ID = GL20.glCreateShader(type);
        GL20.glShaderSource(ID, stringBuilder);

        GL20.glCompileShader(ID);
        checkCompileStatus();
    }

    public int getID() {
        return ID;
    }

    public void destroy() {
        GL20.glDeleteShader(ID);
    }

    private void checkCompileStatus() {
        int result = GL20.glGetShaderi(ID, GL20.GL_COMPILE_STATUS);
        if (result == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(ID, 512));
            System.exit(SHADER_COMPILATION_FAILURE);
        }
    }
}
