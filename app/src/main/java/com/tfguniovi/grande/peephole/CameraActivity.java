package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;

public class CameraActivity extends Activity{
    private Camera camera; // camera object
    private TextView textTimeLeft; // time left field
    private MediaRecorder video;
    private EditText tiempo;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        textTimeLeft=(TextView)findViewById(R.id.textTimeLeft); // make time left object
        tiempo=(EditText) findViewById(R.id.segundos);

        try {
            camera = Camera.open();
        }catch (Exception ex){
            Toast.makeText(this,"No tienes los permisos necesarios revisar configuración",Toast.LENGTH_LONG).show();
        }
        SurfaceView view = new SurfaceView(this);

        try {
            camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
    }

    Camera.PictureCallback jpegCallBack=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // set file destination and file name
            boolean write = comprobar();
            if (write == false){
                Log.d("SD" , "Read Only , escribo en memoria interna");
            }
            //Si la sd es de solo lectura escribe en memoria interna
            File destination=new File(Environment.getExternalStorageDirectory(),"intruso.jpg");
            File destination1=new File(Environment.getExternalStorageDirectory(),"intruso1.jpg");
            File destination2=new File(Environment.getExternalStorageDirectory(),"intruso2.jpg");
            File destination3=new File(Environment.getExternalStorageDirectory(),"intruso3.jpg");
            File destination4=new File(Environment.getExternalStorageDirectory(),"intruso4.jpg");
            File destination5=new File(Environment.getExternalStorageDirectory(),"intruso5.jpg");
            File destination6=new File(Environment.getExternalStorageDirectory(),"intruso6.jpg");
            File destination7=new File(Environment.getExternalStorageDirectory(),"intruso7.jpg");
            File destination8=new File(Environment.getExternalStorageDirectory(),"intruso8.jpg");
            File destination9=new File(Environment.getExternalStorageDirectory(),"intruso9.jpg");
            File destination10=new File(Environment.getExternalStorageDirectory(),"intruso10.jpg");
            File destination11=new File(Environment.getExternalStorageDirectory(),"intruso11.jpg");




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

    public boolean comprobar() {
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible = true;
            sdAccesoEscritura = true;
        }

        if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            sdDisponible = true ;
            sdAccesoEscritura = false;
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

        return sdAccesoEscritura;

    }

    public void startTimer(View v){

        /*String valor1=tiempo.getText().toString();
        //String valor2=et2.getText().toString();
        int nro1=Integer.parseInt(valor1);*/

        // 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
        new CountDownTimer(5000,1000){

            @Override
            public void onFinish() {
                // count finished
                textTimeLeft.setText("Picture Taken");
                camera.takePicture(null, null, null, jpegCallBack);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // every time 1 second passes
                textTimeLeft.setText("Seconds Left: "+millisUntilFinished/1000);
            }

        }.start();
    }

}