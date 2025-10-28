package sv.edu.catedra_recetario

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categoria(
    val nombre: String,
    val imagenRes: Int
) : Parcelable