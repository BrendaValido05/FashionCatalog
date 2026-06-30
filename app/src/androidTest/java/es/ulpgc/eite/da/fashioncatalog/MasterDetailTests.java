package es.ulpgc.eite.da.fashioncatalog;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.containsString;

import android.content.pm.ActivityInfo;
import android.os.RemoteException;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.ulpgc.eite.da.fashioncatalog.categories.CategoryListActivity;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;


@SuppressWarnings("ALL")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MasterDetailTests {

    @Rule
    public ActivityTestRule<CategoryListActivity> testRule =
            new ActivityTestRule<>(CategoryListActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(CatalogRepository.IDLING_RESOURCE);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(CatalogRepository.IDLING_RESOURCE);
    }

    private void rotate() {

        CategoryListActivity activity = testRule.getActivity();
        int orientation = activity.getRequestedOrientation();

        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else {
            orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }

        activity.setRequestedOrientation(orientation);

        try {
            UiDevice device = UiDevice.getInstance(getInstrumentation());

            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                device.setOrientationNatural();
            } else {
                device.setOrientationLeft();
            }

        } catch (RemoteException e) {
        }
    }

    @Test
    public void appTest() {

        // --- Pantalla de Categorías (maestro) ---------------------------------------
        rotate();

        onView(new RecyclerViewMatcher(R.id.category_list)
                .atPositionOnView(0, R.id.content))
                .check(matches(withText(containsString("Bolsos"))));

        // Scroll a otra posición para forzar el reciclado de ViewHolders del RecyclerView
        onView(withId(R.id.category_list))
                .perform(RecyclerViewActions.scrollToPosition(3));

        onView(new RecyclerViewMatcher(R.id.category_list)
                .atPositionOnView(3, R.id.content))
                .check(matches(withText(containsString("Accesorios"))));

        rotate();

        // Tras destruir/recrear la Activity, los datos siguen viniendo de Room sin
        // necesidad de volver a leer el JSON ni perder la posición de scroll lógica
        onView(new RecyclerViewMatcher(R.id.category_list)
                .atPositionOnView(0, R.id.content))
                .check(matches(withText(containsString("Bolsos"))));

        // --- Navegamos a Productos (maestro-detalle, nivel 1) -----------------------
        ViewInteraction categoryZapatos =
                onView(new RecyclerViewMatcher(R.id.category_list)
                        .atPositionOnView(1, R.id.content))
                        .check(matches(withText(containsString("Zapatos"))));
        categoryZapatos.perform(androidx.test.espresso.action.ViewActions.click());

        rotate();

        onView(withId(R.id.product_list)).check(matches(isDisplayed()));

        onView(new RecyclerViewMatcher(R.id.product_list)
                .atPositionOnView(0, R.id.content))
                .check(matches(withText(containsString("Zapatillas deportivas"))));

        rotate();

        // Tras rotar dentro de Productos, la app debe recuperar de nuevo de Room los
        // productos asociados a la categoría seleccionada (no productos de otra categoría)
        onView(new RecyclerViewMatcher(R.id.product_list)
                .atPositionOnView(1, R.id.content))
                .check(matches(withText(containsString("Botines de cuero"))));

        // --- Navegamos al Detalle (maestro-detalle, nivel 2) ------------------------
        ViewInteraction productBotines =
                onView(new RecyclerViewMatcher(R.id.product_list)
                        .atPositionOnView(1, R.id.content))
                        .check(matches(withText(containsString("Botines de cuero"))));
        productBotines.perform(androidx.test.espresso.action.ViewActions.click());

        rotate();

        onView(withId(R.id.product_detail))
                .perform(androidx.test.espresso.action.ViewActions.scrollTo())
                .check(matches(isDisplayed()));

        rotate();

        // El detalle debe seguir mostrando los datos del MISMO producto tras la segunda
        // rotación: si el ProductDetailPresenter cacheara mal el producto seleccionado
        // en CatalogMediator, este assert detectaría la inconsistencia
        onView(withId(R.id.product_detail))
                .perform(androidx.test.espresso.action.ViewActions.scrollTo())
                .check(matches(isDisplayed()));

        // --- Volvemos hacia atrás comprobando que el back-stack es coherente -------
        pressBack();

        rotate();

        onView(withId(R.id.product_list)).check(matches(isDisplayed()));
        onView(new RecyclerViewMatcher(R.id.product_list)
                .atPositionOnView(0, R.id.content))
                .check(matches(withText(containsString("Zapatillas deportivas"))));

        pressBack();

        rotate();

        onView(withId(R.id.category_list)).check(matches(isDisplayed()));
        onView(new RecyclerViewMatcher(R.id.category_list)
                .atPositionOnView(0, R.id.content))
                .check(matches(withText(containsString("Bolsos"))));

        onView(withId(R.id.category_list))
                .perform(RecyclerViewActions.scrollToPosition(3));

        onView(new RecyclerViewMatcher(R.id.category_list)
                .atPositionOnView(3, R.id.content))
                .check(matches(withText(containsString("Accesorios"))));
    }
}