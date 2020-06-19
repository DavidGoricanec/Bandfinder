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

    var current_person : Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list_message)
        verifyUserIsLoggedIn();

        supportActionBar?.title="Musiker in der Nähe!"

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
        else {
            set_current_user()
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

    private fun set_current_user() {
        val uid = FirebaseAuth.getInstance().uid
        Log.d("MESSAGE", "Trying to get current_person for uid: ${uid}")
        val db = FirebaseDatabase.getInstance().getReference("/users/$uid")

        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("MESSAGE","OnCancelled called")
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("MESSAGE","trying to parse user")
                current_person= p0.getValue(Person::class.java)
                Log.d("MESSAGE","Got Person: ${current_person?.uid}")

                //moved from onCreate to here, since it takes time for the DB to return the current user
                getUsersFromDB()
            }

        })
    }

    private fun getUsersFromDB() {
        if (current_person != null) {
            val db = FirebaseDatabase.getInstance().getReference("/users")
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("PersonList", "oCanceledCalled")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()

                    Log.d("PersonList", "Starting to get Firebase DB data")

                    for (child in p0.children) {
                        Log.d("PersonList", "${child.toString()}")
                        val person = child.getValue(Person::class.java)

                        if (person != null && person.uid != current_person?.uid && person.ort == current_person?.ort) {
                            adapter.add(
                                UserProfileItem(
                                    person
                                )
                            )
                        }
                        adapter.setOnItemClickListener { item, view ->
                            val intent = Intent(view.context, ChatActivity::class.java)
                            intent.putExtra("PERSON", (item as UserProfileItem).person)
                            intent.putExtra("CURRENT_PERSON", current_person)
                            startActivity(intent)

                            //Zuruek zur Hauptseite
                            //finish()
                        }
                    }

                    rV_writeMessage.adapter = adapter
                }

            })
        }
        else
        {
            throw Exception("getUsersFromDB, current_person is null")
        }
    }


}
