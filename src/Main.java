
import light.AmbientLight;
import light.Attenuation;
import light.LightList;
import light.PointLight;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import rendering.Camera;
import rendering.DisplayHandler;
import rendering.Renderer;
import model.Mesh;
import model.OBJLoader;
import shaders.*;

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

        TexturedNormalShader simpleNormal = new TexturedNormalShader();
        ShaderList.addShader(simpleNormal);

        ColorNormalShader colorNormal = new ColorNormalShader();
        ShaderList.addShader(colorNormal);

        AmbientLight light = new AmbientLight(new Vector3f(0.2f, 0.2f, 1f));
        LightList.addLight("ambient1", light);

        PointLight pointLight = new PointLight(new Vector3f(0f, 1f, 0f), new Vector3f(0f, -0.6f, -3f), 1f, new Attenuation(0.0f, 0.1f, 0.1f));
        LightList.addLight("point1", pointLight);

        Mesh cube = OBJLoader.loadObj("res/obamium.obj");
        cube.addTexture("res/obunga.jpg");
        cube.translate(0, 0, -7);
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
        thonker.translate(-1, 1.5f, -4);
        thonker.scale(1.5f, 1.5f, 1.5f);

        Mesh base = OBJLoader.loadObj("res/cube.obj");
        base.addTexture("res/white.png");
        base.scale(100f, 1f, 100f);
        base.translate(0, -2f, 0);

        Camera camera = new Camera();


        float i = 1;
        while (!Display.isCloseRequested()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }
            camera.update();

            renderer.clearBuffers(0.2f, 0.2f, 0.8f);

            cube2.rotate(new Vector3f(1, 0, 0), 0.01f);
            cube2.rotate(new Vector3f(0, 1, 0), 0.01f);
            cube2.rotate(new Vector3f(0, 0, 1), 0.01f);


            i += 0.1;
            cat.translate((float)Math.sin(i) * 0.01f, 0, 0);

            cube.rotate(new Vector3f(0, 1, 0), 0.01f);
            thonker.rotate(new Vector3f(0, 1, 0), 0.01f);

            cube.render(renderer, camera);
            cube2.render(renderer, camera);
            cat.render(renderer, camera);
            thonker.render(renderer, camera);
            base.render(renderer, camera);
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
