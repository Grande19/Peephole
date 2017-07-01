package com.tfguniovi.grande.peephole;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FotoService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tfguniovi.grande.peephole.action.FOO";
    private static final String ACTION_BAZ = "com.tfguniovi.grande.peephole.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tfguniovi.grande.peephole.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tfguniovi.grande.peephole.extra.PARAM2";
    private  Camera camera;

    public FotoService() {
        super("FotoService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FotoService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FotoService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {

                    Log.d("CAMARA" , "Generado");
                camera = Camera.open();
            }catch (Exception ex){

            }
            //SurfaceView view = new SurfaceView(FotoService.this);

            /*try {
               // camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            camera.startPreview();
            startTimer();
        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


    Camera.PictureCallback jpegCallBack=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // set file destination and file name


            //Si la sd es de solo lectura escribe en memoria interna
            File destination=new File(Environment.getExternalStorageDirectory(),"intruso.jpg");





            Log.d("Des" , "Generado");
            try {
                Bitmap userImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                // set file out stream
                FileOutputStream out = new FileOutputStream(destination);
                // set compress format quality and stream
                userImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };


    public void startTimer(){

        /*String valor1=tiempo.getText().toString();
        //String valor2=et2.getText().toString();
        int nro1=Integer.parseInt(valor1);*/

        // 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
        new CountDownTimer(5000,1000){

            @Override
            public void onFinish() {
                // count finished

                camera.takePicture(null, null, null, jpegCallBack);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // every time 1 second passes

            }

        }.start();
    }


}
