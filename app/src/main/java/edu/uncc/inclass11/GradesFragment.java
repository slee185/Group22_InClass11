package edu.uncc.inclass11;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import edu.uncc.inclass11.databinding.FragmentGradesBinding;

public class GradesFragment extends Fragment {
    FragmentGradesBinding binding;

    private static final String ARG_USER = "user";

    private FirebaseUser user;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    public static GradesFragment newInstance(FirebaseUser user) {
        GradesFragment fragment = new GradesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_bar_add) {
            mListener.goAddCourse();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.grades_label);

       // binding.textViewGPA.setText(getString(R.string.grades_gpa_label, gpa));

       // binding.textView2.setText(getString(R.string.grades_hours_label, totalHours));

        binding.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      //  FirestoreRecyclerAdapter adapter = mListener.createRecyclerAdapter();
       // binding.gradesRecyclerView.setAdapter(adapter);



    }

    GradesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (GradesListener) context;
    }

    private static class GradeHolder extends RecyclerView.ViewHolder {
        private final View view;

        public GradeHolder(@NonNull View itemView, View view) {
            super(itemView);
            this.view = view;
        }

        public View getView() {
            return view;
        }

        void setCourse_number(String course_number) {
            TextView textView = view.findViewById(R.id.textViewCourseNumber);
            textView.setText(course_number);
        }

        void setCourse_name(String course_name) {
            TextView textView = view.findViewById(R.id.textViewCourseName);
            textView.setText(course_name);
        }

        void setCourse_hours(String course_hours) {
            TextView textView = view.findViewById(R.id.textViewCourseHours);
            textView.setText(course_hours);
        }

        void setCourse_letter_grade(String course_letter_grade) {
            TextView textView = view.findViewById(R.id.textViewCourseLetterGrade);
            textView.setText(course_letter_grade);
        }
    }

    interface GradesListener {
        void goAddCourse();
    }
}