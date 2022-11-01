package edu.uncc.inclass11;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.uncc.inclass11.databinding.FragmentAddCourseBinding;

public class AddCourseFragment extends Fragment {
    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddCourseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSubmit.setOnClickListener(v -> {

            String courseNumber = binding.editTextCourseNumber.getText().toString();
            String courseName = binding.editTextCourseName.getText().toString();
            double courseHours = Double.parseDouble(binding.editTextCourseHours.getText().toString());
            int selectedId = binding.radioGroupGrades.getCheckedRadioButtonId();

            if(courseName.isEmpty() || courseNumber.isEmpty() || binding.editTextCourseHours.getText().toString().isEmpty()) {
               Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
            } else if(selectedId == -1){
                Toast.makeText(getContext(), "Please select a letter grade !!", Toast.LENGTH_SHORT).show();
            } else {
                String courseLetterGrade;
                if(selectedId == R.id.radioButtonA) {
                    courseLetterGrade = "A";
                } else if(selectedId == R.id.radioButtonB) {
                    courseLetterGrade = "B";
                } else if(selectedId == R.id.radioButtonC) {
                    courseLetterGrade = "C";
                } else if(selectedId == R.id.radioButtonD) {
                    courseLetterGrade = "D";
                } else {
                    courseLetterGrade = "F";
                }

            }
        });

    }
}