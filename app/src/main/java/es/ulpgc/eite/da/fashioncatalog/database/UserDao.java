package es.ulpgc.eite.da.fashioncatalog.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

@Dao
//Interfaz para la base de datos de CategoryDao
public interface UserDao {
  //Método para insertar una categoría
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertUser(UserItem user);
  @Update

  //Método para actualizar una categoría
  void updateUser(UserItem user);
  @Delete

  //Método para eliminar una categoría
  void deleteUser(UserItem user);

  //Método para cargar todas las categorías
  @Query(value = "SELECT * FROM usuarios")
  List<UserItem> loadUsers();

  //Método para cargar una categoría por su ID
  @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
  UserItem loadUser(int id);

  //Método para buscar un usuario por su email (login y comprobación de duplicados en registro)
  @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
  UserItem loadUserByEmail(String email);


}
