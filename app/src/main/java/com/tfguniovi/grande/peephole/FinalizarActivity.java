package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

//Activity para salir de la app

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FinalizarActivity extends AppCompatActivity {
    private BroadcastReceiver dispReciever;
    private ArrayList dispositivos;
    TextView lv;
    BufferedReader lector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);
        lv = (TextView) findViewById(R.id.dispo);
        leer();
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


    public void leer(){
        try  {
            File path = Environment.getExternalStorageDirectory();
            File fichero = new File(path.getAbsolutePath(),"intruso.txt");
            InputStreamReader flujo = new InputStreamReader(new FileInputStream(fichero));
            lector = new BufferedReader(flujo);
            String texto = lector.readLine();
            while (texto!=null) {
                lv.append(texto);
                texto=lector.readLine();
            }
        } catch (Exception ex){
            Toast.makeText(this,"Imposible acceder al archivo comprobar memoria interna",Toast.LENGTH_LONG).show();
        }
        finally {
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (Exception ex) {

            }
        }

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
