package sv.edu.catedra_recetario


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class RecetaDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_receta_detail)

        val receta = intent.getParcelableExtra<Receta>("receta") ?: run {
            finish()
            return
        }

        // Asignar los datos a las vistas correctas
        findViewById<TextView>(R.id.textViewNombre).text = receta.titulo
        findViewById<TextView>(R.id.textViewCategoria).text = "Categoría: ${receta.descripcion}" // o lo que sea apropiado
        findViewById<TextView>(R.id.textViewIngredientesLista).text = receta.ingredientes
        findViewById<TextView>(R.id.textViewInstruccionesLista).text = receta.pasos

        // Opcional: puedes ocultar los TextViews de títulos si no los necesitas
        // findViewById<TextView>(R.id.textViewIngredientes).visibility = View.GONE
        // findViewById<TextView>(R.id.textViewInstrucciones).visibility = View.GONE

        // Cargar imagen
        Glide.with(this).load(receta.imageUrl).into(findViewById(R.id.imageViewReceta))
    }
}