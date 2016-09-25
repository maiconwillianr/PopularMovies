package br.com.maiconribeiro.popularmovies.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.model.Filme;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maiconwillianribeiro on 24/09/16.
 */

public class DetalhesFilmeService extends AsyncTask<String, Void, Filme> {

    private final String LOG_TAG = DetalhesFilmeService.class.getSimpleName();

    @Override
    protected Filme doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String jsonResult;

        Response response;

        try {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "movie";
            final String MOVIE_ID = params[0];
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendPath(MOVIE_ID)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();

            response = client.newCall(request).execute();

            jsonResult = response.body().string();

            return this.parseJson(jsonResult);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }

        return null;
    }

    public Filme parseJson(String jsonResult) {

        Filme filme = new Filme();

        final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVAREGE = "vote_average";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_COUNT = "vote_count";
        final String RUNTIME = "runtime";

        try {

            JSONObject f = new JSONObject(jsonResult);

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


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return filme;
    }
}
