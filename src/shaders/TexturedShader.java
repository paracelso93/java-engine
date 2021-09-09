package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

public class TexturedShader extends Program {
    private static final String vertexSource = "src/shaders/sources/shader.vert";
    private static final String fragmentSource = "src/shaders/sources/shader.frag";

    private int transformLocation, mvpLocation, viewLocation;
    private Matrix4f transform, mvp, view;
    int texture;

    public TexturedShader() {
        super(vertexSource, fragmentSource);
    }

    public void addUniforms(Matrix4f transform, Matrix4f mvp, Matrix4f view, int texture) {
        this.transform = transform;
        this.mvp = mvp;
        this.texture = texture;
        this.view = view;
    }

    @Override
    protected void getAllUniforms() {
        transformLocation = getUniformLocation("transform");
        mvpLocation = getUniformLocation("mvp");
        viewLocation = getUniformLocation("view");
    }

    @Override
    public void loadAllUniforms() {
        loadMat4(transformLocation, transform);
        loadMat4(mvpLocation, mvp);
        loadMat4(viewLocation, view);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }
}
