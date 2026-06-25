package es.ulpgc.eite.da.fashioncatalog.product;

import java.lang.ref.WeakReference;

interface ProductDetailContract {

  interface View {
    void injectPresenter(Presenter presenter);

    void displayProductDetailData(ProductDetailViewModel viewModel);
  }

  interface Presenter {
    void injectView(WeakReference<View> view);
    void injectModel(Model model);

    void fetchProductDetailData();

    void onCreateCalled();

    void onRecreateCalled();

      void onPauseCalled();
  }

  interface Model {

  }

}