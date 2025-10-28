package sv.edu.catedra_recetario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java


class FavoritosActivity : AppCompatActivity() {
    private lateinit var repository: RecetaRepository
    private lateinit var adapter: RecetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)
        repository = RecetaRepository()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_favoritos)
        adapter = RecetaAdapter(this, false) { action, receta ->
            if (action == "favorite") {
                toggleFavorite(receta)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        loadFavoritos()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_favoritos
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_inicio -> {
                    startActivity(Intent(this, inicio::class.java))
                    true
                }
                R.id.navigation_favoritos -> true
                R.id.navigation_perfil -> {
                    startActivity(Intent(this, cuenta::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFavoritos() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        repository.getFavoritos(userId) { recetas ->
            val updated = recetas.map { it.copy(isFavorite = true) }
            adapter.updateRecetas(updated)
        }
    }

    private fun toggleFavorite(receta: Receta) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        repository.removeFavorite(receta.id, userId) { success ->
            if (success) {
                Toast.makeText(this, "Removido de favoritos", Toast.LENGTH_SHORT).show()
                loadFavoritos()
            }
        }
    }
}