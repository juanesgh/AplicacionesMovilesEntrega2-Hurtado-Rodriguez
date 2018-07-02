package com.example.nicolas.entrega2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicolas.entrega2.db.AppDatabase;
import com.example.nicolas.entrega2.db.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 08/02/17.
 */
public class SearchRecipesFragment extends Fragment {
    private Context context;

    public SearchRecipesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recipes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickEvent(view);
    }



    private void onClickEvent(View view) {

        view.findViewById(R.id.Search_Ingredients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SearchRecipeByIngredients.class);
                    startActivity(intent);
                }
        });
        view.findViewById(R.id.Search_Titles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchRecipeByName.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.Search_Preparation_Times).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchRecipeByPreparationTime.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.Search_Nationality).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchRecipeByNationality.class);
                startActivity(intent);
            }
        });
    }
}
