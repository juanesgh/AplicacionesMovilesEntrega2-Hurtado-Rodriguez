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
    private GridListAdapter adapter;
    private ArrayList<String> arrayList;
    private List<Integer> idsList;

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
        loadListView(view);
        onClickEvent(view);
    }

    private void loadListView(View view) {
        final ListView listView = (ListView) view.findViewById(R.id.list_view);
        final AppDatabase db = AppDatabase.getDatabase(getActivity());
        arrayList = new ArrayList<>();
        idsList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Ingredient> ingredients = db.ingredientDao().getAllIngredients();
                for (int i=0; i<ingredients.size(); i++) {
                    String aux = ingredients.get(i).getName();
                    Integer auxi = ingredients.get(i).getId();
                    arrayList.add(aux);
                    idsList.add(auxi);
                }
                Handler mainHandler = new Handler(getActivity().getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new GridListAdapter(context, arrayList, true);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }) .start();


    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();
                List<Integer> selectedIdsList = new ArrayList<>();
                if (selectedRows.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < selectedRows.size(); i++) {
                        if (selectedRows.valueAt(i)) {
                            Integer selectedRowLabel = idsList.get(selectedRows.keyAt(i));
                            selectedIdsList.add(selectedRowLabel);
                            stringBuilder.append(selectedRowLabel + "\n");
                        }
                    }
                    Intent intent = new Intent(getActivity(), RecipeIngredientActivity.class);
                    intent.putExtra("ids", (Serializable) selectedIdsList);
                    startActivity(intent);
                }
            }
        });
    }
}
