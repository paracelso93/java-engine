package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ColorShader extends Program {
    private static final String vertexSource = "src/shaders/sources/color.vert";
    private static final String fragmentSource = "src/shaders/sources/color.frag";

    private int transformLocation, mvpLocation, viewLocation;
    private Matrix4f transform, mvp, view;

    public ColorShader() {
        super(vertexSource, fragmentSource);
    }

    public void addUniforms(Matrix4f transform, Matrix4f mvp, Matrix4f view) {
        this.transform = transform;
        this.mvp = mvp;
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
    }
}