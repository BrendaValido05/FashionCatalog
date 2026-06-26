package es.ulpgc.eite.da.fashioncatalog.register;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class RegisterScreen {
  public static void configure(RegisterContract.View view) {
    WeakReference<FragmentActivity> context =
        new WeakReference<>((FragmentActivity) view);

    CatalogMediator mediator = CatalogMediator.getInstance();

    RepositoryContract repository = CatalogRepository.getInstance(context.get());
    //Creamos el presenter del RegisterPresenter importando el mediator
    RegisterContract.Presenter presenter = new RegisterPresenter(mediator);
    //Creamos el model del RegisterModel importando el repositorio
    RegisterContract.Model model = new RegisterModel(repository);

    presenter.injectView(new WeakReference<>(view));

    presenter.injectModel(model);

    view.injectPresenter(presenter);
  }
}
