package io.tutorial.turntotech.infoOrganizerSample;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import io.tutorial.turntotech.infoOrganizerSample.ActivityAndFragments.StartActivity;
import io.tutorial.turntotech.infoOrganizerSample.DataAccessObject.CompanyProductDAO;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<StartActivity> mActivityRule = new ActivityTestRule<>(
            StartActivity.class);
    Activity activity;
    public int count;




    @Test
    public void a_test_useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("io.tutorial.turntotech.listwithrrecyclerview", appContext.getPackageName());
    }


    @Test
    public void b_test_goToProductViewForaCompany() throws Exception{
        Thread.sleep(2000);
        // tap recyclerView cell
        // Click item at position 1 - APPLE and check Apple have Iphone or not
        onView(withId(R.id.vertical_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click())).check(matches(hasDescendant(withText("IPhone"))));

        Thread.sleep(2000);

        // Go to webview
        onView(withId(R.id.vertical_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(2000);

        // going back to productList
        onView(withId(R.id.imageButton)).perform(click());

        Thread.sleep(2000);


        // check that Product list has Iphone or not??
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("IPhone"))));

        Thread.sleep(2000);

        // Going back to CompanyList
        onView(withId(R.id.imageButton)).perform(click());

        Thread.sleep(5000);

        // Checking that is APPLE Company is present or not?
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("Apple"))));

        Thread.sleep(2000);


    }

    @Test
    public void c_test_createNewCompany() throws Exception{
        // Create a company and Chceck that company is present in the recyclerView or not

        // Click to Add button
        onView(withId(R.id.imageButton2)).perform(click());



        // filling Form for adding a new Company
        Espresso.onView(ViewMatchers.withId((R.id.editTextCompanyName)))
                .perform(ViewActions.typeText("RajatTest"),closeSoftKeyboard());


        Espresso.onView(ViewMatchers.withId((R.id.editTextCompanyTicker))).perform(typeText("MSFT"),
                closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId((R.id.editTextCompanyLogo))).perform(typeText("http://www.3dollarlogos.com/images/entries/220x140/logo9.png"),
                closeSoftKeyboard());


        Thread.sleep(2000);

        // Click to Submit Button
        Espresso.onView(ViewMatchers.withId((R.id.button2))).perform(click());

        // Now Check new Company is present or not
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatTest"))));
        Thread.sleep(2000);
    }


    @Test
    public void d_test_createNewProductForNewCompany() throws Exception{


        // Checking that is RajatTest Company is present or not?
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatTest"))));

        Thread.sleep(2000);

        // If it is present then

        // Click a item which has Content "RajatTest"
        onView(withText(startsWith("RajatTest"))).perform(click());
        Thread.sleep(2000);


        // Now we are in ProductView
        // Click to Add button
        onView(withId(R.id.imageButton2)).perform(click());



        // filling Form for adding a new Product
        Espresso.onView(ViewMatchers.withId((R.id.editTextProductName)))
                .perform(ViewActions.typeText("RajatProduct1"),closeSoftKeyboard());


        Espresso.onView(ViewMatchers.withId((R.id.editTextProductURL))).perform(typeText("http://turntotech.io/"),
                closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId((R.id.editTextProductLogo))).perform(typeText("http://www.canadiansolar.com/fileadmin/templates/webroot/img/xNewProduct.png.pagespeed.ic.nnjZnZDs7B.png"),
                closeSoftKeyboard());


        Thread.sleep(2000);

        // Click to Submit Button
        Espresso.onView(ViewMatchers.withId((R.id.productbutton2))).perform(click());

        // Now Check new Company is present or not
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatProduct1"))));
        Thread.sleep(2000);

    }

    @Test
    public void e_test_deleteProduct() throws Exception{
        // Checking that is RajatTest Company is present or not?
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatTest"))));

        Thread.sleep(2000);

        // If it is present go to next View
        onView(withText(startsWith("RajatTest"))).perform(click());

        // then check Product

        // Checking that is RajatTest Company is present or not?
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatProduct1"))));

        Thread.sleep(2000);

        // Now Click delete
        onView(withId(R.id.deleteButtonToolBar)).perform(click());
        Thread.sleep(1000);

        // delete RajatTest1 Product
        onView(withId(R.id.button)).perform(click());
        Thread.sleep(1000);

        // AlertView popUp now you have to click OK
        onView(withText("OK")).perform(click());

        Thread.sleep(2000);

    }



    @Test
    public void f_test_deleteCompany() throws Exception{
        activity = mActivityRule.getActivity();

        // get number of companies

        count = CompanyProductDAO.returnCompanyCount();

        // Checking that is RajatTest Company is present or not?
        onView(withId(R.id.vertical_recycler_view))
                .check(matches(hasDescendant(withText("RajatTest"))));

        Thread.sleep(2000);


        // Now Click delete
        onView(withId(R.id.deleteButtonToolBar)).perform(click());
        Thread.sleep(1000);

        // delete Particular company at last position


        onView(withId(R.id.vertical_recycler_view))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(
                                count-1,
                                new ViewAction() {
                                    @Override
                                    public Matcher<View> getConstraints() {
                                        return null;
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "Click on specific button";
                                    }

                                    /**
                                     * Performs this action on the given view.
                                     *
                                     * @param uiController the controller to use to interact with the UI.
                                     * @param view         the view to act upon. never null.
                                     */
                                    @Override
                                    public void perform(UiController uiController, View view) {
                                        View button = view.findViewById(R.id.imageButton3);
                                        // Maybe check for null
                                        button.performClick();
                                    }


                                })
                );



        Thread.sleep(1000);




        // AlertView popUp now you have to click OK
        onView(withText("OK")).perform(click());

        Thread.sleep(2000);

    }
}
