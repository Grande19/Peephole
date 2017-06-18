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
    private BroadcastReceiver mReciever , gReciever , gpsReceiver , usuReceiver , disReceiver;
    final ArrayList rssi_list = new ArrayList();
    private ArrayList dispositivos = new ArrayList();
    private ArrayList dispostivos_fin = new ArrayList();
    public ArrayList trusted_device = new ArrayList();
    public ArrayList emails = new ArrayList();
    public ArrayList discover = new ArrayList();
    public Double longitud , latitud;
    public String dir1 , dir2 , dir3 , usu1 , usu2 , usu3;
    public boolean localizacion =false;
    File ruta_sd = Environment.getExternalStorageDirectory();
    File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
    public boolean finalizar = false;
    public boolean get = false ;
    boolean running = true;
    boolean coordenadas = false;
    public int cont=1;

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





        //Se inicia el LocationService
        //startGps();

        //this.startActivity();
    }

    public void comenzar(View v){
            //startGps();
            getCoordenadas();
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

            if (get == false){
                getTrustedDevice();
                get = true;
            }




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
                stopGps();
                Log.d("DESCUBRIENDO", "DESCUBRIENDO INTRUSOS");
                //BA.startDiscovery();
                final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (BluetoothDevice.ACTION_FOUND.equals(action) && pairedDevices.contains(device) == false) {
                            //Get the bluetoothDevice from the intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            //señal de bluetooth recibida
                            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                            String address = device.getAddress();
                            discover.add(name);
                            /***pairedDevices = Lista de dispositivos sincronizados del móvil
                            trusted_device = Dispositivos que se le indican por pantalla que son de confianza
                            discover = dispositivos desbiertos
                            dispositivos = lista que contiene los intrusos encontrados*/
                           // discover.contains("TV")==false
                            if(pairedDevices.contains(device)==false && trusted_device.contains(name)==false
                                    && dispositivos.contains(device + "->" + name)==false) {
                                discover.add(device);
                                dispositivos.add(device + "->" + name);
                                //Log.d("DESCUBRE : nombre_disp" , name);
                                fichero(rssi, address, name);

                            }
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


    public void confianza(View v){
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);
    }


    public void fichero(int rss , String add , String name){
        try {
            //getCoordenadas();
            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            //if(longitud!=null && latitud!=null ){
            //stopGps();
            //if(longitud!=null && latitud!=null && cont==0) {
            stopGps();
            //getCoordenadas();
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_APPEND));
            Log.d("Ficheros", "Escribiendo intruso.txt");
            fout.write("[MAC->NOMBRE]:" + dispositivos
                    + "\nUbicacion de Peephole:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
            fout.close();
            cont =2 ;
            //bucle();
            new Bluetooth().execute();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }

    }//fichero


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
        Intent intent = new Intent(this , FinalizarActivity.class);
        intent.putCharSequenceArrayListExtra("lista",dispostivos_fin);
        startActivity(intent);
        //descub.dismiss();
        //finish();
    }

    private void startGps(){
        Intent Gservice = new Intent(this,LocationService.class);
        startService(Gservice);
    }




    private void stopGps(){
        Log.d("STOP","Para el GPS");
        stopService(new Intent(getBaseContext(), LocationService.class));
        //unregisterReceiver(gpsReceiver);
    }



    public void getCoordenadas() {
        startGps();
        Log.d("Coordenadas", "entra en getCoordenadas");
        BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                longitud = intent.getDoubleExtra("longitud",00000);
                latitud = intent.getDoubleExtra("latitud",00000);
        Log.d("GPS" , String.valueOf(latitud));
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.GPS");
        intentFilter.addAction(LOCATION_SERVICE);
        registerReceiver(gpsReceiver,intentFilter);
        stopGps();
    }



    public void getTrustedDevice() {

            Bundle bundle = getIntent().getExtras();
            usu1 = bundle.getString("dispo1");
            trusted_device.add(usu1);
            usu2 = bundle.getString("dispo2");
            trusted_device.add(usu2);
            usu3 = bundle.getString("dispo3");
            trusted_device.add(usu3);
            dir1 = bundle.getString("email1");
            //emails.add(email1);
            dir2 = bundle.getString("email2");
            //emails.add(email2);
            dir3 = bundle.getString("email2");
            //emails.add(email3);
            Log.d("TRUSTED_DEVICES", usu1 + usu2 + usu3 + dir1 + dir2);

    }



    public void onPause() {
        super.onPause();
        try{
            unregisterReceiver(gpsReceiver);
            unregisterReceiver(mReciever);
            unregisterReceiver(disReceiver);
            unregisterReceiver(usuReceiver);
        }catch (Exception ex){
            Log.e("Reciever" , "Error");
        }

    }

    //CameraActivity
    public void camera(View view){
        Intent intent = new Intent(this , CameraActivity.class);
        startActivity(intent);

    }


    public void enviar(View v){
        getTrustedDevice();
        Intent send = new Intent(this,MailActivity.class);
        send.putExtra("correo1" , dir1);
        send.putExtra("correo2", dir2);
        send.putExtra("correo3" , dir3);
        startActivity(send);
    }



}//MainActivity
