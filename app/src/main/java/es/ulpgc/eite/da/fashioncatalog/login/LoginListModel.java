package es.ulpgc.eite.da.fashioncatalog.login;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class LoginListModel implements LoginListContract.Model {

  public static String TAG = LoginListModel.class.getSimpleName();

  private RepositoryContract repository;

  //Constructor de CategoryListModel
  public LoginListModel(RepositoryContract repository) {
    this.repository = repository;

  }


  @Override
  public void fetchCategoryListData(RepositoryContract.GetCategoryListCallback callback) {

  }

  @Override
  public void fetchUserListData(RepositoryContract.GetUserListCallback callback) {
    repository.getUserList(callback);
  }
}
