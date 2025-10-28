package sv.edu.catedra_recetario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecetaDiariaAdapter(
    private var recetas: List<Receta> = emptyList(),
    private val onRecetaClick: (Receta) -> Unit
) : RecyclerView.Adapter<RecetaDiariaAdapter.RecetaDiariaViewHolder>() {

    class RecetaDiariaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.img_receta_diaria)
        val titulo: TextView = itemView.findViewById(R.id.txt_titulo_receta_diaria)
        val descripcion: TextView = itemView.findViewById(R.id.txt_descripcion_receta_diaria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaDiariaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receta_diaria, parent, false)
        return RecetaDiariaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecetaDiariaViewHolder, position: Int) {
        val receta = recetas[position]

        // Aquí deberías cargar la imagen desde URL usando Glide o Picasso
        // Glide.with(holder.itemView.context).load(receta.imageUrl).into(holder.imagen)
        holder.titulo.text = receta.titulo
        holder.descripcion.text = receta.descripcion

        holder.itemView.setOnClickListener {
            onRecetaClick(receta)
        }
    }

    override fun getItemCount(): Int = recetas.size

    fun updateRecetas(newRecetas: List<Receta>) {
        recetas = newRecetas
        notifyDataSetChanged()
    }
}