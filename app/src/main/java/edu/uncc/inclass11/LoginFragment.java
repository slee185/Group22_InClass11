// In Class Assignment 11
// Group22_InClass11
// Stephanie Lee Karp & Ken Stanley

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

import edu.uncc.inclass11.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            if(email.isEmpty()){
                Toast.makeText(requireActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()){
                Toast.makeText(requireActivity(), "Enter valid password!", Toast.LENGTH_SHORT).show();
            } else {
                mListener.authenticate(email, password);
            }
        });

        binding.buttonCreateNewAccount.setOnClickListener(v -> mListener.goCreateNewAccount());

        requireActivity().setTitle(R.string.login_label);
    }

    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (LoginListener) context;
    }

    interface LoginListener {
        void authenticate(String username, String password);
        void goCreateNewAccount();
    }
}