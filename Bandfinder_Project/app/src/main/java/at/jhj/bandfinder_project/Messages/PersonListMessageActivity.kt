package at.jhj.bandfinder_project.Messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import at.jhj.bandfinder_project.Group_ViewHolders.UserProfileItem
import at.jhj.bandfinder_project.Login.LoginActivity
import at.jhj.bandfinder_project.Models.Person
import at.jhj.bandfinder_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_person_list_message.*

class PersonListMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list_message)
        verifyUserIsLoggedIn();

        supportActionBar?.title="Musiker in der Nähe!"
        getUsersFromDB()
    }

    private fun verifyUserIsLoggedIn()
    {
        if (FirebaseAuth.getInstance().uid == null)
        {
            val intent = Intent(this,
                LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.loggout)
        {
            FirebaseAuth.getInstance().signOut()
            verifyUserIsLoggedIn();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUsersFromDB()
        {
        val db = FirebaseDatabase.getInstance().getReference("/users")
        db.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("PersonList", "oCanceledCalled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                Log.d("PersonList","Starting to get Firebase DB data")

                for (child in p0.children) {
                    Log.d("PersonList", "${child.toString()}")
                    val person = child.getValue(Person::class.java)

                    if(person != null && person.uid != FirebaseAuth.getInstance().uid)
                    {
                        adapter.add(
                            UserProfileItem(
                                person
                            )
                        )
                    }
                    adapter.setOnItemClickListener { item, view ->
                        val intent = Intent(view.context,ChatActivity::class.java)
                        intent.putExtra("PERSON",(item as UserProfileItem).person)
                        startActivity(intent)

                        //Zuruek zur Hauptseite
                        //finish()
                    }
                }

                rV_writeMessage.adapter= adapter
            }

        })
    }

}
