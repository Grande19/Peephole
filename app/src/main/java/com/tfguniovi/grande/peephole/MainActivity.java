package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Set;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    private Button    botonDES , botonLocal , cam , list;
    private final static int REQUEST_ENABLE_BT = 1;
    ListView lv, ls;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice device;
    private BluetoothAdapter BA;
    private BroadcastReceiver mReciever , gReciever , gpsReceiver ;
    final ArrayList rssi_list = new ArrayList();
    private ArrayList dispositivos = new ArrayList();
    private ArrayList dispostivos_fin = new ArrayList();
    public Double longitud , latitud , ublat , ublong ;
    public String email1 , email2 , email3;
    public String[] correo;
    public boolean localizacion =false;
    ProgressDialog descub = null ;
    public int contador=0 , cont=0 ;
    File ruta_sd = Environment.getExternalStorageDirectory();
    File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
    public boolean finalizar = false;
    boolean running = true;
    //File f;
    //public LocationService location = new LocationService(getApplicationContext());






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BA = BluetoothAdapter.getDefaultAdapter();
        //lv = (ListView) findViewById(R.id.lvDispositivos);
        ls = (ListView) findViewById(R.id.DispositivosRSSI);
        botonDES = (Button) findViewById(R.id.add);
        cam = (Button) findViewById(R.id.cam);
        list = (Button) findViewById(R.id.lista);


        //encendiendo el bluetooth nada más acceder a la app
        //final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, rssi_list);
        //ls.setAdapter(adapter);


        if (!BA.isEnabled()) {
            Intent ONintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ONintent, REQUEST_ENABLE_BT);
            Toast.makeText(this, "Turning on Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bluetooth is already active", Toast.LENGTH_LONG).show();
        }

        getCoordenadas();
        //Se inicia el LocationService
        //startGps();

        //this.startActivity();
    }

    public void comenzar(View v){
        startGps();
       new Bluetooth().execute();

    }

    /*public void bucle(){
       new Bluetooth().execute();
       new Bluetooth().cancel(finalizar);
    }*/
//Clase para ejecutar la tarea de descubrimiento de Bluetooth en segundo plano
    class Bluetooth extends AsyncTask<Void , Void , ArrayList >{


        @Override
        protected ArrayList doInBackground(Void... params) {


            while (running) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (BA.isDiscovering()) {
                    // El Bluetooth ya está en modo discover, lo cancelamos para iniciarlo de nuevo
                    BA.cancelDiscovery();
                }
                BA.startDiscovery();
                pairedDevices = BA.getBondedDevices();
                Log.d("DESCUBRIENDO", "DESCUBRIENDO WEBAS");

                //BA.startDiscovery();
                final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (BluetoothDevice.ACTION_FOUND.equals(action) && pairedDevices.contains(device) == false) {
                            //Get the bluetoothDevice from the intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            cont++;

                            //señal de bluetooth recibida
                            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                            //dispositivos.add(rssi);
                            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                            if(dispositivos.contains(device)==false) {
                                dispositivos.add(device + "->" + name);
                            }
                            //Log.d("DESCUBIERTO", name);
                            String address = device.getAddress();
                            // Log.d("DISPOSITIVO ->" , name);
                            fichero(rssi, address, name);
                        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                            Log.d("FINISHED", "Descubriendo otra vez");
                            BA.startDiscovery();
                            //discovering();
                        } else {
                            Log.d("NOT FOUND", "No encuentra");
                        }


                    }//onReceive
                };
                if (isCancelled()) {
                }
                ;
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);


            }
            return dispositivos;
        }


        @Override
        protected void onPreExecute(){
            Log.d("ASYCN" , "Doing ASYCN Task");
        }

       @Override
        protected void onCancelled(){
           Log.d("FIN" , "Acaba");
           running = false;
          // unregisterReceiver(mReciever);

       }

    } //Fin de AsynTask


    public void fichero(int rss , String add , String name){
        try {

            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            //if(longitud!=null && latitud!=null ){
            //stopGps();

            //if(longitud!=null && latitud!=null && cont==0) {
            //stopGps();
            Log.d("DESCUBRE : nombre_disp" , name);
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_APPEND));
            Log.d("Ficheros", "Escribiendo intruso.txt");
            fout.write("Dispositivo :" + name + "\n Direccion MAC :" + add + "\nIntensidad :" + rss + "dBm"
                    + "\nUbicacion:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
            fout.close();

            //bucle();
            new Bluetooth().execute();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }
    }//if


    public void list(View v){
        dispostivos_fin = dispositivos;
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, dispositivos);
        ls.setAdapter(adapter);

    }

    public void fin (View view){
        finalizar = true ;
        running = false;
        dispostivos_fin = dispositivos;
        stopGps();
        //Bluetooth.onCancelled();
        Intent intent = new Intent(MainActivity.this , FinalizarActivity.class);
        intent.putCharSequenceArrayListExtra("lista",dispostivos_fin);
        startActivity(intent);
        //descub.dismiss();
        //finish();
    }

private void startGps(){
    Intent Gservice = new Intent(this,LocationService.class);
    stopService(Gservice);
}




    private void stopGps(){
        Intent Gservice = new Intent(this,LocationService.class);
        stopService(Gservice);
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
    }

    public void onPause() {
        super.onPause();
        try{
            unregisterReceiver(gpsReceiver);
            unregisterReceiver(mReciever);
        }catch (Exception ex){
            Log.e("Reciever" , "Error");
        }

    }

    //CameraActivity
    public void camera(View view){
        Intent intent = new Intent(MainActivity.this , CameraActivity.class);
        startActivity(intent);

    }




    public void enviar(View v){
        Intent intent = new Intent(MainActivity.this , MailActivity.class);
        startActivity(intent);
    }



}//MainActivity
