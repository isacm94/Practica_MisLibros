package hlc.daw2.isa.mislibros;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.sql.SQLException;

public class VistaLibro extends AppCompatActivity {

    private Cursor cursor;
    private LibrosDB DB = new LibrosDB(this);
    long ID;

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

        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_autor = (EditText) findViewById(R.id.et_autor);
        et_editorial = (EditText) findViewById(R.id.et_editorial);
        et_isbn = (EditText) findViewById(R.id.et_isbn);
        et_paginas = (EditText) findViewById(R.id.et_paginas);
        et_anho = (EditText) findViewById(R.id.et_anho);
        cb_ebook = (CheckBox) findViewById(R.id.cb_ebook);
        cb_leido = (CheckBox) findViewById(R.id.cb_leido);
        rat_nota = (RatingBar) findViewById(R.id.rat_nota_lib);
        et_resumen = (EditText) findViewById(R.id.et_resumen);

        ID = getIntent().getLongExtra("id", 0);//Recogemos el id que nos llega


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
        cb_ebook.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("ebook")) != 0);
        cb_leido.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("leido")) != 0);
        rat_nota.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
        et_resumen.setText(cursor.getString(cursor.getColumnIndexOrThrow("resumen")));

        //}

        DB.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "Pulsado botón editar", Toast.LENGTH_SHORT).show();
                Actualizar();
                break;
            case R.id.guardar:
                Toast.makeText(this, "Pulsado botón guardar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.eliminar:
                Toast.makeText(this, "Pulsado botón eliminar", Toast.LENGTH_SHORT).show();
                break;

        }
        return true; /** true -> consumimos el item, no se propaga*/
    }

    public void Actualizar() {
        try {
            DB.openW();//Abre BD
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //String titulo, String autor, String editorial, String isbn, Integer anio,
        //Integer paginas, Integer ebook, Integer leido, String nota, String resumen


        //String tit  = et_titulo.getText().toString();

        DB.updateLibro(ID,
                et_titulo.getText().toString(),
                et_autor.getText().toString(),
                et_editorial.getText().toString(),
                et_isbn.getText().toString(),
                Integer.parseInt(et_anho.getText().toString()),
                Integer.parseInt(et_paginas.getText().toString()),
                (cb_ebook.isChecked() ? 1 : 0),
                (cb_leido.isChecked() ? 1 : 0),
                rat_nota.getRating(),
                et_resumen.getText().toString()
        );

        Toast.makeText(this, "Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
        DB.close();
    }
}
