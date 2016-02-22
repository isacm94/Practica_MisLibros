package hlc.daw2.isa.mislibros;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import java.sql.SQLException;

public class VistaLibro extends AppCompatActivity {

    private Cursor cursor;
    private LibrosDB DB = new LibrosDB(this);

    EditText et_titulo;
    EditText et_autor;
    EditText et_editorial;
    EditText et_isbn;
    EditText et_paginas;
    EditText et_anho;
    CheckBox cb_ebook;
    CheckBox cb_leido;
    RatingBar rat_nota;
    EditText et_resumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_libro);

        et_titulo = (EditText)findViewById(R.id.et_titulo);
        et_autor = (EditText)findViewById(R.id.et_autor);
        et_editorial = (EditText)findViewById(R.id.et_editorial);
        et_isbn = (EditText)findViewById(R.id.et_isbn);
        et_paginas = (EditText)findViewById(R.id.et_paginas);
        et_anho = (EditText)findViewById(R.id.et_anho);
        cb_ebook = (CheckBox)findViewById(R.id.cb_ebook);
        cb_leido = (CheckBox)findViewById(R.id.cb_leido);
        rat_nota = (RatingBar)findViewById(R.id.rat_nota_lib);
        et_resumen = (EditText)findViewById(R.id.et_resumen);

        long ID = getIntent().getLongExtra("id", 0);//Recogemos el id que nos llega


        try {
            DB.openR();//Abre BD
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cursor = DB.getUnLibro(ID);//Guardamos en el cursor el libro

        //if(cursor.moveToFirst()) {
            et_titulo.setText(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            et_autor.setText(cursor.getString(cursor.getColumnIndexOrThrow("autor")));
            et_editorial.setText(cursor.getString(cursor.getColumnIndexOrThrow("editorial")));
            et_isbn.setText(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
            et_paginas.setText(cursor.getString(cursor.getColumnIndexOrThrow("paginas")));
            et_anho.setText(cursor.getString(cursor.getColumnIndexOrThrow("anio")));

        Boolean checkeado_ebook = false;

        if(cursor.getColumnIndexOrThrow("ebook") == '1')
            checkeado_ebook = true;
        else
            checkeado_ebook = false;

        cb_ebook.setChecked(checkeado_ebook);

        Boolean checkeado_leido = false;

        if(cursor.getColumnIndexOrThrow("leido") == '1')
            checkeado_leido = true;
        else
            checkeado_leido = false;

        cb_leido.setChecked(checkeado_leido);
        rat_nota.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
        et_resumen.setText(cursor.getString(cursor.getColumnIndexOrThrow("resumen")));

        //}

        DB.close();

    }
}
