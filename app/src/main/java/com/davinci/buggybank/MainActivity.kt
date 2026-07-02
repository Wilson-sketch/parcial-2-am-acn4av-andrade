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
// Usamos la importación base que no depende de KTX
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Componentes de la interfaz
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvLoginError: TextView

    // Variable para controlar Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización estándar compatible con todas las versiones de SDK
        auth = FirebaseAuth.getInstance()

        // Inicializar componentes vinculándolos con el XML
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvLoginError = findViewById(R.id.tvLoginError)

        // Configurar el click del botón
        btnLogin.setOnClickListener {
            val emailText = etEmail.text.toString().trim()
            val passwordText = etPassword.text.toString().trim()

            // 1. Validaciones previas de formato (QA Sandbox)
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                tvLoginError.text = "Error: Todos los campos son obligatorios"
                tvLoginError.visibility = View.VISIBLE
            } else if (!emailText.contains("@") || !emailText.contains(".")) {
                tvLoginError.text = "Error de formato: Ingrese un email válido"
                tvLoginError.visibility = View.VISIBLE
            } else if (passwordText.length < 6) {
                tvLoginError.text = "Error de seguridad: Mínimo 6 caracteres"
                tvLoginError.visibility = View.VISIBLE
            } else {
                // Ocultamos errores previos
                tvLoginError.visibility = View.GONE

                // 2. LOGIC REAL CON FIREBASE AUTH (Instancia estándar)
                auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Éxito: Navegamos al Dashboard Financiero
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Falló: El usuario no existe o la contraseña es incorrecta
                            tvLoginError.text = "Error de autenticación: Credenciales inválidas o usuario inexistente"
                            tvLoginError.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }
}