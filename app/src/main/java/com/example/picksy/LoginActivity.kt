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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }


        val login : Button = findViewById(R.id.login)
        val password : TextView = findViewById(R.id.password)
        val email : TextView = findViewById(R.id.username)
        val fauth: FirebaseAuth = FirebaseAuth.getInstance()
        val pass : TextView = findViewById(R.id.forgetPass)
        val bar : ProgressBar = findViewById(R.id.progressBar)
        val account : TextView = findViewById(R.id.anotherAccount)
        account.isClickable = true
        account.isFocusable = true


        login.setOnClickListener{

            val mail: String = email.text.toString().trim()
            val pass: String = password.text.toString().trim()

            if (TextUtils.isEmpty(mail)) {
                email.error = "Required section"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pass)) {
                password.error = "Required section"
                return@setOnClickListener
            }

            bar.setVisibility(View.VISIBLE)

            fauth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this) {
                task -> bar.visibility = ProgressBar.GONE

                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                }
            }
        }

        pass.setOnClickListener{


        }

        account.setOnClickListener{

//            bar.setVisibility(View.VISIBLE)
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

//        fauth.getInstance()



// logout
//        fauth.getInstance.signOut()




    }
}