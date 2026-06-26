package es.ulpgc.eite.da.fashioncatalog.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.FavoriteItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

@Database(entities = {CategoryItem.class, ProductItem.class, UserItem.class, FavoriteItem.class}, version = 6,
        exportSchema = false)
public abstract class CatalogDatabase extends RoomDatabase {

  public abstract CategoryDao categoryDao();
  public abstract ProductDao productDao();
  public abstract  UserDao userDao();
  public abstract FavoriteDao favoriteDao();
}
