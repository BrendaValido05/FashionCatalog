package es.ulpgc.eite.da.fashioncatalog.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;



import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;

import es.ulpgc.eite.da.fashioncatalog.products.ProductListActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CategoryListActivity
        extends AppCompatActivity implements CategoryListContract.View {

    public static String TAG = CategoryListActivity.class.getSimpleName();

    //Presenter de CategoryList
    CategoryListContract.Presenter presenter;

    //Adapter de CategoryList
    private CategoryListAdapter listAdapter;

    private FloatingActionButton goTofavouriteButtonFB;

    //onCreate de CategoryList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Recogemos el estado recibido por parámetro
        super.onCreate(savedInstanceState);
        //Actualizamos el layout
        setContentView(R.layout.activity_category_list);
        //Obtenemos la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Establecemos la toolbar
        setSupportActionBar(toolbar);
        //Obtenemos la Actionbar
        ActionBar actionBar = getSupportActionBar();

        //Establecemos el título de la Actionbar en caso que no sea null
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        //Se crea el adapter de CategoryList
        listAdapter = new CategoryListAdapter(new View.OnClickListener() {
            //Decidimos que hacer al pulsar un elemento de la lista
            @Override
            public void onClick(View view) {
                //Obtenemos el la categoría pulsada por el usuario
                CategoryItem item = (CategoryItem) view.getTag();
                //Usamos el presenter para pasar el item pulsado al ProductListActivity
                //y navegar a ProductListActivity
                presenter.selectCategoryListData(item);
            }
        });

    /*
    goTofavouriteButtonFB = findViewById(R.id.goTofavouriteButtonFB);

    goTofavouriteButtonFB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(CategoryListActivity.this, FavoriteListActivity.class);
        startActivity(intent);
      }
    });
    */


        //Obtenemos el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.category_list);
        //Establecemos el adapter
        recyclerView.setAdapter(listAdapter);

        goTofavouriteButtonFB = findViewById(R.id.goTofavouriteButtonFB);

        // do the setup
        CategoryListScreen.configure(this);

        // do some work
        presenter.fetchCategoryListData();
    }

    @Override
    public void displayCategoryListData(final CategoryListViewModel viewModel) {
        Log.e(TAG, "displayCategoryListData()");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // deal with the data
                listAdapter.setItems(viewModel.categories);
            }

        });

    }





    @Override
    public void navigateToProductListScreen() {
        Intent intent = new Intent(this, ProductListActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public void injectPresenter(CategoryListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showFavoriteButton() {
        goTofavouriteButtonFB.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFavoriteButton() {
        goTofavouriteButtonFB.setVisibility(View.INVISIBLE);
    }

}