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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;

import edu.uncc.inclass11.databinding.FragmentGradesBinding;

public class GradesFragment extends Fragment {
    FragmentGradesBinding binding;

    private static final String ARG_USER = "user";

    private FirebaseUser firebaseUser;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Grade, GradeHolder> adapter;

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
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firebaseUser = getArguments().getParcelable(ARG_USER);
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

        binding.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firebaseFirestore
                .collection("students")
                .document(firebaseUser.getUid())
                .collection("grades")
                .orderBy("created_at", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Grade> options = new FirestoreRecyclerOptions.Builder<Grade>()
                .setQuery(query, Grade.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Grade, GradeHolder>(options) {
            private Double hours = 0.0;
            private Double points = 0.0;

            @Override
            public void onBindViewHolder(@NonNull GradeHolder holder, int position, @NonNull Grade model) {
                holder.setCourse_hours(model.getCourse_hours());
                holder.setCourse_letter_grade(model.getCourse_grade());
                holder.setCourse_name(model.getCourse_name());
                holder.setCourse_number(model.getCourse_number());
                holder.setCourse_id(model.getGrade_id());
            }

            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                super.onChildChanged(type, snapshot, newIndex, oldIndex);

                Grade model = snapshot.toObject(Grade.class);

                assert model != null;
                switch (type) {
                    case ADDED:
                        points += model.calcCourse_points();
                        hours += model.getCourse_hours();
                        break;
                    case CHANGED:
                        hours = 0.0;
                        points = 0.0;
                        break;
                    case REMOVED:
                        points -= model.calcCourse_points();
                        hours -= model.getCourse_hours();
                        break;
                }

                setGpa(hours > 0 ? points / hours : 0);
                setHours(hours);
            }

            @NonNull
            @Override
            public GradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_row_item, parent, false);
                return new GradeHolder(view);
            }
        };
        binding.gradesRecyclerView.setAdapter(adapter);

        requireActivity().setTitle(R.string.grades_label);
    }

    GradesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (GradesListener) context;
    }

    private void setGpa(Double gpa) {
        TextView textView = binding.textViewGPA;
        textView.setText(getString(R.string.grades_gpa_label, gpa));
    }

    private void setHours(Double hours) {
        TextView textView = binding.textViewHours;
        textView.setText(getString(R.string.grades_hours_label, hours));
    }

    private class GradeHolder extends RecyclerView.ViewHolder {
        private final View view;

        public GradeHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        void setCourse_number(String course_number) {
            TextView textView = view.findViewById(R.id.textViewCourseNumber);
            textView.setText(course_number);
        }

        void setCourse_name(String course_name) {
            TextView textView = view.findViewById(R.id.textViewCourseName);
            textView.setText(course_name);
        }

        void setCourse_hours(Double course_hours) {
            TextView textView = view.findViewById(R.id.textViewCourseHours);
            textView.setText(String.format(Locale.US, "%.1f", course_hours));
        }

        void setCourse_letter_grade(String course_letter_grade) {
            TextView textView = view.findViewById(R.id.textViewCourseLetterGrade);
            textView.setText(course_letter_grade);
        }

        public void setCourse_id(String grade_id) {
            ImageView imageViewDelete = view.findViewById(R.id.imageViewDelete);
            imageViewDelete.setOnClickListener(view -> firebaseFirestore
                    .collection("students")
                    .document(firebaseUser.getUid())
                    .collection("grades")
                    .document(grade_id)
                    .delete()
                    .addOnSuccessListener(unused -> Log.d("demo", "Grade successfully deleted"))
                    .addOnFailureListener(e -> Log.w("demo", "Error deleting grade", e)));
        }
    }

    interface GradesListener {
        void goAddCourse();

        void refreshGrades();
    }
}