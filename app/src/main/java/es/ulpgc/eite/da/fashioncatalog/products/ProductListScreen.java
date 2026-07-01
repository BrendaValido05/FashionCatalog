package es.ulpgc.eite.da.fashioncatalog.products;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


public class ProductListScreen {

    public static void configure(ProductListContract.View view) {

        WeakReference<FragmentActivity> context =
                new WeakReference<>((FragmentActivity) view);


        CatalogMediator mediator = CatalogMediator.getInstance();

        RepositoryContract repository = CatalogRepository.getInstance(context.get());

        ProductListContract.Presenter presenter = new ProductListPresenter(mediator);
        ProductListModel model = new ProductListModel(repository);
        presenter.injectView(new WeakReference<>(view));
        presenter.injectModel(model);

        view.injectPresenter(presenter);

    }


}
