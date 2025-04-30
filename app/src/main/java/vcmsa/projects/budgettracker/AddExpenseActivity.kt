package vcmsa.projects.budgettracker

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Expense
import vcmsa.projects.budgettracker.adapter.ExpenseAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etDescription: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var tvDate: TextView
    private lateinit var btnSaveExpense: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    private lateinit var spinnerPayment: Spinner
    private lateinit var etTags: EditText
    private lateinit var etNotes: EditText
    private lateinit var btnAttachReceipt: Button
    private lateinit var btnCapturePhoto: Button

    private val categoryMap = mutableMapOf<String, Int>()

    private var selectedDateMillis: Long = System.currentTimeMillis()
    private var currentPhotoPath: String? = null
    private var receiptUri: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            1001
        )

        // Bind views
        etAmount = findViewById(R.id.etAmount)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etDescription = findViewById(R.id.etDescription)
        btnSelectDate = findViewById(R.id.btnSelectDate)
        tvDate = findViewById(R.id.tvDate)
        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        recyclerView = findViewById(R.id.rvExpenses)

        spinnerPayment = findViewById(R.id.spinnerPayment)
        etTags = findViewById(R.id.etTags)
        etNotes = findViewById(R.id.etNotes)
        btnAttachReceipt = findViewById(R.id.btnAttachReceipt)
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto)

        // RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        // New features
        setupPaymentSpinner()
        btnAttachReceipt.setOnClickListener { pickReceiptFromGallery() }
        btnCapturePhoto.setOnClickListener { dispatchTakePictureIntent() }
        fetchLastLocation()

        // Existing flows
        loadCategories()
        btnSelectDate.setOnClickListener { showDatePicker() }
        btnSaveExpense.setOnClickListener { saveExpense() }
    }

    private fun setupPaymentSpinner() {
        val methods = listOf("Cash", "Card", "Mobile Pay")
        spinnerPayment.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            methods
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun pickReceiptFromGallery() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK).apply { type = "image/*" },
            2001
        )
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takeIntent ->
            takeIntent.resolveActivity(packageManager)?.also {
                createImageFile()?.also { file ->
                    val uri = FileProvider.getUriForFile(
                        this,
                        "$packageName.fileprovider",
                        file
                    )
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takeIntent, 2002)
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            2001 -> data?.data?.let { uri ->
                receiptUri = uri.toString()
                Toast.makeText(this, "Receipt attached", Toast.LENGTH_SHORT).show()
            }

            2002 -> currentPhotoPath?.let { path ->
                receiptUri = path
                Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLastLocation() {
        LocationServices.getFusedLocationProviderClient(this)
            .lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                    selectedDateMillis = timeInMillis
                }
                tvDate.text = getString(R.string.selected_date, dayOfMonth, month + 1, year)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = BudgetDatabase.getDatabase(applicationContext)
            val categories = db.categoryDao().getAllCategories()
            val names = categories.map {
                categoryMap[it.name] = it.id
                it.name
            }
            withContext(Dispatchers.Main) {
                spinnerCategory.adapter = ArrayAdapter(
                    this@AddExpenseActivity,
                    android.R.layout.simple_spinner_item,
                    names
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }
        }
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", 0) // default to 0 if not found
    }

    private fun saveExpense() {
        val amountText = etAmount.text.toString().trim()
        val descText = etDescription.text.toString().trim()
        val catName = spinnerCategory.selectedItem?.toString()

        // Check if the amount and category are selected
        if (amountText.isEmpty() || catName.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Please enter an amount and select a category.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Convert amount text to a double or show error
        val amt = amountText.toDoubleOrNull() ?: run {
            Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the category ID from the map using the category name (which is a String)
        val catId = categoryMap[catName] ?: run {
            Toast.makeText(this, "Selected category not found.", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve payment method, tags, notes
        val payment = spinnerPayment.selectedItem.toString()
        val tagsStr = etTags.text.toString().takeIf(String::isNotBlank)
        val notesStr = etNotes.text.toString().takeIf(String::isNotBlank)

        // Get the userId
        val userId = getCurrentUserId()

        // Create an Expense object and pass the correct categoryId (Int) and category (String)
        val expense = Expense(
            userId = userId,
            amount = amt,
            date = selectedDateMillis,
            description = descText,
            categoryId = catId,  // Ensure categoryId is an Int
            category = catName,  // This is the category name as a String
            paymentMethod = payment,
            latitude = latitude,
            longitude = longitude,
            tags = tagsStr,
            notes = notesStr,
            receiptPhoto = receiptUri
        )

        // Insert the expense into the database in a background thread
        lifecycleScope.launch(Dispatchers.IO) {
            val db = BudgetDatabase.getDatabase(applicationContext)
            db.expenseDao().insertExpense(expense)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@AddExpenseActivity,
                    "Expense saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                clearForm()  // Calling the method to clear the form
            }
        }
    }



    // Function to clear the form fields
    private fun clearForm() {
        etAmount.text.clear()
        etDescription.text.clear()
        spinnerCategory.setSelection(0)  // Reset category to first item
        tvDate.text = ""  // Clear date
        etTags.text.clear()
        etNotes.text.clear()
        receiptUri = null  // Reset receipt URI
    }
}