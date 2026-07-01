package es.ulpgc.eite.da.fashioncatalog.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    @ColumnInfo(name = "userId")
    public int userId;

    @NonNull
    @ColumnInfo(name = "productId")
    public int productId;
}
