package es.ulpgc.eite.da.fashioncatalog.login;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


public class LoginListScreen {
  public static void configure(LoginListContract.View view) {
    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);

    CatalogMediator mediator = CatalogMediator.getInstance();
    RepositoryContract repository = CatalogRepository.getInstance(context.get());
    LoginListContract.Presenter presenter=new LoginListPresenter(mediator);
    LoginListModel model = new LoginListModel(repository);
    presenter.injectView(new WeakReference<>(view));
    presenter.injectModel(model);
    view.injectPresenter(presenter);
  }
}
