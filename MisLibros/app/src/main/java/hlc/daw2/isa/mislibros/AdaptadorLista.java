package hlc.daw2.isa.mislibros;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by 2DAW on 03/02/2016.
 */
public class AdaptadorLista extends CursorAdapter {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AdaptadorLista(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_fila, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Cogemos los elementos de la vista
        TextView tv_titulo = (TextView) view.findViewById(R.id.tv_titulo_lib);
        TextView tv_autor = (TextView) view.findViewById(R.id.tv_autor);
        RatingBar rating_nota = (RatingBar) view.findViewById(R.id.rating_nota);
        ImageView imagen = (ImageView) view.findViewById(R.id.imagen);

        //Extraemos los datos del cursor
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
        String autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
        Float nota = cursor.getFloat(cursor.getColumnIndexOrThrow("nota"));

        //Log.d("AdaptadorLista","Nota: "+nota.toString());

        //Guardamos en los elementos, los datos guardados en el cursor
        tv_titulo.setText(titulo);
        tv_autor.setText(autor);
        rating_nota.setRating(nota);

        //Establece una imagen por defecto
        switch((int)(Math.random()*3)) {
            case 0:
                imagen.setImageResource(R.drawable.libro1);
                break;
            case 1:
                imagen.setImageResource(R.drawable.libro2);
                break;
            case 2:
                imagen.setImageResource(R.drawable.libro3);
                break;
            default:
                imagen.setImageResource(R.drawable.libro1);
                break;

        }
    }
}
