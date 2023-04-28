package com.exercicios.mynewcolors

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.exercicios.mynewcolors.adapter.ColorAdapter
import com.exercicios.mynewcolors.model.MyColor
import com.exercicios.mynewcolors.model.MyColorList
import com.exercicios.mynewcolors.model.getId
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var colors: MyColorList = MyColorList()

    private lateinit var rvColors: RecyclerView
    private lateinit var fabAddColor: FloatingActionButton
    private lateinit var intentColorRegisterActivity: Intent
    private lateinit var registerResultContract: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity()
    }

    private fun startActivity() {
        initializeViewsAndIntents()
        createColorAdapter()
        createRegisterResultContract()
        createComponentInteractions()
    }

    private fun initializeViewsAndIntents() {
        this.rvColors = findViewById(R.id.rvColors)
        this.fabAddColor = findViewById(R.id.fabAddColor)
        this.intentColorRegisterActivity = Intent(this, ColorRegisterActivity::class.java)
    }

    private fun createColorAdapter() {
        this.rvColors.adapter = ColorAdapter(this.colors)
        (this.rvColors.adapter as ColorAdapter).onItemClickRecyclerView = OnItemClick()
    }

    private fun createRegisterResultContract() {
        registerResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            createColorFromActivityResult(it)
        }
    }

    private fun createColorFromActivityResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == RESULT_OK) {
            val newColor = extractColorFromIntent(activityResult.data)
            addColorOntoList(newColor)
        } else {
            Toast.makeText(this, "Operação de cadastro cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun extractColorFromIntent(intent: Intent?): MyColor {
        val id = intent?.getSerializableExtra("ID") as Int?
        val name = intent?.getSerializableExtra("NAME") as String
        val red = intent.getSerializableExtra("RED") as Int
        val green = intent.getSerializableExtra("GREEN") as Int
        val blue = intent.getSerializableExtra("BLUE") as Int

        if(id == null) {
            return MyColor(getId(), name, red, green, blue)
        }
        return MyColor(id, name, red, green, blue)
    }

    private fun addColorOntoList(newColor: MyColor) {
        (this.rvColors.adapter as ColorAdapter).add(newColor)
    }

    private fun createComponentInteractions() {
        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvColors)
        this.fabAddColor.setOnClickListener {
            registerResultContract.launch(intentColorRegisterActivity)
        }
    }

    inner class OnItemClick : OnItemClickRecyclerView {
        override fun onItemClick(position: Int) {
            val intent = Intent(this@MainActivity, ColorRegisterActivity::class.java).apply {
                putExtra("COLOR", this@MainActivity.colors.getColor(position))
            }
            this@MainActivity.registerResultContract.launch(intent)
        }
    }

    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            (this@MainActivity.rvColors.adapter as ColorAdapter).move(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when(direction) {
                ItemTouchHelper.RIGHT -> confirmDeletion(viewHolder.adapterPosition)
                ItemTouchHelper.LEFT -> shareColorHexCode(colors.getColor(viewHolder.adapterPosition))
            }
            this@MainActivity.rvColors.adapter?.notifyItemChanged(viewHolder.adapterPosition)
        }

        private fun confirmDeletion(position: Int) {
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle("Confirmação")
                setMessage("Tem certeza de que quer remover a cor?")
                setPositiveButton("Sim", OnClickRemoveColor(position))
                setNegativeButton("Não", null)
            }.create().show()
        }

        private fun shareColorHexCode(color: MyColor) {
            val intent = createIntentForSharingColorHexCode(color)

            if (isThereAnAppToResolveIntent()) {
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Sem compartilhamento", Toast.LENGTH_SHORT).show()
            }
        }

        private fun createIntentForSharingColorHexCode(sharedColor: MyColor): Intent {
            return Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, sharedColor.getHexCode())
            }
        }

        private fun isThereAnAppToResolveIntent(): Boolean {
            return intent.resolveActivity(packageManager) != null
        }

        inner class OnClickRemoveColor(private val positionToDelete: Int): DialogInterface.OnClickListener {

            override fun onClick(p0: DialogInterface?, p1: Int) {
                (this@MainActivity.rvColors.adapter as ColorAdapter).del(positionToDelete)
            }
        }
    }
}
