package stockme.stockme.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.logica.Producto;
import stockme.stockme.util.Util;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class BDHandler  extends SQLiteOpenHelper {
    private static int bdVersion = Util.getBD_VERSION();

    public BDHandler(Context context) {
        super(context, "Productos", null, bdVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertarIniciales(db);
    }

    private void insertarIniciales(SQLiteDatabase db) {
        //tabla de producto
        db.execSQL("DROP TABLE IF EXISTS 'PRODUCTO';");
        db.execSQL("CREATE TABLE IF NOT EXISTS `PRODUCTO` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `NOMBRE` TEXT NOT NULL,`MARCA` TEXT,`SUPERMERCADO` TEXT NOT NULL, `PRECIO` REAL NOT NULL);");
        //tabla de supermercado
        db.execSQL("DROP TABLE IF EXISTS 'SUPERMERCADO';");
        db.execSQL("CREATE TABLE IF NOT EXISTS `SUPERMERCADO` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `NOMBRE` TEXT NOT NULL,`DIRECCION` TEXT NOT NULL);");
        //tabla de stock
        db.execSQL("DROP TABLE IF EXISTS 'STOCK';");
        db.execSQL("CREATE TABLE IF NOT EXISTS `STOCK` ( `PRODUCTO` INTEGER NOT NULL, `CANTIDAD` INTEGER NOT NULL, PRIMARY KEY(PRODUCTO), FOREIGN KEY(`PRODUCTO`) REFERENCES PRODUCTO(ID));");

        //ahora se añaden los elementos iniciales
        db.execSQL("INSERT INTO `PRODUCTO` VALUES(1, 'Leche', 'Hacendado', 'Mercadona', 0.60);");
        db.execSQL("INSERT INTO 'SUPERMERCADO' VALUES(1, 'Mercadona', 'Calle Vara del Rey 5, 26003');");
        db.execSQL("INSERT INTO 'STOCK' VALUES(1, 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //cuando cambie la versión de la base de datos
        //lo recomendado es que se borre lo existente y se importe de nuevo
    }

    public SQLiteDatabase obtenerManejadorLectura()
    {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase obtenerManejadorEscritura()
    {
        return this.getWritableDatabase();
    }

    public void abrir()
    {
        this.getWritableDatabase();
    }

    public void cerrar()
    {
        this.close();
    }

    public List<Producto> obtenerProductos(){
        ArrayList<Producto> lista = new ArrayList();
        String query = "SELECT * FROM PRODUCTO";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Producto book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Producto();
                book.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                book.setNombre(cursor.getString(cursor.getColumnIndex("NOMBRE")));
                book.setMarca(cursor.getString(cursor.getColumnIndex("MARCA")));
                book.setSupermercado(cursor.getString(cursor.getColumnIndex("SUPERMERCADO")));
                book.setPrecio(cursor.getFloat(cursor.getColumnIndex("PRECIO")));
                lista.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", lista.toString());
        db.close();
        return lista;
    }

    public boolean insertarProducto(Producto producto){
        boolean ok = false;
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        ContentValues values = new ContentValues();

        values.put("ID", producto.getId());
        values.put("NOMBRE", producto.getNombre());
        values.put("MARCA", producto.getMarca());
        values.put("SUPERMERCADO", producto.getSupermercado());
        values.put("PRECIO", producto.getPrecio());

        long indent = -1;
        SQLiteDatabase dbRead = this.obtenerManejadorLectura();
        String query = "select id from producto where id = " + producto.getId();
        Cursor cursor = db.rawQuery(query, null);
        if(!cursor.moveToFirst()){//no existe en la bd
            indent = db.insert("PRODUCTO",
                    null,
                    values);
        }
        dbRead.close();

        if(indent != -1){
            producto.setId((int) indent);
            ok = true;
        }

        db.close();
        return ok;
        /*
        * PreparedStatement prep = conn.prepareStatement("insert into participants values ($next_id,?,?);");
            prep.setString(2, "bla");
            prep.setString(3, "blub");
        * */
    }
}
