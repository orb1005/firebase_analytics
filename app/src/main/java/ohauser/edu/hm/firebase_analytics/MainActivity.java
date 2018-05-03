package ohauser.edu.hm.firebase_analytics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    String language;
    String gender;

    private Fragment profileFragment;
    private Fragment searchCoursesFragment;
    private Fragment scheduleFragment;

    //firebase analytics fields to track
    private TextView screenTitle;
    private BottomNavigationView navigation;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    public FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_schedule:
                    screenTitle.setText(R.string.title_schedule);
                    transaction.replace(R.id.linear_layout, scheduleFragment);
                    transaction.commitNow();
                    break;
                case R.id.navigation_search:
                    screenTitle.setText(R.string.title_search);
                    transaction.replace(R.id.linear_layout, searchCoursesFragment);
                    transaction.commitNow();
                    break;
                case R.id.navigation_profile:
                    screenTitle.setText(R.string.title_profile);
                    transaction.replace(R.id.linear_layout, profileFragment);
                    transaction.commitNow();

                    final Spinner languageSpinner = findViewById(R.id.spinner_language);
                    languageSpinnerListener(languageSpinner);

                    final Spinner genderSpinner = findViewById(R.id.spinner_gender);
                    genderSpinnerListener(genderSpinner);
                    break;
            }
            //firebase analytics track screen
            recordScreenView();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // [END shared_app_measurement]

        //initialize fields
        screenTitle = findViewById(R.id.message);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        profileFragment = ProfileActivity.newInstance();
        searchCoursesFragment = SearchCoursesActivity.newInstance();
        scheduleFragment = ScheduleActivity.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linear_layout, profileFragment);
        transaction.commit();

        //firebase analytics track first screen
        recordScreenView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recordUserProperties();

        final Spinner languageSpinner = findViewById(R.id.spinner_language);
        language = languageSpinner.getSelectedItem().toString();
        languageSpinnerListener(languageSpinner);

        final Spinner genderSpinner = findViewById(R.id.spinner_gender);
        gender = genderSpinner.getSelectedItem().toString();
        genderSpinnerListener(genderSpinner);
    }

    /**
     * Return the title of the currently displayed screen.
     *
     * @return title of view
     */
    private String getCurrentScreenTitle() {
        return (String) screenTitle.getText();
    }

    /**
     * This sample has a single Activity, so we need to manually record "screen views" as
     * we change fragments.
     */
    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        String screenName = getCurrentScreenTitle();

        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
        // [END set_current_screen]
    }

    /**
     * Tracks the user properties on app start
     */
    private void recordUserProperties(){

        TextView department = findViewById(R.id.text_department);
        TextView semester = findViewById(R.id.text_semester);
        TextView accountType = findViewById(R.id.text_accountType);
        TextView university = findViewById(R.id.text_university);

        mFirebaseAnalytics.setUserProperty("department", (String) department.getContentDescription());
        mFirebaseAnalytics.setUserProperty("semester", (String) semester.getContentDescription());
        mFirebaseAnalytics.setUserProperty("account_type", (String) accountType.getContentDescription());
        mFirebaseAnalytics.setUserProperty("university", (String) university.getContentDescription());
    }

    /**
     * Track events
     */
    private void recordEvents(String id, String name, String content_type){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content_type);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Set up a selected item listener for the language spinner
     */
    private void languageSpinnerListener(final Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String newLanguageValue = spinner.getSelectedItem().toString();

                if(newLanguageValue != language) {

                    language = newLanguageValue;
                    recordEvents(String.valueOf(spinner.getId()),newLanguageValue,"spinner_language");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    /**
     * Set up a selected item listener for the gender spinner
     */
    private void genderSpinnerListener(final Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String newGenderValue = spinner.getSelectedItem().toString();

                if(newGenderValue != gender) {

                    gender = newGenderValue;

                    recordEvents(String.valueOf(spinner.getId()),newGenderValue,"spinner_gender");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }


}
