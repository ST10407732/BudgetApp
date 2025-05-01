package vcmsa.projects.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etLoginUsername)
        etPassword = findViewById(R.id.etLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                lifecycleScope.launch {
                    val db = BudgetDatabase.getDatabase(applicationContext)
                    val user = db.userDao().getUser(username, password)

                    if (user != null) {
                        // Fetch the userId from the User object
                        val userId = user.id

                        // Pass the userId to the next activity (DashboardActivity)
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", userId)  // Pass the userId
                        startActivity(intent)
                        finish()  // Close LoginActivity
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid username or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }


        btnGoToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
