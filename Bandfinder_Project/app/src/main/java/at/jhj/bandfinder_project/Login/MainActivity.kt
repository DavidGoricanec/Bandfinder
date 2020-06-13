package at.jhj.bandfinder_project.Login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.jhj.bandfinder_project.Messages.MessagesActivity
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

        //initialize variables
        //btn_verify = findViewById(R.id.btn_register)


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

        Log.d("Main","Email: ${email} Password: ${password} Name: ${name} Ort: ${ort}")

        if(password.isEmpty() || email.isEmpty() || name.isEmpty() || ort.isEmpty() || v_profilephotoUri == null) {
            Toast.makeText(this,"Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            Log.i("Main", "Not all Fields are filled out")
            return
        }

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

        Toast.makeText(this,"Registrierung beendet", Toast.LENGTH_SHORT).show()
    }

    private fun uploadProfilePhoto()
    {
        if (v_profilephotoUri == null)
        {
            Log.w("Main", "No profilephoto avaiable")
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
        }.addOnFailureListener{ Log.e("Main" ,"Error: Uploading Photo failed ${it.message}")}

    }

    private fun saveUserToDatabase(p_profilbildUrl: String)
    {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val db = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = Person(
            uid,
            txt_Name.text.toString(),
            txt_Ort.text.toString(),
            p_profilbildUrl
        )
        db.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "User saved to Firebase")

                val intent = Intent(this, MessagesActivity::class.java)
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
            //val draw_bitmap = BitmapDrawable(bitmap)
            //btn_profile_photo.setBackgroundDrawable(draw_bitmap)
        }
    }


}
