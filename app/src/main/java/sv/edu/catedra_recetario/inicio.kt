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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater

class inicio : AppCompatActivity() {

    private lateinit var repository: RecetaRepository
    private lateinit var adapter: RecetaAdapter
    private var isMaster = false
    private var editingReceta: Receta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        repository = RecetaRepository()
        val user = FirebaseAuth.getInstance().currentUser
        isMaster = user?.email?.contains("chefmaster") == true

        val btnAgregar = findViewById<Button>(R.id.btnAgregarReceta)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddRecetaActivity::class.java)
            startActivity(intent)
        }


        setupClickListeners()
        setupBottomNavigation()



    }



    private fun setupClickListeners() {
        // Botón de búsqueda
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
        if (isMaster && fabAdd != null) {
            fabAdd.visibility = View.VISIBLE
            fabAdd.setOnClickListener {
                showAddEditDialog(null)
            }
        }

        // Configurar clicks en categorías
        setupCategoriaClicks()

        // Configurar clicks en tipos de platillo
        setupTipoPlatilloClicks()

        // Configurar clicks en recetas diarias
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
                        true
                    }
                    R.id.navigation_perfil -> {
                        startActivity(Intent(this, cuenta::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun loadRecetas() {
        repository.getRecetas { recetas ->
            if (isMaster) {
                adapter.updateRecetas(recetas)
            } else {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
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
                    }
                }.start()
            }
        }
    }

    private fun filtrarRecetasPorCategoria(categoria: String) {
        repository.getRecetasPorCategoria(categoria) { recetas ->
            adapter.updateRecetas(recetas)
            Toast.makeText(this, "Mostrando recetas con $categoria", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filtrarRecetasPorTipo(tipo: String) {
        repository.getRecetasPorTipo(tipo) { recetas ->
            adapter.updateRecetas(recetas)
            Toast.makeText(this, "Mostrando recetas de $tipo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSearchDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)
        val searchInput = dialogView.findViewById<EditText>(R.id.search_input)
        val searchButton = dialogView.findViewById<Button>(R.id.search_action_button)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Buscar recetas")
            .create()

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
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
            adapter.updateRecetas(recetas)
            Toast.makeText(this, "Resultados para: $query", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOptionsMenu() {
        val options = arrayOf("Perfil", "Cerrar sesión")

        AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, cuenta::class.java))
                    1 -> cerrarSesion()
                }
            }
            .show()
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showRecetaDetailDialog(receta: Receta) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_receta_detail, null)

        // Obtener referencias a las vistas de tu layout
        val imageView = dialogView.findViewById<ImageView>(R.id.imageViewReceta)
        val textViewNombre = dialogView.findViewById<TextView>(R.id.textViewNombre)
        val textViewCategoria = dialogView.findViewById<TextView>(R.id.textViewCategoria)
        val textViewTiempo = dialogView.findViewById<TextView>(R.id.textViewTiempo)
        val textViewIngredientesLista = dialogView.findViewById<TextView>(R.id.textViewIngredientesLista)
        val textViewInstruccionesLista = dialogView.findViewById<TextView>(R.id.textViewInstruccionesLista)

        // Configurar los datos de la receta
        textViewNombre.text = receta.titulo
        textViewCategoria.text = "Receta" // Puedes personalizar esto si tienes categorías
        textViewTiempo.text = "Tiempo estimado: 30 min" // Puedes agregar este campo a tu modelo Receta
        textViewIngredientesLista.text = receta.ingredientes
        textViewInstruccionesLista.text = receta.pasos

        // Aquí puedes cargar la imagen desde la URL si tienes una biblioteca como Glide o Picasso
        // Por ahora usamos la imagen por defecto que ya tienes en el layout
        // Glide.with(this).load(receta.imageUrl).into(imageView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }

    // MÉTODOS EXISTENTES (mantén los que ya tienes sin cambios)

    private fun showAddEditDialog(receta: Receta?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_receta, null)
        val title = dialogView.findViewById<EditText>(R.id.edit_titulo)
        val desc = dialogView.findViewById<EditText>(R.id.edit_descripcion)
        val ing = dialogView.findViewById<EditText>(R.id.edit_ingredientes)
        val pas = dialogView.findViewById<EditText>(R.id.edit_pasos)
        val imageUrlInput = dialogView.findViewById<EditText>(R.id.edit_image_url)

        if (receta != null) {
            title.setText(receta.titulo)
            desc.setText(receta.descripcion)
            ing.setText(receta.ingredientes)
            pas.setText(receta.pasos)
            imageUrlInput.setText(receta.imageUrl)
            editingReceta = receta
        } else {
            editingReceta = null
        }

        val save = dialogView.findViewById<Button>(R.id.save_button)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()



        save.setOnClickListener {
            val t = title.text.toString()
            val d = desc.text.toString()
            val i = ing.text.toString()
            val pa = pas.text.toString()
            val url = imageUrlInput.text.toString()

            if (t.isEmpty() || d.isEmpty() || i.isEmpty() || pa.isEmpty() || url.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveReceta(t, d, i, pa, url)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun mostrarDialogoAgregarReceta() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_receta, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = dialogBuilder.create()
        dialog.show()
    }


    private fun saveReceta(t: String, d: String, i: String, pa: String, url: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val receta = Receta(
            id = editingReceta?.id ?: "",
            titulo = t,
            descripcion = d,
            ingredientes = i,
            pasos = pa,
            imageUrl = url,
            authorId = userId
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
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
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
}