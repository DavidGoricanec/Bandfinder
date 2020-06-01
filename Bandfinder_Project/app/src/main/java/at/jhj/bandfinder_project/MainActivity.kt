package at.jhj.bandfinder_project

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var btn_verify: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_verify = findViewById(R.id.btn_verify)


        btn_verify.setOnClickListener{

            var phoneumber: String = findViewById<TextView>(R.id.txtNummer).text.toString()
            var code: String = findViewById<TextView>(R.id.txt_code).text.toString()

            phoneumber = code + phoneumber


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks

            //val intent: Intent = Intent(applicationContext,PersonDataActivity::class.java)
            //startActivity(intent)
        }
    }


}
