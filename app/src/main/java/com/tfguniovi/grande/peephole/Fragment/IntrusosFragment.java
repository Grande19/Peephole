package com.tfguniovi.grande.peephole.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 Fragmento que muesntra los dispositivos descubiertos en tiempo real
 */
public class IntrusosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> mParam1 , listaDips;
    private ArrayList<String> mParam2;
    ListView lista,trusted;
    ArrayList listadispositivos,listaconfianza;

    private OnFragmentInteractionListener mListener;

    public IntrusosFragment() {
        // Required empty public constructor
    }

    public static IntrusosFragment newInstance(ArrayList param1, ArrayList trusted_device) {


        IntrusosFragment fragment = new IntrusosFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArrayList(ARG_PARAM1, param1);
        args.putCharSequenceArrayList(ARG_PARAM2,trusted_device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getStringArrayList(ARG_PARAM2);



            //final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, mParam1);
            //lista.setAdapter(adapter);



            listaconfianza = mParam2;
            listadispositivos = mParam1;


        }
    }

    @Override public void onViewCreated(View v,Bundle savedInstanceState){


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.fragment_intrusos, container, false);
        if(v != null){
            lista = (ListView) v.findViewById(R.id.dispostivos_lista);
            trusted = (ListView) v.findViewById(R.id.dispostivos_confianza);
            if(listaconfianza.isEmpty()==true){
                Toast.makeText(IntrusosFragment.this.getActivity(), "Registre los paramteros", Toast.LENGTH_LONG).show();
            }
                try {

                    ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,listaconfianza);
                    trusted.setAdapter(adapter1);

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,listadispositivos);
                    lista.setAdapter(adapter);
                    Log.d("LISTA", "Escribelista" + listaconfianza + listadispositivos);
                }


                catch (Exception ex){
                    Log.d("LISTA","aaaa");
                }
            }
        return v;

        }





    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(ArrayList disp,ArrayList arrayList);


}



