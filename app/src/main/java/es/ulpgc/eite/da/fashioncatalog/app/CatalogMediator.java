package es.ulpgc.eite.da.fashioncatalog.app;

import es.ulpgc.eite.da.fashioncatalog.categories.CategoryListState;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;
import es.ulpgc.eite.da.fashioncatalog.login.LoginListState;
import es.ulpgc.eite.da.fashioncatalog.product.ProductDetailState;
import es.ulpgc.eite.da.fashioncatalog.products.ProductListState;
import es.ulpgc.eite.da.fashioncatalog.register.RegisterState;


public class CatalogMediator {

    //Estado de la lista de Categorias
    private CategoryListState categoryListState = new CategoryListState();
    //Estado de la lista de Productos
    private ProductListState productListState = new ProductListState();
    //Estado del detalle de un Producto
    private ProductDetailState productDetailState = new ProductDetailState();
    private  LoginListState loginListState = new LoginListState();
    private RegisterState registerState = new RegisterState();


    //Variable que representa la categoria seleccionada

    //Variable que representa el producto seleccionado

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

    //Método que nos permite obtener el estado de la lista de Categorias
    public CategoryListState getCategoryListState() {
        return categoryListState;
    }

    //Método que nos permite obtener el estado del producto seleccionado
    public ProductDetailState getProductDetailState() {
        return productDetailState;
    }

    //Método que nos permite obtener el estado de la lista de Productos
    public ProductListState getProductListState() {
        return productListState;
    }

    public LoginListState getLoginListState() {
        return loginListState;
    }

    public RegisterState getRegisterState() {
        return registerState;
    }


    public UserItem getUser() {
        UserItem item = user;
        return item;
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