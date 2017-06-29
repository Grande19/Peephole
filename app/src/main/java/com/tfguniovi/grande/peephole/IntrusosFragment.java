package com.tfguniovi.grande.peephole;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntrusosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntrusosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntrusosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1 , listaDips;
    ListView lista;
    ArrayList listadispositivos;

    private OnFragmentInteractionListener mListener;

    public IntrusosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IntrusosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntrusosFragment newInstance(ArrayList<CharSequence> param1) {
        IntrusosFragment fragment = new IntrusosFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lista = (ListView) getView().findViewById(R.id.dispostivos_lista);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList("lista");

    

            //final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, mParam1);
            //lista.setAdapter(adapter);






        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       /* if(listadispositivos.isEmpty()==true){
            Toast.makeText(IntrusosFragment.this.getActivity(), "No hay dispositivos registrados", Toast.LENGTH_LONG).show();}
        else {
            try {
                lista.setAdapter(new ArrayAdapter<String>(getView().getContext(),
                        android.R.layout.simple_list_item_1, listadispositivos));
                ArrayAdapter adapter = new ArrayAdapter(getView().getContext(),android.R.layout.simple_expandable_list_item_1,listadispositivos);
                lista.setAdapter(adapter);
            }
            catch (Exception ex){
                Log.d("LISTA","aaaa");
            }
        }*/
        return inflater.inflate(R.layout.fragment_intrusos, container, false);


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
        void onFragmentInteraction(ArrayList disp);


}
}
