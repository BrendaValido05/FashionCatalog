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
public interface UserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertUser(UserItem user);

  @Update
  void updateUser(UserItem user);

  @Delete
  void deleteUser(UserItem user);


  @Query(value = "SELECT * FROM usuarios")
  List<UserItem> loadUsers();

  @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
  UserItem loadUser(int id);

  @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
  UserItem loadUserByEmail(String email);


}
