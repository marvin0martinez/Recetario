package sv.edu.catedra_recetario

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Registro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val e = email.text.toString()
            val p = password.text.toString()
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Por favor, llena los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Permite registro con juniorchef o masterchef y exactamente 6 dígitos
            if (
                (!e.contains("juniorchef") && !e.contains("masterchef")) ||
                p.length != 6
            ) {
                Toast.makeText(
                    this,
                    "El correo debe incluir 'juniorchef' o 'masterchef' y la contraseña debe tener 6 dígitos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(e, p)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al registrar: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}