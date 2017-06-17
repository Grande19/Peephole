package com.tfguniovi.grande.peephole;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    private Button enviar;
    public EditText dis1,dis2,dis3 ;
    public EditText dir1,dir2,dir3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        enviar = (Button)findViewById(R.id.insertar);
        dis1 = (EditText)findViewById(R.id.dispositivo1);
        dis2 = (EditText)findViewById(R.id.dispositvo2);
        dis3 = (EditText)findViewById(R.id.dispositivo3);
        enviar = (Button)findViewById(R.id.insertar);
        dir1 = (EditText)findViewById(R.id.email1);
        dir2 = (EditText)findViewById(R.id.email2);
        dir3 = (EditText)findViewById(R.id.email3);

    }

    public void send (View v){
        String d1 = dis1.getText().toString();
        String d2 = dis2.getText().toString();
        String d3 = dis3.getText().toString();
        String e1 = dir1.getText().toString();
        String e2 = dir2.getText().toString();
        String e3 = dir3.getText().toString();
        Log.d("SHOW" , d1);
        if (d1!=null && d2!=null  && d3!=null  && e1!=null  && e2!=null  && e3!=null){

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email1", e1);
            intent.putExtra("email2", e2);
            intent.putExtra("email3", e3);
            intent.putExtra("dispo1", d1);
            intent.putExtra("dispo2", d2);
            intent.putExtra("dispo3", d3);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "RELLENE TODOS LOS CAMPOS", Toast.LENGTH_LONG).show();

        }
    }

}
