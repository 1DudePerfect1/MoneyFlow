package com.example.moneyflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moneyflow.databinding.ActivitySignInBinding
import com.example.moneyflow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener{
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPass.text.toString()
            val conPassword = binding.signupPassconfirm.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && conPassword.isNotEmpty()){
                if (password == conPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, SignIn::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Toast.makeText(this,"Password are not matching",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Froms are empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginredirect.setOnClickListener{
            val logintent = Intent(this, SignIn::class.java)
            startActivity(logintent)
        }

    }
}