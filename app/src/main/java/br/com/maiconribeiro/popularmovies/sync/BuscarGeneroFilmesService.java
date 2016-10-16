package br.com.maiconribeiro.popularmovies.sync;

import android.content.Context;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Genero;

/**
 * Created by maiconwillianribeiro on 09/10/16.
 */

public class BuscarGeneroFilmesService {

    private final String LOG_TAG = BuscarGeneroFilmesService.class.getSimpleName();

    private ArrayList<Genero> generos;

    private AsyncTaskDelegate delegate = null;
    private Context context;

    public BuscarGeneroFilmesService(AsyncTaskDelegate responder, Context context) {
        this.delegate = responder;
        this.context = context;
    }

    public void buscarGeneros() {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "genre";
            final String TIPO_ESPETACULO = "movie";
            final String LIST = "list";
            final String API_KEY = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String LANGUAGE_VALUE = "pt-Br";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendEncodedPath(TIPO_ESPETACULO)
                    .appendEncodedPath(LIST)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
/*
            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url.toString(),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject generoJson) {

                            generos = new ArrayList<>();

                            final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
                            final String RESULTS = "results";
                            final String TITLE = "title";
                            final String POSTER_PATH = "poster_path";
                            final String VOTE_AVAREGE = "vote_average";
                            final String OVERVIEW = "overview";
                            final String RELEASE_DATE = "release_date";
                            final String VOTE_COUNT = "vote_count";
                            final String ID = "id";

                            try {

                                JSONArray filmesArray = generoJson.getJSONArray(RESULTS);

                                if (filmesArray.length() > 0) {
                                    for (int i = 0; i < filmesArray.length(); i++) {
                                        JSONObject f = filmesArray.getJSONObject(i);
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
                                        filmes.add(filme);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (delegate != null) {
                                delegate.processFinish(filmes);
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
            */
        }
    }

}
