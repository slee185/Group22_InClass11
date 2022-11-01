package edu.uncc.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, GradesFragment.GradesListener, AddCourseFragment.AddCourseListener {

    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter<Grade, GradeHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void authenticate(@NonNull String username, @NonNull String password) {
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                assert exception != null;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();

                return;
            }

            this.firebaseUser = task.getResult().getUser();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, GradesFragment.newInstance(this.firebaseUser))
                    .commit();
        });
    }

    @Override
    public void createAccount(@NonNull String name, @NonNull String email, @NonNull String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createTask -> {
            if (!createTask.isSuccessful()) {
                Exception exception = createTask.getException();
                assert exception != null;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();

                return;
            }

            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            FirebaseUser user = createTask.getResult().getUser();
            assert user != null;
            user.updateProfile(request).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Exception exception = task.getException();
                    assert exception != null;
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("An Error Occurred")
                            .setMessage(exception.getLocalizedMessage())
                            .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                            .show();

                    return;
                }

                this.firebaseUser = user;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, GradesFragment.newInstance(this.firebaseUser))
                        .commit();
            });
        });
    }

    @Override
    public void createGrade(String course_number, String course_name, Double course_hours, String course_grade) {
        Grade grade = new Grade(firebaseUser.getUid(), course_number, course_name, course_hours, course_grade);

        firebaseFirestore
                .collection("students")
                .document(grade.getUser_id())
                .collection("grades")
                .document(grade.getGrade_id())
                .set(grade)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        assert exception != null;
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("An Error Occurred")
                                .setMessage(exception.getLocalizedMessage())
                                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                                .show();

                        return;
                    }

                    goGrades();
                });
    }

    @Override
    public void goAddCourse() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createRecyclerAdapter() {
        Query query = firebaseFirestore
                .collection("students")
                .document(firebaseUser.getUid())
                .collection("grades")
                .orderBy("created_at", Query.Direction.DESCENDING);

        query.addSnapshotListener((value, error) -> {
            assert value != null;
            for (DocumentSnapshot document : value.getDocuments()) {
                Log.d("demo", "authenticate: " + document.toObject(Grade.class));
            }
        });

        FirestoreRecyclerOptions<Grade> options = new FirestoreRecyclerOptions.Builder<Grade>()
                .setQuery(query, Grade.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Grade, GradeHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GradeHolder holder, int position, @NonNull Grade model) {
                holder.setCourse_number(model.getCourse_number());
                holder.setCourse_name(model.getCourse_name());
                holder.setCourse_hours(model.getCourse_hours());
                holder.setCourse_letter_grade(model.getCourse_grade());
            }

            @NonNull
            @Override
            public GradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_row_item, parent, false);
                return new GradeHolder(view);
            }
        };

        //return adapter;
    }
    private static class GradeHolder extends RecyclerView.ViewHolder {
        private final View view;

        public GradeHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
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

        void setCourse_hours(double course_hours) {
            TextView textView = view.findViewById(R.id.textViewCourseHours);
            String hours = new Double(course_hours).toString();
            textView.setText(hours);
        }

        void setCourse_letter_grade(String course_letter_grade) {
            TextView textView = view.findViewById(R.id.textViewCourseLetterGrade);
            textView.setText(course_letter_grade);
        }
    }

    @Override
    public void goCreateNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goGrades() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goLogin() {
        getSupportFragmentManager().popBackStack();
    }
}