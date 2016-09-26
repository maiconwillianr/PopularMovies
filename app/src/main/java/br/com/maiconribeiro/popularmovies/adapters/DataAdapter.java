package br.com.maiconribeiro.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.FilmeDetalhesActivity;
import br.com.maiconribeiro.popularmovies.R;
import br.com.maiconribeiro.popularmovies.VolleySingleton;
import br.com.maiconribeiro.popularmovies.helpers.Util;
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
        if (!"".equals(filmes.get(i).getPathImagemPoster())) {
            Picasso.with(context).load(filmes.get(i).getPathImagemPoster()).into(viewHolder.imagemfilme);
        } else {
            Picasso.with(context).load(R.drawable.not_found).into(viewHolder.imagemfilme);
        }

        viewHolder.imagemfilme.setTag(i);
        viewHolder.imagemfilme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Integer pos = (Integer) view.getTag();

                final String URL_BASE = "https://api.themoviedb.org/3/";
                final String TIPO_SERVICO = "movie";
                final String MOVIE_ID = filmes.get(pos).getIdFilme();
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                        .appendPath(TIPO_SERVICO)
                        .appendPath(MOVIE_ID)
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = null;
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                //Verifica se a conexao com a internet
                if (Util.checkConnection(context)) {

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject f) {

                                    final String ID = "id";
                                    final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
                                    final String TITLE = "title";
                                    final String POSTER_PATH = "poster_path";
                                    final String VOTE_AVAREGE = "vote_average";
                                    final String OVERVIEW = "overview";
                                    final String RELEASE_DATE = "release_date";
                                    final String VOTE_COUNT = "vote_count";
                                    final String RUNTIME = "runtime";

                                    try {

                                        filmes.get(pos).setIdFilme(f.getString(ID));
                                        filmes.get(pos).setTitulo(f.getString(TITLE));
                                        if (!"null".equals(f.getString(POSTER_PATH))) {
                                            filmes.get(pos).setPathImagemPoster(IMAGE_PATH + f.get(POSTER_PATH));
                                        } else {
                                            filmes.get(pos).setPathImagemPoster("");
                                        }
                                        filmes.get(pos).setNotaMedia(f.getString(VOTE_AVAREGE));
                                        filmes.get(pos).setSinopse(f.getString(OVERVIEW));
                                        filmes.get(pos).setDataLancamento(f.getString(RELEASE_DATE));
                                        filmes.get(pos).setNumeroVotos(f.getString(VOTE_COUNT));
                                        filmes.get(pos).setDuracao(f.getString(RUNTIME));

                                        Intent intent = new Intent(context, FilmeDetalhesActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        intent.putExtra(Filme.PARCELABLE_KEY, filmes.get(pos));

                                        context.startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                    RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
                    queue.add(request);
                }

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