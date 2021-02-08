package com.metacoders.e_proshashonadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoders.e_proshashonadmin.Models.EmpModel;
import com.metacoders.e_proshashonadmin.databinding.ActivityLoginBinding;
import com.metacoders.e_proshashonadmin.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    Boolean isUserFound = false;
    private DatabaseReference mref;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mref = FirebaseDatabase.getInstance().getReference("emp_list");
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = binding.loginPassword.getText().toString();
                String number = binding.loginNumber.getText().toString();

                if (number.length() != 11 || pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Check The Form", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        login(number, Utils.convertItTohash(pass));
                        Log.d("TAG", "onClick: " +Utils.convertItTohash(pass)   );

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void login(String number, String pass) {
        // we will download the list and check for the pass
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                loop the data  for the certain number and pass
                 */
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    EmpModel user = postSnapshot.getValue(EmpModel.class);

                    if (user.getEmp_ph().equals(number) && user.getEmp_password().equals(pass)) {
                        isUserFound = true;
                        Toast.makeText(getApplicationContext(), "User Found !!", Toast.LENGTH_SHORT).show();
                        //TODO send the route via role based
                        break;
                    }

                }

                if (!isUserFound) {
                    Toast.makeText(getApplicationContext(), "User Not Found !!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}