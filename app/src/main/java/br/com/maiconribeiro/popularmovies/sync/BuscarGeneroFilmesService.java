package br.com.maiconribeiro.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url.toString(),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject generoJson) {

                            generos = new ArrayList<>();

                            final String GENRES = "genres";
                            final String ID = "id";
                            final String NAME = "name";

                            try {

                                JSONArray generosArray = generoJson.getJSONArray(GENRES);

                                if (generosArray.length() > 0) {
                                    for (int i = 0; i < generosArray.length(); i++) {

                                        JSONObject f = generosArray.getJSONObject(i);

                                        Genero genero = new Genero();
                                        genero.setId(f.getString(ID));
                                        genero.setName(f.getString(NAME));

                                        generos.add(genero);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (delegate != null) {
                                delegate.processFinish(generos);
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

        }
    }

}
