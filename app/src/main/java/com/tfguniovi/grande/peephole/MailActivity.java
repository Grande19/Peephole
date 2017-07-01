package com.tfguniovi.grande.peephole;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailActivity extends AppCompatActivity {

    EditText user,password,subject,body;
    Button enviar;
    String asunto,textMessage;
    String usu,pass;
    Session session = null;
    boolean validez;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);



        enviar = (Button) findViewById(R.id.button);

                    // Perform action on click
        user=(EditText)findViewById(R.id.editUser);
        password=(EditText)findViewById(R.id.editPass);
        subject=(EditText)findViewById(R.id.editSubject);
        body=(EditText)findViewById(R.id.editBody);





        }
    private boolean validar(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        validez = pattern.matcher(email).matches();
        return validez;
    }

    public void email(View view) {

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
                    PasswordAuthentication autenticacion = new PasswordAuthentication(usu, pass);
                    return new PasswordAuthentication(usu, pass);
                }
            });

        usu = user.getText().toString();
        validar(usu);
        if(validez) {
            pass = password.getText().toString();
            asunto = subject.getText().toString(); //Poner de asunto algo significativo !
            textMessage = body.getText().toString();



            new MailActivity.RetreiveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else{
            Toast.makeText(getApplicationContext(), "Correo no válido", Toast.LENGTH_LONG).show();
        }
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
                MimeMultipart multiParte = new MimeMultipart();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(usu, "[Comentario Peephole]"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("peepholeuniovi@gmail.com"));


                // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(extra));
                message.setSubject(asunto);
                multiParte.addBodyPart(texto);
                message.setContent(multiParte);
                Transport.send(message);


            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Error de autenticacion o fallo de conexión", Toast.LENGTH_LONG).show();
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


}

