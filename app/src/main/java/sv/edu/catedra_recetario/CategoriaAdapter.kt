package sv.edu.catedra_recetario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CategoriaAdapter(
    private var categoria: List<Categoria> = emptyList(),
    private val onCategoriaClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.img_categoria)
        val nombre: TextView = itemView.findViewById(R.id.txt_nombre_categoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categoria[position]

        holder.imagen.setImageResource(categoria.imagenRes)
        holder.nombre.text = categoria.nombre

        holder.itemView.setOnClickListener {
            onCategoriaClick(categoria)
        }
    }

    override fun getItemCount(): Int = categoria.size

    fun updateCategorias(newCategorias: List<Categoria>) {
        categoria = newCategorias
        notifyDataSetChanged()
    }
}