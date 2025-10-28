package sv.edu.catedra_recetario

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Receta(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val ingredientes: String = "",
    val pasos: String = "",
    val imageUrl: String = "",
    val authorId: String = "",
    var isFavorite: Boolean = false,
    val categoria: String = "", // Nuevo campo opcional
    val tipo: String = "",
    val tiempoPreparacion: String = ""
): Parcelable