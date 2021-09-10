package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

public abstract class Program {
    private final Shader vertex, fragment;
    private final int ID;
    private static final int PROGRAM_LINK_FAILURE = -2;

    public Program(String vertexPath, String fragmentPath) {
        this.vertex = new Shader(vertexPath, GL20.GL_VERTEX_SHADER);
        this.fragment = new Shader(fragmentPath, GL20.GL_FRAGMENT_SHADER);

        ID = GL20.glCreateProgram();
        bind();

        GL20.glAttachShader(ID, this.vertex.getID());
        GL20.glAttachShader(ID, this.fragment.getID());

        GL20.glLinkProgram(ID);
        checkLinkStatus();

        getAllUniforms();

        unbind();
    }

    public Program(Shader vertex, Shader fragment) {
        this.vertex = vertex;
        this.fragment = fragment;

        ID = GL20.glCreateProgram();
        bind();

        GL20.glAttachShader(ID, this.vertex.getID());
        GL20.glAttachShader(ID, this.fragment.getID());

        GL20.glLinkProgram(ID);
        checkLinkStatus();

        unbind();
        vertex.destroy();
        fragment.destroy();
    }


    protected abstract void getAllUniforms();

    public abstract void loadAllUniforms();

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(ID, name);
    }

    public void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    public void loadVec3(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    public void loadVec4(int location, Vector4f value) {
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void loadBoolean(int location, boolean value) {
        float load = value ? 1 : 0;
        GL20.glUniform1f(location, load);
    }

    public void loadMat4(int location, Matrix4f mat4) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        mat4.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }


    public void bind() {
        GL20.glUseProgram(ID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        GL20.glDeleteProgram(ID);
        GL20.glDetachShader(ID, vertex.getID());
        GL20.glDetachShader(ID, fragment.getID());
    }

    private void checkLinkStatus() {
        int status = GL20.glGetProgrami(ID, GL20.GL_LINK_STATUS);
        if (status == GL11.GL_FALSE) {
            System.out.println(GL20.glGetProgramInfoLog(ID, 512));
            System.exit(PROGRAM_LINK_FAILURE);
        }
    }
}
