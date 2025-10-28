package sv.edu.catedra_recetario

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddRecetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_receta)

        val titulo = findViewById<EditText>(R.id.edit_titulo)
        val descripcion = findViewById<EditText>(R.id.edit_descripcion)
        val ingredientes = findViewById<EditText>(R.id.edit_ingredientes)
        val pasos = findViewById<EditText>(R.id.edit_pasos)
        val imageUrl = findViewById<EditText>(R.id.edit_image_url)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            // Aquí puedes crear una instancia de Receta y guardarla
            val nuevaReceta = Receta(
                titulo = titulo.text.toString(),
                descripcion = descripcion.text.toString(),
                ingredientes = ingredientes.text.toString(),
                pasos = pasos.text.toString(),
                imageUrl = imageUrl.text.toString(),
                authorId = "master" // o el usuario actual
            )

            // Por ahora solo mostramos un log o mensaje
            // Más adelante se puede enviar a Firebase o al adapter
            println("Receta agregada: $nuevaReceta")

            finish() // Cierra esta Activity y regresa a la anterior
        }
    }
}
