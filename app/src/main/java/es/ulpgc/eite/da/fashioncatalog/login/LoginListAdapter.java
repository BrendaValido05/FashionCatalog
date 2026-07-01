package es.ulpgc.eite.da.fashioncatalog.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;

public class LoginListAdapter extends RecyclerView.Adapter<LoginListAdapter.ViewHolder> {

  private List<CategoryItem> itemList;
  private final View.OnClickListener clickListener;



  public LoginListAdapter(View.OnClickListener listener) {
    itemList = new ArrayList();
    clickListener = listener;
  }


  public void addItem(CategoryItem item){
    itemList.add(item);
    notifyDataSetChanged();
  }

  public void addItems(List<CategoryItem> items){
    itemList.addAll(items);
    notifyDataSetChanged();
  }

  public void setItems(List<CategoryItem> items){
    itemList = items;
    notifyDataSetChanged();
  }


  @Override
  public int getItemCount() {
    return itemList.size();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.category_list_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.itemView.setTag(itemList.get(position));
    holder.itemView.setOnClickListener(clickListener);

    holder.contentView.setText(itemList.get(position).content);

  }

  class ViewHolder extends RecyclerView.ViewHolder {
    final TextView contentView;

    ViewHolder(View view) {
      super(view);
      contentView = view.findViewById(R.id.content);
    }
  }
}
