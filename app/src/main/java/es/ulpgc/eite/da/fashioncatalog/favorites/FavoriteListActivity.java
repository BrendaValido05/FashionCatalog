package es.ulpgc.eite.da.fashioncatalog.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.product.ProductDetailActivity;
import es.ulpgc.eite.da.fashioncatalog.products.ProductListViewModel;

public class FavoriteListActivity extends AppCompatActivity implements FavoriteListContract.View {

  public static String TAG = FavoriteListActivity.class.getSimpleName();

  FavoriteListContract.Presenter presenter;
  private FavoriteListAdapter listAdapter;
  private ActionBar actionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.e(TAG, "Se ha iniciado la pantalla de favoritos");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.favorite_list);
    setTitle(R.string.title_favorite_list);

    Toolbar toolbar = findViewById(R.id.toolbarFavorite);
    setSupportActionBar(toolbar);

    actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    listAdapter = new FavoriteListAdapter(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ProductItem item = (ProductItem) view.getTag();
        presenter.selectProductListData(item);
      }
    });

    RecyclerView recyclerView = findViewById(R.id.favorite_list);
    recyclerView.setAdapter(listAdapter);

    // do the setup
    FavoriteListScreen.configure(this);

    // Fetch the favorite list data
    presenter.fetchFavoriteListData();
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.onResume();
  }

  public void onRestart() {
    super.onRestart();
    presenter.onRestart();
  }


  @Override
  public void navigateToProductDetailScreen() {
    Log.e(TAG, "navigateToProductDetailScreen()");
    Intent intent = new Intent(this, ProductDetailActivity.class);
    startActivity(intent);
  }

  @Override
  public void showFavoriteButton() {

  }

  @Override
  public void displayFavoriteListData(FavoriteListState state) {
    Log.e(TAG, "displayFavoriteListData()");

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        // Update the list adapter with the favorite products
        listAdapter.setItems(state.products);
      }
    });
  }

  @Override
  public void displayProductListData(ProductListViewModel viewModel) {

  }

  @Override
  public void injectPresenter(FavoriteListContract.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void displayProductListData(FavoriteListViewModel viewModel) {

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
