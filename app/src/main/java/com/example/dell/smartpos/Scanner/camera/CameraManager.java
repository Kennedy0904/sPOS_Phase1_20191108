package com.example.dell.smartpos.Scanner.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/12.
 */

//used for both preview and decoding   can modify scanning frame size in getFramingRect()
public final class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

//    private static final int MIN_FRAME_WIDTH = 240;
//    private static final int MIN_FRAME_HEIGHT = 240;
//    private static final int MAX_FRAME_WIDTH = 480;
//    private static final int MAX_FRAME_HEIGHT = 360;

    private static CameraManager cameraManager;

    static final int SDK_INT;
    static {
        int sdkInt;
        try {
            sdkInt = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException nfe) {
            // Just to be safe
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private boolean previewing;
    private final boolean useOneShotPreviewCallback;


    private final PreviewCallback previewCallback;

    private final AutoFocusCallback autoFocusCallback;

    public static void init(Context context) {
        if (cameraManager == null) {
            cameraManager = new CameraManager(context);
        }
    }

    public static CameraManager get() {
        return cameraManager;
    }

    private CameraManager(Context context) {

        this.context = context;
        this.configManager = new CameraConfigurationManager(context);

        useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3 = Cupcake

        previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
        autoFocusCallback = new AutoFocusCallback();
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);

            FlashlightManager.enableFlashlight();
        }
    }

    /**
     * Closes the camera driver if still in use.
     */
    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.release();
            camera = null;
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public void stopPreview() {
        if (camera != null && previewing) {
            if (!useOneShotPreviewCallback) {
                camera.setPreviewCallback(null);
            }
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            if (useOneShotPreviewCallback) {
                camera.setOneShotPreviewCallback(previewCallback);
            } else {
                camera.setPreviewCallback(previewCallback);
            }
        }
    }

    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            camera.autoFocus(autoFocusCallback);
        }
    }

    //moodify scanning frame size here
//    public Rect getFramingRect() {
//        Point screenResolution = configManager.getScreenResolution();
//        if (framingRect == null) {
//            if (camera == null) {
//                return null;
//            }
//            int width = screenResolution.x * 3 / 4;
//            if (width < MIN_FRAME_WIDTH) {
//                width = MIN_FRAME_WIDTH;
//            } else if (width > MAX_FRAME_WIDTH) {
//                width = MAX_FRAME_WIDTH;
//            }
//            int height = screenResolution.y * 3 / 4;
//            if (height < MIN_FRAME_HEIGHT) {
//                height = MIN_FRAME_HEIGHT;
//            } else if (height > MAX_FRAME_HEIGHT) {
//                height = MAX_FRAME_HEIGHT;
//            }
//            int leftOffset = (screenResolution.x - width) / 2;
//            int topOffset = (screenResolution.y - height) / 2;
//            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
//            Log.d(TAG, "Calculated framing rect: " + framingRect);
//        }
//        return framingRect;
//    }
    public Rect getFramingRect() {
        Point screenResolution = configManager.getScreenResolution();
        if (framingRect == null) {
            if (camera == null) {
                return null;
            }

            //修改之后
            int width = screenResolution.x * 3 / 4;
            int height = screenResolution.x * 3 / 4;

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 3;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);


            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    public Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

//    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
//        Rect rect = getFramingRectInPreview();
//        int previewFormat = configManager.getPreviewFormat();
//        String previewFormatString = configManager.getPreviewFormatString();
//        switch (previewFormat) {
//            case PixelFormat.YCbCr_420_SP:
//            case PixelFormat.YCbCr_422_SP:
//                return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
//            default:
//                if ("yuv420p".equals(previewFormatString)) {
//                    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
//                }
//        }
//        throw new IllegalArgumentException("Unsupported picture format: " +
//                previewFormat + '/' + previewFormatString);
//    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = configManager.getPreviewFormat();
        String previewFormatString = configManager.getPreviewFormatString();
        switch (previewFormat) {
            // This is the standard Android format which all devices are REQUIRED to support.
            // In theory, it's the only one we should ever care about.
            case PixelFormat.YCbCr_420_SP:
                // This format has never been seen in the wild, but is compatible as we only care
                // about the Y channel, so allow it.
            case PixelFormat.YCbCr_422_SP:
                return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height);
            default:
                // The Samsung Moment incorrectly uses this variant instead of the 'sp' version.
                // Fortunately, it too has all the Y data up front, so we can read it.
                if ("yuv420p".equals(previewFormatString)) {
                    return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height);
                }
        }
        throw new IllegalArgumentException("Unsupported picture format: " +
                previewFormat + '/' + previewFormatString);
    }

    public Context getContext() {
        return context;
    }

}
