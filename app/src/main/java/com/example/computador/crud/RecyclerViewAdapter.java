package com.example.computador.crud;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends Fragment{

    private RecyclerView mRecycler;
    private List<StringParametro> mList;
    private static final String PREF_NAME = "StringActivity";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teste, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.my_recycler_string);
        //mRecycler.setHasFixedSize(true);
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycler, new ClickListener(){

            @Override
            public void onClick(View view, int position) {
                StringParametro parametro = mList.get(position);

                Toast.makeText(getActivity(), parametro.getNome() +   "  Foi Selecionado", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("string", parametro.getNome());
                editor.commit();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener(){
        @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView,dx,dy);

            LinearLayoutManager lM = (LinearLayoutManager) mRecycler.getLayoutManager();
            StringAdapter stringAdapter = (StringAdapter) mRecycler.getAdapter();


        }

        });

        LinearLayoutManager lM = new LinearLayoutManager(getActivity());
        lM.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(lM);
        mRecycler.addItemDecoration(new DividerDecotarion(getActivity()));
        mList = new ArrayList<>();
        String [] strings = new String[]{"restaurant", "school", "hospital"};
        int [] photos = new int[] {R.drawable.ic_action_name, R.drawable.ic_action_name_school, R.drawable.ic_action_name_hospital};

        for (int i=0; i<strings.length; i++){
            StringParametro str = new StringParametro( strings[i],photos[i]);
            mList.add(str);
        }

        StringAdapter adapter = new StringAdapter(getActivity(),mList );
        mRecycler.setAdapter(adapter);
        return view;
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private Context context;
        private GestureDetector gestureDetector;
        private RecyclerViewAdapter.ClickListener clickListener;


        public RecyclerTouchListener(RecyclerViewAdapter recyclerViewAdapter, final RecyclerView mRecycler, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = mRecycler.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, mRecycler.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}
