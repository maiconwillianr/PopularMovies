package br.com.maiconribeiro.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.maiconribeiro.popularmovies.FilmeDetalhesActivity;
import br.com.maiconribeiro.popularmovies.R;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.sync.DetalhesFilmeService;

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
        if (!"".equals(filmes.get(i).getPathImagemPoster())) {
            Picasso.with(context).load(filmes.get(i).getPathImagemPoster()).into(viewHolder.imagemfilme);
        } else {
            Picasso.with(context).load(R.drawable.not_found).into(viewHolder.imagemfilme);
        }


        viewHolder.imagemfilme.setTag(i);
        viewHolder.imagemfilme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Integer pos = (Integer) view.getTag();

                Intent intent = new Intent(context, FilmeDetalhesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                DetalhesFilmeService detalhesFilmeService = new DetalhesFilmeService();

                try {

                    //Verifica se a conexao com a internet
                    if (Util.checkConnection(context)) {
                        intent.putExtra(Filme.PARCELABLE_KEY,
                                detalhesFilmeService.execute(filmes.get(pos).getIdFilme()).get());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (filmes != null) {
            return filmes.size();
        }
        return 0;
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