package com.tfguniovi.grande.peephole;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothService extends Service {
    private Set<BluetoothDevice> pairedDevices;
    public BluetoothDevice device;
    BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private BroadcastReceiver mReciever , gReciever , gpsReceiver ;
    final ArrayList rssi_list = new ArrayList();
    private ArrayList dispositivos = new ArrayList();
    public Double longitud=null , latitud=null ;
    public int cont=0 ;
    ProgressDialog pdialog = null;
    boolean coordenadas = true;


    public BluetoothService() {
    }

    public void onCreate(){
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        Log.d("ISERV" , "Entra Iserv");
        //startGps();}
       // for(int i = 0 ; i==1000000 ; i++) {
            pairedDevices = BA.getBondedDevices();
            BA.startDiscovery(); //start looking for new devices
            Log.d("DES", "Starting discover new devices");
            Toast.makeText(this, "Discovering devices", Toast.LENGTH_LONG).show();
            discovering();
        }


    public void discovering(){
        Log.d("DISCOVER" , "dis");
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //Get the bluetoothDevice from the intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (pairedDevices.contains(device) == false) {
                        cont++;
                        dispositivos.add(device);
                        //señal de bluetooth recibida
                        int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                        //dispositivos.add(rssi);
                        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                        //Log.d("DESCUBIERTO", name);
                        String address = device.getAddress();
                        fichero(rssi, address, name);

                    }//if paired
                    //bucle();


                }//if
                //bucle();

            }//onReceive
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }


        //}//while
    //discovering

    public void send(){
        Intent i = new Intent("android.intent.action.INTRUSOS").putExtra("dispositivos",dispositivos);
        //Se envian los valores de latitud y longitud a la MainActivity
        sendBroadcast(i);
    }


    private void startGps(){
        Log.d("START" , "start");
        Intent service = new Intent(this , LocationService.class);
        startService(service);
        //onCreate();
    }

    private void stopGps(){
        Log.d("STOP" , "stop");
        Intent service = new Intent(this , LocationService.class);
        stopService(service);
    }

    public void getCoordenadas() {


        Log.d("GPS", "entra en gps");
        //LocationService location = new LocationService(getApplicationContext());
        //Intent service = new Intent(this,LocationService.class);
        //location.onStart(service , 1 );

        BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                longitud = intent.getDoubleExtra("longitud",00000);
                latitud = intent.getDoubleExtra("latitud",00000);
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.intent.action.GPS");
        registerReceiver(gpsReceiver,intentFilter);
        stopGps();

    }

    public void fichero(int rssi , String add,String nom){



        try {

            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            //if(longitud!=null && latitud!=null ){
            //stopGps();

            //if(longitud!=null && latitud!=null && cont==0) {
                //stopGps();
                Log.d("DESCUBRE : nombre_disp" , nom);
                File ruta_sd = Environment.getExternalStorageDirectory();
                File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_APPEND));
                Log.d("Ficheros", "Escribiendo intruso.txt");
                fout.write("Dispositivo :" + nom + "\n Direccion MAC :" + add + "\nIntensidad :" + rssi + "dBm"
                        + "\nUbicacion:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
                fout.close();
            coordenadas=false;
            discovering();



        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }




    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //mLocationManager = (LocationManager) getApplicationContext().getSystemService(this.LOCATION_SERVICE);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return START_STICKY;


        //return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;



        //lo que quieras que haga tu servicio
    }

    public void onStart(Intent intent, int startId){
        System.out.println("El servicio a comenzado");
        this.stopSelf();
    }


    @Override
    public void onDestroy(){
        send();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        try{
            unregisterReceiver(mReciever);
            unregisterReceiver(gpsReceiver);
        }catch (Exception ex){
            Log.e("Reciever" , "Error");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
