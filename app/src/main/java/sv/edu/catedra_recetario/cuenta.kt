package sv.edu.catedra_recetario

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java


class cuenta : AppCompatActivity() {
    private lateinit var repository: RecetaRepository
    private var isMaster = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)
        repository = RecetaRepository()
        val user = FirebaseAuth.getInstance().currentUser
        isMaster = user?.email?.contains("chefmaster") == true
        val emailText = findViewById<TextView>(R.id.email_text)
        emailText.text = user?.email
        loadStats()
        if (isMaster) {
            val permissionsList = findViewById<TextView>(R.id.permissions_list)
            permissionsList.text = """
                • Crear nuevas recetas
                • Editar recetas existentes
                • Eliminar recetas
                • Gestión completa del recetario
            """.trimIndent()
            val addUserButton = findViewById<Button>(R.id.add_user_button)
            addUserButton.visibility = View.VISIBLE
            addUserButton.setOnClickListener {
                startActivity(Intent(this, Registro::class.java))
            }
        } else {
            findViewById<TextView>(R.id.permissions_title).visibility = View.GONE
            findViewById<TextView>(R.id.permissions_list).visibility = View.GONE
            val addUserButton = findViewById<Button>(R.id.add_user_button)
            addUserButton.visibility = View.GONE
        }
        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.navigation_perfil
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_inicio -> {
                    startActivity(Intent(this, inicio::class.java))
                    true
                }
                R.id.navigation_favoritos -> {
                    startActivity(Intent(this, FavoritosActivity::class.java))
                    true
                }
                R.id.navigation_perfil -> true
                else -> false
            }
        }
    }

    private fun loadStats() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            repository.getCreatedCount(userId) { created ->
                findViewById<TextView>(R.id.created_count).text = created.toString()
            }
            repository.getFavoritesCount(userId) { favs ->
                findViewById<TextView>(R.id.favorites_count).text = favs.toString()
            }
        } else {
            // Manejar el caso cuando no hay usuario logueado, por ejemplo mostrar valores por defecto o un mensaje
            findViewById<TextView>(R.id.created_count).text = "0"
            findViewById<TextView>(R.id.favorites_count).text = "0"
        }
    }

}