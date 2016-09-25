package br.com.maiconribeiro.popularmovies.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Filme;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class BuscarFilmesService extends AsyncTask<String, Void, ArrayList<Filme>> {

    private final String LOG_TAG = BuscarFilmesService.class.getSimpleName();

    private AsyncTaskDelegate delegate = null;

    public BuscarFilmesService(AsyncTaskDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected ArrayList<Filme> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String page = params[0];

        String jsonResult;

        Response response;

        try {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "discover";
            final String TIPO_ESPETACULO = "movie";
            final String DATA_INICIO_PARAM = "primary_release_date.gte";
            final String DATA_INICIO_VALUE = params[1];
            final String DATA_FINAL_PARAM = "primary_release_date.lte";
            final String DATA_FINAL_VALUE = params[2];
            final String SORT_BY_PARAM = "sort_by";
            final String SORT_BY_VALUE = params[3];
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

            URL url = new URL(builtUri.toString());

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();

            response = client.newCall(request).execute();

            jsonResult = response.body().string();

            return this.criarListaFilmes(jsonResult);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Filme> filmes) {
        super.onPostExecute(filmes);
        if(delegate != null){
            delegate.processFinish(filmes);
        }
    }

    private ArrayList<Filme> criarListaFilmes(String jsonResult) {

        ArrayList<Filme> filmes = new ArrayList<>();

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

            JSONObject filmesJson = new JSONObject(jsonResult);
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


        return filmes;
    }
}
