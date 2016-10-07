package br.com.maiconribeiro.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.VolleySingleton;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Filme;

/**
 * Created by maiconribeiro on 06/10/16.
 */

public class BuscarDetalhesFilmeService {

    private final String LOG_TAG = BuscarDetalhesFilmeService.class.getSimpleName();

    private AsyncTaskDelegate delegate = null;
    private Context context;

    public BuscarDetalhesFilmeService(AsyncTaskDelegate responder, Context context) {
        this.delegate = responder;
        this.context = context;
    }

    public void buscarDetalhesFilmes(String idFilme) {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "movie";
            final String MOVIE_ID = idFilme;
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

                                Filme filme = new Filme();

                                filme.setIdFilme(f.getString(ID));
                                filme.setTitulo(f.getString(TITLE));
                                if (!"null".equals(f.getString(POSTER_PATH))) {
                                    filme.setPathImagemPoster(IMAGE_PATH + f.get(POSTER_PATH));
                                } else {
                                    filme.setPathImagemPoster("");
                                }
                                filme.setNotaMedia(f.getString(VOTE_AVAREGE));
                                filme.setSinopse(f.getString(OVERVIEW));
                                filme.setDataLancamento(f.getString(RELEASE_DATE));
                                filme.setNumeroVotos(f.getString(VOTE_COUNT));
                                filme.setDuracao(f.getString(RUNTIME));

                                if (delegate != null) {
                                    delegate.processFinish(filme);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
        }
    }

}
