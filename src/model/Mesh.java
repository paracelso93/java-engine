package model;

import buffers.VAO;
import light.AmbientLight;
import light.LightList;
import light.PointLight;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.Camera;
import rendering.Renderer;
import shaders.*;
import util.FileLoader;

import java.util.ArrayList;


public class Mesh {
    private final VAO vao;
    private final Program shader;
    private Matrix4f transform, mvp;
    private Vector3f position, scale, rotation;
    private int textureID;
    private boolean hasUV, hasNormals;
    private Material material;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    public Mesh(VAO vao, boolean hasUV, boolean hasNormals) {

        this.vao = vao;
        this.hasUV = hasUV;
        this.hasNormals = hasNormals;
        if (hasUV && hasNormals) {
            this.shader = ShaderList.getShader(TexturedNormalShader.class);

            this.material = new Material(new Vector4f(), new Vector4f(), new Vector4f(), 1, 1.0f);
        } else if (hasUV) {
            this.shader = ShaderList.getShader(TexturedShader.class);
            this.material = new Material(new Vector4f(), new Vector4f(), new Vector4f(), 1, 1.0f);
        } else if (hasNormals) {
            this.shader = ShaderList.getShader(ColorNormalShader.class);
            this.material = new Material(new Vector4f(0.8f, 0.8f, 0, 1f), new Vector4f(0.8f, 0.8f, 0, 1f), new Vector4f(0.8f, 0.8f, 0, 1f), 0, 1.0f);
        } else {
            this.shader = ShaderList.getShader(ColorShader.class);
            this.material = new Material(new Vector4f(), new Vector4f(), new Vector4f(), 0, 1.0f);
        }

        position = new Vector3f();
        scale = new Vector3f(1.0f, 1.0f, 1.0f);
        rotation = new Vector3f();
        transform = new Matrix4f();
        transform.setIdentity();
        mvp = new Matrix4f();
        mvp = calculateMVP();
    }

    public void addTexture(String filePath) {

        textureID = FileLoader.loadTexture(filePath);
    }

    public static Matrix4f calculateMVP() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        Matrix4f mvp = new Matrix4f();
        mvp.m00 = x_scale;
        mvp.m11 = y_scale;
        mvp.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        mvp.m23 = -1;
        mvp.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        mvp.m33 = 0;
        return mvp;
    }

    private void calculateTransform() {
        transform.setIdentity();
        transform.translate(position);
        transform.rotate(rotation.x, new Vector3f(1, 0, 0));
        transform.rotate(rotation.y, new Vector3f(0, 1, 0));
        transform.rotate(rotation.z, new Vector3f(0, 0, 1));
        transform.scale(scale);
    }

    public void translate(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
        calculateTransform();
    }

    public void rotate(Vector3f direction, float amount) {
        if (direction.x != 0)
            rotation.x += amount;
        if (direction.y != 0)
            rotation.y += amount;
        if (direction.z != 0)
            rotation.z += amount;
        calculateTransform();
    }

    public void scale(float dx, float dy, float dz) {
        scale.x *= dx;
        scale.y *= dy;
        scale.z *= dz;
        calculateTransform();
    }

    public void render(Renderer renderer, Camera camera) {
        if (hasUV && hasNormals) {
            ArrayList<AmbientLight> ambientLights = LightList.getLightsWithType(AmbientLight.class);
            assert ambientLights.size() != 0;
            ArrayList<PointLight> pointLights = LightList.getLightsWithType(PointLight.class);
            assert pointLights.size() != 0;
            ((TexturedNormalShader) shader).addUniforms(transform, mvp, camera.getView(), textureID, ambientLights.get(0), pointLights.get(0), material);
        } else if (hasUV) {
            ((TexturedShader) shader).addUniforms(transform, mvp, camera.getView(), textureID);
        } else if (hasNormals) {

            ArrayList<AmbientLight> ambientLights = LightList.getLightsWithType(AmbientLight.class);
            assert ambientLights.size() != 0;
            ArrayList<PointLight> pointLights = LightList.getLightsWithType(PointLight.class);
            assert pointLights.size() != 0;
            ((ColorNormalShader)shader).addUniforms(transform, mvp, camera.getView(), ambientLights.get(0), pointLights.get(0), material);
        } else {
            ((ColorShader)shader).addUniforms(transform, mvp, camera.getView());
        }
        renderer.renderObject(vao, shader);
    }

    public void destroy() {
        vao.destroy();
    }
}
