package es.ulpgc.eite.da.fashioncatalog.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.data.FavoriteItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;

@Dao

public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteItem(FavoriteItem favoriteItem);
    @Update
    void updateFavoriteItem(FavoriteItem favoriteItem);
    @Delete
    void deleteFavoriteItem(FavoriteItem favoriteItem);

    @Query("SELECT COUNT(*) FROM favoritos")
    int getFavoriteItemTableSize();

    @Query("SELECT * FROM favoritos WHERE userId = :userId")
    List<FavoriteItem> getFavoritesByUserId(int userId);
    @Query("SELECT p.* FROM products p INNER JOIN favoritos f ON p.id = f.productId WHERE f.userId = :userId")
    List<ProductItem> getFavoriteProductsByUserId(int userId);

    @Query("SELECT COUNT(*) > 0 FROM favoritos WHERE productId = :productId AND userId = :userId")
    boolean isFavorite(int userId, int productId);

    @Query("SELECT * FROM favoritos WHERE userId = :userId AND productId = :productId")
    FavoriteItem getFavoriteByUserAndProduct(int userId, int productId);

    //Elimina todos los favoritos de un usuario (usado para dejar la bd en un estado
    //conocido antes de cada test de Espresso, ya que catalog.db no se recrea entre tests)
    @Query("DELETE FROM favoritos WHERE userId = :userId")
    void deleteFavoritesByUserId(int userId);

}