package sv.edu.catedra_recetario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TipoPlatilloAdapter(
    private var tipos: List<TipoPlatillo> = emptyList(),
    private val onTipoClick: (TipoPlatillo) -> Unit
) : RecyclerView.Adapter<TipoPlatilloAdapter.TipoPlatilloViewHolder>() {

    class TipoPlatilloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.img_tipo)
        val nombre: TextView = itemView.findViewById(R.id.txt_nombre_tipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoPlatilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tipo_platillo, parent, false)
        return TipoPlatilloViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipoPlatilloViewHolder, position: Int) {
        val tipo = tipos[position]

        holder.imagen.setImageResource(tipo.imagenRes)
        holder.nombre.text = tipo.nombre

        holder.itemView.setOnClickListener {
            onTipoClick(tipo)
        }
    }

    override fun getItemCount(): Int = tipos.size

    fun updateTipos(newTipos: List<TipoPlatillo>) {
        tipos = newTipos
        notifyDataSetChanged()
    }
}