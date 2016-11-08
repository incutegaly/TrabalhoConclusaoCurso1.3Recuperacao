package com.example.computador.crud;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by COMPUTADOR on 08/11/2016.
 */

public class AvaliacaoListAdapter extends BaseAdapter {

    private Context context;

    public AvaliacaoListAdapter(Context context, List<String> mAvaliacao) {
        this.context = context;
        this.mAvaliacao = mAvaliacao;
    }

    private List<String> mAvaliacao;

    @Override
    public int getCount() {
        return mAvaliacao.size();
    }

    @Override
    public Object getItem(int position) {
        return mAvaliacao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.item_avaliacao, null);
        TextView tvNome_usuario = (TextView)view.findViewById(R.id.nome_usuario);
        TextView tvComentario = (TextView)view.findViewById(R.id.comentario);
        tvNome_usuario.setText((mAvaliacao.get(position)));
        tvComentario.setText(mAvaliacao.get(position));

        return view;
    }
}
