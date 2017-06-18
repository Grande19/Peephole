package com.tfguniovi.grande.peephole;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    private String OUTPUT_FILE;
    Button start , fin ;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
    int i = 0;


    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1 ;



    //Lugar donde se almacenará el audio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/intruso.3gpp";
        start = (Button) findViewById(R.id.empezar);
        fin = (Button) findViewById(R.id.acabar);

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
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
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
        permisos();
        //ditchMediaRecorder();
    }
    public void permisos(){

    }


    private void beginRecording() throws IOException {
        //ditchMediaRecorder();
        File outFile = new File (OUTPUT_FILE);

        if (outFile.exists())
            outFile.delete();


        recorder = new MediaRecorder();
        //Grabamos del micro del movil
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();
}

    public void stop (View v){
        stopRecording();
    }

    public void stopRecording(){
        if(recorder!=null)
            recorder.stop();
    }


}
