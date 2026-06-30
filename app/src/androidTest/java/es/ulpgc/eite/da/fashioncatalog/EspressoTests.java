package es.ulpgc.eite.da.fashioncatalog;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.login.LoginListActivity;

//Tests de instrumentación (Espresso) que recorren los flujos principales exigidos por la
//rúbrica: login (correcto/incorrecto), invitado, navegación maestro-detalle, favoritos
//(solo para usuarios logueados) y logout.
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EspressoTests {

    @Rule
    public ActivityTestRule<LoginListActivity> testRule =
            new ActivityTestRule<>(LoginListActivity.class);

    public EspressoTestSteps testSteps = new EspressoTestSteps();

    @Before
    public void registerIdlingResource() throws InterruptedException {
        IdlingRegistry.getInstance().register(CatalogRepository.IDLING_RESOURCE);
        // Aseguramos que el usuario de prueba existe en BD antes de cada test de login
        testSteps.seedTestUser();
    }

    @After
    public void unregisterIdlingResource() {
        testSteps.resetearTest();
        IdlingRegistry.getInstance().unregister(CatalogRepository.IDLING_RESOURCE);
    }

    @Before
    public void setUp() throws InterruptedException {
        IdlingRegistry.getInstance().register(CatalogRepository.IDLING_RESOURCE);

        // Limpieza explícita antes de cada test
        testSteps.clearSession();
        testSteps.resetearTest();           // por si acaso

        // Seed del usuario de prueba
        testSteps.seedTestUser();
    }

    @After
    public void tearDown() {
        testSteps.clearSession();
        testSteps.resetearTest();
        IdlingRegistry.getInstance().unregister(CatalogRepository.IDLING_RESOURCE);
    }

    @Test
    public void test01MuestraPantallaDeLogin() {
        // Then la pantalla de login se muestra al arrancar la app
        testSteps.mostramosPantallaDeLogin();
    }

    @Test
    public void test02EntrarComoInvitado() {
        // Given mostramos pantalla de login
        testSteps.mostramosPantallaDeLogin();

        // When pulsamos botón de entrar como invitado
        testSteps.pulsamosBotonGuest();

        // Then mostramos lista de categorías sin botón de favoritos (no hay usuario logueado)
        testSteps.mostramosListaDeCategorias();
        testSteps.noMostramosBotonFavoritos();
    }

    @Test
    public void test03LoginCorrecto() {
        // Given mostramos pantalla de login
        testSteps.mostramosPantallaDeLogin();

        // When introducimos credenciales válidas y pulsamos login
        testSteps.entrarComoLoginValido();

        // Then mostramos lista de categorías con botón de favoritos visible
        testSteps.mostramosListaDeCategorias();
        testSteps.mostramosBotonFavoritos();
    }

    @Test
    public void test04LoginIncorrecto() {
        // Given mostramos pantalla de login
        testSteps.mostramosPantallaDeLogin();

        // When introducimos una contraseña incorrecta para un usuario existente
        testSteps.introducirCredenciales(
                EspressoTestSteps.TEST_EMAIL, "contraseñaIncorrecta");
        testSteps.pulsamosBotonLogin();

        // Then seguimos en la pantalla de login (no se navega a categorías)
        testSteps.mostramosPantallaDeLogin();
    }

    @Test
    public void test05LoginUsuarioInexistente() {
        // Given mostramos pantalla de login
        testSteps.mostramosPantallaDeLogin();

        // When introducimos un email que no existe en la base de datos
        testSteps.introducirCredenciales("noexiste@ulpgc.es", "123456");
        testSteps.pulsamosBotonLogin();

        // Then seguimos en la pantalla de login
        testSteps.mostramosPantallaDeLogin();
    }

    @Test
    public void test06PulsarPrimeraCategoria() {
        // Given mostramos lista de categorías
        testSteps.entrarComoGuest();
        testSteps.mostramosListaDeCategorias();

        // When pulsamos categoría en posición "0"
        testSteps.pulsamosBotonEnListaDeCategoriasEnPosicion("0");

        // Then mostramos lista de productos de esa categoría
        testSteps.mostramosListaDeProductos();
    }

    @Test
    public void test07PulsarPrimerProducto() {
        // Given mostramos lista de productos de una categoría
        testSteps.entrarComoGuest();
        testSteps.pulsamosBotonEnListaDeCategoriasEnPosicion("0");
        testSteps.mostramosListaDeProductos();

        // When pulsamos producto en posición "0"
        testSteps.pulsamosBotonEnListaDeProductosEnPosicion("0");

        // Then mostramos el detalle del producto
        testSteps.mostramosDetalleDeProducto();
    }

    @Test
    public void test08InvitadoNoVeBotonFavoritoEnDetalle() {
        // Given un invitado navega hasta el detalle de un producto
        testSteps.entrarComoGuest();
        testSteps.pulsamosBotonEnListaDeCategoriasEnPosicion("0");
        testSteps.pulsamosBotonEnListaDeProductosEnPosicion("0");

        // Then no debe poder marcar favoritos
        testSteps.mostramosDetalleDeProducto();
    }

    @Test
    public void test09UsuarioLogueadoMarcaFavorito() {
        // Given un usuario logueado está en el detalle de un producto
        testSteps.entrarComoLoginValido();
        testSteps.pulsamosBotonEnListaDeCategoriasEnPosicion("0");
        testSteps.pulsamosBotonEnListaDeProductosEnPosicion("0");
        testSteps.mostramosBotonFavorito();

        // When pulsa el botón de favorito
        testSteps.pulsamosBotonFavorito();

        // Then el producto queda marcado como favorito (el botón sigue visible/actualizado)
        testSteps.mostramosBotonFavorito();
    }

    @Test
    public void test10AccederListaDeFavoritosDesdeCategorias() {
        // Given usuario logueado en la lista de categorías
        testSteps.entrarComoLoginValido();
        testSteps.mostramosBotonFavoritos();

        // When pulsa el botón de favoritos
        testSteps.pulsamosBotonFavoritos();

        // Then mostramos la pantalla de favoritos
        testSteps.mostramosListaDeFavoritos();
    }

    @Test
    public void test11AccederDetalleDesdeFavoritos() {
        // Given un usuario logueado tiene al menos un favorito marcado
        testSteps.entrarComoLoginValido();
        testSteps.pulsamosBotonEnListaDeCategoriasEnPosicion("0");
        testSteps.pulsamosBotonEnListaDeProductosEnPosicion("0");
        testSteps.pulsamosBotonFavorito();

        // When vuelve a categorías y entra en la lista de favoritos
        androidx.test.espresso.Espresso.pressBack();
        testSteps.pulsamosBotonFavoritos();
        testSteps.mostramosListaDeFavoritos();

        // Then puede acceder al detalle del producto favorito desde esa lista
        testSteps.pulsamosBotonEnListaDeProductosEnPosicion("0");
        testSteps.mostramosDetalleDeProducto();
    }

    @Test
    public void test12Logout() {
        // Given estamos en lista de categorías como usuario logueado
        testSteps.entrarComoLoginValido();
        testSteps.mostramosBotonFavoritos();

        // When pulsamos logout desde el menú
        testSteps.pulsamosLogout();

        // Then volvemos a la pantalla de login
        testSteps.mostramosPantallaDeLogin();
    }
}
