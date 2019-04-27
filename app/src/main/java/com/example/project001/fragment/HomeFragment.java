package com.example.project001.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.project001.PlanTrip;
import com.example.project001.R;
import com.example.project001.RidersActivity;

public class
HomeFragment extends Fragment {

    //Tabs
    LinearLayout linearLayout;
    TabHost frameLayout;
    LinearLayout triliniarLayout;

    //Add trips
    String email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(getArguments() != null){
            email = getArguments().getString("email");
            Log.e("homeFragment", email);
        }else{
            Log.e("doesn't work", "");
        }


        //Tabs
        frameLayout = getView().findViewById(R.id.tabHost);
        TabHost host = getView().findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.chats);
        spec.setIndicator("Passenger");
        host.addTab(spec);

        //set text color tab 1
        TextView tv = host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(getResources().getColor(R.color.white));

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.users);
        spec.setIndicator("Driver");
        host.addTab(spec);

        //set text color tab 2
        TextView tv2 = host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextColor(getResources().getColor(R.color.white));

        linearLayout = getView().findViewById(R.id.chats);
        triliniarLayout = getView().findViewById(R.id.users);


        //load map
        mainScreen();
        //load add trip
        planTrip();

    }

    //Handle map fragment
    public void mainScreen() {

        Bundle bun = new Bundle();
        bun.putString("email", email);





        //Fragment Map
        Fragment fragment=new RidersActivity();
        fragment.setArguments(bun);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(linearLayout.getId(), fragment, "maps");
        ft.commit();


        for(int i=0; i<linearLayout.getChildCount(); i++){

            Log.e("oneone", linearLayout.getChildAt(i).toString());

        }
    }

    public void planTrip() {


        Bundle bun = new Bundle();
        bun.putString("email", email);

        PlanTrip plan = new PlanTrip();
        plan.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(triliniarLayout.getId(), plan)
                .commit();
    }

}
