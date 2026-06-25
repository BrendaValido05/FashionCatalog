package es.ulpgc.eite.da.fashioncatalog.product;

import android.util.Log;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;


public class ProductDetailPresenter implements ProductDetailContract.Presenter {

  public static String TAG = "AdvMasterDetail.ProductDetailPresenter";

  private WeakReference<ProductDetailContract.View> view;
  private ProductDetailState state;
  private ProductDetailContract.Model model;
  private CatalogMediator mediator;

  public ProductDetailPresenter(CatalogMediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void onCreateCalled() {
    // Log.e(TAG, "onCreateCalled");

    state = new ProductDetailState();
  }

  @Override
  public void onRecreateCalled() {
    // Log.e(TAG, "onRecreateCalled");

    state = mediator.getProductDetailState();
  }

  @Override
  public void onPauseCalled() {
    Log.e(TAG, "onPauseCalled()");

    mediator.setProductDetailState(state);
  }


  private ProductItem getDataFromProductListScreen() {

    // set passed state
    CategoryItem category = mediator.getCategory();

    if (category != null) {
      state.category = category;
    }

    ProductItem product = mediator.getProduct();
    return product;
  }


  @Override
  public void fetchProductDetailData() {
    // Log.e(TAG, "fetchProductDetailData()");

    // set passed state
    ProductItem product = getDataFromProductListScreen();
    if(product != null) {
        state.product = product;
    }

    view.get().displayProductDetailData(state);
  }


  @Override
  public void injectView(WeakReference<ProductDetailContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(ProductDetailContract.Model model) {
    this.model = model;
  }

}
