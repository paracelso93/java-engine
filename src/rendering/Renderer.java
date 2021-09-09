package rendering;

import buffers.VAO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import shaders.Program;

public class Renderer {
    private final DisplayHandler displayHandler;

    public Renderer(DisplayHandler displayHandler) {
        this.displayHandler = displayHandler;
    }

    public void clearBuffers(float r, float g, float b) {
        GL11.glClearColor(r, g, b, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void renderObject(VAO vao, Program program) {
        program.bind();

        vao.bind();
        program.loadAllUniforms();
        GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getSize(), GL11.GL_UNSIGNED_INT, 0);
        vao.unbind();
        program.unbind();
    }

    public void destroy() {

    }
}
