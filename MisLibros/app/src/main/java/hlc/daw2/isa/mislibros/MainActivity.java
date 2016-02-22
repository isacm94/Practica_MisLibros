package hlc.daw2.isa.mislibros;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv_listaLibros;
    private Cursor cursor;
    LibrosDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Generar intencion
            }
        });

        DB = new LibrosDB(this);
        InsertaDatosIniciales();
        ActualizaVista();
        lv_listaLibros.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, VistaLibro.class);
        intent.putExtra("id", id);
        startActivity(intent);
        //String item = ((TextView)view).getText().toString();
        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
    }
    public void ActualizaVista() {
        lv_listaLibros = (ListView) findViewById(R.id.lv_listaLibros);

        try {
            DB.openR();//Abrimos la Bd en modo lectura
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cursor = DB.getLibros();//Guardamos en el cursor los libros

        if (cursor.moveToFirst()) {
            AdaptadorLista adaptador = new AdaptadorLista(this, cursor, 0);

            lv_listaLibros.setAdapter(adaptador);//Pasamos al listview los libros del cursor
        }

        DB.close();
    }

    public void InsertaDatosIniciales() {

        try {
            DB.openW();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (DB.getCount() == 0) {
                DB.insertLibro("CASI SIN QUERER", "DEFREDS", "FRIDA", "9788494398902", 2015,
                        172, 1, 0, 1f, "El amor algunas veces es tan complicado como impredecible. Pero al final lo que más valoramos son los detalles más simples, los más bonitos, los que llegan sin avisar. Y a la hora de escribir sobre sentimientos, no hay nada más limpio que hacerlo desde el corazón. Y eso hace Defreds en este libro.");

                DB.insertLibro("PALMERAS EN LA NIEVE", "LUZ GABAS", "TEMAS DE HOY", "9788499985138", 2015,
                        736, 1, 0, 2f, "Es 1953 Kilian abandona la nieve de la montaña oscense para iniciar junto a su hermano, Jacobo, el viaje de ida hacia una tierra desconocida, lejana y exótica, la isla de Fernando Poo. En las entrañas de este territorio exuberante y seductor, le espera su padre, un veterano de la finca Sampaka, el lugar donde se cultiva y tuesta uno de los mejores cacaos del mundo.En esa tierra eternamente verde, cálida y voluptuosa, los jóvenes hermanos descubren la ligereza de la vida social de la colonia en comparación con una España encorsetada y gris; comparten el duro trabajo necesario para conseguir el cacao perfecto de la finca Sampaka; aprenden las diferencias y similitudes culturales entre coloniales y autóctonos; y conocen el significado de la amistad, la pasión, el amor y el odio. Pero uno de ellos cruzará una línea prohibida e invisible y se enamorará perdidamente de una nativa. Su amor por ella, enmarcado en unas complejas circunstancias históricas");

                DB.insertLibro("LA CHICA DEL TREN", "PAULA HAWKINS", "PLANETA", "9788408141471", 2015,
                        496, 1, 0, 3f, "El bestseller que arrasa en las listas de más vendidos en EE. UU. y Reino Unido. ¿Estabas en el tren de las 8.04? ¿Viste algo sospechoso? Rachel, sí. Rachel toma siempre el tren de las 8.04 h. Cada mañana lo mismo: el mismo paisaje, las mismas casas? y la misma parada en la señal roja. Son solo unos segundos, pero le permiten observar a una pareja desayunando tranquilamente en su terraza. Siente que los conoce y se inventa unos nombres para ellos: Jess y Jason. Su vida es perfecta, no como la suya. Pero un día ve algo. Sucede muy deprisa, pero es suficiente. ¿Y si Jess y Jason no son tan felices como ella cree? ¿Y si nada es lo que parece? Tú no la conoces. Ella a ti, sí.");

                DB.insertLibro("LA GRAN APUESTA", "MICHAEL LEWIS", "DEBATE", "9788499922331", 2015,
                        336, 1, 0, 4f, "La gran apuesta, de Michael Lewis, autor de otras obras con la economía financiera de fondo como El poquer del mentiroso o Boomerang: viajes al nuevo tercer mundo europeo, es la excepcional crónica del crash inmobiliario que originó la mayor crisis de los últimos 70 años. Michael Lewis es autor de algunos de los libros más vendidos en las dos últimas décadas en Estados Unidos, como The Blind Side, recientemente adaptada al cine y protagonizada por Sandra Bullock; Moneyball, con Brad Pitt en el papel principal; La gran apuesta ha encabezado durante ocho meses las principales listas de ventas.");

                DB.insertLibro("EL MARCIANO", "ANDY WEIR", "DEBATE", "9788499922331", 2015,
                        336, 1, 0, 5f, "La gran apuesta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DB.close();
    }
}
