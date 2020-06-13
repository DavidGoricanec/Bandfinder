package at.jhj.bandfinder_project.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import at.jhj.bandfinder_project.Messages.MessagesActivity
import at.jhj.bandfinder_project.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btn_login.setOnClickListener {
            login()
        }

        btn_go_to_register.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            //intent.putExtra("keyIdentifier", value)
            startActivity(intent)
        }


    }

    private fun login()
    {
        val email = txt_login_email.text.toString()
        val passwort = txt_login_password.text.toString()

        if(email.isEmpty() || passwort.isEmpty())
        {
            Toast.makeText(this,"Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            Log.i("Login", "Not all Fields are filled out")
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,passwort)
            .addOnCompleteListener{
                if(it.isSuccessful)
                {
                    Log.d("Login", "Login user uid: ${it.result?.user?.uid}")
                    val intent = Intent(this,
                        MessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else
                {
                    Log.d("Login", "Something went wrong urghh")
                }
            }
            .addOnFailureListener {
                Log.e("Login", "Login Failed ${it.message}")
            }
    }
}
