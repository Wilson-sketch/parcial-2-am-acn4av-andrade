package com.davinci.buggybank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Declaración de variables para los componentes de la interfaz
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvLoginError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicializar los componentes vinculándolos con sus IDs del XML
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvLoginError = findViewById(R.id.tvLoginError)

        // 2. Configurar el evento de escucha del clic para el botón de ingreso
        btnLogin.setOnClickListener {
            val emailText = etEmail.text.toString().trim()
            val passwordText = etPassword.text.toString().trim()

            // 3. Lógica de Validación (Estilo Sandbox de QA)
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                // Caso de prueba: Campos vacíos
                tvLoginError.text = "Error: Todos los campos son obligatorios"
                tvLoginError.visibility = View.VISIBLE
            } else if (!emailText.contains("@") || !emailText.contains(".")) {
                // Caso de prueba: Formato de correo inválido
                tvLoginError.text = "Error de formato: Ingrese un email válido (ej: usuario@banco.com)"
                tvLoginError.visibility = View.VISIBLE
            } else if (passwordText.length < 6) {
                // Caso de prueba: Contraseña muy corta/vulnerable
                tvLoginError.text = "Error de seguridad: La clave debe tener al menos 6 caracteres"
                tvLoginError.visibility = View.VISIBLE
            } else {
                // Éxito: Ocultamos el error si pasa los filtros de QA
                tvLoginError.visibility = View.GONE

                // 4. Mostrar mensaje de éxito en la interfaz
                tvLoginError.text = "¡Validación de QA aprobada! Ingresando..."
                tvLoginError.setTextColor(resources.getColor(R.color.success_green, theme))
                tvLoginError.visibility = View.VISIBLE

                // 5. NAVEGACIÓN: Viajar desde esta pantalla hacia el Dashboard Financiero
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)

                // 6. Cerramos el Login para que no se pueda volver atrás al estar logueado
                finish()
            }
        }
    }
}