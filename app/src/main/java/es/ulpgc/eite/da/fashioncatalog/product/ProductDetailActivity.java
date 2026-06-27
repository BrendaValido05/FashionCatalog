package es.ulpgc.eite.da.fashioncatalog.product;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;


public class ProductDetailActivity
        extends AppCompatActivity implements ProductDetailContract.View {

    public static String TAG = "AdvMasterDetail.ProductDetailActivity";

    ProductDetailContract.Presenter presenter;
    private ActionBar actionBar;
    private FloatingActionButton favouriteButtonFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        favouriteButtonFB = findViewById(R.id.favouriteButtonFB);

        // do the setup
        ProductDetailScreen.configure(this);

        favouriteButtonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFavoriteButtonClicked();
            }
        });

        // do some work
        if(savedInstanceState == null) {
            presenter.onCreateCalled();

        }else{
            presenter.onRecreateCalled();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // do some work
        presenter.fetchProductDetailData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPauseCalled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void displayProductDetailData(ProductDetailViewModel viewModel) {
        Log.e(TAG, "displayProductDetailData");

        // deal with the data
        ProductItem product = viewModel.product;

        if (product != null) {

            if (actionBar != null) {
                actionBar.setTitle(product.content);
            }

            ((TextView) findViewById(R.id.product_detail)).setText(product.details);
            loadImageFromURL(
                    (ImageView) findViewById(R.id.product_image),
                    product.picture
            );

        }
    }

    @Override
    public void showFavoriteButton() {
        favouriteButtonFB.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFavoriteButton() {
        favouriteButtonFB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateFavoriteIcon(boolean isFavorite) {
        favouriteButtonFB.setImageResource(
                isFavorite ? R.drawable.ic_favorite_border_full : R.drawable.ic_favorite_border_empty
        );
    }

    @Override
    public void showGuestFavoriteError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProductDetailActivity.this,
                        "Debes iniciar sesión para marcar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImageFromURL(ImageView imageView, String imageUrl){
        RequestManager reqManager = Glide.with(imageView.getContext());
        RequestBuilder reqBuilder = reqManager.load(imageUrl);
        RequestOptions reqOptions = new RequestOptions();
        reqOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        reqBuilder.apply(reqOptions);
        reqBuilder.into(imageView);
    }


    @Override
    public void injectPresenter(ProductDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}