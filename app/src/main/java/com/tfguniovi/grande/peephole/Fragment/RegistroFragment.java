package com.tfguniovi.grande.peephole.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.MainActivity;
import com.tfguniovi.grande.peephole.R;

import java.util.ArrayList;


/**
 Fragmento para el registro de los dispositivos y correos electronicos
 */
public class RegistroFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";


    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;
    public String d1,d2,d3,e1,e2,e3;
    ArrayList dispos;
    ArrayList email;
    Context context;


    private OnFragmentInteractionListener mListener;
    public EditText dis1,dis2,dis3 ;
    public EditText dire1,dire2,dire3 ;
    Button enviar,restablecer;
    boolean congelar;
    CheckBox recuperar,guardar;
    public RegistroFragment() {
        // Required empty public constructor
    }

    public static RegistroFragment newInstance(String param1, String param2 , String param3 , String param4,
                                               String param5,String param6) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        recuperar = (CheckBox) getView().findViewById(R.id.recuperar);
        guardar = (CheckBox) getView().findViewById(R.id.recordar);
        enviar = (Button) getView().findViewById(R.id.enviar);
        dis1 = (EditText) getView().findViewById(R.id.dispositivo1);
        dis2 = (EditText) getView().findViewById(R.id.dispositvo2);
        dis3 = (EditText) getView().findViewById(R.id.dispositivo3);
        dire1 = (EditText) getView().findViewById(R.id.email1);
        dire2 = (EditText) getView().findViewById(R.id.email2);
        dire3 = (EditText) getView().findViewById(R.id.email3);
        enviar = (Button) getView().findViewById(R.id.enviar);
        restablecer = (Button) getView().findViewById(R.id.restablecer);


        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LIMPIA","LIMPIA");
                SharedPreferences correos_activity = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = correos_activity.edit();
                editor.remove("email1_bd").commit();
                editor.remove("email2_bd").commit();
                editor.remove("email3_bd").commit();
                editor.remove("dispositivo1_bd").commit();
                editor.remove("dispositivo2_bd").commit();
                editor.remove("dispositivo3_bd").commit();
                dis1.setText("");
                dis2.setText("");
                dis3.setText("");
                dire1.setText("");
                dire2.setText("");
                dire3.setText("");

            }
        });

        try {
            SharedPreferences correos_activity = PreferenceManager.getDefaultSharedPreferences(getActivity());
            e1 = correos_activity.getString("email1_ac","");
            e2 = correos_activity.getString("email2_ac","");
            e3 = correos_activity.getString("email3_ac","");
            d1= correos_activity.getString("dispositivo1_ac","");
            d2 = correos_activity.getString("dispositivo2_ac","");
            d3 = correos_activity.getString("dispositivo3_ac","");

            dis1.setText(d1);
            dis2.setText(d2);
            dis3.setText(d3);
            dire1.setText(e1);
            dire2.setText(e2);
            dire3.setText(e3);

        } catch (Exception ex){

        }



        Log.d("OBTENER",d1 + d2);




        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recuperar.isChecked()){

                    SharedPreferences restablecer = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    e1 = restablecer.getString("email1_bd","");
                    e2 = restablecer.getString("email2_bd","");
                    e3 = restablecer.getString("email3_bd","");
                    d1= restablecer.getString("dispositivo1_bd","");
                    d2 = restablecer.getString("dispositivo2_bd","");
                    d3 = restablecer.getString("dispositivo3_bd","");

                    dis1.setText(d1);
                    dis2.setText(d2);
                    dis3.setText(d3);
                    dire1.setText(e1);
                    dire2.setText(e2);
                    dire3.setText(e3);



                    Log.d("OBTENER",e1 + e2);


                }

            }
        });


            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        d1 = dis1.getText().toString();
                        d2 = dis2.getText().toString();
                        d3 = dis3.getText().toString();
                        e1 = dire1.getText().toString();
                        e2 = dire2.getText().toString();
                        e3 = dire3.getText().toString();
                        Log.d("DISPOSITIVOS", d1 + d2 + d3);






                        if (TextUtils.isEmpty(d1) == false && TextUtils.isEmpty(d1) == false && TextUtils.isEmpty(d1) == false
                                && TextUtils.isEmpty(d1) == false && TextUtils.isEmpty(d1) == false && TextUtils.isEmpty(d1) == false) {
                            Toast.makeText(RegistroFragment.this.getActivity(), "Registrado,puede empezar el descubrimiento", Toast.LENGTH_LONG).show();
                        onButtonPressed(d1,d2,d3,e1,e2,e3);
                        } else {

                            Toast.makeText(RegistroFragment.this.getActivity(), "Introduzca todos los campos", Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception ex) {
                        Toast.makeText(RegistroFragment.this.getActivity(), "RELLENE TODOS LOS CAMPOS", Toast.LENGTH_LONG).show();
                    }

                }
            });


        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null ) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(final String dir1, String dir2, String dir3 , final String disp1 , String disp2,
                                String disp3) {
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(guardar.isChecked()){
            Toast.makeText(RegistroFragment.this.getActivity(), "Datos guardados", Toast.LENGTH_LONG).show();
            SharedPreferences correos_bd = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = correos_bd.edit();


                    editor.putString("email1_bd","");
                    editor.putString("email2_bd","");
                    editor.putString("email3_bd","");
                    editor.putString("email1_bd",e1);
                    editor.putString("email2_bd",e2);
                    editor.putString("email3_bd",e3);
                    editor.putString("dispositivo1_bd","");
                    editor.putString("dispositivo2_bd","");
                    editor.putString("dispositivo3_bd","");
                    editor.putString("dispositivo1_bd",d1);
                    editor.putString("dispositivo2_bd",d2);
                    editor.putString("dispositivo3_bd",d3);


            editor.commit();

                    dire1.setText(e1);
                    dire2.setText(e2);
                    dire3.setText(e3);
                    dis1.setText(d1);
                    dis2.setText(d2);
                    dis3.setText(d3);

            Log.d("SHARED","Shared preferences creadas" );


        }
        else {
            dire1.setEnabled(true);
        }

    }
            });

        SharedPreferences correos_activity = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = correos_activity.edit();

        editor.putString("email1_ac","");
        editor.putString("email2_ac","");
        editor.putString("email3_ac","");
        editor.putString("email1_ac",e1);
        editor.putString("email2_ac",e2);
        editor.putString("email3_ac",e3);
        editor.putString("dispositivo1_ac","");
        editor.putString("dispositivo2_ac","");
        editor.putString("dispositivo3_ac","");
        editor.putString("dispositivo1_ac",d1);
        editor.putString("dispositivo2_ac",d2);
        editor.putString("dispositivo3_ac",d3);


        editor.commit();



        mListener.onFragmentInteraction(dir1,dir2,dir3,disp1,disp2,disp3);

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
        void onFragmentInteraction(String data, String dir2, String dir3, String dis1, String dis2, String dis3);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences correos_activity = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = correos_activity.edit();

        editor.remove("email1_ac").commit();
        editor.remove("email2_ac").commit();
        editor.remove("email3_ac").commit();
        editor.remove("dispositivo1_ac").commit();
        editor.remove("dispositivo2_ac").commit();
        editor.remove("dispositivo3_ac").commit();




    }
}
