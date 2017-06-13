package com.tfguniovi.grande.peephole;

/**
 * Alvaro grande
 */
//Clase para descubrir nuevos dispositivos Bluetooth asíncronamente
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BluetoothService extends IntentService {

    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter BA;
    private ArrayList dispositivos = new ArrayList();
    int cont;

    //Nombre de acción
    private static final String ACTION_DISCOVERING = "com.tfguniovi.grande.peephole.action.DISCOVERING";
    private static final String ACTION_BAZ = "com.tfguniovi.grande.peephole.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tfguniovi.grande.peephole.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tfguniovi.grande.peephole.extra.PARAM2";

    public BluetoothService() {
        super("BluetoothService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


   /* // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BluetoothService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    /*// TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BluetoothService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/


    //Metodo que contiene el código de la tarea a ejecutar
    @Override
    protected void onHandleIntent(Intent intent) {


            //pulsado = false;
            //getCoordenadas();
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
                           // alerta(rssi,name,address);
                           // fichero(rssi,address,name);
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
}



/*Envío de datos al Main
  Intent i = new Intent("android.intent.action.GPS").putExtra("latitud",latitud);
            i.putExtra("longitud" , longitud);

            //Se envian los valores de latitud y longitud a la MainActivity
            sendBroadcast(i);
 */