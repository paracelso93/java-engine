package util;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;

public class FileLoader {
    public static final int FILELOADER_LOADTEXTURE_FAILURE = -5;

    public static int loadTexture(String file) {
        Texture texture = null;
        String format = "PNG";
        if (file.contains(".png")) {
            format = "PNG";
        } else if (file.contains(".jpg") || file.contains(".jpeg")) {
            format = "JPEG";
        } else if (file.contains(".tga")) {
            format = "TGA";
        } else {
            System.err.println("Unrecognised format " + file.substring(file.indexOf('.')));
            System.exit(FILELOADER_LOADTEXTURE_FAILURE);
        }
        try {
            texture = TextureLoader.getTexture(format, new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(FILELOADER_LOADTEXTURE_FAILURE);
        }

        //assert texture != null;
        if (texture == null) {
            System.err.println("Error: unable to load texture " + file);
            System.exit(FILELOADER_LOADTEXTURE_FAILURE);
        }
        return texture.getTextureID();
    }
}
