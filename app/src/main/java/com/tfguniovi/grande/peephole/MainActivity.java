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
    private Button    botonDES , botonLocal , cam;
    private final static int REQUEST_ENABLE_BT = 1;
    ListView lv, ls;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice device;
    private BluetoothAdapter BA;
    private BroadcastReceiver mReciever , gReciever , gpsReceiver ;
    final ArrayList rssi_list = new ArrayList();
    private ArrayList dispositivos = new ArrayList();
    public Double longitud , latitud , ublat , ublong ;
    public String email1 , email2 , email3;
    public String[] correo;
    public boolean localizacion =false;
    ProgressDialog descub = null ;
    public int contador=0 , cont ;
    File ruta_sd = Environment.getExternalStorageDirectory();
    File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
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
        startGps();

        //this.startActivity();


    }

    private void startGps(){
        Intent service = new Intent(this , LocationService.class);
        startService(service);
    }

    private void stopGps(){
        Intent service = new Intent(this , LocationService.class);
        stopService(service);
    }

    //Muestra con un ListView Los dispositivos sincronizados (no vale de mucho)
    /*public void discover(View v) {
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        if (pairedDevices.size() > 0) {
            //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                list.add(device.getName() + "\n" + device.getAddress());
            }
            Toast.makeText(this, "Showing paired devices", Toast.LENGTH_LONG).show();
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
            lv.setAdapter(adapter);

        }
    }*/



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
        }catch (Exception ex){
            Log.e("Reciever" , "Error");
        }

    }

    //CameraActivity
    public void camera(View view){
        Intent intent = new Intent(MainActivity.this , CameraActivity.class);
        startActivity(intent);

    }


    public void discovering () {
        //pulsado = false;
        getCoordenadas();
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

            if (pairedDevices.size() > 0) {
                //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice device : pairedDevices) {
                    list.add(device.getName() + "\n" + device.getAddress());
                }
            }

            //for(int i=0 ; i<10 ; i++) {
            BA.startDiscovery(); //start looking for new devices
            Log.d("DES", "Starting discover new devices");
            Toast.makeText(this, "Discovering devices", Toast.LENGTH_LONG).show();


            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        //Get the bluetoothDevice from the intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (pairedDevices.contains(device) == false && dispositivos.contains(device) == false) {
                            cont++ ;
                            dispositivos.add(device);
                            //señal de bluetooth recibida
                            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                            dispositivos.add(rssi);
                            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                            String address = device.getAddress();
                            alerta(rssi,name,address);
                            fichero(rssi,address,name);
                            // ublat = getlatitud();
                            //  ublong = getlongitud();
                            //correo = getmail();
                            // sendE(rssi,address,ublat,ublong);
                           // bucle();
                        }//if paired
                     //bucle();

                    }//if
                    //bucle();

                }//onReceive
            };//BroadcastReceiver

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);


        //}//while
    }//discovering
    public void bucle(){
        Log.d("Bucle","Entra");
        //pulsado=true;
        discovering();
    }

    public void alerta(int rssi , String name , String add){
        imprimir(rssi, add, name, dispositivos);

    }

    public void fin (View view){
        Intent intent = new Intent(MainActivity.this , FinalizarActivity.class);
        intent.putExtra("lista",rssi_list);
        startActivity(intent);



        //descub.dismiss();
        //finish();

    }
    public void comenzar(View v){


        discovering();
    }

    //Chequea los dispositivos que no están en la lista get BondedDevices






    public void imprimir(int rss , String address , String name , ArrayList disp) {
        //rssi_list.clear();
        rssi_list.add("["+ address + "]" + "\n" + name + "="+  + rss + "dBm\n");
       // rssi_list.add(disp);
        //
        Toast.makeText(getApplicationContext(), "Imprimir lista", Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, rssi_list);
        ls.setAdapter(adapter);

    }



    public void fichero(int rssi , String add,String nom){

        try {

            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            if(longitud!=null && latitud!=null ){
                    stopGps();


                        //File ruta_sd = Environment.getExternalStorageDirectory();
                        //File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
                        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                        //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_PRIVATE));
                        Log.d("Ficheros", "Escribiendo intruso.txt");
                        fout.write("Dispositivo :" + nom + "\n Direccion MAC :" + add + "\nIntensidad :" + rssi + "dBm"
                                + "\nUbicacion:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
                        fout.close();
                        cont++;
                        discovering();


            }
            else {
                Thread.sleep(5000);
                discovering();
            }





        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }


    }

    public void enviar(View v){
        Intent intent = new Intent(MainActivity.this , MailActivity.class);
        startActivity(intent);
    }



}//MainActivity
