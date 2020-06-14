package at.jhj.bandfinder_project.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import at.jhj.bandfinder_project.Group_ViewHolders.ChatItemFrom
import at.jhj.bandfinder_project.Group_ViewHolders.ChatItemTo
import at.jhj.bandfinder_project.Models.Nachricht
import at.jhj.bandfinder_project.Models.Person
import at.jhj.bandfinder_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Exception

class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    var person : Person? = null
    var current_person : Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        person = intent.getParcelableExtra<Person>("PERSON")

        set_current_user()
        msg_added()

        supportActionBar?.title = "Chat mit ${person?.name}"

        activity_chat_chatview.adapter = adapter


        activity_chat_btn_send_message.setOnClickListener {
            sendMsg(person)
        }
    }

    private fun set_current_user() {
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("/users/$uid")
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                return
            }

            override fun onDataChange(p0: DataSnapshot) {
                current_person= p0.getValue(Person::class.java)
            }

        })
    }

    private fun msg_added()
    {
        val db = FirebaseDatabase.getInstance().getReference("/nachrichten")

        db.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                throw Exception("Message Canceld, this should not happen")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                throw Exception("Message Moved, this should not happen")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                throw Exception("Message Changed, this should not happen")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("MESSAGE","called msg added")
                val nachricht = p0.getValue(Nachricht::class.java)

                if(nachricht != null) {

                    if(nachricht.from_id == FirebaseAuth.getInstance().uid) {

                        adapter.add(ChatItemFrom(nachricht.msg, current_person))
                    }
                    else {
                        adapter.add(ChatItemTo(nachricht.msg, person))
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                throw Exception("Message Removed, this should not happen")
            }

        })
    }

    private fun sendMsg(person: Person?)
    {
        if (person != null) {
            val db = FirebaseDatabase.getInstance().getReference("/nachrichten").push()

            val nachricht = Nachricht(
                db.key!!,
                FirebaseAuth.getInstance().uid!!
                , person.uid
                , activity_chat_new_message.text.toString()
                , System.currentTimeMillis()
            )

            db.setValue(nachricht).addOnSuccessListener {
                Log.d("MESSAGE", "Message send success")
            }
        }
        else
        {
            throw Exception("Message send, Peron is null")
        }
    }
}
