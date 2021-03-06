package at.jhj.bandfinder_project.Login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.jhj.bandfinder_project.Messages.PersonListMessageActivity
import at.jhj.bandfinder_project.Models.Person
import at.jhj.bandfinder_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    //lateinit var btn_verify: Button;
    var v_profilephotoUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Registrieren bei Bandfinder!"

        //initialize variables
        //btn_verify = findViewById(R.id.btn_register)

        val instrumente = resources.getStringArray(R.array.Instrumente)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, instrumente
            )
            spinner.adapter = adapter
        }


        //onclick button
        btn_register.setOnClickListener{
            register()
        }
        btn_profile_photo.setOnClickListener{
            Log.d("Main", "Photo Selection")

            val intent = Intent(Intent.ACTION_PICK)
            //images only
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    //private functions
    private fun register()
    {
        val email = txt_email.text.toString()
        val password = txt_password.text.toString();
        val name =  txt_Name.text.toString();
        val ort = txt_Ort.text.toString();
        val isntrument = findViewById<Spinner>(R.id.spinner).getSelectedItem().toString();

        Log.d("Main","Email: ${email} Password: ${password} Name: ${name} Ort: ${ort} Instrument: ${isntrument}")

        if(password.isEmpty() || email.isEmpty() || name.isEmpty() || ort.isEmpty() || v_profilephotoUri == null || isntrument == "Gespieltes Instrument wählen:"|| isntrument == null) {
            Toast.makeText(this,"Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            Log.i("Main", "Not all Fields are filled out")
            return
        }

        Toast.makeText(this,"Upload und Registrierung läuft, bitte warten...", Toast.LENGTH_SHORT).show()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful)
                {
                    Log.d("Main", "Login created user uid: ${it.result?.user?.uid}")
                    Log.d("Main", "Strating Photo Upload")
                    uploadProfilePhoto()
                }
                else
                {
                    Log.e("Main", "No User can be created")
                    return@addOnCompleteListener
                }
            }.addOnFailureListener{
                Log.e("Main", "User creation Error: ${it.message}")
                Toast.makeText(this,"Benutzer konnte nicht erstellt werden. ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfilePhoto()
    {
        if (v_profilephotoUri == null)
        {
            Log.w("Main", "No profilephoto avaiable")
            Toast.makeText(this,"Kein Profilphoto ausgewählt", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = UUID.randomUUID().toString()
        val storage= FirebaseStorage.getInstance().getReference("/images/$filename")

        storage.putFile(v_profilephotoUri!!).addOnSuccessListener {
            Log.d("Main", "Profilephoto successfully uploaded. Path: ${it.metadata?.path}")

            storage.downloadUrl.addOnSuccessListener {
                Log.d("Main","File Location: $it")

                saveUserToDatabase(it.toString())
            }
        }.addOnFailureListener{
            Log.e("Main" ,"Error: Uploading Photo failed ${it.message}")
            Toast.makeText(this,"Fehler beim hochladen des Profilphotos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveUserToDatabase(p_profilbildUrl: String)
    {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val db = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = Person(
            uid,
            txt_Name.text.toString(),
            txt_Ort.text.toString(),
            p_profilbildUrl,
            findViewById<Spinner>(R.id.spinner).getSelectedItem().toString()
        )
        db.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "User saved to Firebase")

                Toast.makeText(this,"Willkommen bei Bandfinder!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PersonListMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener {
                Log.e("Main", "Error: Saving User to DB failed: ${it.message}") }
    }

    //override functions
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0
            && resultCode == Activity.RESULT_OK
            && data != null)
        {
            Log.d("Main", "Photoselection complete")

            v_profilephotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,v_profilephotoUri)

            iV_pfoilfoto.setImageBitmap(bitmap)
            btn_profile_photo.alpha= 0f;
        }
    }


}
