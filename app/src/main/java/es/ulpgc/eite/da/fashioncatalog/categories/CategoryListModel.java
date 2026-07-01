package es.ulpgc.eite.da.fashioncatalog.categories;

import android.util.Log;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class CategoryListModel implements CategoryListContract.Model {

    public static String TAG = CategoryListModel.class.getSimpleName();

    private RepositoryContract repository;

    public CategoryListModel(RepositoryContract repository) {
        this.repository = repository;

    }

    @Override
    public void fetchCategoryListData(final RepositoryContract.GetCategoryListCallback callback) {
        Log.d(TAG, "fetchCategoryListData()");
        //Pedimos al repositorio la lista de categorías. El  repositorio decide
        // si necesita cargar el catálogo desde el JSON (solo la primera vez)
        //o si debe leer directamente de Room (en todas las ejecuciones siguientes).
        repository.loadCatalog(
                false, new RepositoryContract.FetchCatalogDataCallback() {
                    @Override
                    public void onCatalogDataFetched(boolean error) {
                        if(!error) {
                            repository.getCategoryList(callback);
                        }
                    }
                });

    }

    @Override
    public void clearSession() {
        repository.clearSessionUserId();
    }

}