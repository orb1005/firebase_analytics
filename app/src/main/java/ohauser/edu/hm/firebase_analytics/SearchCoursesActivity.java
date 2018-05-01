package ohauser.edu.hm.firebase_analytics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ohauser.edu.hm.firebase_analytics.R;

public class SearchCoursesActivity extends Fragment {
    public static SearchCoursesActivity newInstance() {
        SearchCoursesActivity fragment = new SearchCoursesActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchcourses_view, container, false);
    }
}