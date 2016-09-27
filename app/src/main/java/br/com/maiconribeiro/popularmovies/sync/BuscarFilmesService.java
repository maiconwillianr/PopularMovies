package br.com.maiconribeiro.popularmovies.sync;

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.VolleySingleton;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Filme;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class BuscarFilmesService {

    private final String LOG_TAG = BuscarFilmesService.class.getSimpleName();

    private ArrayList<Filme> filmes;

    private AsyncTaskDelegate delegate = null;
    private Context context;

    public BuscarFilmesService(AsyncTaskDelegate responder, Context context) {
        this.delegate = responder;
        this.context = context;
    }

    public void buscarFilmes(String page, String dataInicio, String dataFim, String filtroPesquisa) {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "discover";
            final String TIPO_ESPETACULO = "movie";
            final String DATA_INICIO_PARAM = "primary_release_date.gte";
            final String DATA_INICIO_VALUE = dataInicio;
            final String DATA_FINAL_PARAM = "primary_release_date.lte";
            final String DATA_FINAL_VALUE = dataFim;
            final String SORT_BY_PARAM = "sort_by";
            final String SORT_BY_VALUE = filtroPesquisa;
            final String PAGE_PARAM = "page";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendEncodedPath(TIPO_ESPETACULO)
                    .appendQueryParameter(DATA_INICIO_PARAM, DATA_INICIO_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
                    .appendQueryParameter(PAGE_PARAM, page)
                    .appendQueryParameter(SORT_BY_PARAM, SORT_BY_VALUE)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url.toString(),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject filmesJson) {

                            filmes = new ArrayList<>();

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

                                JSONArray filmesArray = filmesJson.getJSONArray(RESULTS);

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

                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
        }
    }

}