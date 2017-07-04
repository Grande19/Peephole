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
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AudioService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tfguniovi.grande.peephole.action.FOO";
    private static final String ACTION_BAZ = "com.tfguniovi.grande.peephole.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tfguniovi.grande.peephole.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tfguniovi.grande.peephole.extra.PARAM2";

    private MediaRecorder recorderAudio;
    int iterator = 1 ;
    public AudioService() {
        super("AudioService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AudioService.class);
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
        Intent intent = new Intent(context, AudioService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //beginAudio();
            beginAudio();

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



    private void beginAudio(){


        String nombrepath = String.valueOf(iterator)+"intruso_audio.3gpp";

        //OUTPUT_FILE_AUDIO  = Environment.getExternalStorageDirectory() + nombrepath;
        File destination=new File(Environment.getExternalStorageDirectory(),nombrepath);
        iterator++;
        if (destination.exists()){
            destination.delete();
        }



        try {
            recorderAudio = new MediaRecorder();
            //Grabamos del micro del movil
            recorderAudio.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorderAudio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorderAudio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorderAudio.setOutputFile(String.valueOf(destination));
            recorderAudio.prepare();
            recorderAudio.start();
            temporizaAudio();


        } catch (Exception e){

        }

    }

    public void temporizaAudio() {
        Log.d("TEMPORIZADOR","tus webas de audio");
        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if(recorderAudio!=null)
                    recorderAudio.stop();
                    recorderAudio.release();
                Log.d("AUDIO","STOP AUDIO");

            }

            @Override
            public void onFinish() {
                stopRecordingAudio();
            }
        }.start();
    }

    public void stopRecordingAudio(){
        if(recorderAudio!=null)
            recorderAudio.stop();
        Log.d("AUDIO","STOP AUDIO");

    }
    @Override
    public void onDestroy() {
        Log.d("SERVICIO" , "Servicio hecho");
        super.onDestroy();
    }






}
