package edu.uncc.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, GradesFragment.GradesListener, AddCourseFragment.AddCourseListener {
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;

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