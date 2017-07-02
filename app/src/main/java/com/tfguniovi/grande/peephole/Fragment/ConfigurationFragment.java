package com.tfguniovi.grande.peephole.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigurationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Switch intrusos,intervalo;
    public EditText nintrusos,secintervalo;
    private OnFragmentInteractionListener mListener;
    private ImageButton send;
    String segundos,numintrusos;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigurationFragment.
     */
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

        secintervalo.setEnabled(false);
        nintrusos.setEnabled(false);


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
                    if(intervalo.isChecked()==true){
                        segundos = String.valueOf(secintervalo.getText());
                        if(segundos=="" || segundos=="0"){
                            Toast.makeText(ConfigurationFragment.this.getActivity(),  "Introduzca los segundos", Toast.LENGTH_LONG).show();

                        }else
                            {
                        numintrusos = "0";
                        onButtonPressed(segundos,numintrusos);
                        }


                    }else if(intrusos.isChecked()==true){
                        String numerointrusos=String.valueOf(nintrusos);
                        if(numerointrusos=="" || numerointrusos=="0"){
                            Toast.makeText(ConfigurationFragment.this.getActivity(),  "Introduzca los segundos", Toast.LENGTH_LONG).show();
                        }
                        segundos="0";
                        onButtonPressed(numerointrusos,segundos);
                    }else {
                        Toast.makeText(ConfigurationFragment.this.getActivity(),  "Marque al menos una de las 2 opciones", Toast.LENGTH_LONG).show();
                    }



                }catch (Exception ex){

                }

            }


        });
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionConfiguration(String intervalosec,String numintrusos);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("intervalo", segundos);
        outState.putString("numero_intusos",numintrusos);
        outState.putBoolean("snintrusos", intrusos.isChecked());
        outState.putBoolean("sintervalo",intervalo.isChecked());

    }
}

