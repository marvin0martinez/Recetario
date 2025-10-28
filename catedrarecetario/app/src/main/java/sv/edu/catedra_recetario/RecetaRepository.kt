package sv.edu.catedra_recetario

import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.CountDownLatch


class RecetaRepository {
    private val db = FirebaseFirestore.getInstance()

    // Métodos existentes (manteniendo tu estructura)
    fun getRecetas(callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun addReceta(receta: Receta, callback: (Boolean) -> Unit) {
        val recetaData = hashMapOf(
            "titulo" to receta.titulo,
            "descripcion" to receta.descripcion,
            "ingredientes" to receta.ingredientes,
            "pasos" to receta.pasos,
            "imageUrl" to receta.imageUrl,
            "authorId" to receta.authorId,
            "categoria" to receta.categoria,
            "tipo" to receta.tipo,
            "destacada" to receta.isFavorite
        )

        db.collection("recetas")
            .add(recetaData)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updateReceta(receta: Receta, callback: (Boolean) -> Unit) {
        val recetaData = hashMapOf(
            "titulo" to receta.titulo,
            "descripcion" to receta.descripcion,
            "ingredientes" to receta.ingredientes,
            "pasos" to receta.pasos,
            "imageUrl" to receta.imageUrl,
            "authorId" to receta.authorId,
            "categoria" to receta.categoria,
            "tipo" to receta.tipo,
            "destacada" to receta.isFavorite
        )

        db.collection("recetas").document(receta.id)
            .set(recetaData)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun deleteReceta(id: String, callback: (Boolean) -> Unit) {
        db.collection("recetas").document(id)
            .delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun addFavorite(recetaId: String, userId: String, callback: (Boolean) -> Unit) {
        val favorite = hashMapOf(
            "userId" to userId,
            "recetaId" to recetaId
        )
        db.collection("favoritos")
            .add(favorite)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun removeFavorite(recetaId: String, userId: String, callback: (Boolean) -> Unit) {
        db.collection("favoritos")
            .whereEqualTo("userId", userId)
            .whereEqualTo("recetaId", recetaId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    db.collection("favoritos").document(doc.id).delete()
                }
                callback(true)
            }
            .addOnFailureListener { callback(false) }
    }

    fun isFavorite(recetaId: String, userId: String, callback: (Boolean) -> Unit) {
        db.collection("favoritos")
            .whereEqualTo("userId", userId)
            .whereEqualTo("recetaId", recetaId)
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty)
            }
            .addOnFailureListener { callback(false) }
    }

    fun getFavoritos(userId: String, callback: (List<Receta>) -> Unit) {
        db.collection("favoritos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { favDocs ->
                val recetaIds = favDocs.map { it.getString("recetaId") ?: "" }
                if (recetaIds.isEmpty()) {
                    callback(emptyList())
                    return@addOnSuccessListener
                }

                // Para Firestore, necesitamos hacer consultas individuales si hay muchos IDs
                val recetasList = mutableListOf<Receta>()
                val countDownLatch = CountDownLatch(recetaIds.size)

                for (recetaId in recetaIds) {
                    db.collection("recetas").document(recetaId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val receta = Receta(
                                    document.id,
                                    document.getString("titulo") ?: "",
                                    document.getString("descripcion") ?: "",
                                    document.getString("ingredientes") ?: "",
                                    document.getString("pasos") ?: "",
                                    document.getString("imageUrl") ?: "",
                                    document.getString("authorId") ?: "",
                                    categoria = document.getString("categoria") ?: "",
                                    tipo = document.getString("tipo") ?: "",
                                    isFavorite = document.getBoolean("destacada") ?: false
                                )
                                recetasList.add(receta)
                            }
                            countDownLatch.countDown()
                        }
                        .addOnFailureListener {
                            countDownLatch.countDown()
                        }
                }

                Thread {
                    countDownLatch.await()
                    callback(recetasList)
                }.start()
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun getCreatedCount(userId: String, callback: (Int) -> Unit) {
        db.collection("recetas")
            .whereEqualTo("authorId", userId)
            .get()
            .addOnSuccessListener { callback(it.size()) }
            .addOnFailureListener { callback(0) }
    }

    fun getFavoritesCount(userId: String, callback: (Int) -> Unit) {
        db.collection("favoritos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { callback(it.size()) }
            .addOnFailureListener { callback(0) }
    }

    // NUEVOS MÉTODOS PARA LAS FUNCIONALIDADES ADICIONALES

    fun getRecetasDestacadas(callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .whereEqualTo("destacada", true)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun getRecetasPorCategoria(categoria: String, callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .whereEqualTo("categoria", categoria)
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun getRecetasPorTipo(tipo: String, callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .whereEqualTo("tipo", tipo)
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    fun buscarRecetas(query: String, callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }.filter { receta ->
                    receta.titulo.contains(query, ignoreCase = true) ||
                            receta.descripcion.contains(query, ignoreCase = true) ||
                            receta.ingredientes.contains(query, ignoreCase = true) ||
                            receta.categoria.contains(query, ignoreCase = true) ||
                            receta.tipo.contains(query, ignoreCase = true)
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }

    // Método para obtener recetas por autor
    fun getRecetasPorAutor(userId: String, callback: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .whereEqualTo("authorId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val recetas = documents.map { doc ->
                    Receta(
                        doc.id,
                        doc.getString("titulo") ?: "",
                        doc.getString("descripcion") ?: "",
                        doc.getString("ingredientes") ?: "",
                        doc.getString("pasos") ?: "",
                        doc.getString("imageUrl") ?: "",
                        doc.getString("authorId") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        isFavorite = doc.getBoolean("destacada") ?: false
                    )
                }
                callback(recetas)
            }
            .addOnFailureListener { callback(emptyList()) }
    }
}