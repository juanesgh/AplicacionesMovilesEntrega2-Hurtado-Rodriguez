package com.example.nicolas.entrega2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {
    private AppDatabase db;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientsFragment newInstance(String param1, String param2) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getDatabase(getActivity());
        lv = (ListView) getView().findViewById(R.id.List);
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Ingredient> ingredients = db.ingredientDao().getAllIngredients();
                List<String> your_array_list = new ArrayList<String>();
                for (int i=0; i<ingredients.size(); i++) {
                    String aux = ingredients.get(ingredients.size()-i-1).getName();
                    your_array_list.add(aux);
                }
                arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,your_array_list );
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(arrayAdapter);
                    }
                });
            }
        }) .start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
