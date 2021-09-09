
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import rendering.Camera;
import rendering.DisplayHandler;
import rendering.Renderer;
import model.Mesh;
import model.OBJLoader;
import shaders.ColorShader;
import shaders.ShaderList;
import shaders.TexturedShader;

public class Main {
    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;

    public static void main(String[] args) {
        DisplayHandler handler = new DisplayHandler("gaming", WIDTH, HEIGHT);
        Renderer renderer = new Renderer(handler);

        TexturedShader simple = new TexturedShader();
        ShaderList.addShader(simple);

        ColorShader color = new ColorShader();
        ShaderList.addShader(color);

        Mesh cube = OBJLoader.loadObj("res/antonino.obj");
        cube.addTexture("res/obunga.jpg");
        cube.translate(0, 0, -5);
        cube.rotate(new Vector3f(0, 1, 0), 3.141f * 3 / 2);

        Mesh cube2 = OBJLoader.loadObj("res/cube.obj");
        cube2.addTexture("res/chungus.png");
        cube2.translate(2, 0, -5);

        Mesh cat = OBJLoader.loadObj("res/cat.obj");
        cat.addTexture("res/cat.png");
        cat.translate(-1.5f, -1, -4);
        cat.scale(0.05f, 0.05f, 0.05f);
        cat.rotate(new Vector3f(1, 0, 0), -3.141f / 2);

        Mesh thonker = OBJLoader.loadObj("res/thonker.obj");
        thonker.translate(0, -0.5f, -2);

        Camera camera = new Camera();


        float i = 1;
        while (!Display.isCloseRequested()) {
            camera.update();

            renderer.clearBuffers(0.2f, 0.2f, 0.8f);

            //cube2.rotate(new Vector3f(1, 0, 0), 0.01f);
            //cube2.rotate(new Vector3f(0, 1, 0), 0.01f);
            //cube2.rotate(new Vector3f(0, 0, 1), 0.01f);


            i += 0.1;
            //cat.translate((float)Math.sin(i) * 0.01f, 0, 0);

            //cube.rotate(new Vector3f(0, 1, 0), 0.01f);
            //thonker.rotate(new Vector3f(0, 1, 0), 0.01f);

            cube.render(renderer, camera);
            cube2.render(renderer, camera);
            cat.render(renderer, camera);
            //thonker.render(renderer, camera);
            camera.render(Mesh.calculateMVP());

            handler.update();
        }

        cube.destroy();
        cube2.destroy();
        cat.destroy();
        thonker.destroy();

        renderer.destroy();
        handler.destroy();
    }
}
