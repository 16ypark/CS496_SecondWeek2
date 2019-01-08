package com.madcamp.test01012012.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.madcamp.test01012012.models.Contact;
import com.madcamp.test01012012.models.UCatalog;
import com.madcamp.test01012012.util.ServiceManager;
import com.madcamp.test01012012.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment1 extends Fragment {

    public static final String TAG = "SR Studios";
    private ServiceManager rManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        rManager = new ServiceManager();
        Call<UCatalog> requestCatalog = rManager.getUService().listCatalog();
        final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        requestCatalog.enqueue(new Callback<UCatalog>() {
            @Override
            public void onResponse(Call<UCatalog> call, Response<UCatalog> response) {

                if (response.isSuccess()) {
                    UCatalog catalog = response.body();
                    List<String> cat = new ArrayList<>();
                    for (Contact c : catalog.contacts) {
                        cat.add(String.format("%s\n%s\n%s", c.name, c.number, c.email));
                    }
                    listAdapter.addAll(cat);
                } else {
                    Log.i(TAG, "Error " + response.code());
                    Toast.makeText(getActivity().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UCatalog> call, Throwable t) {
                Log.e(TAG, "Error " + t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity().getApplicationContext(), "Selected Course: \n"+item, Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }
}
