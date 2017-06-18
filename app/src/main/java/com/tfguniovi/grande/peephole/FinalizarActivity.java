package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

//Activity para salir de la app

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FinalizarActivity extends AppCompatActivity {
    private BroadcastReceiver dispReciever;
    private ArrayList dispositivos;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);
        lv = (ListView) findViewById(R.id.dispo);
        /*
        try{
        Bundle bundle = getIntent().getExtras();
        dispositivos = bundle.getCharSequenceArrayList("dispositivos_fin");
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, dispositivos);
        lv.setAdapter(adapter);}
        catch (Exception ex){
            Toast.makeText(this, "No se han encontrado intrusos", Toast.LENGTH_LONG).show();

    }^*/
    }




    //Podría leer el fichero escrito en memoria para mostrarlo por pantalla como resumen , de intrusos

    //Botón para salir de la app
    public void salir(View v){


        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
