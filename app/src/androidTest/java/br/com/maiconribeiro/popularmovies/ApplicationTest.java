package br.com.maiconribeiro.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import br.com.maiconribeiro.popularmovies.data.DbHelper;
import br.com.maiconribeiro.popularmovies.data.FilmesContract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    Context context;

    @Before
    public void configure() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FilmesContract.FilmeEntry.TABLE_NAME);

        context.deleteDatabase(DbHelper.DATABASE_NAME);

        SQLiteDatabase db = new DbHelper(
                context).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the filme entry
        // and video entry tables
        assertTrue("Error: Your database was created without both the filme entry table",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FilmesContract.FilmeEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> filmeColumnHashSet = new HashSet<String>();
        filmeColumnHashSet.add(FilmesContract.FilmeEntry._ID);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.TITULO);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.PATH_IMAGEM_POSTER);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.SINOPSE);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.DATA_LANCAMENTO);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.NOTA_MEDIA);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.NUMERO_VOTOS);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.HOMEPAGE);
        filmeColumnHashSet.add(FilmesContract.FilmeEntry.DURACAO);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            filmeColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required filme
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required filme entry columns",
                filmeColumnHashSet.isEmpty());

        db.close();
    }
}