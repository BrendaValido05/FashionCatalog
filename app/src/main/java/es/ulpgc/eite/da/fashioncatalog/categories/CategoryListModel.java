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
        Log.e(TAG, "fetchCategoryListData()");
        //Cargamos el catálogo desde el repositorio
        repository.loadCatalog(
                true, new RepositoryContract.FetchCatalogDataCallback() {
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

}
