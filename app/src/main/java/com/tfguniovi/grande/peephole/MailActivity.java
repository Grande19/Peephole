package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;


public class MailActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 1;

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText email1, email2, msg;
    java.lang.String e1;
    java.lang.String e2;
    String e3 , pol;
    InternetAddress[] ad = new InternetAddress[4];
    String rec, subject, textMessage, extra , dir;
    BodyPart adjunto_texto;
    BodyPart adjunto_audio;
    String path = Environment.getExternalStorageDirectory()+"/intruso.txt";
    String path1 = Environment.getExternalStorageDirectory()+"/intruso_audio.3gpp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        context = this;
        try {
            if(isNetwork()==false){
                Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_LONG).show();
            };
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //El usuario debe introducir los correos a los que quiere notificar

        //email1 = (EditText) findViewById(R.id.email1);
        //sub = (EditText) findViewById(R.id.et_sub);
        //email2 = (EditText) findViewById(R.id.email2);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE: {
                //Si la petición es cancelada, el resultado estará vacío.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permiso aceptado, se podría acceder a los contactos del dispositivo.

                } else {
                    //Permiso denegado. Desactivar la funcionalidad que dependía de dicho permiso.
                }
                return;
            }
        }}

    public boolean isNetwork() throws InterruptedException, IOException {

            String comando = "ping -c 1 facebook.com";
            return (Runtime.getRuntime().exec (comando).waitFor() == 0);


    }


    //CUANDO PULSAMOS PARA ENVIAR EL MAIL EMPIEZA AQUI , método onClick()
    //quiero que se haga autómaticamente después de cierto tiempo descubriendo dispositivos
    public void correo(View v) {
        //rec = email1.getText().toString();
        //extra = email2.getText().toString();
        //if (isNetwork()==true) {
            Bundle bundle = getIntent().getExtras();
            e1 = bundle.getString("correo1");

            if (e1 == null) {
                e1 = "peepholeuniovi@gmail.com";
            }
            e2 = bundle.getString("correo2");

            if (e2 == null) {
                e2 = "peepholeuniovi@gmail.com";
            }
            e3 = bundle.getString("correo3");
            if (e3 == null) {
                e3 = "peepholeuniovi@gmail.com";
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

            pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

            RetreiveFeedTask task = new RetreiveFeedTask(); //crea una tarea asíncrona
            task.execute(); //ejecuta la tarea asíncrona

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
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(e1));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(e2));
                    message.addRecipient(Message.RecipientType.CC , new InternetAddress(e3));
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
            pdialog.dismiss();
            //email1.setText("");
            //msg.setText("");
            //sub.setText("");
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
            main();
        }
    }

    public void main(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}