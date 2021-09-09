package rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ColorShader;
import shaders.ShaderList;
import util.Defines;

import java.nio.FloatBuffer;

public class Camera {

    private Matrix4f view;
    private Vector3f position;
    private Vector3f forward;
    private float pitch, yaw, roll;
    private int VAO;
    private int[] VBO;
    private static final float SPEED = 0.02f;

    public Camera() {
        view = new Matrix4f();
        view.setIdentity();
        position = new Vector3f(0, 0, 0);
        pitch = 0;
        yaw = 0;
        roll = 0;
        forward = new Vector3f(0, 0, -1);
        calculateView();
        if (Defines.DEBUG) {
            VAO = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(VAO);
            VBO = new int[2];
            VBO[0] = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO[0]);
            float[] positions = {
                    position.x, position.y, position.z,
                    position.x + forward.x * 5, position.y + forward.y * 5, position.z + forward.z * 5
            };

            float[] colors = {
                    1, 0, 0, 1, 0, 1, 0, 1
            };

            FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
            buffer.put(positions);
            buffer.flip();

            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            VBO[1] = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO[1]);
            FloatBuffer buffer_color = BufferUtils.createFloatBuffer(colors.length);
            buffer_color.put(colors);
            buffer_color.flip();

            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer_color, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO[0]);

            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
            GL20.glEnableVertexAttribArray(0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO[1]);

            GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
            GL20.glEnableVertexAttribArray(1);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            GL30.glBindVertexArray(0);
        }
    }

    public void calculateView() {
        view.setIdentity();


        view.rotate(pitch, new Vector3f(1, 0, 0));
        view.rotate(yaw, new Vector3f(0, 1, 0));
        view.rotate(roll, forward);

        Vector3f negPosition = new Vector3f(-position.x, -position.y, -position.z);

        view.translate(negPosition);
    }

    public void calculateForward() {
        forward.x = (float) Math.sin(yaw) * (float) Math.cos(pitch);
        forward.y = (float) -Math.sin(pitch);
        forward.z = -((float) Math.cos(yaw) * (float) Math.cos(pitch));
    }

    public Vector3f calculateRightVector() {
        Vector3f result = new Vector3f();
        Vector3f base = new Vector3f(forward.x, 0, forward.z);
        Vector3f.cross(base, new Vector3f(0, 1, 0), result);
        return result;
    }

    public Vector3f calculateUpVector() {
        Vector3f result = new Vector3f();
        Vector3f.cross(new Vector3f(1, 0, 0), forward, result);
        return result;
    }

    public Matrix4f getView() {
        return view;
    }

    public void update() {
        boolean moved = false;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.x += forward.x * SPEED;
            position.y += forward.y * SPEED;
            position.z += forward.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x -= forward.x * SPEED;
            position.y -= forward.y * SPEED;
            position.z -= forward.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            Vector3f right = calculateRightVector();
            position.x -= right.x * SPEED;
            position.y -= right.y * SPEED;
            position.z -= right.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            Vector3f right = calculateRightVector();
            position.x += right.x * SPEED;
            position.y += right.y * SPEED;
            position.z += right.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            Vector3f up = calculateUpVector();
            position.x += up.x * SPEED;
            position.y += up.y * SPEED;
            position.z += up.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            Vector3f up = calculateUpVector();
            position.x -= up.x * SPEED;
            position.y -= up.y * SPEED;
            position.z -= up.z * SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            roll += SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            roll -= SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            yaw -= SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            yaw += SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
            pitch -= SPEED;
            moved = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            pitch += SPEED;
            moved = true;
        }

        if (moved) {
            calculateView();
            calculateForward();
        }
    }

    public void render(Matrix4f mvp) {
        if (Defines.DEBUG) {
            float[] positions = {
                    position.x, position.y, position.z,
                    position.x + forward.x, position.y + forward.y, position.z + forward.z
            };

            FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
            buffer.put(positions);
            buffer.flip();


            GL30.glBindVertexArray(VAO);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO[0]);
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            ColorShader shader = ShaderList.getShader(ColorShader.class);
            assert shader != null;
            shader.bind();
            Matrix4f transform = new Matrix4f();
            transform.setIdentity();
            shader.addUniforms(transform, mvp, view);
            shader.loadAllUniforms();
            GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
            shader.unbind();
            GL30.glBindVertexArray(0);
        }
    }
}
