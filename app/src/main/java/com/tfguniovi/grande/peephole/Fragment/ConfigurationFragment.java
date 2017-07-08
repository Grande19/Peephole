package com.tfguniovi.grande.peephole.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;


/**
 * Fragmento para la configuración del envío de correo
 * electronico
 */
public class ConfigurationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Switch intrusos,intervalo;
    public EditText nintrusos,secintervalo;
    private OnFragmentInteractionListener mListener;
    private ImageButton send;
    String segundos,numintrusos;
    Button restablecer;

    public ConfigurationFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static ConfigurationFragment newInstance(String param1, String param2) {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=savedInstanceState){
            Toast.makeText(ConfigurationFragment.this.getActivity(),  "Parametros guardados" + segundos, Toast.LENGTH_LONG).show();
            //boolean seleccionado_intrusos = savedInstanceState.getBoolean("snintrusos");
            //  intervalo.isChecked(savedInstanceState.getBoolean("sintervalo"));
            //segundos = savedInstanceState.getString("numintrusos");
            segundos = (String) savedInstanceState.get("intervalo");
            secintervalo.setText(segundos);
            numintrusos = savedInstanceState.getString("numero_intrusos");
            nintrusos.setText(numintrusos);
        }else {

        }



    }




    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        intrusos = (Switch) getView().findViewById(R.id.numintrusos);
        intervalo = (Switch) getView().findViewById(R.id.intervalo);
        nintrusos = (EditText) getView().findViewById(R.id.n_intrusos);
        secintervalo = (EditText) getView().findViewById(R.id.intervalosec);
        send = (ImageButton) getView().findViewById(R.id.enviar);
        restablecer = (Button) getView().findViewById(R.id.restablecer);

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("segundos");
                editor.remove("intervalo_switch");
                editor.remove("intrusos");
                editor.remove("intervalo_switch");
                editor.commit();

                poner();

            }
        });

        try {
            SharedPreferences configuracion = PreferenceManager.getDefaultSharedPreferences(getActivity());

            boolean is = configuracion.getBoolean("intervalo_switch",false);
            boolean isn = configuracion.getBoolean("intrusos_switch",true);
            String sec = configuracion.getString("segundos","");
            String intr = configuracion.getString("intrusos","");

            intrusos.setChecked(isn);
            intervalo.setChecked(is);

            Log.d("CONFG" , String.valueOf(sec + intr));
            nintrusos.setText(intr);
            secintervalo.setText(String.valueOf(sec));

        } catch (Exception ex){

        }





        intervalo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secintervalo.setEnabled(true);
                    nintrusos.setEnabled(false);
                    intrusos.setEnabled(false);
                } else if (intervalo.isChecked() == false) {
                    secintervalo.setEnabled(false);
                    intrusos.setEnabled(true);
                }


            }
        });

        intrusos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (intrusos.isChecked()) {
                    nintrusos.setEnabled(true);
                    intervalo.setEnabled(false);

                } else if (intrusos.isChecked() == false) {
                    nintrusos.setEnabled(false);
                    intervalo.setEnabled(true);
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(intervalo.isChecked()==true && intrusos.isChecked()==true){
                        Toast.makeText(ConfigurationFragment.this.getActivity(),  "Error, solo puede marcar UNA opción", Toast.LENGTH_LONG).show();
                    }
                    if(intervalo.isChecked()==true){
                        segundos = String.valueOf(secintervalo.getText());
                        if(segundos=="" || segundos=="0"){
                            Toast.makeText(ConfigurationFragment.this.getActivity(),  "Introduzca los segundos,mínimo 1", Toast.LENGTH_LONG).show();

                        }else
                            {
                        numintrusos = "0";
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("segundos",segundos);
                                editor.putBoolean("intervalo_switch",true);
                                editor.putBoolean("intruso_switch",false);
                                editor.commit();


                        onButtonPressed(segundos,numintrusos);
                        }


                    }
                    else if(intrusos.isChecked()==true){
                        numintrusos=String.valueOf(nintrusos.getText());
                        if(numintrusos=="" || numintrusos=="0" ){
                            Toast.makeText(ConfigurationFragment.this.getActivity(),  "Introduzca los intrusos,mínimo 2", Toast.LENGTH_LONG).show();
                        }
                        segundos="0";
                        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = pref1.edit();
                        editor.putString("intrusos",numintrusos);
                        editor.putBoolean("intervalo_switch",false);
                        editor.putBoolean("intruso_switch",true);
                        editor.commit();
                        onButtonPressed(segundos,numintrusos);
                    }else {
                        Toast.makeText(ConfigurationFragment.this.getActivity(),  "Marque al menos una de las 2 opciones", Toast.LENGTH_LONG).show();
                    }



                }catch (Exception ex){

                }

                Toast.makeText(ConfigurationFragment.this.getActivity(),  "Configuracion guardada correctamente", Toast.LENGTH_LONG).show();
            }


        });
    }



    public void poner(){
        SharedPreferences configuracion = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean is = configuracion.getBoolean("intervalo_switch",false);
        boolean isn = configuracion.getBoolean("intrusos_switch",true);
        String sec = configuracion.getString("segundos","");
        String intr = configuracion.getString("intrusos","4");

        intrusos.setChecked(isn);
        intervalo.setChecked(is);

        Log.d("CONFG" , String.valueOf(sec + intr));
        nintrusos.setText(intr);
        secintervalo.setText(String.valueOf(sec));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



            return inflater.inflate(R.layout.fragment_configuration, container, false);

        }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String intervalosec,String numintrusos) {
        if (mListener != null) {
            mListener.onFragmentInteractionConfiguration(intervalosec,numintrusos);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionConfiguration(String intervalosec,String numintrusos);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


    }
}

