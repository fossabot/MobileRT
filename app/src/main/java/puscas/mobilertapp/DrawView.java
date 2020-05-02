package puscas.mobilertapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import java8.util.Optional;
import puscas.mobilertapp.exceptions.LowMemoryException;
import puscas.mobilertapp.utils.ConstantsError;
import puscas.mobilertapp.utils.ConstantsMethods;
import puscas.mobilertapp.utils.ConstantsRenderer;
import puscas.mobilertapp.utils.ConstantsToast;
import puscas.mobilertapp.utils.State;

/**
 * The {@link GLSurfaceView} to show the scene being rendered.
 */
public final class DrawView extends GLSurfaceView {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(DrawView.class.getName());

    /**
     * The {@link GLSurfaceView.Renderer}.
     */
    private final MainRenderer renderer = new MainRenderer();

    /**
     * @see Activity#isChangingConfigurations()
     */
    private boolean changingConfigs = false;

    /**
     * The {@link ExecutorService} which holds {@link ConstantsRenderer#NUMBER_THREADS} number of threads that will
     * create Ray Tracer engine renderer.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(ConstantsRenderer.NUMBER_THREADS);

    /**
     * The last task submitted to {@link ExecutorService}.
     */
    private Future<Boolean> lastTask = null;

    /**
     * The constructor for this class.
     *
     * @param context The context of the Android system.
     */
    public DrawView(@Nonnull final Context context) {
        super(context);

        this.renderer.prepareRenderer(this::requestRender);
        initEGLContextFactory();
    }

    /**
     * The constructor for this class.
     *
     * @param context The context of the Android system.
     * @param attrs   The attributes of the Android system.
     */
    public DrawView(@Nonnull final Context context, @Nonnull final AttributeSet attrs) {
        super(context, attrs);

        this.renderer.prepareRenderer(this::requestRender);
    }

    /**
     * Helper method which initiates the {@link GLSurfaceView.EGLContextFactory}.
     */
    private void initEGLContextFactory() {
        this.changingConfigs = false;

        final GLSurfaceView.EGLContextFactory eglContextFactory = new MyEGLContextFactory(this);
        setEGLContextClientVersion(MyEGLContextFactory.EGL_CONTEXT_CLIENT_VERSION);
        setEGLContextFactory(eglContextFactory);
    }

    /**
     * Stops the Ray Tracer engine and sets its {@link State} to {@link State#STOP}.
     */
    private native void rtStopRender();

    /**
     * Sets the Ray Tracer engine {@link State} to {@link State#BUSY}.
     */
    private native void rtStartRender();

    /**
     * Gets the number of lights in the scene.
     *
     * @return The number of lights.
     */
    private native int rtGetNumberOfLights();

    /**
     * Helper method which gets the instance of the {@link Activity}.
     *
     * @return The current {@link Activity}.
     */
    @Nonnull
    private Activity getActivity() {
        Context context = getContext();
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        throw new IllegalStateException(ConstantsError.UNABLE_TO_FIND_AN_ACTIVITY + context);
    }

    /**
     * Sets the {@link DrawView#renderer} as the {@link GLSurfaceView.Renderer} of this object.
     *
     * @param textView        The {@link TextView} to set in the {@link DrawView#renderer}.
     * @param activityManager The {@link ActivityManager} to set in the {@link DrawView#renderer}.
     */
    void setViewAndActivityManager(final TextView textView, final ActivityManager activityManager) {
        this.renderer.setTextView(textView);
        this.renderer.setActivityManager(activityManager);
        setRenderer(this.renderer);
    }

    /**
     * Stops the Ray Tracer engine and waits for it to stop rendering.
     */
    void stopDrawing() {
        LOGGER.info("stopDrawing");

        this.lastTask.cancel(false);
        waitLastTask();
        rtStopRender();
        this.renderer.waitLastTask();
        this.renderer.updateButton(R.string.render);

        LOGGER.info("stopDrawing finished");
    }

