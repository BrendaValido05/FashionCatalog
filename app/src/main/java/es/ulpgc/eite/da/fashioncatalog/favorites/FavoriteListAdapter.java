package es.ulpgc.eite.da.fashioncatalog.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;

public class FavoriteListAdapter
    extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

  //ArrayList de productos
  private List<ProductItem> itemList;
  private final View.OnClickListener clickListener;

  //Constructor de la clase
  public FavoriteListAdapter(View.OnClickListener listener) {
    //Creamos el ArrayList
    itemList = new ArrayList();
    //Asignamos el listener
    clickListener = listener;
  }

  //Método para agregar un producto
  public void addItem(ProductItem item){
    //Agregamos el producto al ArrayList
    itemList.add(item);
    //Notificamos al adaptador
    notifyDataSetChanged();
  }

  //Método para agregar una lista de productos
  public void addItems(List<ProductItem> items){
    //Agregamos los productos al ArrayList
    itemList.addAll(items);
    //Notificamos al adaptador
    notifyDataSetChanged();
  }

  //Método para intercambiar con el setter una lista de productos
  public void setItems(List<ProductItem> items){
    //Cambiamos la lista de productos
    itemList = items;
    //Notificamos al adaptador
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.favorite_list_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.contentView.setText(itemList.get(position).content);

    holder.itemView.setTag(itemList.get(position));
    holder.itemView.setOnClickListener(clickListener);
  }

  @Override
  //Método para obtener el tamaño de la lista
  public int getItemCount() {
    return itemList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final TextView contentView;

    ViewHolder(View view) {
      super(view);
      contentView = view.findViewById(R.id.content);
    }
  }
}
