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

//Tests de instrumentación del flujo de registro: registro correcto, navegación de vuelta
//al login, y rechazo de un email ya registrado.
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterEspressoTest {

    @Rule
    public ActivityTestRule<LoginListActivity> testRule =
            new ActivityTestRule<>(LoginListActivity.class);

    public EspressoTestSteps testSteps = new EspressoTestSteps();

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(CatalogRepository.IDLING_RESOURCE);
    }

    @After
    public void unregisterIdlingResource() {
        testSteps.resetearTest();
        IdlingRegistry.getInstance().unregister(CatalogRepository.IDLING_RESOURCE);
    }

    @Test
    public void test01NavegarAPantallaDeRegistro() {
        // Given mostramos pantalla de login
        testSteps.mostramosPantallaDeLogin();

        // When pulsamos el botón de registro
        testSteps.pulsamosBotonRegistro();

        // Then mostramos la pantalla de registro
        testSteps.mostramosPantallaDeRegistro();
    }

    @Test
    public void test02RegistroCorrectoNavegaALogin() {
        // Given estamos en la pantalla de registro
        testSteps.pulsamosBotonRegistro();
        testSteps.mostramosPantallaDeRegistro();

        // When rellenamos el formulario con un email nuevo y confirmamos
        testSteps.rellenamosFormularioDeRegistro(
                "Usuario Nuevo", "nuevo.usuario." + System.currentTimeMillis() + "@ulpgc.es", "123456");
        testSteps.pulsamosBotonConfirmarRegistro();

        // Then volvemos a la pantalla de login
        testSteps.mostramosPantallaDeLogin();
    }

    @Test
    public void test03RegistroConEmailDuplicadoNoNavega() throws InterruptedException {
        // Given ya existe un usuario de prueba registrado
        testSteps.seedTestUser();
        testSteps.pulsamosBotonRegistro();
        testSteps.mostramosPantallaDeRegistro();

        // When intentamos registrar otro usuario con el mismo email
        testSteps.rellenamosFormularioDeRegistro(
                "Otro Nombre", EspressoTestSteps.TEST_EMAIL, "otraPassword");
        testSteps.pulsamosBotonConfirmarRegistro();

        // Then seguimos en la pantalla de registro (el registro no se completa)
        testSteps.mostramosPantallaDeRegistro();
    }
}
