package br.com.maiconribeiro.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.R;
import br.com.maiconribeiro.popularmovies.model.Filme;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Filme> filmes;
    private Context context;

    public DataAdapter(Context context, ArrayList<Filme> filmes) {
        this.filmes = filmes;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tituloFilme.setText(filmes.get(i).getTitulo());
        Picasso.with(context).load(filmes.get(i).getPathImagemPoster()).resize(240, 120).into(viewHolder.imagemfilme);
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloFilme;

        private ImageView imagemfilme;

        public ViewHolder(View view) {
            super(view);

            tituloFilme = (TextView) view.findViewById(R.id.tituloFilme);
            imagemfilme = (ImageView) view.findViewById(R.id.imgPosterFilme);
        }

    }

}