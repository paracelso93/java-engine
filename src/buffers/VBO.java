package buffers;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class VBO {
    private final int ID;

    public VBO(float[] data, int location, int type, int size) {
        ID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(location, size, type, false, 0, 0);
        GL20.glEnableVertexAttribArray(location);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public int getID() { return ID; }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        GL15.glDeleteBuffers(ID);
    }
}
