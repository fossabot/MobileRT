package puscas.mobilertapp.utils;

import android.graphics.Bitmap;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

public final class UtilsGLMatrices {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilsGLMatrices.class.getName());

    /**
     * Empirical value that makes the OpenGL perspective camera more similar
     * to the camera from the Ray Tracing engine.
     * This value is a multiplier of the FOV values.
     */
    private static final float FIX_ASPECT_PERSPECTIVE = 0.955F;

    /**
     * Empirical value that makes the OpenGL orthographic camera more similar
     * to the camera from the Ray Tracing engine.
     * This values is a multiplier of the size values.
     */
    private static final float FIX_ASPECT_ORTHOGRAPHIC = 0.5F;

    /**
     * The minimum clipping bounds of a scene.
     */
    private static final float Z_NEAR = 0.1F;

    /**
     * The maximum clipping bounds of a scene.
     */
    private static final float Z_FAR = 1.0e+30F;

    /**
     * A private constructor in order to prevent instantiating this helper class.
     */
    private UtilsGLMatrices() {
        LOGGER.info("UtilsGLMatrices");
    }

    /**
     * Creates the model matrix and sets it as an identity matrix.
     *
     * @return A float array with the model matrix data.
     */
    @Nonnull
    public static float[] createModelMatrix() {
        LOGGER.info("createModelMatrix");

        final float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        return modelMatrix;
    }

    /**
     * Creates the projection matrix by using the camera's data from a
     * {@link ByteBuffer} read from the Ray Tracing engine.
     *
     * @param bbCamera The camera's data (like FOV or size).
     * @param width    The width of the {@link Bitmap} to render.
     * @param height   The height of the {@link Bitmap} to render.
     * @return A float array with the projection matrix data.
     */
    @Nonnull
    public static float[] createProjectionMatrix(@Nonnull final ByteBuffer bbCamera,
                                                 final int width,
                                                 final int height) {
        LOGGER.info("createProjectionMatrix");

        final float fovX =
            bbCamera.getFloat(16 * Constants.BYTES_IN_FLOAT) * FIX_ASPECT_PERSPECTIVE;
        final float fovY =
            bbCamera.getFloat(17 * Constants.BYTES_IN_FLOAT) * FIX_ASPECT_PERSPECTIVE;

        final float sizeH =
            bbCamera.getFloat(18 * Constants.BYTES_IN_FLOAT) * FIX_ASPECT_ORTHOGRAPHIC;
        final float sizeV =
            bbCamera.getFloat(19 * Constants.BYTES_IN_FLOAT) * FIX_ASPECT_ORTHOGRAPHIC;

        final float[] projectionMatrix = new float[16];

        // If the camera is a perspective camera.
        if (fovX > 0.0F && fovY > 0.0F) {
            final float aspectPerspective = (float) width / (float) height;
            Matrix.perspectiveM(projectionMatrix, 0, fovY, aspectPerspective, Z_NEAR, Z_FAR);
        }

        // If the camera is an orthographic camera.
        if (sizeH > 0.0F && sizeV > 0.0F) {
            Matrix.orthoM(projectionMatrix, 0, -sizeH, sizeH,
                -sizeV, sizeV, Z_NEAR, Z_FAR);
        }

        return projectionMatrix;
    }

    /**
     * Creates the view matrix by using the camera's data from a
     * {@link ByteBuffer} read from the Ray Tracing engine.
     *
     * @param bbCamera The camera's data (like eye, direction and up vector).
     * @return A float array with the view matrix data.
     */
    @Nonnull
    public static float[] createViewMatrix(@Nonnull final ByteBuffer bbCamera) {
        LOGGER.info("createViewMatrix");

        final float eyeX = bbCamera.getFloat(0);
        final float eyeY = bbCamera.getFloat(Constants.BYTES_IN_FLOAT);
        final float eyeZ = -bbCamera.getFloat(2 * Constants.BYTES_IN_FLOAT);

        final float dirX = bbCamera.getFloat(4 * Constants.BYTES_IN_FLOAT);
        final float dirY = bbCamera.getFloat(5 * Constants.BYTES_IN_FLOAT);
        final float dirZ = -bbCamera.getFloat(6 * Constants.BYTES_IN_FLOAT);

        final float upX = bbCamera.getFloat(8 * Constants.BYTES_IN_FLOAT);
        final float upY = bbCamera.getFloat(9 * Constants.BYTES_IN_FLOAT);
        final float upZ = -bbCamera.getFloat(10 * Constants.BYTES_IN_FLOAT);

        final float centerX = eyeX + dirX;
        final float centerY = eyeY + dirY;
        final float centerZ = eyeZ + dirZ;

        final float[] viewMatrix = new float[16];

        Matrix.setLookAtM(
            viewMatrix, 0,
            eyeX, eyeY, eyeZ,
            centerX, centerY, centerZ,
            upX, upY, upZ
        );

        return viewMatrix;
    }

}
