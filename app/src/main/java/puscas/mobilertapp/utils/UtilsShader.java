package puscas.mobilertapp.utils;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import puscas.mobilertapp.exceptions.FailureException;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;

public final class UtilsShader {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilsShader.class.getName());

    /**
     * A private constructor in order to prevent instantiating this helper class.
     */
    private UtilsShader() {
    }

    /**
     * Helper method that attaches some GLSL shaders into an OpenGL program.
     *
     * @param shaderProgram      The index of OpenGL shader program.
     * @param vertexShaderCode   The code for the Vertex shader.
     * @param fragmentShaderCode The code for the Fragment shader.
     */
    public static void attachShaders(final int shaderProgram,
                                     @Nonnull final String vertexShaderCode,
                                     @Nonnull final String fragmentShaderCode) {
        LOGGER.info("attachShaders");
        final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode);

        // Attach and link shaders to program
        UtilsGL.run(() -> GLES20.glAttachShader(shaderProgram, vertexShader));
        UtilsGL.run(() -> GLES20.glAttachShader(shaderProgram, fragmentShader));
        UtilsGL.run(() -> GLES20.glLinkProgram(shaderProgram));

        final int[] attachedShaders = new int[1];
        UtilsGL.run(() -> GLES20.glGetProgramiv(shaderProgram, GLES20.GL_ATTACHED_SHADERS,
            attachedShaders, 0));

        checksShaderLinkStatus(shaderProgram);
    }

    /**
     * Helper method that checks the OpenGL shader link status in a program.
     *
     * @param shaderProgram The OpenGL shader program index.
     */
    public static void checksShaderLinkStatus(final int shaderProgram) {
        LOGGER.info("checksShaderLinkStatus");
        final int[] linkStatus = new int[1];
        UtilsGL
            .run(() -> GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0));

        if (linkStatus[0] != GLES20.GL_TRUE) {
            final String strError = UtilsGL.run(shaderProgram, GLES20::glGetProgramInfoLog);
            final String msg = "Could not link program shader: " + strError;
            UtilsGL.run(() -> GLES20.glDeleteProgram(shaderProgram));
            throw new FailureException(msg);
        }
    }

    /**
     * Helper method which loads an OpenGL shader.
     *
     * @param shaderType The type of the shader (vertex or fragment shader).
     * @param source     The code of the shader.
     * @return The OpenGL index of the shader.
     */
    public static int loadShader(final int shaderType,
                                 @Nonnull final String source) {
        LOGGER.info("loadShader");
        final int shader = UtilsGL.run(() -> glCreateShader(shaderType));
        if (shader == 0) {
            final String msg = "There was an error while creating the shader object.";
            throw new FailureException(msg);
        }

        UtilsGL.run(() -> glShaderSource(shader, source));
        UtilsGL.run(() -> glCompileShader(shader));
        final int[] compiled = new int[1];
        UtilsGL.run(() -> glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0));
        if (compiled[0] == 0) {
            final String informationLog = UtilsGL.run(() -> glGetShaderInfoLog(shader));
            final String msg = "Could not compile shader " + shaderType + ": " + informationLog;
            LOGGER.severe(msg);
            LOGGER.severe(source);
            UtilsGL.run(() -> glDeleteShader(shader));
            throw new FailureException(informationLog);
        }

        return shader;
    }

    /**
     * Helper method that initializes a GLSL program.
     * It deletes the previous GLSL program if it was created.
     *
     * @param shaderProgram The OpenGL shader program index to recreate.
     * @return A new created OpenGL shader program index.
     */
    public static int reCreateProgram(final int shaderProgram) {
        LOGGER.info("reCreateProgram");
        if (shaderProgram != 0) {
            final String deleteProgramMessage = "Deleting GL program: " + shaderProgram;
            LOGGER.info(deleteProgramMessage);
            UtilsGL.run(() -> GLES20.glDeleteProgram(shaderProgram));
        }
        final int newShaderProgram = UtilsGL.<Integer>run(() -> GLES20.glCreateProgram());

        if (newShaderProgram == 0) {
            LOGGER.severe("Could not create GL program.");
            final String programInfo = GLES20.glGetProgramInfoLog(0);
            throw new FailureException(programInfo);
        }

        return newShaderProgram;
    }

    /**
     * Helper method that binds and enables an OpenGL attribute.
     *
     * @param buffer             The {@link Buffer} with the data to bind.
     * @param attributeLocation  The desired location of the OpenGL attribute.
     * @param shaderProgram      The shader program index.
     * @param componentsInBuffer The number of components in each cell in the
     *                           {@link ByteBuffer}.
     * @param attributeName      The name of the OpenGL attribute.
     */
    public static void connectOpenGlAttribute(@Nonnull final Buffer buffer,
                                              final int attributeLocation,
                                              final int shaderProgram,
                                              final int componentsInBuffer,
                                              @Nonnull final String attributeName) {
        LOGGER.info("connectOpenGlAttribute");
        UtilsGL.run(() -> GLES20.glBindAttribLocation(
            shaderProgram, attributeLocation, attributeName));
        UtilsGL.run(() -> GLES20.glVertexAttribPointer(attributeLocation,
            componentsInBuffer, GLES20.GL_FLOAT, false, 0, buffer));
        UtilsGL.run(() -> GLES20.glEnableVertexAttribArray(attributeLocation));
    }

}
