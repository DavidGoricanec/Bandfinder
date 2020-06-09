package at.jhj.bandfinder_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_write_message.*

class WriteMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_message)

        supportActionBar?.title="Benutzer auswählen"
        getUsersFromDB()
    }

    private fun getUsersFromDB()
        {
        val db = FirebaseDatabase.getInstance().getReference("/users")
        db.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                Log.d("WriteMessage","Starting to get Firebase DB data")

                for (child in p0.children) {
                    Log.d("WriteMessage", "${child.toString()}")
                    val person = child.getValue(Person::class.java)

                    if(person != null)
                    {
                        adapter.add(UserProfileItem(person))
                    }
                }

                rV_writeMessage.adapter= adapter
            }

        })
    }

}
