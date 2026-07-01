package es.ulpgc.eite.da.fashioncatalog.categories;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


public class CategoryListScreen {
    public static void configure(CategoryListContract.View view) {
        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);
        //Recogemos el mediator del CatalogMediator
        CatalogMediator mediator = CatalogMediator.getInstance();
        //Recogemos el repositorio del CatalogRepository
        RepositoryContract repository = CatalogRepository.getInstance(context.get());
        //Creamos el presenter del CategoryListPresenter importando el mediator
        CategoryListContract.Presenter presenter=new CategoryListPresenter(mediator);
        //Creamos el model del CategoryListModel importando el repositorio
        CategoryListModel model = new CategoryListModel(repository);
        presenter.injectView(new WeakReference<>(view));
        presenter.injectModel(model);
        view.injectPresenter(presenter);
    }
}
