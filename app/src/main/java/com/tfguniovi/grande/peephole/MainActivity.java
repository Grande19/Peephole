package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.tfguniovi.grande.peephole.Fragment.AcercadeFragment;
import com.tfguniovi.grande.peephole.Fragment.ConfigurationFragment;
import com.tfguniovi.grande.peephole.Fragment.HomeFragment;
import com.tfguniovi.grande.peephole.Fragment.IntrusosFragment;
import com.tfguniovi.grande.peephole.Fragment.RegistroFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


public class MainActivity extends AppCompatActivity implements
        RegistroFragment.OnFragmentInteractionListener,ConfigurationFragment.OnFragmentInteractionListener{

    private ImageButton homebutton;
    private final static int REQUEST_ENABLE_BT = 1;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice device;
    private BluetoothAdapter BA;
    private BroadcastReceiver mReciever, gReciever, gpsReceiver, usuReceiver, disReceiver;
    public ArrayList dispositivos = new ArrayList();
    public ArrayList trusted_device = new ArrayList();
    public ArrayList discover = new ArrayList();
    public ArrayList dispostivos_fin = new ArrayList();
    public Double longitud, latitud;
    public String dir1, dir2, dir3, usu1, usu2, usu3;
    public boolean finalizar = false;
    boolean running = true;
    public int cont = 0;
    int segundos, num_intrusos;
    Session session = null;
    String pol;
    String subject, textMessage;
    BodyPart adjunto_texto;
    BodyPart adjunto_audio;
    String path = Environment.getExternalStorageDirectory() + "/intruso.txt";
    String path1 = Environment.getExternalStorageDirectory() + "/intruso_audio.3gpp";
    //Solicitud de permisos
    DrawerLayout drawerLayout;
    Button video;
    private MediaRecorder recorderVideo;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
    int i = 0;
    int iterator = 0;
    private VideoView videoView = null;
    private SurfaceHolder holder = null;
    private Camera camera = null;



    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 8;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 4;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH_ADMIN = 5;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CAPTURE_VIDEO_OUTPUT = 7;


    /***
     * Creacion de la Pantalla principal
     * @param savedInstanceState
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Log.d("ENTRANDO", String.valueOf(trusted_device));

            setContentView(R.layout.activity_main);
            BA = BluetoothAdapter.getDefaultAdapter();
            Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
            ImageButton homebutton = (ImageButton) findViewById(R.id.home);
            setSupportActionBar(toolbar1);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.start);
            videoView = (VideoView) findViewById(R.id.grabadora);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null) {
                setupNavigationDrawerContent(navigationView);
            }

            setupNavigationDrawerContent(navigationView);
            setFragment(4);


            if (!BA.isEnabled()) {
                Intent ONintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(ONintent, REQUEST_ENABLE_BT);
                Toast.makeText(this, "Turning on Bluetooth", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Bluetooth is already active", Toast.LENGTH_LONG).show();
            }

        } else {
            Log.d("ENTRANDO", "SEGUNDA VED");
            /*outState.putCharSequenceArrayList("dispostiivos de confianza", trusted_device);
            outState.putInt("segundos",segundos);
            outState.putInt("nintrusos",num_intrusos);
            outState.putString("email1",dir1);
            outState.putString("email2",dir2);
            outState.putString("email3",dir3);*/
            try {
                segundos = savedInstanceState.getInt("segundos");
                num_intrusos = savedInstanceState.getInt("nintrusos");
                trusted_device = savedInstanceState.getCharSequenceArrayList("dispositivos de confianza");
                dir1 = savedInstanceState.getString("email1");
                dir2 = savedInstanceState.getString("email2");
                dir3 = savedInstanceState.getString("email3");

            } catch (Exception ex) {

            }

        }


    }//OnCreate

    /**
     * Creación del NavigationDraver
     *
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


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.tools:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.registro:
                                menuItem.setChecked(true);
                                setFragment(1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_camera:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent i = new Intent(MainActivity.this, VideoActivity.class);
                                startActivity(i);
                                return true;
                            case R.id.acerdade:
                                menuItem.setChecked(true);
                                Toast.makeText(MainActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                setFragment(3);
                                return true;
                            case R.id.intrusos:
                                menuItem.setChecked(true);
                                Toast.makeText(MainActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                setFragment(2);
                                return true;
                            case R.id.home:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                setFragment(4);
                                return true;
                            case R.id.email:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent im = new Intent(MainActivity.this, MailActivity.class);
                                startActivity(im);
                        }
                        return true;
                    }
                });
    }


    public void home(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ConfigurationFragment configurationFragment = new ConfigurationFragment();
                fragmentTransaction.replace(R.id.cmain, configurationFragment);
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                RegistroFragment registroFragment = new RegistroFragment();
                fragmentTransaction.replace(R.id.cmain, registroFragment);
                fragmentTransaction.addToBackStack(null).commit();

                break;
            case 2:
                Bundle arguments = new Bundle();
                arguments.putCharSequenceArrayList("id", dispositivos);
                IntrusosFragment intrusosFragment = new IntrusosFragment().newInstance(dispositivos, trusted_device);
                // intrusosFragment.setArguments(bundle);
                FragmentManager managerintruso = getSupportFragmentManager();
                managerintruso.beginTransaction().replace(R.id.cmain, intrusosFragment,
                        intrusosFragment.getTag()).addToBackStack(null).commit();


                break;
            case 3:
                AcercadeFragment acercadeFragment = new AcercadeFragment();
                FragmentManager manageracercade = getSupportFragmentManager();
                manageracercade.beginTransaction().replace(R.id.cmain, acercadeFragment,
                        acercadeFragment.getTag()).addToBackStack(null).commit();
                break;
            case 4:
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager managerhome = getSupportFragmentManager();
                managerhome.beginTransaction().replace(R.id.cmain, homeFragment,
                        homeFragment.getTag()).addToBackStack(null).commit();

        }
    }


    public void discover(View view) {
        try {

            if (trusted_device.isEmpty() == false) {
                //Intent serviceBluetooth = new Intent(this,BluetoothService.class);
                //serviceBluetooth.putCharSequenceArrayListExtra("trusted_devices",trusted_device);
                //startService(serviceBluetooth);
                new Bluetooth().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Bundle arguments = new Bundle();
                arguments.putCharSequenceArrayList("id", dispositivos);
                IntrusosFragment intrusosFragment = new IntrusosFragment().newInstance(dispositivos, trusted_device);
                // intrusosFragment.setArguments(bundle);
                FragmentManager managerintruso = getSupportFragmentManager();
                managerintruso.beginTransaction().replace(R.id.cmain, intrusosFragment,
                        intrusosFragment.getTag()).commit();

                startGps();
                getCoordenadas();
            } else {
                Toast.makeText(this, "Introduzca los parametros", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();
        }


    }



    //Clase para ejecutar la tarea de descubrimiento de Bluetooth en segundo plano
    class Bluetooth extends AsyncTask<Void, Void, ArrayList> {


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
                            // discover.contains("TV")==false
                            if (pairedDevices.contains(device) == false && trusted_device.contains(name) == false
                                    && dispositivos.contains(device + "->" + name) == false) {
                                discover.add(device);
                                dispositivos.add(device + "->" + name);
                                //Log.d("DESCUBRE : nombre_disp" , name);
                                fichero(name);

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
                    unregisterReceiver(mReciever);
                }


                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);


            }
            unregisterReceiver(mReciever);
            return dispositivos;

        }


        @Override
        protected void onPreExecute() {

            Log.d("ASYCN", "Doing ASYCN Task");

        }

        @Override
        protected void onCancelled() {
            Intent intent = new Intent(MainActivity.this, FinalizarActivity.class);
            startActivity(intent);
            //unregisterReceiver(mReciever);
            Log.d("FIN", "Acaba");
            running = false;


        }

        @Override
        protected void onCancelled(ArrayList arrayList) {
            super.onCancelled(arrayList);
        }
    } //Fin de AsynTask


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


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Introduzca los usuarios y dispositivos para comenzar", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onFragmentInteractionConfiguration(String intervalosec, String numintrusos) {
        segundos = Integer.parseInt(intervalosec) * 60 * 1000; //se pasa de minutos a milisegundos que es con lo que trabaja java
        num_intrusos = Integer.parseInt(numintrusos);
        Toast.makeText(this, "Segundos + intrusos" + segundos + "" + num_intrusos, Toast.LENGTH_LONG).show();
    }

    /*


    */

    public void fichero(String name) {
        try {
            Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
            int dia = calendarNow.get(Calendar.DAY_OF_MONTH);
            int month = calendarNow.get(Calendar.MONTH) + 1;
            int min = calendarNow.get(Calendar.MINUTE);
            int hora = calendarNow.get(Calendar.HOUR_OF_DAY);
            if (latitud == null && longitud == null) {
                getCoordenadas();
            }
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), "intruso.txt");
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
            //fout = new OutputStreamWriter(openFileOutput("intruso.txt", Context.MODE_APPEND));
            Log.d("Ficheros", "Escribiendo intruso.txt");
            fout.write("Intruso detectado[hora/día/mes]" + "[" + hora + ":" + min + "/" + dia + "/" + month
                    + "\n[MAC->NOMBRE]:" + dispositivos
                    + "\nUbicacion de Peephole:[longitud,latitud]" + "[" + longitud + "," + latitud + "]");
            IntrusosFragment intrusosFragment = new IntrusosFragment().newInstance(dispositivos, trusted_device);
            FragmentManager managerintruso = getSupportFragmentManager();
            managerintruso.beginTransaction().replace(R.id.cmain, intrusosFragment,
                    intrusosFragment.getTag()).commit();
            fout.close();
            new Camara().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            cont += 1;
            if (cont == 4) {
                Intent audio = new Intent(MainActivity.this, AudioService.class);
                stopService(audio);
                email();
            }
            //bucle();
            //new Bluetooth().execute();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }

    }//fichero


    public void fin(View view) {
        confirmacion();
    }

    private void startGps() {
        Intent Gservice = new Intent(this, LocationService.class);
        startService(Gservice);
    }


    private void confirmacion() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        //builder.setView(inflater.inflate(R.layout.dialogo, null));
        builder.setTitle(R.string.dialogo_linea_1);
        builder.setMessage(R.string.dialogo_linea_2);
        finalizar = true;
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                SharedPreferences correos_activity = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = correos_activity.edit();

                editor.remove("email1_ac").commit();
                editor.remove("email2_ac").commit();
                editor.remove("email3_ac").commit();
                editor.remove("dispositivo1_ac").commit();
                editor.remove("dispositivo2_ac").commit();
                editor.remove("dispositivo3_ac").commit();


                Log.i("Dialogos", "Confirmacion Aceptada.");
                Intent intent = new Intent(MainActivity.this, FinalizarActivity.class);
                intent.putCharSequenceArrayListExtra("lista", dispostivos_fin);
                new Bluetooth().cancel(finalizar);

                startActivity(intent);
                finalizar = true;
                running = false;
                dispostivos_fin = dispositivos;
                stopGps();
                //descub.dismiss();
                finish();

            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        Toast.makeText(MainActivity.this, "Continua el descubrimto", Toast.LENGTH_LONG).show();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();


    }


    private void stopGps() {
        Log.d("STOP", "Para el GPS");
        stopService(new Intent(getBaseContext(), LocationService.class));

        //unregisterReceiver(gpsReceiver);
    }


    public void getCoordenadas() {
        //startGps();
        Log.d("Coordenadas", "entra en getCoordenadas");
        BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                longitud = intent.getDoubleExtra("longitud", 00000);
                latitud = intent.getDoubleExtra("latitud", 00000);
                Log.d("GPS", String.valueOf(latitud));
            }
        };
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.GPS");
            intentFilter.addAction(LOCATION_SERVICE);
            registerReceiver(gpsReceiver, intentFilter);
            stopGps();
        } catch (Exception ex) {

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(gpsReceiver);
            unregisterReceiver(mReciever);


        } catch (Exception ex) {
            Log.e("Reciever", "Error");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//       unregisterReceiver(gpsReceiver);
        // unregisterReceiver(mReciever);


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

            try {
                BodyPart texto = new MimeBodyPart();
                texto.setText(textMessage);
                File out = new File(path);
                File out2 = new File(path1);
                MimeMultipart multiParte = new MimeMultipart();
                if (out.exists() == true) {
                    multiParte.addBodyPart(adjunto_texto);
                }
                if (out2.exists() == true) {
                    adjunto_audio.setFileName("intruso_audio.3gpp");
                    Log.d("NO", "existe");
                    multiParte.addBodyPart(adjunto_audio);
                }

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("peepholeuniovi@gmail.com", "[Peephole]"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dir1));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(dir2));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(dir3));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(pol));

                // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(extra));
                message.setSubject(subject);
                multiParte.addBodyPart(texto);
                message.setContent(multiParte);
                Transport.send(message);


            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }


    public int path() {
        iterator = (int) (Math.random());
        return iterator;
    }



    /**
     * AsycnTask para capturar imágenes cada vez que se detecta un dispositivo
     */

    class Camara extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            final Camera camarafotos;

            Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    // set file destination and file name


                    //Si la sd es de solo lectura escribe en memoria interna

                    String nombrepath = String.valueOf(iterator) + "intruso.jpg";
                    File destination = new File(Environment.getExternalStorageDirectory(), nombrepath);

                    if (destination.exists()) {
                        path();
                        nombrepath = String.valueOf(iterator) + "intruso.jpg";
                        destination = new File(Environment.getExternalStorageDirectory(), nombrepath);

                    }
                    //Log.d("CAMARA" , "Generado");;
                    try {
                        //Log.d("FOTO" , "Generado");
                        Bitmap userImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                        // set file out stream
                        FileOutputStream out = new FileOutputStream(destination);
                        // set compress format quality and stream
                        userImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        onCancelled();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            };

            camarafotos = Camera.open();
            camarafotos.startPreview();
            camarafotos.takePicture(null, null, null, jpegCallBack);


            return null;
        }

        public int path() {
            iterator = (int) (Math.random());
            return iterator;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            //camarafotos.release();
            //Log.d("FOTOFIN" , "Foto FInalizada");
            super.onPostExecute(aVoid);
        }
    }


    /**
     * Video
     */




}//MainActivity














