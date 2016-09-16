package br.com.maiconribeiro.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.maiconribeiro.popularmovies.model.Filme;

public class FilmeDetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filme_detalhes);

        if (getIntent().getExtras() != null) {

            Filme filme = (Filme) getIntent().getSerializableExtra("filme");
            TextView labelTitulo = (TextView) findViewById(R.id.tituloDetalhe);
            labelTitulo.setText(filme.getTitulo());
        }

        //Adiciona o botão up navegation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
