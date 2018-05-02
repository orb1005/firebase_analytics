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

    //firebase analytics fields to track
    private TextView screenTitle;
    private BottomNavigationView navigation;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    public FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]

    Fragment profileFragment;
    Fragment searchCoursesFragment;
    Fragment scheduleFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_schedule:
                    screenTitle.setText(R.string.title_schedule);
                    selectedFragment = scheduleFragment;
                    break;
                case R.id.navigation_search:
                    screenTitle.setText(R.string.title_search);
                    selectedFragment = searchCoursesFragment;
                    break;
                case R.id.navigation_profile:
                    screenTitle.setText(R.string.title_profile);
                    selectedFragment = profileFragment;
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.linear_layout, selectedFragment);
            transaction.commit();

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

    private void recordUserProperties(){

        TextView department = findViewById(R.id.text_department);
        TextView semester = findViewById(R.id.text_semester);
        TextView accountType = findViewById(R.id.text_accountType);
        TextView university = findViewById(R.id.text_university);

        mFirebaseAnalytics.setUserProperty("department", (String) department.getContentDescription());
        mFirebaseAnalytics.setUserProperty("semester", (String) semester.getContentDescription());
        mFirebaseAnalytics.setUserProperty("account_type", (String) accountType.getContentDescription());
        mFirebaseAnalytics.setUserProperty("university", (String) university.getContentDescription());

        final Spinner genderSpinner = findViewById(R.id.spinner_gender);
        spinnerListener(genderSpinner, "gender");

        final Spinner languageSpinner = findViewById(R.id.spinner_language);
        spinnerListener(languageSpinner, "language");
    }

    private void spinnerListener(final Spinner spinner, final String name){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value = spinner.getSelectedItem().toString();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(spinner.getId()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, value);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "spinner_" + name);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }


}
