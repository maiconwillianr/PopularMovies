package br.com.maiconribeiro.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import br.com.maiconribeiro.popularmovies.model.Filme;

public class FilmeDetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filme_detalhes);

        if (getIntent().getExtras() != null) {

            Filme filme = getIntent().getExtras().getParcelable(Filme.PARCELABLE_KEY);

            //Seta o titulo da ActionBar com o nome do filme escolhido
            getSupportActionBar().setTitle(filme.getTitulo());

            TextView labelTitulo = (TextView) findViewById(R.id.tituloDetalhe);
            labelTitulo.setText(filme.getTitulo());

            TextView dataLancamento = (TextView) findViewById(R.id.dataLancamento);
            if (filme.getDataLancamento() != null) {
                DateTime dt = DateTime.parse(filme.getDataLancamento(), DateTimeFormat.forPattern("yyyy-MM-dd"));
                dataLancamento.setText(dt.toString("dd-MM-yyyy"));
            } else {
                dataLancamento.setText("");
            }

            ImageView imageFilme = (ImageView) findViewById(R.id.imagemDetalhe);

            if (!"".equals(filme.getPathImagemPoster())) {
                Picasso.with(this).load(filme.getPathImagemPoster()).into(imageFilme);
            } else {
                Picasso.with(this).load(R.drawable.not_found).into(imageFilme);
            }

            TextView labelNumeroVotos = (TextView) findViewById(R.id.numeroVotos);
            labelNumeroVotos.setText(filme.getNumeroVotos());

            Float notaMedia = Float.valueOf(filme.getNotaMedia());
            RatingBar rb = (RatingBar) findViewById(R.id.mediaVotos);
            rb.setIsIndicator(true);
            rb.setRating(notaMedia / 2);

            TextView labelSinopse = (TextView) findViewById(R.id.sinopse);
            labelSinopse.setText(filme.getSinopse());

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
