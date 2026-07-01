package es.ulpgc.eite.da.fashioncatalog.login;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class LoginListModel implements LoginListContract.Model {

    public static String TAG = LoginListModel.class.getSimpleName();

    private RepositoryContract repository;

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

    @Override
    public int getSessionUserId() {
        return repository.getSessionUserId();
    }

    @Override
    public void fetchUserById(int userId, RepositoryContract.GetUserCallback callback) {
        repository.getUser(userId, callback);
    }

    @Override
    public void saveSessionUserId(int userId) {
        repository.saveSessionUserId(userId);
    }

    @Override
    public void clearSession() {
        repository.clearSessionUserId();
    }
}