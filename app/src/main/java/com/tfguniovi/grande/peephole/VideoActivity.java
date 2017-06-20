

package com.tfguniovi.grande.peephole;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.session.MediaController;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback ,
        MediaPlayer.OnInfoListener , MediaPlayer.OnErrorListener, MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String OUTPUT_FILE;
    Button startBtn =null , stopBtn=null ,
            initBtn = null , playBtn=null , stopPlayBtn ;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
    int i = 0;
    private VideoView videoView = null;
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private String outputFilename;



    private static final int MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT = 1 ;



    //Lugar donde se almacenará el audio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/intruso_video.mp4";
        startBtn = (Button) findViewById(R.id.empezar);
        stopBtn = (Button) findViewById(R.id.acabar);
        //initBtn = (Button) findViewById(R.id.ini);
        playBtn = (Button) findViewById(R.id.rep);
        videoView = (VideoView)this.findViewById(R.id.videoView);
        stopPlayBtn = (Button) findViewById(R.id.stoprev);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAPTURE_VIDEO_OUTPUT)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAPTURE_VIDEO_OUTPUT)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > i  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
    

    public void start (View v) throws IOException {
       beginRecording();
        //ditchMediaRecorder();
    }
    public void stop(View v){
        stopRecording();
    }

    public void rep(View v){
        playRecording();
    }

    public void st_rv(View v){
        stopPlayback();
    }

    /*private void initRecorder() {
        if (recorder != null) return;
        outputFilename = Environment.getExternalStorageDirectory()+"/intruso.mp4";
        //Abre el archivo en la ruta que le especificamos
        File outFile = new File(outputFilename);
        if(outFile.exists())
            outFile.delete();

        try {
            camera.stopPreview();
            camera.unlock();
            recorder = new MediaRecorder();
            recorder.setCamera(camera);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoFrameRate(15);
            recorder.setVideoSize(176,144);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(outputFilename);
            recorder.prepare();
            initBtn.setEnabled(false);
        }catch (Exception ex){

        }

    }*/

    private  void beginRecording(){
        if (recorder != null) return;
        outputFilename = Environment.getExternalStorageDirectory()+"/intruso.mp4";
        //Abre el archivo en la ruta que le especificamos
        File outFile = new File(outputFilename);
        if(outFile.exists())
            outFile.delete();

        try {
            camera.stopPreview();
            camera.unlock();
            recorder = new MediaRecorder();
            recorder.setCamera(camera);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoFrameRate(15);
            recorder.setVideoSize(176,144);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(outputFilename);
            recorder.prepare();
            initBtn.setEnabled(false);
        }catch (Exception ex){

        }


        recorder.setOnInfoListener(this);
        recorder.setOnErrorListener(this);
        recorder.start();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);


    }







    public void stopRecording(){
        if(recorder!=null)
            recorder.setOnErrorListener(null);
            recorder.setOnInfoListener(null);
            try {
                recorder.stop();
            }catch (IllegalStateException e){
                Log.e("ERROR" , "Fallo al parar de grabar");
            }
            releaseRecorder();
            releaseCamera();
            startBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
    }

    private void releaseRecorder() {
        if(recorder != null){
            recorder.release();
            recorder = null;
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

    public void playRecording(){
        android.widget.MediaController mc = new android.widget.MediaController(this);
        videoView.setMediaController(mc);
        videoView.setVideoPath(outputFilename);
        videoView.start();
        stopPlayBtn.setEnabled(false);

    }

    public void stopPlayback(){
        videoView.stopPlayback();

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
        startBtn.setEnabled(true);

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

    @Override
    protected void onResume(){
        super.onResume();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(false);
        //initBtn.setEnabled(false);
        if(!initCamera())
            finish();
    }

    public boolean initCamera(){
        try{
            camera = Camera.open();
            Camera.Parameters camParams = camera.getParameters();
            camera.lock();
            holder = videoView.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
