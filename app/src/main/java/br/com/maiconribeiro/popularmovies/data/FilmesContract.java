package br.com.maiconribeiro.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by maiconribeiro on 13/10/16.
 */

public class FilmesContract {

    public static final class FilmeEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "tb_filme";

        public static final String TITULO = "titulo";
        public static final String PATH_IMAGEM_POSTER = "pathImagemPoster";
        public static final String SINOPSE = "sinopse";
        public static final String DATA_LANCAMENTO = "dataLancamento";
        public static final String NOTA_MEDIA = "notaMedia";
        public static final String NUMERO_VOTOS = "numeroVotos";
        public static final String HOMEPAGE = "homepage";
        public static final String DURACAO = "duracao";

    }
}
