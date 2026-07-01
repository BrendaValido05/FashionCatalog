package es.ulpgc.eite.da.fashioncatalog.app;

import es.ulpgc.eite.da.fashioncatalog.categories.CategoryListState;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;
import es.ulpgc.eite.da.fashioncatalog.login.LoginListState;
import es.ulpgc.eite.da.fashioncatalog.product.ProductDetailState;
import es.ulpgc.eite.da.fashioncatalog.products.ProductListState;
import es.ulpgc.eite.da.fashioncatalog.register.RegisterState;
import es.ulpgc.eite.da.fashioncatalog.favorites.FavoriteListState;


public class CatalogMediator {

    private CategoryListState categoryListState = new CategoryListState();
    private ProductListState productListState = new ProductListState();
    private ProductDetailState productDetailState = new ProductDetailState();
    private  LoginListState loginListState = new LoginListState();
    private RegisterState registerState = new RegisterState();
    private FavoriteListState favoriteListState = new FavoriteListState();


    private UserItem user;
    private ProductItem product;
    private CategoryItem category;


    private static CatalogMediator INSTANCE;

    private CatalogMediator() {

    }

    public static void resetInstance() {
        INSTANCE = null;
    }


    public static CatalogMediator getInstance() {
        if(INSTANCE == null){
            INSTANCE = new CatalogMediator();
        }

        return INSTANCE;
    }

    public CategoryListState getCategoryListState() {
        return categoryListState;
    }

    public ProductDetailState getProductDetailState() {
        return productDetailState;
    }

    public ProductListState getProductListState() {
        return productListState;
    }

    public LoginListState getLoginListState() {
        return loginListState;
    }

    public RegisterState getRegisterState() {
        return registerState;
    }

    public FavoriteListState getFavoriteListState() {
        return favoriteListState;
    }


    public UserItem getUser() {
        return user;   // sin copia innecesaria
    }

    public void setUser(UserItem item) {

        this.user = item;
    }

    public void setProduct(ProductItem item) {
        this.product = item;
    }
    public void setProductDetailState(ProductDetailState state) {
        this.productDetailState = state;
    }

    public void setCategory(CategoryItem item) {
        this.category = item;
    }

    public ProductItem getProduct() {
        ProductItem item = product;
        //product = null;
        return item;
    }

    public CategoryItem getCategory() {
        CategoryItem categoryItem = category;
        //product = null;
        return categoryItem;
    }

}