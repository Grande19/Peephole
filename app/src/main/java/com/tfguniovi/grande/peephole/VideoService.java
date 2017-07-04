package com.tfguniovi.grande.peephole;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class VideoService extends IntentService implements SurfaceHolder.Callback,
        MediaPlayer.OnInfoListener , MediaPlayer.OnErrorListener, MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener  {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tfguniovi.grande.peephole.action.FOO";
    private static final String ACTION_BAZ = "com.tfguniovi.grande.peephole.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tfguniovi.grande.peephole.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tfguniovi.grande.peephole.extra.PARAM2";


    private MediaRecorder recorderVideo;


    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
    int iterator = 0;
    private VideoView videoView = null;
    private SurfaceHolder holder;
    private Camera camera = null;
    private String outputFilename ;

    public VideoService() {
        super("VideoService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, VideoService.class);
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
        Intent intent = new Intent(context, VideoService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        outputFilename = Environment.getExternalStorageDirectory() + "/intruso_video.mp4";
        if (intent != null) {
            //beginAudio();
            initCamera();

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


    public void temporizaVideo() {
        Log.d("TEMPORIZADOR ","tus webas de video");
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                stopRecording();
            }

            @Override
            public void onFinish() {
                stopRecording();
            }
        }.start();
    }







    private  void beginRecording(){

        //Abre el archivo en la ruta que le especificamos

        String nombrepath = String.valueOf(iterator)+"intruso.mp4";

        //OUTPUT_FILE_AUDIO  = Environment.getExternalStorageDirectory() + nombrepath;
        File destination=new File(Environment.getExternalStorageDirectory(),nombrepath);
        iterator++;
        recorderVideo = new MediaRecorder();
        if(destination.exists()){
            path();
        nombrepath = String.valueOf(iterator)+"intruso.mp4";
        destination=new File(Environment.getExternalStorageDirectory(),nombrepath);
        }


        try {
            camera.stopPreview();
            camera.unlock();
            recorderVideo.setCamera(camera);
            recorderVideo.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorderVideo.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorderVideo.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorderVideo.setVideoFrameRate(15);
            recorderVideo.setVideoSize(176,144);
            recorderVideo.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorderVideo.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorderVideo.setOutputFile(String.valueOf(destination));
            recorderVideo.prepare();
            recorderVideo.start();
            temporizaVideo();


        }catch (Exception ex){

        }
    }
    public int path(){
        iterator=(int)(Math.random());
        return iterator;

    }




    public void stopRecording(){
        if(recorderVideo!=null)
            recorderVideo.setOnErrorListener(null);
        recorderVideo.setOnInfoListener(null);
        try {
            recorderVideo.stop();
            Log.d("AUDIO","STOP AUDIO");
        }catch (IllegalStateException e){
            //Log.e("ERROR" , "Fallo al parar de grabar");
        }
        releaseRecorder();
        releaseCamera();

    }

    private void releaseRecorder() {
        if(recorderVideo != null){
            recorderVideo.release();
            recorderVideo = null;
        }
    }

    private void releaseCamera(){
        if(camera != null){
            try {
                camera.reconnect();

            }catch (Exception e){
                e.printStackTrace();
            }
            camera.release();
            camera=null;
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("SCreated" , "in surface created");
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }



    public boolean initCamera(){
        try{
            camera = Camera.open();
            Camera.Parameters camParams = camera.getParameters();
            camera.lock();
            holder = videoView.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            beginRecording();
        }
        catch (RuntimeException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {

    }



}
