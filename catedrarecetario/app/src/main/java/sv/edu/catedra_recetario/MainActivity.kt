package sv.edu.catedra_recetario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerText = findViewById<TextView>(R.id.register_text)

        loginButton.setOnClickListener {
            val e = email.text.toString()
            val p = password.text.toString()
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Por favor, llena los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(e, p)
                .addOnSuccessListener {
                    startActivity(Intent(this, inicio::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error de inicio de sesión: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        registerText.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }
    }
}