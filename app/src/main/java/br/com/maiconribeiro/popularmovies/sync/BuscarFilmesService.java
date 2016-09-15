package br.com.maiconribeiro.popularmovies.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.maiconribeiro.popularmovies.BuildConfig;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class BuscarFilmesService extends AsyncTask<Void, Void, String> {

    private final String LOG_TAG = BuscarFilmesService.class.getSimpleName();

    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonResult = null;

        //String url  = "https:/api.themoviedb.org/3/discover/movie?primary_release_date.gte=2016-08-01&primary_release_date.lte=2016-09-15&api_key=7ea383009eb3a8e046f6b5bf47afba18";
        try {


            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "discover";
            final String TIPO_ESPETACULO = "movie";
            final String DATA_INICIO_PARAM = "primary_release_date.gte";
            final String DATA_INICIO_VALUE = "2016-08-01";
            final String DATA_FINAL_PARAM = "primary_release_date.lte";
            final String DATA_FINAL_VALUE = "2016-09-1";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendEncodedPath(TIPO_ESPETACULO)
                    .appendQueryParameter(DATA_INICIO_PARAM, DATA_INICIO_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
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

            return jsonResult;

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
}
