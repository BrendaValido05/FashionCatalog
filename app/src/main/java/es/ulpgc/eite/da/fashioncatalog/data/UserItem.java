package es.ulpgc.eite.da.fashioncatalog.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "usuarios")
public class UserItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nombre = "";

    @NonNull
    public String email = "";

    @NonNull
    public String password = "";

}


