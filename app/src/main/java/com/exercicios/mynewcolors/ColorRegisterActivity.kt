package com.exercicios.mynewcolors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.exercicios.mynewcolors.model.MyColor

class ColorRegisterActivity : AppCompatActivity() {

    lateinit var edRegisterName: EditText
    lateinit var edRegisterRed: EditText
    lateinit var edRegisterGreen: EditText
    lateinit var edRegisterBlue: EditText
    lateinit var btnRegisterCadastrar: Button
    var recievedColor: MyColor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_form)
        startActivity()
    }

    private fun startActivity() {
        initializeViews()
        setBtnRegisterOnClickListener()
    }

    private fun initializeViews() {
        findViewsById()
        fulfillTextsWithReceivedColorInfo()
    }

    private fun findViewsById() {
        edRegisterName = findViewById(R.id.edRegisterName)
        edRegisterRed = findViewById(R.id.edRegisterRed)
        edRegisterGreen = findViewById(R.id.edRegisterGreen)
        edRegisterBlue = findViewById(R.id.edRegisterBlue)
        btnRegisterCadastrar = findViewById(R.id.btnRegisterCadastrar)
    }

    private fun fulfillTextsWithReceivedColorInfo() {
        if(anyColorWasRecieved()) {
            edRegisterName.setText(recievedColor!!.name)
            edRegisterRed.setText(recievedColor!!.red.toString())
            edRegisterGreen.setText(recievedColor!!.green.toString())
            edRegisterBlue.setText(recievedColor!!.blue.toString())
        }
    }

    private fun anyColorWasRecieved(): Boolean {
        recievedColor =  intent.getSerializableExtra("COLOR") as MyColor?
        return recievedColor != null
    }

    private fun setBtnRegisterOnClickListener() {
        btnRegisterCadastrar.setOnClickListener{
            finishRegister()
        }
    }

    private fun finishRegister() {
        val intent = createIntentWithColorInfo()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun createIntentWithColorInfo(): Intent {
        val id = recievedColor?.id
        val name = edRegisterName.text.toString()
        val red = edRegisterRed.text.toString().toInt()
        val green = edRegisterGreen.text.toString().toInt()
        val blue = edRegisterBlue.text.toString().toInt()

        return Intent().apply {
            putExtra("ID", id)
            putExtra("NAME", name)
            putExtra("RED", red)
            putExtra("GREEN", green)
            putExtra("BLUE", blue)
        }
    }
}