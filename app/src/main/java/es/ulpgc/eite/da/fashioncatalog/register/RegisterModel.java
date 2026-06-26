package es.ulpgc.eite.da.fashioncatalog.register;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

public class RegisterModel implements RegisterContract.Model {

  public static String TAG = RegisterModel.class.getSimpleName();

  private RepositoryContract repository;

  //Constructor de RegisterModel
  public RegisterModel(RepositoryContract repository) {
    this.repository = repository;
  }

  @Override
  public void registerUser(UserItem user, RepositoryContract.RegisterUserCallback callback) {
    repository.registerUser(user, callback);
  }
}
