package edu.uncc.inclass11;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.inclass11.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentSignUpBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCancel.setOnClickListener(v -> mListener.goLogin());

        binding.buttonSignup.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString();                String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(requireActivity(), "Enter valid name!", Toast.LENGTH_SHORT).show();
            } else if(email.isEmpty()){
                Toast.makeText(requireActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()){
                Toast.makeText(requireActivity(), "Enter valid password!", Toast.LENGTH_SHORT).show();
            } else {
                mListener.createAccount(name, email, password);
            }
        });

        requireActivity().setTitle(R.string.create_account_label);

    }
    SignUpListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SignUpListener) context;
    }

    interface SignUpListener {
        void createAccount(String name, String email, String password);
        void goLogin();
    }
}