package com.example.computador.crud;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MeusLugares extends Fragment {
    private Context mContext;
    private static final String PREF_NAME = "LoginActivityPreference";
    private static final String String = "String";
    DbHelper dbH = new DbHelper(getActivity());
    List<String> lista;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comentarios, container, false);

        listView = (ListView) view.findViewById(R.id.listComentario);

        SharedPreferences sp = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int position = sp.getInt("codigo", 0);
        //List<String> lista;
        try {
            lista = dbH.selectPlaces(position);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        } catch (Exception e) {
            Log.i("Error:  ", "" + e.getMessage());
            String msg = (e.getMessage()==null)?"Login failed!":e.getMessage();
            Log.i("Login Error1",msg);
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TelaComentarios.class);
                SharedPreferences so = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String name;
                int pos = so.getInt("codigo", 0);
                List<String> lista;
                lista = dbH.selectPlaces(pos);
                name = lista.get(position);
                SharedPreferences sp = getActivity().getSharedPreferences(String, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("lugar", name);
                editor.commit();
                startActivity(intent);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(adapter);

    }



}




