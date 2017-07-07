package com.tfguniovi.grande.peephole;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * An {@link IntentService} para grabar audio
 *
 *
 */
public class AudioService extends IntentService {


    private MediaRecorder recorderAudio;
    int iterator = 0 ;
    boolean isRecording = false;
    public AudioService() {
        super("AudioService");
    }


    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AudioService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(isRecording){
                try {
                    recorderAudio.stop();
                }catch (RuntimeException e){

                }
            }releaseRecorder();

        }
        beginAudio();
    }

    public void releaseRecorder(){
        if (recorderAudio != null) {
            // clear recorder configuration
            recorderAudio.reset();
            // release the recorder object
            recorderAudio.release();
            recorderAudio = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
        }
    }

    private void beginAudio(){

        String nombrepath = String.valueOf(iterator)+"intruso_audio.3gpp";
        File destination=new File(Environment.getExternalStorageDirectory(),nombrepath);

        if (destination.exists()) {
            iterator++;
            nombrepath = String.valueOf(iterator)+"intruso_audio.3gpp";
            destination=new File(Environment.getExternalStorageDirectory(),nombrepath);
            iterator++;
        }
        try {
            recorderAudio = new MediaRecorder();
            //Grabamos del micro del movil
            recorderAudio.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorderAudio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorderAudio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorderAudio.setOutputFile(String.valueOf(destination));
            recorderAudio.setMaxDuration(9000);
            recorderAudio.prepare();
            recorderAudio.start();
            isRecording=true;
            temporizaAudio();
        } catch (Exception e){
        }

    }

    public void temporizaAudio() {
        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                recorderAudio.stop();
                recorderAudio.release();
                isRecording=false;
                Log.d("AUDIO","STOP AUDIO");
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}