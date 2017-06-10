package com.tfguniovi.grande.peephole;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.io.OutputStreamWriter;



public class MainActivity extends AppCompatActivity {
    private Button botonON, botonOFF, botonDIS, botonDES , botonLocal , cam;
    private final static int REQUEST_ENABLE_BT = 1;
    ListView lv, ls;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice device;
    private BluetoothAdapter BA;
    private BroadcastReceiver mReciever;
    final ArrayList rssi_list = new ArrayList();
    private ArrayList dispositivos = new ArrayList();
    public Double longitud , latitud , ublat , ublong ;
    public String email1 , email2 , email3;
    public String[] correo;
    public boolean localizacion =false;
    ProgressDialog descub = null ;
    public int cont ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BA = BluetoothAdapter.getDefaultAdapter();
        //botonON = (Button) findViewById(R.id.on);
        //botonOFF = (Button) findViewById(R.id.off);
        //botonDIS = (Button) findViewById(R.id.list);
        botonLocal = (Button)findViewById(R.id.localizacion);
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

    }

/* Apagar BT lo cual es innecesario
    public void off(View v) {
        BA.disable();
        //fichero();
        Toast.makeText(this, "Turning off Bluetooth", Toast.LENGTH_LONG).show();
    } */

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
    /*
    //Camara2Activity
    public void camera(View view){
        Intent intent = new Intent(MainActivity.this , Camera2Activity.class);
        startActivity(intent);

    }*/

    //CameraActivity
    public void camera(View view){
        Intent intent = new Intent(MainActivity.this , CameraActivity.class);
        startActivity(intent);

    }


    public void discovering () {
        //pulsado = false;
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
                            // ublat = getlatitud();
                            //  ublong = getlongitud();
                            //correo = getmail();
                            // sendE(rssi,address,ublat,ublong);
                            bucle();
                        }//if paired
                     bucle();

                    }//if
                    bucle();

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
        //SystemClock.sleep(50000);
        //Log.d("Sleep","Durmiendo");
        //fichero(rssi,add,name);
    }

    public void fin (View view){
        //Intent intent = new Intent(MainActivity.this , FinalizarActivity.class);
        //intent.putExtra("lista",rssi_list);
        //startActivity(intent);

        descub = ProgressDialog.show(this, "", "Finalizando descubrimiento , intrusos descubiertos : " + cont, true);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, rssi_list);
        ls.setAdapter(adapter);

        //descub.dismiss();
        //finish();

    }
    public void comenzar(View v){
        discovering();
    }

    //Chequea los dispositivos que no están en la lista get BondedDevices


    //Llamando a otra Activity para obtener la ub del dispositivo
    public void gps(View v){
        Intent localizacion = new Intent(MainActivity.this,LocationActivity.class);
        startActivity(localizacion);
        //requerir que sea obligatorio pedir la ub

    }

    /*public void getloc(){
        Intent localizacion = new Intent(MainActivity.this,LocationActivity.class);
        startActivity(localizacion);
    }*

    public void info (View v){
        Intent intent = new Intent(MainActivity.this,DatosActivity.class);
        startActivity(intent);
    }*/



    public void imprimir(int rss , String address , String name , ArrayList disp) {
        //rssi_list.clear();
        rssi_list.add("["+ address + "]" + "\n" + name + "="+  + rss + "dBm\n");
       // rssi_list.add(disp);
        //
        Toast.makeText(getApplicationContext(), "Imprimir lista", Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, rssi_list);
        ls.setAdapter(adapter);
        fichero(rss,address,name);
    }

    public double getlongitud(){
        Bundle datos = this.getIntent().getExtras();
        longitud = datos.getDouble("Longitud");
        Log.d("ubicacion","Devuelve la ubicación del LocationActivity");
        return longitud;
    }

    public double getlatitud(){
        Bundle datos = this.getIntent().getExtras();
        latitud = datos.getDouble("Latitud");
        return latitud;
    }

    public void fichero(int rssi , String add,String nom){
        //ublong = getlatitud();
        //ublong = getlongitud();
        try {

            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            //getloc();
            ublong = getlongitud();
            ublat = getlatitud();
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(),"intruso.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            Log.d("Ficheros", "Escribiendo intruso.txt");
            String dir = add.toString();
            fout.write("Dispositivo :" + nom + "\n Direccion MAC :" + add + "\nIntensidad :" + rssi + "dBm"
            + "\nUbicacion:[longitud,latitud]" + ublong + ublat + ublat);
            //fout.write("ubicacion : " + ubicacion );
            //fout.write("prueba");
            fout.close();
            discovering();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }


    }


    public void sendE(int rssi , String add , double latitud , double longitud){
        Bundle direcciones = this.getIntent().getExtras();
        email1 = direcciones.getString("email1");
        email2 = direcciones.getString("email2");
        email3 = direcciones.getString("email3");

        String[] TO ={email1,email2,email3};
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT , "Alerta intruso");
        emailIntent.putExtra(Intent.EXTRA_TEXT , "Hemos detectado movimiento en tu casa , detalles : \n Intesidad " + rssi + "dBm" + "\n " +
                "Localización[latitud , longitud] : " + latitud +","+ longitud + "\n Direccion MAC:" + add );


        try{
            startActivity(Intent.createChooser(emailIntent,"Send mail..."));
            finish();
            Log.i("Finished sending email","");
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this , "There is no email cliente installed" , Toast.LENGTH_SHORT).show();
        }


    }

    public void enviar(View v){
        Intent intent = new Intent(MainActivity.this , MailActivity.class);
        startActivity(intent);
    }



}//MainActivity
