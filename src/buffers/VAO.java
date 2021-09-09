package buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class VAO {
    private final int ID;
    private final List<VBO> vbos;
    private EBO ebo;
    private int verticesSize;

    public VAO() {
        ID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(ID);
        vbos = new ArrayList<VBO>();
    }

    public VAO addAttribArray(float[] data, int location, int size) {
        VBO vbo = new VBO(data, location, GL11.GL_FLOAT, size);
        vbos.add(vbo);
        return this;
    }

    public VAO end(int[] indices) {

        verticesSize = indices.length;
        ebo = new EBO(indices);
        unbind();
        return this;
    }

    public int getSize() { return this.verticesSize; }

    public void bind() {
        GL30.glBindVertexArray(ID);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void destroy() {
        GL30.glDeleteVertexArrays(ID);
        for (VBO vbo : vbos) {
            vbo.destroy();
        }
        ebo.destroy();
    }
}
