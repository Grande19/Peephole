package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.tfguniovi.grande.peephole.R;

public class DatosActivity extends AppCompatActivity {
    private Button enviar;
    public EditText dir1,dir2,dir3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        enviar = (Button)findViewById(R.id.insertar);
        dir1 = (EditText)findViewById(R.id.email1);
        dir2 = (EditText)findViewById(R.id.email2);
        dir3 = (EditText)findViewById(R.id.email3);

    }

    public void send (View v){
        String e1 = dir1.getText().toString();
        String e2 = dir2.getText().toString();
        String e3 = dir3.getText().toString();
        Intent intent = new Intent(DatosActivity.this,MainActivity.class);
        intent.putExtra("email1" , e1);
        intent.putExtra("email2" , e2);
        intent.putExtra("email3" , e3);
        Log.d("Email",e1);
        startActivity(intent);

    }

}
