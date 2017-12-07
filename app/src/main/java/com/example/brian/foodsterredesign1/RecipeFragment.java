package com.example.brian.foodsterredesign1;
// https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends android.support.v4.app.Fragment {
    View myView;
    protected RecyclerView mRecyclerView;
    //protected RecyclerView.LayoutManager mLayoutManager;
    private List<User> persons;

    public RecipeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecyclerView = (RecyclerView)myView.findViewById(R.id.rv);
        RVAdapter adapter = new RVAdapter(persons);
        //rv = (RecyclerView) myView.findViewById(R.id.rv);
        //LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        mRecyclerView.setAdapter(adapter);

        return myView;
    }

    // This method creates an ArrayList that has three Person objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.
    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new User("uniqueID", "Jarosz", "Brian"));
        persons.add(new User("uniqueID2", "Kerem", "Kücük"));
        persons.add(new User("uniqueID3", "AlHaji", "Ahmed"));
        persons.add(new User("uniqueID4", "Jarosz", "Brian"));
        persons.add(new User("uniqueID5", "Kerem", "Kücük"));
        persons.add(new User("uniqueID6", "AlHaji", "Ahmed"));
    }
}
