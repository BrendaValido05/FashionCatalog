package es.ulpgc.eite.da.fashioncatalog.product;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

interface ProductDetailContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void displayProductDetailData(ProductDetailViewModel viewModel);

        void showFavoriteButton();

        void hideFavoriteButton();

        void updateFavoriteIcon(boolean isFavorite);

        //Mensaje mostrado si un invitado intenta marcar/desmarcar favorito
        //(defensa adicional por si el botón llegara a ser visible/clicable sin sesión)
        void showGuestFavoriteError();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);
        void injectModel(Model model);

        void fetchProductDetailData();

        void onCreateCalled();

        void onRecreateCalled();

        void onPauseCalled();

        void onFavoriteButtonClicked();
    }

    interface Model {

        void isProductFavorite(int userId, int productId, RepositoryContract.IsProductFavoriteCallback callback);

        void addFavorite(int userId, int productId, RepositoryContract.AddFavoriteCallback callback);

        void removeFavorite(int userId, int productId, RepositoryContract.RemoveFavoriteCallback callback);
    }

}