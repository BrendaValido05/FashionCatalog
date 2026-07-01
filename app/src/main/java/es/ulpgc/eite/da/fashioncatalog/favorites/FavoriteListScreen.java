package es.ulpgc.eite.da.fashioncatalog.favorites;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


public class FavoriteListScreen {

  public static void configure(FavoriteListContract.View view) {

    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);

    CatalogMediator mediator = CatalogMediator.getInstance();
    RepositoryContract repository = CatalogRepository.getInstance(context.get());
    FavoriteListContract.Presenter presenter = new FavoriteListPresenter(mediator);
    FavoriteListModel model = new FavoriteListModel(repository);
    presenter.injectView(new WeakReference<>(view));
    presenter.injectModel(model);
    view.injectPresenter(presenter);

  }


}
