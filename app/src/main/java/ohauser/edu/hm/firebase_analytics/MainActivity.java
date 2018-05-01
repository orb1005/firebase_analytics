package ohauser.edu.hm.firebase_analytics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView navigation;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_schedule:
                    mTextMessage.setText(R.string.title_schedule);
                    selectedFragment = ScheduleActivity.newInstance();
                    break;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    selectedFragment = SearchCoursesActivity.newInstance();
                    break;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    selectedFragment = ProfileActivity.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.linear_layout, selectedFragment);
            transaction.commit();
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

        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linear_layout, ScheduleActivity.newInstance());
        transaction.commit();
        recordScreenView();
    }

    /**
     * Return the title of the currently displayed screen.
     *
     * @return title of view
     */
    private String getCurrentScreenTitle() {
        return (String) mTextMessage.getText();
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
}
