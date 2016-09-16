package br.com.maiconribeiro.popularmovies.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.model.Filme;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class BuscarFilmesService extends AsyncTask<String, Void, ArrayList<Filme>> {

    private final String LOG_TAG = BuscarFilmesService.class.getSimpleName();

    @Override
    protected ArrayList<Filme> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String page = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonResult;

        try {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "discover";
            final String TIPO_ESPETACULO = "movie";
            final String DATA_INICIO_PARAM = "primary_release_date.gte";
            final String DATA_INICIO_VALUE = params[1];
            final String DATA_FINAL_PARAM = "primary_release_date.lte";
            final String DATA_FINAL_VALUE = params[2];
            final String SORT_BY_PARAM = "sort_by";
            final String SORT_BY_VALUE = "primary_release_date.desc";
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

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            jsonResult = buffer.toString();

            return this.criarListaFilmes(jsonResult);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
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

        try {

            JSONObject filmesJson = new JSONObject(jsonResult);
            JSONArray filmesArray = filmesJson.getJSONArray(RESULTS);

            if (filmesArray.length() > 0) {
                for (int i = 0; i < filmesArray.length(); i++) {
                    JSONObject f = filmesArray.getJSONObject(i);
                    Filme filme = new Filme();
                    filme.setTitulo(f.getString(TITLE));
                    if(!"null".equals(f.getString(POSTER_PATH))){
                        filme.setPathImagemPoster(IMAGE_PATH + f.get(POSTER_PATH));
                    }else{
                        filme.setPathImagemPoster("");
                    }
                    filme.setNotaMedia(f.getString(VOTE_AVAREGE));
                    filme.setSinopse(f.getString(OVERVIEW));
                    filme.setDataLancamento(f.getString(RELEASE_DATE));
                    filmes.add(filme);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return filmes;
    }
}
