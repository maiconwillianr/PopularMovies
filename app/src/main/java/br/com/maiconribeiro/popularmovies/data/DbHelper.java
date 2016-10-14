package br.com.maiconribeiro.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maiconribeiro on 13/10/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "filmes.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FILME_TABLE = "CREATE TABLE " + FilmesContract.FilmeEntry.TABLE_NAME + " (" +
                FilmesContract.FilmeEntry._ID + " INTEGER PRIMARY KEY," +
                FilmesContract.FilmeEntry.TITULO + " TEXT NOT NULL, " +
                FilmesContract.FilmeEntry.PATH_IMAGEM_POSTER + " TEXT, " +
                FilmesContract.FilmeEntry.SINOPSE + " TEXT NOT NULL, " +
                FilmesContract.FilmeEntry.DATA_LANCAMENTO + " TEXT NOT NULL, " +
                FilmesContract.FilmeEntry.NOTA_MEDIA + " REAL, " +
                FilmesContract.FilmeEntry.NUMERO_VOTOS + " INTEGER, " +
                FilmesContract.FilmeEntry.HOMEPAGE + " TEXT, " +
                FilmesContract.FilmeEntry.DURACAO + " REAL " +
                " );";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + FilmesContract.VideoEntry.TABLE_NAME + " (" +
                FilmesContract.VideoEntry._ID + " INTEGER PRIMARY KEY," +
                FilmesContract.VideoEntry.ISO_639_1 + " TEXT, " +
                FilmesContract.VideoEntry.ISO_3166_1 + " TEXT, " +
                FilmesContract.VideoEntry.KEY + " TEXT NOT NULL, " +
                FilmesContract.VideoEntry.NAME + " TEXT NOT NULL, " +
                FilmesContract.VideoEntry.SITE + " TEXT, " +
                FilmesContract.VideoEntry.SIZE + " TEXT, " +
                FilmesContract.VideoEntry.TYPE + " TEXT, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + FilmesContract.VideoEntry.FILME_KEY + ") REFERENCES " +
                    FilmesContract.FilmeEntry.TABLE_NAME + " (" + FilmesContract.FilmeEntry._ID + "), " +
                " );";

        db.execSQL(SQL_CREATE_FILME_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FilmesContract.FilmeEntry.TABLE_NAME);
        onCreate(db);
    }
}
