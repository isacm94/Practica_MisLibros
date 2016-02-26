package hlc.daw2.isa.mislibros;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by 2DAW on 29/01/2016.
 */

/**
 * Clase que gestiona la Base de datos
 */
public class LibrosDB {

    //CONSTANTES
    //Columnas de la tabla libro
    public static final String ROW_ID = "_id";
    public static final String TITULO = "titulo";
    public static final String AUTOR = "autor";
    public static final String EDITORIAL = "editorial";
    public static final String ISBN = "isbn";
    public static final String ANIO = "anio";
    public static final String PAGINAS = "paginas";
    public static final String EBOOK = "ebook";
    public static final String LEIDO = "leido";
    public static final String NOTA = "nota";
    public static final String RESUMEN = "resumen";


    static final String TAG = "LibrosDB";
    static final String DATABASE_NAME = "myDB";
    static final String DATABASE_TABLE = "libros";
    static final int DATABASE_VERSION = 1;


    //Setencia para crear la tabla libros
    private static final String DATABASE_CREATE = "create table libros (_id integer primary key autoincrement, "
            + TITULO + " text, "
            + AUTOR + " text, "
            + EDITORIAL + " text, "
            + ISBN + " text, "
            + ANIO + " text, "
            + PAGINAS + " text, "
            + EBOOK + " integer, "
            + LEIDO + " integer, "
            + NOTA + " float, "
            + RESUMEN + " text "
            + ");";

    DatabaseHelper mDbHelper;
    SQLiteDatabase myBD;

    final Context mCtx;

/*    public LibrosDB(Context ctx){
        this.mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }*/

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);//Ejecutamos la setencia de crear la tabla libros
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " //$NON-NLS-1$//$NON-NLS-2$
                    + newVersion + ", which will destroy all old data"); //$NON-NLS-1$
            //db.execSQL("DROP TABLE IF EXISTS usersinfo"); //$NON-NLS-1$
            onCreate(db);
        }
    }


    public LibrosDB(Context ctx) {
        this.mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }


    /**
     * Añade un registro de libro a la base de datos
     */
    public long insertLibro(String titulo, String autor, String editorial, String isbn, String anio,
                            String paginas, Integer ebook, Integer leido, Float nota, String resumen) {
        ContentValues campos = new ContentValues();

        campos.put(TITULO, titulo);
        campos.put(AUTOR, autor);
        campos.put(EDITORIAL, editorial);
        campos.put(ISBN, isbn);
        campos.put(ANIO, anio);
        campos.put(PAGINAS, paginas);
        campos.put(EBOOK, ebook);
        campos.put(LEIDO, leido);
        campos.put(NOTA, nota);
        campos.put(RESUMEN, resumen);

        return this.myBD.insert(DATABASE_TABLE, null, campos);
    }


    /**
     * Elimina un registro de libro en la base de datos
     * @param rowId ID del libro que debe eliminar
     * @return
     */
    public boolean deleteLibro(long rowId) {

        return this.myBD.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Actualiza el registro de un libro
     */
    public boolean updateLibro(long rowId, String titulo, String autor, String editorial, String isbn, String anio,
                              String paginas, Integer ebook, Integer leido, Float nota, String resumen) {
        ContentValues campos = new ContentValues();

        campos.put(TITULO, titulo);
        campos.put(AUTOR, autor);
        campos.put(EDITORIAL, editorial);
        campos.put(ISBN, isbn);
        campos.put(ANIO, anio);
        campos.put(PAGINAS, paginas);
        campos.put(EBOOK, ebook);
        campos.put(LEIDO, leido);
        campos.put(NOTA, nota);
        campos.put(RESUMEN, resumen);

        return this.myBD.update(DATABASE_TABLE, campos, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Devuelve todos los libros guardados en la base de datos
     */
    public Cursor getLibros() {

        return this.myBD.rawQuery("SELECT * FROM libros", null);
    }

    /**
     * Devuelve un determinado libro
     * @param rowId ID del libro que debe devolver
     */
    public Cursor getUnLibro(long rowId) {

        Cursor cursor = myBD.rawQuery("SELECT * FROM libros" + " WHERE _id = " + rowId, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    /**
     * Devuelve el número de registros que tiene la tabla libros
     */
    public int getCount() throws SQLException {

        Cursor cursor = myBD.rawQuery("select count(*) from libros ", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * Abre la base de datos en modo escritura
     */
    public LibrosDB openW() throws SQLException {
        //this.mDbHelper = new DatabaseHelper(this.mCtx);
        myBD = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Abre la base de datos en modo lectura
     */
    public LibrosDB openR() throws SQLException {
       // this.mDbHelper = new DatabaseHelper(this.mCtx);
        myBD = mDbHelper.getReadableDatabase();
        return this;
    }

    /**
     * Cierra la base de datos
     */
    public void close() {
        this.mDbHelper.close();
    }
}