package at.jhj.bandfinder_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class PersonDataActivity : AppCompatActivity() {

    lateinit var btn_speichern: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_data)

        btn_speichern  = findViewById(R.id.btn_speichern)


        btn_speichern.setOnClickListener{

            var vorname  = (findViewById(R.id.txt_vorname) as EditText).text
            println(vorname)
        }
    }
}
