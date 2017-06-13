package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.Activity;
import android.os.Bundle;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MailActivity extends AppCompatActivity {

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText email1, email2, msg;
    String rec, subject, textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        context = this;

        //El usuario debe introducir los correos a los que quiere notificar

        email1 = (EditText) findViewById(R.id.email1);
        //sub = (EditText) findViewById(R.id.et_sub);
        email2 = (EditText) findViewById(R.id.email2);



    }

    //CUANDO PULSAMOS PARA ENVIAR EL MAIL EMPIEZA AQUI , método onClick()
    //quiero que se haga autómaticamente después de cierto tiempo descubriendo dispositivos
    public void correo(View v) {
        rec = email1.getText().toString();
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
                return new PasswordAuthentication("peepholeuniovi@gmail.com", "pass");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask(); //crea una tarea asíncrona
        task.execute(); //ejecuta la tarea asíncrona
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        //doInbackground recibe un string como parametro de entrada
        //publishProgress y onProgressUpdate reciben vacío
        //doInBackground devuelve un String que lo recibe onPostexecute


        @Override
        //AsyckTask para hacer operaciones en segundo plano (background)
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("peepholeuniovi@gmail.com" , "[Peephole]"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
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
        }
    }

    public void main_screen (View v ){
        Intent intent = new Intent(MailActivity.this,MainActivity.class);
        startActivity(intent);

    }


}