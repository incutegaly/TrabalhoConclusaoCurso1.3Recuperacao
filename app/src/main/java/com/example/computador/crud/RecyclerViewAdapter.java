package com.example.computador.crud;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends Fragment {

    private RecyclerView mRecycler;
    private List<StringParametro> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_teste, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.my_recycler_string);
        mRecycler.setHasFixedSize(true);
      /*  mRecycler.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycler, new ClickListener(){

            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

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
        mList = new ArrayList<>();
        String [] strings = new String[]{"Restaurant", "Schools", "Hospital"};
        int [] photos = new int[] {R.drawable.ic_action_name, R.drawable.ic_action_name_school, R.drawable.ic_action_name_hospital};

        for (int i=0; i<3; i++){
            StringParametro str = new StringParametro( strings[i % strings.length],photos[i % photos.length]);
            mList.add(str);
        }

        StringAdapter adapter = new StringAdapter(getActivity(),mList );
        mRecycler.setAdapter(adapter);
        return view;
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private Context context;
        private GestureDetector gestureDetector;
        private RecyclerView recyclerhack;
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

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
