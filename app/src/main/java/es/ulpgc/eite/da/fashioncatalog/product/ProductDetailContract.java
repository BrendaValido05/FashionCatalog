package es.ulpgc.eite.da.fashioncatalog.product;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

interface ProductDetailContract {

    interface View {
        void injectPresenter(Presenter presenter);

        void displayProductDetailData(ProductDetailViewModel viewModel);

        //Muestra/oculta el botón de favoritos (oculto para invitados)
        void showFavoriteButton();

        void hideFavoriteButton();

        //Actualiza el icono del botón según si el producto es favorito o no
        void updateFavoriteIcon(boolean isFavorite);
    }

    interface Presenter {
        void injectView(WeakReference<View> view);
        void injectModel(Model model);

        void fetchProductDetailData();

        void onCreateCalled();

        void onRecreateCalled();

        void onPauseCalled();

        //Se llama cuando el usuario pulsa el botón de favoritos
        void onFavoriteButtonClicked();
    }

    interface Model {

        void isProductFavorite(int userId, int productId, RepositoryContract.IsProductFavoriteCallback callback);

        void addFavorite(int userId, int productId, RepositoryContract.AddFavoriteCallback callback);

        void removeFavorite(int userId, int productId, RepositoryContract.RemoveFavoriteCallback callback);
    }

}