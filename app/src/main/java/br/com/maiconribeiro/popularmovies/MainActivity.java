package br.com.maiconribeiro.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import br.com.maiconribeiro.popularmovies.sync.BuscarFilmesService;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuscarFilmesService buscarFilmesService = new BuscarFilmesService();
        try {
            String jsonResult = buscarFilmesService.execute().get();
            Log.i(LOG_TAG, jsonResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

       // Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);

        setContentView(R.layout.activity_main);
    }
}
