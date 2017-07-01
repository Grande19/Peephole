package com.tfguniovi.grande.peephole.Fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tfguniovi.grande.peephole.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntrusosFragment interface
 * to handle interaction events.
 * Use the {@link IntrusosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntrusosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1 , listaDips;
    private ArrayList<String> mParam2;
    ListView lista,trusted;
    ArrayList listadispositivos,listaconfianza;

    private OnFragmentInteractionListener mListener;

    public IntrusosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param trusted_device
     * @return A new instance of fragment IntrusosFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            if(listaconfianza.isEmpty()==false){
                Toast.makeText(IntrusosFragment.this.getActivity(), "Registre los paramteros", Toast.LENGTH_LONG).show();
            }
            }
            if(listadispositivos.isEmpty()==true){
                Toast.makeText(IntrusosFragment.this.getActivity(), "No hay dispositivos registrados", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,listaconfianza);
                    lista.setAdapter(adapter1);
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,listadispositivos);
                    lista.setAdapter(adapter);
                    Log.d("LISTA", "Escribelista");
                }
                catch (Exception ex){
                    Log.d("LISTA","aaaa");
                }
            }
        return v;

        }





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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(ArrayList disp,ArrayList arrayList);


}



