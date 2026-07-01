package es.ulpgc.eite.da.fashioncatalog.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.favorites.FavoriteListActivity;
import es.ulpgc.eite.da.fashioncatalog.login.LoginListActivity;
import es.ulpgc.eite.da.fashioncatalog.products.ProductListActivity;

public class CategoryListActivity extends AppCompatActivity implements CategoryListContract.View {

    public static String TAG = CategoryListActivity.class.getSimpleName();
    CategoryListContract.Presenter presenter;

    private CategoryListAdapter listAdapter;

    private FloatingActionButton goTofavouriteButtonFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        RecyclerView recyclerView = findViewById(R.id.category_list);

        // Botón de Favoritos
        goTofavouriteButtonFB = findViewById(R.id.goTofavouriteButtonFB);
        if (goTofavouriteButtonFB != null) {
            goTofavouriteButtonFB.setOnClickListener(v ->
                    navigateToFavoriteListScreen()
            );
        } else {
            Log.e(TAG, "FloatingActionButton (goTofavouriteButtonFB) no encontrado");
        }

        // Configurar Adapter
        listAdapter = new CategoryListAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryItem item = (CategoryItem) view.getTag();
                if (item != null && presenter != null) {
                    presenter.selectCategoryListData(item);
                }
            }
        });

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        }

        CategoryListScreen.configure(this);

        if (presenter != null) {
            presenter.fetchCategoryListData();
            presenter.showFavoriteButton();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.showFavoriteButton();
        }
    }

    @Override
    public void displayCategoryListData(final CategoryListViewModel viewModel) {
        Log.d(TAG, "displayCategoryListData()");
        runOnUiThread(() -> {
            if (listAdapter != null && viewModel != null) {
                listAdapter.setItems(viewModel.categories);
            }
        });
    }

    @Override
    public void navigateToProductListScreen() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToFavoriteListScreen() {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        startActivity(intent);
    }

    @Override
    public void injectPresenter(CategoryListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showFavoriteButton() {
        if (goTofavouriteButtonFB != null) {
            goTofavouriteButtonFB.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideFavoriteButton() {
        if (goTofavouriteButtonFB != null) {
            goTofavouriteButtonFB.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            if (presenter != null) {
                presenter.logout();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginListActivity.class);
        //Limpiamos el back stack: tras hacer Logout no debe poder volverse a Categorías con "atrás"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}