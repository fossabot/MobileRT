package puscas.mobilertapp.utils;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import puscas.mobilertapp.DrawView;
import puscas.mobilertapp.MainRenderer;
import puscas.mobilertapp.RenderTask;

/**
 * Utility class with the constants for the {@link GLSurfaceView.Renderer}.
 */
public final class ConstantsRenderer {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(ConstantsRenderer.class.getName());

    /**
     * The name for the attribute location of vertex positions in {@link GLES20}.
     */
    public static final String VERTEX_POSITION = "vertexPosition";

    /**
     * The name for the attribute location of texture coordinates in {@link GLES20}.
     */
    public static final String VERTEX_TEX_COORD = "vertexTexCoord";

    /**
     * The name for the attribute location of texture colors in {@link GLES20}.
     */
    public static final String VERTEX_COLOR = "vertexColor";

    /**
     * The number of threads to be used by the {@link DrawView}, {@link MainRenderer} and {@link RenderTask}
     * {@link ExecutorService}.
     */
    public static final int NUMBER_THREADS = 1;

    /**
     * The OpenGL ES version required to run this application.
     */
    public static final int REQUIRED_OPENGL_VERSION = 0x20000;

    /**
     * A private constructor in order to prevent instantiating this helper class.
     */
    private ConstantsRenderer() {
        LOGGER.info("ConstantsRenderer");
    }
}
