package com.example.nicolas.entrega2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Recipe;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavoriteRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipesFragment extends Fragment {
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


    public FavoriteRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteRecipesFragment newInstance(String param1, String param2) {
        FavoriteRecipesFragment fragment = new FavoriteRecipesFragment();
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
        return inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getDatabase(getActivity());
        lv = (ListView) getView().findViewById(R.id.List);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Recipe> recipes = db.recipeDao().fetchFavoriteRecipes();
                List<String> recipeNames= new ArrayList<String>();
                final List<Integer> recipeIds= new ArrayList<Integer>();
                for (int i=0; i<recipes.size(); i++) {
                    String aux = recipes.get(recipes.size()-i-1).getName();
                    recipeNames.add(aux);
                    Integer auxi = recipes.get(recipes.size()-i-1).getId();
                    recipeIds.add(auxi);
                }
                arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, recipeNames );
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(arrayAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getActivity(), DisplayRecipeRegular.class);
                                intent.putExtra("recipeId", String.valueOf(recipeIds.get(i)));
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }) .start();

    }
}
