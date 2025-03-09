package com.example.picksy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signUp: Button = findViewById(R.id.signup)
        val userName: EditText = findViewById(R.id.username)
        val passWord: EditText = findViewById(R.id.password)
        val email: EditText = findViewById(R.id.email)
        val login: TextView = findViewById(R.id.login_text)
        val progressBar: ProgressBar = findViewById(R.id.Bar)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


        signUp.setOnClickListener {
            val mail: String = email.text.toString().trim()
            val pass: String = passWord.text.toString().trim()

            if (TextUtils.isEmpty(mail)) {
                email.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pass)) {
                passWord.error = "Required section"
                return@setOnClickListener
            }

            progressBar.setVisibility(View.VISIBLE)

            firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = ProgressBar.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                         startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "Sign up failed"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                    }
                }

        }

        login.setOnClickListener{

            progressBar.setVisibility(View.VISIBLE)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}

