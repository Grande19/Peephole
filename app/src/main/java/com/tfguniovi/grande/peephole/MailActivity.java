package com.tfguniovi.grande.peephole;



import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailActivity extends AppCompatActivity {

    String correo;
    String contraseña;
    String to = "alvarogrande19@gmail.com";
    EditText mensaje;
    Button enviar;
    Session session;
    public InternetAddress toAddress=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        mensaje = (EditText) findViewById(R.id.mensaje);
        enviar = (Button) findViewById(R.id.enviar);
        correo = "1b15agc@gmail.com";
        //correo = "peephole_uniovi@outlook.com";
        //contraseña = "tfg_2017" ; //OJO pedirla por teclado al usuario , crear un mail de la app
        contraseña = "pobladura";

        //método Onclick de distinta forma a definirlo mediante layout
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Properties properties = new Properties();
                properties.put("mail.smtp.host","smtp.googlemail.com");
                properties.put("mail.smtp.socketFactory.port","465");
                //properties.put("mail.smtp.socketFatory.class","javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth","true");
                //properties.put("mail.smtp.auth","false"); Nos autenticamos¿?
                properties.put("mail.smtp.startttls.enable","true");
                properties.put("mail.smtp.EnableSSL.enable","true");
                properties.setProperty("mail.debug", "true");


                try{
                    InternetAddress toAddress = new InternetAddress(to);
                    session = Session.getDefaultInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(correo , contraseña); //nos autenticamos en la session
                        }
                    });
                    if (session!=null){
                        javax.mail.Message message = new MimeMessage(session);
                        //de quien recibo el correo
                        message.setFrom(new InternetAddress(correo,"Detector de intrusos"));
                        //asunto
                        message.setSubject("Intruso");
                        //a quien le mandamos el correo
                        message.setRecipient(javax.mail.Message.RecipientType.TO,toAddress);
                        message.setContent(mensaje.getText().toString(),"txt/html; charset=utf-8");

                        //enviamos correo
                        //Transport.send(message);
                        Transport transport = session.getTransport("smtps");
                        //transport.connect("smtp.live.com",587,correo,contraseña);
                        transport.sendMessage(message,message.getAllRecipients());
                        //Transport.send(message);
                        transport.close();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();

                }


            }

        });
    }

}


