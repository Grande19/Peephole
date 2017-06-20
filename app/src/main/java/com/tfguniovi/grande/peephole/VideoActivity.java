

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
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback ,
        MediaPlayer.OnInfoListener , MediaPlayer.OnErrorListener, MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private MediaRecorder recorderVideo;
    private MediaRecorder recorderAudio;
    private String OUTPUT_FILE , OUTPUT_FILE_AUDIO;
    Button startBtn =null , stopBtn=null ,
            initBtn = null , playBtn=null , stopPlayBtn ;
    Button startaudio , stopaudio;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
    int i = 0;
    private VideoView videoView = null;
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private String outputFilename ;



    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 2 ;
    private static final int MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT = 1 ;



    //Lugar donde se almacenará el audio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        OUTPUT_FILE_AUDIO = Environment.getExternalStorageDirectory()+"/intruso_audio.3gpp";
        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/intruso_video.mp4";
        startBtn = (Button) findViewById(R.id.empezar);
        stopBtn = (Button) findViewById(R.id.acabar);
        playBtn = (Button) findViewById(R.id.rep);
        videoView = (VideoView)this.findViewById(R.id.videoView);
        startaudio = (Button) findViewById(R.id.startaudio);
        stopaudio = (Button) findViewById(R.id.stopaudio);

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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > i && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                    // If request is cancelled, the result arrays are empty.
                if (grantResults.length > i  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }


    public void start_audio (View v) throws IOException {

       beginAudio();
        //ditchMediaRecorder();
    }

    public void stop_audio(View v){
        stopRecordingAudio();
    }
    public void stop(View v){
        stopRecording();
    }

    public void stopRecordingAudio(){
        if(recorderAudio!=null)
            Toast.makeText(this, "Deteniendo grabación de audio", Toast.LENGTH_LONG).show();
            recorderAudio.stop();
            startaudio.setEnabled(true);
    }

    public void rep(View v){
        playRecording();
    }

    public void start (View v) throws IOException {
        beginRecording();

        //ditchMediaRecorder();
    }
    private void beginAudio(){
        File outFileaudio = new File(OUTPUT_FILE_AUDIO);

        if(outFileaudio.exists())
            outFileaudio.delete();

        try {
            recorderAudio = new MediaRecorder();
            //Grabamos del micro del movil
            recorderAudio.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorderAudio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorderAudio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorderAudio.setOutputFile(OUTPUT_FILE_AUDIO);
            recorderAudio.prepare();
            Toast.makeText(this, "Grabando audio ", Toast.LENGTH_LONG).show();
            recorderAudio.start();
            startaudio.setEnabled(false);

        } catch (Exception e){

        }

    }


    private  void beginRecording(){
        if (recorderVideo != null) return;
        outputFilename = Environment.getExternalStorageDirectory()+"/intruso.mp4";
        //Abre el archivo en la ruta que le especificamos
        File outFile = new File(outputFilename);
        if(outFile.exists())
            outFile.delete();

        try {
            camera.stopPreview();
            camera.unlock();
            recorderVideo = new MediaRecorder();
            recorderVideo.setCamera(camera);
            recorderVideo.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorderVideo.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorderVideo.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorderVideo.setVideoFrameRate(15);
            recorderVideo.setVideoSize(176,144);
            recorderVideo.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorderVideo.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorderVideo.setOutputFile(outputFilename);
            recorderVideo.prepare();
        }catch (Exception ex){

        }


        recorderVideo.setOnInfoListener(this);
        recorderVideo.setOnErrorListener(this);
        Toast.makeText(this, "Grabación de vídeo", Toast.LENGTH_LONG).show();
        recorderVideo.start();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);


    }







    public void stopRecording(){
        if(recorderVideo!=null)
            recorderVideo.setOnErrorListener(null);
            recorderVideo.setOnInfoListener(null);
            try {
                Toast.makeText(this, "Vídeo detenido", Toast.LENGTH_LONG).show();
                recorderVideo.stop();
            }catch (IllegalStateException e){
                //Log.e("ERROR" , "Fallo al parar de grabar");
            }
            releaseRecorder();
            releaseCamera();
            startBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
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

    public void playRecording(){
        android.widget.MediaController mc = new android.widget.MediaController(this);
        videoView.setMediaController(mc);
        videoView.setVideoPath(outputFilename);
        videoView.start();

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
