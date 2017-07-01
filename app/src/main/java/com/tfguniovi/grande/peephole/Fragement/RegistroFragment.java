package com.tfguniovi.grande.peephole.Fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;
    public String d1,d2,d3,e1,e2,e3;
    ArrayList dispos;
    ArrayList email;



    private OnFragmentInteractionListener mListener;
    public EditText dis1,dis2,dis3 ;
    public EditText dire1,dire2,dire3 ;
    Button enviar;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        enviar = (Button)getView().findViewById(R.id.enviar);
        dis1 = (EditText)getView().findViewById(R.id.dispositivo1);
        dis2 = (EditText)getView().findViewById(R.id.dispositvo2);
        dis3 = (EditText)getView().findViewById(R.id.dispositivo3);
        dire1 = (EditText)getView().findViewById(R.id.email1);
        dire2 = (EditText)getView().findViewById(R.id.email2);
        dire3 = (EditText)getView().findViewById(R.id.email3);
        enviar = (Button)getView().findViewById(R.id.enviar);
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
                    Log.d("DISPOSITIVOS" , d1 + d2 + d3);
                    if (TextUtils.isEmpty(d1)==false && TextUtils.isEmpty(d1)==false  && TextUtils.isEmpty(d1)==false
                            && TextUtils.isEmpty(d1)==false  &&TextUtils.isEmpty(d1)==false  && TextUtils.isEmpty(d1)==false){
                        Toast.makeText(RegistroFragment.this.getActivity(), "Registrado,puede empezar el descubrimiento", Toast.LENGTH_LONG).show();
                        onButtonPressed(d1,d2,d3,e1,e2,e3);
                        //Intent intent= new Intent(RegistroFragment.this.getActivity(),MainActivity.class);
                        //startActivity(intent);
                    }
                    else {

                        Toast.makeText(RegistroFragment.this.getActivity(), "Introduzca todos los campos", Toast.LENGTH_LONG).show();

                    }
                }catch(Exception ex){
                    Toast.makeText(RegistroFragment.this.getActivity(), "RELLENE TODOS LOS CAMPOS", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String dir1,String dir2, String dir3 , String dis1 , String dis2,
                               String dis3) {

        mListener.onFragmentInteraction(dir1,dir2,dir3,dis1,dis2,dis3);

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
    //Despues de hacer algo, pasa los datos fuera(al main por ejemplo para que empiece a registrar usuarios)
    public interface OnFragmentInteractionListener {


        // TODO: Update argument type and name
        void onFragmentInteraction(String data, String dir2, String dir3, String dis1, String dis2, String dis3);
    }


}
