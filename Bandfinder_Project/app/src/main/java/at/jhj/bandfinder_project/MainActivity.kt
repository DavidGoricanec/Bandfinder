package at.jhj.bandfinder_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var btn_secondPage: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_secondPage = findViewById(R.id.btnSecondPage)

        btn_secondPage.setOnClickListener{
            val intent: Intent = Intent(applicationContext,PersonDataActivity::class.java)
            startActivity(intent)
        }
    }


}