    /**
     * Asynchronously creates the requested scene and starts rendering it.
     *
     * @param config     The ray tracer configuration.
     * @param numThreads The number of threads to be used in the Ray Tracer engine.
     * @param rasterize  Whether should show a preview (rasterize one frame) or not.
     */
    public void renderScene(
            @Nonnull final  Config config,
            final int numThreads,
            final boolean rasterize) {
        LOGGER.info(ConstantsMethods.RENDER_SCENE);

        waitLastTask();
        rtStartRender();

        this.lastTask = this.executorService.submit(() -> {
            LOGGER.info(ConstantsMethods.RENDER_SCENE);

            this.renderer.waitLastTask();

            try {
                createScene(config, numThreads, rasterize);
                requestRender();
                return Boolean.TRUE;
            } catch (final LowMemoryException ex) {
                warningError(ex, ConstantsToast.DEVICE_WITHOUT_ENOUGH_MEMORY);
            } catch (final RuntimeException ex) {
                warningError(ex, ConstantsToast.COULD_NOT_LOAD_THE_SCENE);
            }
            rtStopRender();
            this.renderer.rtFinishRender();
            this.renderer.updateButton(R.string.render);
            return Boolean.FALSE;
        });
        this.renderer.updateButton(R.string.stop);
    }

    /**
     * Waits for the result of the last task submitted to the {@link ExecutorService}.
     */
    private void waitLastTask() {
        Optional.ofNullable(this.lastTask)
            .ifPresent(task -> {
                try {
                    task.get(1L, TimeUnit.DAYS);
                } catch (final ExecutionException | TimeoutException | RuntimeException ex) {
                    LOGGER.warning(Strings.nullToEmpty(ex.getMessage()));
                } catch (final InterruptedException ex) {
                    LOGGER.warning(Strings.nullToEmpty(ex.getMessage()));
                    Thread.currentThread().interrupt();
                }
            });
    }

    /**
     * Loads the scene and creates the Ray Tracer renderer.
     *
     * @param config     The ray tracer configuration.
     * @param numThreads The number of threads to be used in the Ray Tracer engine.
     * @param rasterize  Whether should show a preview (rasterize one frame) or not.
     */
    private void createScene(
            final Config config,
            final int numThreads,
            final boolean rasterize) throws LowMemoryException {
        LOGGER.info("createScene");

        this.renderer.freeArrays();

        final int numPrimitives = this.renderer.rtInitialize(config);
        this.renderer.resetStats(
            numThreads, config.getSamplesPixel(), config.getSamplesLight(), numPrimitives, rtGetNumberOfLights()
        );
        final int widthView = getWidth();
        final int heightView = getHeight();
        this.renderer.setBitmap(config.getWidth(), config.getHeight(), widthView, heightView, rasterize);
    }

    /**
     * A helper method that warnings the user about a system error.
     *
     * @param exception    The exception caught.
     * @param errorMessage The error message.
     */
    private void warningError(@Nonnull final Exception exception, final CharSequence errorMessage) {
        this.renderer.resetStats(-1, -1, -1, -1, -1);
        LOGGER.severe(exception.getClass() + ":" + exception.getMessage());
        post(() -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show());
    }

    /**
     * Gets the {@link MainRenderer}.
     *
     * @return The {@link MainRenderer} of this object.
     */
    @Contract(pure = true)
    MainRenderer getRenderer() {
        return this.renderer;
    }

    /**
     * Gets the {@link Activity#isChangingConfigurations()}.
     *
     * @return The {@link DrawView#changingConfigs}.
     */
    @Contract(pure = true)
    boolean isChangingConfigs() {
        return this.changingConfigs;
    }

    @Override
    public void onPause() {
        super.onPause();
        LOGGER.info("onPause");

        final Activity activity = getActivity();
        this.changingConfigs = activity.isChangingConfigurations();
    }

    @Override
    public void onWindowFocusChanged(final boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        LOGGER.info("onWindowFocusChanged");

        if (hasWindowFocus && getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LOGGER.info(ConstantsMethods.ON_DETACHED_FROM_WINDOW);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        LOGGER.info("performClick");

        return true;
    }
}
