package rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayHandler {
    public int WIDTH, HEIGHT;
    private static final int FPS_MAX = 60;

    public DisplayHandler(String title, int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        Display.setTitle(title);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException exception) {
            exception.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void update() {
        Display.sync(FPS_MAX);
        Display.update();
    }

    public void destroy() {
        Display.destroy();
    }
}
