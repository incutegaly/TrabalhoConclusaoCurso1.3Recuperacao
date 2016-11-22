package com.example.computador.crud;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
public class StringAdapter extends RecyclerView.Adapter<StringAdapter.MyViewHolder> {
    private List<StringParametro> mList;
    private LayoutInflater mLayoutInflater;

    public StringAdapter(Context c, List<StringParametro> l){
        mList = l;
        mLayoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_string, parent, false);
        MyViewHolder mvH = new MyViewHolder(v);
        return mvH;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.img_string.setImageResource(mList.get(position).getPhoto());
        holder.img_string2.setImageResource(mList.get(position).getPhoto());
        holder.img_string1.setImageResource(mList.get(position).getPhoto());
        holder.string_id.setText(mList.get(position).getNome());
//        holder.string_id1.setText(mList.get(position).getNome());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder{
        ViewGroup viewGroup;
        public ImageView img_string;
        public TextView string_id;
        public TextView string_id1;
        public ImageView img_string1;
        public ImageView img_string2;



        public MyViewHolder(View itemView) {
            super(itemView);
            viewGroup = (ViewGroup)itemView;
            img_string1 = (ImageView) itemView.findViewById(R.id.img_string1);
           img_string2 = (ImageView) itemView.findViewById(R.id.img_string2) ;
            img_string = (ImageView) itemView.findViewById(R.id.img_string);
            string_id = (TextView) itemView.findViewById(R.id.string_id);
            //string_id1 = (TextView) itemView.findViewById(R.id.string_id1);

        }


    }
}
