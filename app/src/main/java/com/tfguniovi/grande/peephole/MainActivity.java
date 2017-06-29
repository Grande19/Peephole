package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Set;
import java.io.OutputStreamWriter;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RegistroFragment.OnFragmentInteractionListener {
    private Button    botonDES , botonLocal , cam , list ;
    private final static int REQUEST_ENABLE_BT = 1;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice device;
    private BluetoothAdapter BA;
    private BroadcastReceiver mReciever , gReciever , gpsReceiver , usuReceiver , disReceiver;
    public ArrayList dispositivos = new ArrayList();
    public ArrayList trusted_device = new ArrayList();
    public ArrayList discover = new ArrayList();
    public ArrayList dispostivos_fin = new ArrayList();
    public Double longitud , latitud;
    public String dir1 , dir2 , dir3 , usu1 , usu2 , usu3;
    File ruta_sd = Environment.getExternalStorageDirectory();
    public boolean finalizar = false;
    public boolean get = false ;
    boolean running = true;
    public int cont=1;
    Session session = null;
    String  pol;
    String  subject, textMessage;
    BodyPart adjunto_texto;
    BodyPart adjunto_audio;
    String path = Environment.getExternalStorageDirectory()+"/intruso.txt";
    String path1 = Environment.getExternalStorageDirectory()+"/intruso_audio.3gpp";
    //Solicitud de permisos

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA=8;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH = 2 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 4 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH_ADMIN = 5 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 6;
    private static  final int MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT=7;


    /***
     * Creacion de la Pantalla principal
     * @param savedInstanceState
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BA = BluetoothAdapter.getDefaultAdapter();
        //lv = (ListView) findViewById(R.id.lvDispositivos);
        //ls = (ListView) findViewById(R.id.DispositivosRSSI);
        botonDES = (Button) findViewById(R.id.add);
        cam = (Button) findViewById(R.id.cam);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.start);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if (!BA.isEnabled()) {
            Intent ONintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ONintent, REQUEST_ENABLE_BT);
            Toast.makeText(this, "Turning on Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bluetooth is already active", Toast.LENGTH_LONG).show();
        }




    }//OnCreate

    /**
     * Creación del NavigationDraver
     * @param
     * @return
     */


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        //getMenuInflater().inflate(R.menu.menu_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            item.setChecked(true);
            Intent i = new Intent(this,VideoActivity.class);
            startActivity(i);
            //replace(lo que quieres remplazar,lo que añades)
            // Handle the camera action
        } else if (id == R.id.registro) {
            item.setChecked(true);
            Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();
            RegistroFragment registroFragment = RegistroFragment.newInstance("dire1","dire2","dire3","dis1","dis2","dis3");
            FragmentManager maganerregistro = getSupportFragmentManager();
            maganerregistro.beginTransaction().replace(R.id.cmain,registroFragment,
                    registroFragment.getTag()).commit();

        } else if (id == R.id.permisos) {
            item.setChecked(true);

        } else if (id == R.id.nav_manage) {
            item.setChecked(true);

        } else if (id == R.id.acerdade) {
            item.setChecked(true);
            AcercadeFragment acercadeFragment = new AcercadeFragment();
            FragmentManager manageracercade = getSupportFragmentManager();
            manageracercade.beginTransaction().replace(R.id.cmain,acercadeFragment,
                    acercadeFragment.getTag()).commit();

        } else if (id == R.id.intrusos) {
           item.setChecked(true);
            Bundle arguments = new Bundle();
            arguments.putCharSequenceArrayList("id", dispositivos);
            IntrusosFragment intrusosFragment = new IntrusosFragment().newInstance(dispositivos);
           // intrusosFragment.setArguments(bundle);
            FragmentManager managerintruso = getSupportFragmentManager();
            managerintruso.beginTransaction().replace(R.id.cmain,intrusosFragment,
            intrusosFragment.getTag()).commit();
            

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void discover(View view) {
        try {
            startGps();
            getCoordenadas();
            if (dir1 != null && dir1 != "") {
                new Bluetooth().execute();
            } else {
                Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onFragmentInteraction(String dire1, String dire2, String dire3, String dis1, String dis2, String dis3) {

        try {

            usu1 = dire1;
            trusted_device.add(usu1);
            usu2 = dire2;
            trusted_device.add(usu2);
            usu3 = dire3;
            trusted_device.add(usu3);
            dir1 = dis2;
            dir2 = dis1;
            dir3 = dis3;
            Log.d("TRUSTED_DEVICES", usu1 + usu2 + usu3 + dir1 + dir2);


        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();

        }

    }



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
                                fichero();

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


        }

    } //Fin de AsynTask



    public ArrayList dispositivos(){
        dispostivos_fin=dispositivos;

        return dispostivos_fin;
    }

    public void fichero(){
        try {
            //getCoordenadas();
            //falta añadir 2 dispositivos en el mismo fichero porque ahora solo detecta y escribe 1
            //if(longitud!=null && latitud!=null ){
            //stopGps();
            //if(longitud!=null && latitud!=null && cont==0) {
            Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
            int dia=calendarNow.get(Calendar.DAY_OF_MONTH);
            int month = calendarNow.get(Calendar.MONTH)+1;
            int min = calendarNow.get(Calendar.MINUTE);
            int hora = calendarNow.get(Calendar.HOUR_OF_DAY);
            stopGps();
            //getCoordenadas();
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_APPEND));
            Log.d("Ficheros", "Escribiendo intruso.txt");
            fout.write("Intruso detectado[hora/día/mes]"+"["+hora+":"+ min + "/"+dia+"/"+month
                    +"\n[MAC->NOMBRE]:" + dispositivos
                    + "\nUbicacion de Peephole:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
            fout.close();
            cont =2 ;
            //bucle();
            new Bluetooth().execute();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }

    }//fichero



    public void fin (View view){
        finalizar = true ;
        running = false;
        dispostivos_fin = dispositivos;
        stopGps();
        //Bluetooth.onCancelled();
        Intent intent = new Intent(this , FinalizarActivity.class);
        //intent.putCharSequenceArrayListExtra("lista",dispostivos_fin);
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
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.GPS");
            intentFilter.addAction(LOCATION_SERVICE);
            registerReceiver(gpsReceiver, intentFilter);
            stopGps();
        }
        catch (Exception ex){

        }
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
            email();
    }


    public void grabar(View v){
        Intent intent = new Intent(this,VideoActivity.class);
        startActivity(intent);
    }


    /***
     * Clase MailActivity para enviar correos de alerta
     */

    public void email() {
        //rec = email1.getText().toString();
        //extra = email2.getText().toString();
        //if (isNetwork()==true) {


        if (dir1 == null) {
            dir1 = "peepholeuniovi@gmail.com";
        }

        if (dir2 == null) {
            dir2 = "peepholeuniovi@gmail.com";
        }
        if (dir3 == null) {
            dir3 = "peepholeuniovi@gmail.com";
        }
        pol = "policia@policia.com";

        adjunto_audio = new MimeBodyPart();
        adjunto_texto = new MimeBodyPart();


        try {
            adjunto_texto.setDataHandler(new DataHandler(new FileDataSource(path)));
            adjunto_texto.setFileName("intruso.txt");
            adjunto_audio.setDataHandler(new DataHandler(new FileDataSource(path1)));
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        subject = "Alerta intruso"; //Poner de asunto algo significativo !
        textMessage = "Hemos detectado los siguientes dispositivos sospechosos en su casa , adjuntamos información";

        //Conectamos con los servicios de gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {

                //Registro del correo en servidor Gmail
                //ACORDARSE DE METER PASSWORD CUANDO QUIERA MANDAR CORREO!!!!!!!!!!!!!!!!
                //Aplicación creada para que envie correos a los usuarios
                return new PasswordAuthentication("peepholeuniovi@gmail.com", "tfg_2017");
            }
        });

        //pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        //MainActivity.RetreiveFeedTask task = new MainActivity.RetreiveFeedTask(); //crea una tarea asíncrona
        //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//ejecuta la tarea asíncrona

        new RetreiveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);

        //}//if
    }//correo

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        //doInbackground recibe un string como parametro de entrada
        //publishProgress y onProgressUpdate reciben vacío
        //doInBackground devuelve un String que lo recibe onPostexecute


        @Override
        //AsyckTask para hacer operaciones en segundo plano (background)
        protected String doInBackground(String... params) {

            try{
                BodyPart texto=new MimeBodyPart();
                texto.setText(textMessage);
                File out = new File(path);
                File out2 = new File(path1);
                MimeMultipart multiParte = new MimeMultipart();
                if(out.exists()==true) {
                    multiParte.addBodyPart(adjunto_texto);
                }
                if(out2.exists()==true) {
                    adjunto_audio.setFileName("intruso_audio.3gpp");
                    Log.d("NO","existe");
                    multiParte.addBodyPart(adjunto_audio);
                }

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("peepholeuniovi@gmail.com", "[Peephole]"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dir1));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(dir2));
                message.addRecipient(Message.RecipientType.CC , new InternetAddress(dir3));
                message.addRecipient(Message.RecipientType.CC , new InternetAddress(pol));

                // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(extra));
                message.setSubject(subject);
                multiParte.addBodyPart(texto);
                message.setContent(multiParte);
                Transport.send(message);


            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           // pdialog.dismiss();
            //email1.setText("");
            //msg.setText("");
            //sub.setText("");


            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }

}//MainActivity
