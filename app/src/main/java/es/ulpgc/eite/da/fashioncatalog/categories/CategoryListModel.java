package es.ulpgc.eite.da.fashioncatalog.categories;

import android.util.Log;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class CategoryListModel implements CategoryListContract.Model {

    public static String TAG = CategoryListModel.class.getSimpleName();

    private RepositoryContract repository;

    //Constructor de CategoryListModel
    public CategoryListModel(RepositoryContract repository) {
        this.repository = repository;

    }

    @Override
    public void fetchCategoryListData(final RepositoryContract.GetCategoryListCallback callback) {
        Log.d(TAG, "fetchCategoryListData()");
        //Pedimos al repositorio la lista de categorías. El propio repositorio decide
        //internamente si necesita cargar el catálogo desde el JSON (solo la primera vez)
        //o si debe leer directamente de Room (en todas las ejecuciones siguientes).
        repository.loadCatalog(
                false, new RepositoryContract.FetchCatalogDataCallback() {
                    @Override
                    public void onCatalogDataFetched(boolean error) {
                        //En caso que no haya error
                        if(!error) {
                            //Cargamos la lista de categorías desde el repositorio
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