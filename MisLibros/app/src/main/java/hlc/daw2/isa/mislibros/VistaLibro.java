package hlc.daw2.isa.mislibros;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    boolean anhadir, editar, eliminar;

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

        //Coge los elementos de la vista
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

        if (ID != 0) {//Si es distinto de 0, es que llega para editar

            //Se usa en la función onPrepareMenuOptions()
            anhadir = false;//Para que no muestre el botón de anhadir/guardar ya que está editando
            editar = true;//Para que si muestre el botón de editar
            eliminar = true;//Para que si muestre el botón de eliminar

            try {
                DB.openR();//Abre BD
            } catch (SQLException e) {
                e.printStackTrace();
            }

            cursor = DB.getUnLibro(ID);//Guardamos en el cursor el libro

            //Guardamos los datos que nos viene del cursor en la vista
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

            DB.close();
        } else {//Si llega 0, es para añadir un libro
            //Se usa en la función onPrepareMenuOptions(), Solo debe mostrar el botón de anhadir/guardar porque está añadiendo un nuevo libro
            anhadir = true;
            editar = false;
            eliminar = false;
        }

    }

    /**
     * Carga el menu de la barra de arriba
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Establece las funciones de las opciones del menú
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Actualizar();
                break;
            case R.id.guardar: {
                Insertar();
                break;
            }
            case R.id.eliminar: {
                MuestraAlertaEliminar("Eliminar libro", "¿Desea eliminar el libro " + et_titulo.getText().toString() + "?").show();
                break;
            }
        }
        return true;
    }

    /**
     * Establece si se debe mostrar o no una opción del menú
     */
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.guardar).setVisible(anhadir);
        menu.findItem(R.id.eliminar).setVisible(eliminar);
        menu.findItem(R.id.edit).setVisible(editar);
        return true;
    }

    /**
     * Eliminamos el libro de la ID que hayamos guardado anteriormente
     */
    public void Eliminar() {
        try {
            try {
                DB.openW();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            DB.deleteLibro(ID);

            DB.close();
        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error eliminando", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra una alerta modal, preguntando si quiere o no eliminar un libro
     * @param titulo
     * @param mensaje
     * @return
     */
    private AlertDialog MuestraAlertaEliminar(String titulo, String mensaje) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle(titulo);
        alerta.setMessage(mensaje);

        //Botón CANCELAR
        DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        };

        //Botón ACEPTAR
        DialogInterface.OnClickListener listenerOK = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Eliminar();
                Toast.makeText(getApplicationContext(), "Libro eliminado", Toast.LENGTH_SHORT).show();
                finish();//Finaliza actividad para volver a la vista principal
            }
        };

        //Establece el nombre del botón y la función que tiene que ejecutar
        alerta.setPositiveButton("Aceptar", listenerOK);
        alerta.setNegativeButton("Cancelar", listenerCancelar);

        return alerta.create();
    }

    /**
     * Modifica los datos de un libro, se tiene que introducir el título y el autor para poder guardar la modificación
     */
    public void Actualizar() {
        try {
            if (et_titulo.getText().toString().matches("") || et_autor.getText().toString().matches("")) {
                Toast.makeText(this, "Debe introducir un título y un autor", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    DB.openW();//Abre BD
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                DB.updateLibro(ID,
                        et_titulo.getText().toString(),
                        et_autor.getText().toString(),
                        et_editorial.getText().toString(),
                        et_isbn.getText().toString(),
                        et_anho.getText().toString(),
                        et_paginas.getText().toString(),
                        (cb_ebook.isChecked() ? 1 : 0),
                        (cb_leido.isChecked() ? 1 : 0),
                        rat_nota.getRating(),
                        et_resumen.getText().toString()
                );

                Toast.makeText(this, "Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
                DB.close();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error editando", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Añade un libro a la base de datos, se tiene que introducir el título y el autor para poder guardarlo
     */
    public void Insertar() {
        try {
            if (et_titulo.getText().toString().matches("") || et_autor.getText().toString().matches("")) {
                Toast.makeText(this, "Debe introducir un título y un autor", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    DB.openW();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                DB.insertLibro(
                        et_titulo.getText().toString(),
                        et_autor.getText().toString(),
                        et_editorial.getText().toString(),
                        et_isbn.getText().toString(),
                        et_anho.getText().toString(),
                        et_paginas.getText().toString(),
                        (cb_ebook.isChecked() ? 1 : 0),
                        (cb_leido.isChecked() ? 1 : 0),
                        rat_nota.getRating(),
                        et_resumen.getText().toString());

                DB.close();

                Toast.makeText(this, "Libro " + et_titulo.getText().toString() + " añadido correctamente", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error insertado", Toast.LENGTH_SHORT).show();
        }
    }
}
