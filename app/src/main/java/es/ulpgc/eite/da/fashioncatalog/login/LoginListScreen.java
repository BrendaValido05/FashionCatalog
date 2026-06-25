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
    //Recogemos el mediator del CatalogMediator
    CatalogMediator mediator = CatalogMediator.getInstance();
    //Recogemos el repositorio del CatalogRepository
    RepositoryContract repository = CatalogRepository.getInstance(context.get());
    //Creamos el presenter del CategoryListPresenter importando el mediator
    LoginListContract.Presenter presenter=new LoginListPresenter(mediator);
    //Creamos el model del CategoryListModel importando el repositorio
    LoginListModel model = new LoginListModel(repository);
    //Inyectamos la vista con el presenter
    presenter.injectView(new WeakReference<>(view));
    //Inyectamos el model con el presenter
    presenter.injectModel(model);
    //Inyectamos el presenter con la vista
    view.injectPresenter(presenter);
  }
}
