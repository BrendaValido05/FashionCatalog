package es.ulpgc.eite.da.fashioncatalog.products;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.product.ProductDetailActivity;


public class ProductListActivity extends AppCompatActivity implements ProductListContract.View {

    public static String TAG = ProductListActivity.class.getSimpleName();

    ProductListContract.Presenter presenter;

    private ProductListAdapter listAdapter;
    private ActionBar actionBar;
    private FloatingActionButton goTofavouriteButtonFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        goTofavouriteButtonFB = findViewById(R.id.goTofavouriteButtonFB);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listAdapter = new ProductListAdapter( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ProductItem item = (ProductItem) view.getTag();
                presenter.selectProductListData(item);
            }
        });




        RecyclerView recyclerView = findViewById(R.id.product_list);
        recyclerView.setAdapter(listAdapter);

        // do the setup
        ProductListScreen.configure(this);

        // do some work
        presenter.fetchProductListData();
    }


    @Override
    public void navigateToProductDetailScreen() {
        Log.e(TAG, "navigateToProductDetailScreen()");
        Intent intent = new Intent(this, ProductDetailActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void showFavoriteButton() {
        Log.e(TAG, "showFavoriteButton()");
        goTofavouriteButtonFB.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFavoriteButton() {
        Log.e(TAG, "hideFavoriteButton()");
        goTofavouriteButtonFB.setVisibility(View.INVISIBLE);
    }


    @Override
    public void injectPresenter(ProductListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayProductListData(final ProductListViewModel viewModel) {
        Log.e(TAG, "displayProductListData()");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // deal with the data
                CategoryItem category = viewModel.category;
                if (actionBar != null) {
                    actionBar.setTitle(category.content);
                }

                listAdapter.setItems(viewModel.products);
            }
        });
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
