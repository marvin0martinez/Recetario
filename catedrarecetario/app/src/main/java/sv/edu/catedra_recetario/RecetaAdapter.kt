package sv.edu.catedra_recetario


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecetaAdapter(
    private val context: Context,
    private val isMaster: Boolean,
    private val listener: (action: String, receta: Receta) -> Unit
) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    private var recetas: List<Receta> = listOf()

    fun updateRecetas(newRecetas: List<Receta>) {
        recetas = newRecetas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_receta, parent, false)
        return RecetaViewHolder(view)
    }

    override fun getItemCount(): Int = recetas.size

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val receta = recetas[position]
        holder.bind(receta)
    }

    inner class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloText: TextView = itemView.findViewById(R.id.receta_titulo)
        private val descText: TextView = itemView.findViewById(R.id.receta_descripcion)
        private val imageView: ImageView = itemView.findViewById(R.id.receta_image)
        private val editButton: ImageButton = itemView.findViewById(R.id.btn_edit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete)
        private val favoriteButton: ImageButton = itemView.findViewById(R.id.btn_favorite)

        fun bind(receta: Receta) {
            tituloText.text = receta.titulo
            descText.text = receta.descripcion

            // Cargar la imagen desde la URL
            Glide.with(context)
                .load(receta.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery) // imagen temporal mientras carga
                .error(android.R.drawable.ic_menu_report_image) // si falla la URL
                .into(imageView)


            // Mostrar botones solo si es chef master
            editButton.visibility = if (isMaster) View.VISIBLE else View.GONE
            deleteButton.visibility = if (isMaster) View.VISIBLE else View.GONE

            // Cambiar icono según favorito
            favoriteButton.setImageResource(
                if (receta.isFavorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )


            editButton.setOnClickListener { listener("edit", receta) }
            deleteButton.setOnClickListener { listener("delete", receta) }
            favoriteButton.setOnClickListener { listener("favorite", receta) }
        }
    }
}
