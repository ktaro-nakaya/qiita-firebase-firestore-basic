package jp.co.casareal.firebasebasic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val TAG by lazy {
        this.localClassName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editName = findViewById<EditText>(R.id.edt_name)
        val editAge = findViewById<EditText>(R.id.edt_age)
        val textViewResult = findViewById<TextView>(R.id.txt_result)

        findViewById<Button>(R.id.btn_get_all).setOnClickListener {
            textViewResult.text = ""
            db.collection("users")
                .get()
                .apply {
                    addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            task.result?.forEach {
                                textViewResult.text =
                                    "${textViewResult.text}${it.data}${System.lineSeparator()}"
                                Log.e(
                                    TAG,
                                    "Document ID：${it.id}、Document Reference：${it.reference.path}、Contents：${it.data}"
                                )
                            }
                        } else {
                            textViewResult.text = "データの取得に失敗しました。"
                        }
                    }
                }
        }

        findViewById<Button>(R.id.btn_register).setOnClickListener {

            if (editName.text.toString().isNotBlank()) {

                editAge.text.toString().toIntOrNull().let { age ->

                    val user = mapOf(
                        "name" to editName.text.toString(),
                        "age" to age
                    )

                    db.collection("users")
                        .add(user)
                        .apply {
                            addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    textViewResult.text =
                                        "Document Reference：${task.result?.path}"
                                    Log.e(
                                        TAG,
                                        "Document ID：${it.id}、Document Reference：${task.result?.path}、Contents：${task.result}"
                                    )
                                }else{
                                    textViewResult.text = "${task.exception?.stackTrace}"
                                    Log.e(TAG, "登録に失敗しました。", task.exception)
                                }
                            }
                        }
                }

            }
        }
    }
}