package es.ulpgc.eite.da.fashioncatalog.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Define the FavoriteItem entity
@Entity(
        tableName = "favoritos",
        foreignKeys = {
                @ForeignKey(
                        entity = UserItem.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = ProductItem.class,
                        parentColumns = "id",
                        childColumns = "productId",
                        onDelete = CASCADE
                )
        },
        indices = { @Index(value = {"userId", "productId"}, unique = true) }
)
public class FavoriteItem {
    // Define the id column with auto generation
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    // Define the userId column and mark it as non-null
    @NonNull
    @ColumnInfo(name = "userId")
    public int userId;

    // Define the productId column and mark it as non-null
    @NonNull
    @ColumnInfo(name = "productId")
    public int productId;
}
