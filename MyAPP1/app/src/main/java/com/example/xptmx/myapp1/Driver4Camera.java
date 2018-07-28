package com.example.xptmx.myapp1;

import android.hardware.Camera;

public class Driver4Camera {
        private Camera camera;
        private static final String LOG_TAG = "Driver4Camera";

        public Driver4Camera() {
            camera=Camera.open();
        }

        public Camera getCamera() {
            return camera;
        }

        private void releaseCamera() {
            if (camera != null) {
                camera.release(); // release the camera for other applications
                camera = null;
            }
        }

        public void onPause() {
            releaseCamera();
        }

        public void onResume() {
            if (camera == null) {
                camera = getCameraInstance();
            }

        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private static Camera getCameraInstance() {
            Camera c = null;
            try {
                c = Camera.open(); // attempt to get a Camera instance
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
            }
            return c; // returns null if camera is unavailable
        }


    }
