package sv.edu.catedra_recetario

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater

@Suppress("DEPRECATION")
class inicio : AppCompatActivity() {

    private lateinit var repository: RecetaRepository
    private lateinit var adapter: RecetaAdapter
    private lateinit var recyclerView: RecyclerView
    private var isMaster = false
    private var editingReceta: Receta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        repository = RecetaRepository()
        val user = FirebaseAuth.getInstance().currentUser
        isMaster = user?.email?.contains("chefmaster") == true

        setupClickListeners()
        setupBottomNavigation()

    }

    private fun setupClickListeners() {
        val searchButton = findViewById<ImageButton>(R.id.btn_search)
        searchButton?.setOnClickListener {
            showSearchDialog()
        }

        // Botón de menú
        val menuButton = findViewById<ImageButton>(R.id.btn_menu)
        menuButton?.setOnClickListener {
            showOptionsMenu()
        }

        // FAB para agregar receta (solo para masters)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add_recipe)
        if (isMaster) {
            fabAdd?.visibility = View.VISIBLE
            fabAdd?.setOnClickListener {
                showAddEditDialog(null)
            }
        } else {
            fabAdd?.visibility = View.GONE
        }

        setupCategoriaClicks()

        setupTipoPlatilloClicks()

        setupRecetaDiariaClicks()
    }

    private fun setupCategoriaClicks() {
        val layoutCategorias = findViewById<LinearLayout>(R.id.layout_categorias)
        if (layoutCategorias != null) {
            for (i in 0 until layoutCategorias.childCount) {
                val child = layoutCategorias.getChildAt(i)
                if (child is LinearLayout) {
                    child.setOnClickListener {
                        when (i) {
                            0 -> filtrarRecetasPorCategoria("Avena")
                            1 -> filtrarRecetasPorCategoria("Camarones")
                            2 -> filtrarRecetasPorCategoria("Champiñones")
                            3 -> filtrarRecetasPorCategoria("Carne")
                            else -> loadRecetas()
                        }
                    }
                }
            }
        }
    }

    private fun setupTipoPlatilloClicks() {
        val layoutTipos = findViewById<LinearLayout>(R.id.layout_tipos_platillo)
        if (layoutTipos != null) {
            for (i in 0 until layoutTipos.childCount) {
                val child = layoutTipos.getChildAt(i)
                if (child is LinearLayout) {
                    child.setOnClickListener {
                        when (i) {
                            0 -> filtrarRecetasPorTipo("Bebidas")
                            1 -> filtrarRecetasPorTipo("Cremas")
                            2 -> filtrarRecetasPorTipo("Dips")
                            3 -> filtrarRecetasPorTipo("Entradas")
                            else -> loadRecetas()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecetaDiariaClicks() {
        val layoutRecetasDiarias = findViewById<LinearLayout>(R.id.layout_recetas_diarias)
        if (layoutRecetasDiarias != null) {
            for (i in 0 until layoutRecetasDiarias.childCount) {
                val child = layoutRecetasDiarias.getChildAt(i)
                if (child is LinearLayout) {
                    child.setOnClickListener {
                        when (i) {
                            0 -> filtrarRecetasPorTipo("Brunch")
                            1 -> filtrarRecetasPorTipo("Plato Principal")
                            2 -> filtrarRecetasPorTipo("Desayuno")
                            3 -> filtrarRecetasPorTipo("Almuerzo")
                            4 -> filtrarRecetasPorTipo("Cena")
                            else -> loadRecetas()
                        }
                    }
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.let {
            it.selectedItemId = R.id.navigation_inicio
            it.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_inicio -> true
                    R.id.navigation_favoritos -> {
                        startActivity(Intent(this, FavoritosActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        true
                    }
                    R.id.navigation_perfil -> {
                        startActivity(Intent(this, cuenta::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun loadRecetas() {
        repository.getRecetas { recetas ->
            if (recetas.isEmpty()) {
                Toast.makeText(this, "No hay recetas disponibles", Toast.LENGTH_SHORT).show()
                return@getRecetas
            }

            if (isMaster) {
                adapter.updateRecetas(recetas)
            } else {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                if (userId.isNotEmpty()) {
                    val updatedRecetas = mutableListOf<Receta>()
                    val countDown = java.util.concurrent.CountDownLatch(recetas.size)

                    for (receta in recetas) {
                        repository.isFavorite(receta.id, userId) { isFav ->
                            updatedRecetas.add(receta.copy(isFavorite = isFav))
                            countDown.countDown()
                        }
                    }

                    Thread {
                        countDown.await()
                        runOnUiThread {
                            adapter.updateRecetas(updatedRecetas)
                            Toast.makeText(this, "Se cargaron ${recetas.size} recetas", Toast.LENGTH_SHORT).show()
                        }
                    }.start()
                } else {
                    adapter.updateRecetas(recetas)
                }
            }
        }
    }

    private fun filtrarRecetasPorCategoria(categoria: String) {
        repository.getRecetasPorCategoria(categoria) { recetas ->
            if (recetas.isNotEmpty()) {
                adapter.updateRecetas(recetas)
                Toast.makeText(this, "Mostrando ${recetas.size} recetas con $categoria", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay recetas con $categoria", Toast.LENGTH_SHORT).show()
                loadRecetas()
            }
        }
    }

    private fun filtrarRecetasPorTipo(tipo: String) {
        repository.getRecetasPorTipo(tipo) { recetas ->
            if (recetas.isNotEmpty()) {
                adapter.updateRecetas(recetas)
                Toast.makeText(this, "Mostrando ${recetas.size} recetas de $tipo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay recetas de $tipo", Toast.LENGTH_SHORT).show()
                loadRecetas()
            }
        }
    }

    private fun showSearchDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)
        val searchInput = dialogView.findViewById<EditText>(R.id.search_input)
        val searchButton = dialogView.findViewById<Button>(R.id.search_action_button)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Buscar recetas")
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                buscarRecetas(query)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun buscarRecetas(query: String) {
        repository.buscarRecetas(query) { recetas ->
            if (recetas.isNotEmpty()) {
                adapter.updateRecetas(recetas)
                Toast.makeText(this, "${recetas.size} resultados para: $query", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay resultados para: $query", Toast.LENGTH_SHORT).show()
                loadRecetas()
            }
        }
    }

    private fun showOptionsMenu() {
        val options = if (isMaster) {
            arrayOf("Perfil", "Administrar Recetas", "Cerrar sesión")
        } else {
            arrayOf("Perfil", "Cerrar sesión")
        }

        AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, cuenta::class.java))
                    1 -> {
                        if (isMaster && options.size > 2) {
                            // Opción adicional para masters
                            Toast.makeText(this, "Funcionalidad de administración", Toast.LENGTH_SHORT).show()
                        } else {
                            cerrarSesion()
                        }
                    }
                    2 -> cerrarSesion()
                }
            }
            .show()
    }

    private fun cerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteRecetaConfirmation(receta: Receta) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar receta")
            .setMessage("¿Estás seguro de que quieres eliminar '${receta.titulo}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteReceta(receta)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showAddEditDialog(receta: Receta?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_receta, null)
        val title = dialogView.findViewById<EditText>(R.id.edit_titulo)
        val desc = dialogView.findViewById<EditText>(R.id.edit_descripcion)
        val ing = dialogView.findViewById<EditText>(R.id.edit_ingredientes)
        val pas = dialogView.findViewById<EditText>(R.id.edit_pasos)
        val imageUrlInput = dialogView.findViewById<EditText>(R.id.edit_image_url)
        val categoriaInput = dialogView.findViewById<EditText>(R.id.edit_categoria)
        val tiempoInput = dialogView.findViewById<EditText>(R.id.edit_tiempo)

        // Buscar los campos si existen en tu layout
        if (receta != null) {
            title.setText(receta.titulo)
            desc.setText(receta.descripcion)
            ing.setText(receta.ingredientes)
            pas.setText(receta.pasos)
            imageUrlInput.setText(receta.imageUrl)
            // Setear categoría y tiempo si existen en el modelo
            categoriaInput.setText(receta.categoria ?: "")
            tiempoInput.setText(receta.tiempoPreparacion ?: "")
            editingReceta = receta
        } else {
            editingReceta = null
        }

        val save = dialogView.findViewById<Button>(R.id.save_button)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(if (receta != null) "Editar Receta" else "Agregar Receta")
            .create()

        save.setOnClickListener {
            val t = title.text.toString().trim()
            val d = desc.text.toString().trim()
            val i = ing.text.toString().trim()
            val pa = pas.text.toString().trim()
            val url = imageUrlInput.text.toString().trim()
            val cat = categoriaInput.text.toString().trim()
            val tiempo = tiempoInput.text.toString().trim()

            if (t.isEmpty() || d.isEmpty() || i.isEmpty() || pa.isEmpty()) {
                Toast.makeText(this, "Los campos título, descripción, ingredientes y pasos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveReceta(t, d, i, pa, url, cat, tiempo)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveReceta(t: String, d: String, i: String, pa: String, url: String, categoria: String, tiempo: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val receta = Receta(
            id = editingReceta?.id ?: "",
            titulo = t,
            descripcion = d,
            ingredientes = i,
            pasos = pa,
            imageUrl = url,
            authorId = userId,
            categoria = categoria.ifEmpty { "General" },
            tiempoPreparacion = tiempo.ifEmpty { "No especificado" }
        )

        if (editingReceta != null) {
            repository.updateReceta(receta) { success ->
                if (success) {
                    loadRecetas()
                    Toast.makeText(this, "Receta actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            repository.addReceta(receta) { success ->
                if (success) {
                    loadRecetas()
                    Toast.makeText(this, "Receta agregada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteReceta(receta: Receta) {
        repository.deleteReceta(receta.id) { success ->
            if (success) {
                loadRecetas()
                Toast.makeText(this, "Receta eliminada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleFavorite(receta: Receta) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Debes iniciar sesión para usar favoritos", Toast.LENGTH_SHORT).show()
            return
        }

        repository.isFavorite(receta.id, userId) { isFav ->
            if (isFav) {
                repository.removeFavorite(receta.id, userId) { success ->
                    if (success) {
                        Toast.makeText(this, "Removido de favoritos", Toast.LENGTH_SHORT).show()
                        loadRecetas()
                    }
                }
            } else {
                repository.addFavorite(receta.id, userId) { success ->
                    if (success) {
                        Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                        loadRecetas()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar datos cuando la actividad se reanude
        loadRecetas()
    }
}