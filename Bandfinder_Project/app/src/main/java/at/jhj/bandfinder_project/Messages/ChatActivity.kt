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
        current_person = intent.getParcelableExtra<Person>("CURRENT_PERSON")


        msg_added()

        supportActionBar?.title = "Chat mit ${person?.name}"

        activity_chat_chatview.adapter = adapter


        activity_chat_btn_send_message.setOnClickListener {
            sendMsg()
        }
    }


    private fun msg_added() {
        if (person != null && current_person != null) {

            val db = FirebaseDatabase.getInstance()
                .getReference("/nachrichten/${current_person?.uid}/${person?.uid}")

            db.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("MESSAGE","Message Canceled, this should not happen")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    Log.e("MESSAGE","Message Moved, this should not happen")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    Log.e("MESSAGE","Message Changed, this should not happen")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    Log.d("MESSAGE", "called msg added")
                    val nachricht = p0.getValue(Nachricht::class.java)

                    if (nachricht != null) {

                        if (nachricht.from_id == FirebaseAuth.getInstance().uid) {

                            adapter.add(ChatItemFrom(nachricht.msg, current_person))
                        } else {
                            adapter.add(ChatItemTo(nachricht.msg, person))
                        }
                    }
                    activity_chat_chatview.scrollToPosition(adapter.itemCount-1)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    Log.e("MESSAGE","Message Removed, this should not happen")
                }

            })
        }
        else
        {
            Log.e("MESSAGE", "Person: ${person?.uid} , current_person: ${current_person?.uid}")
            Log.e("MESSAGE","Message send, Person is null")
        }
    }

    private fun sendMsg()
    {
        if (person?.uid != null && current_person?.uid != null) {
            val db = FirebaseDatabase.getInstance()
                .getReference("/nachrichten/${current_person?.uid}/${person?.uid}").push()

            val db_2 = FirebaseDatabase.getInstance()
                .getReference("/nachrichten/${person?.uid}/${current_person?.uid}").push()

            val nachricht = Nachricht(
                db.key!!
                , current_person?.uid!!
                , person?.uid!!
                , activity_chat_new_message.text.toString()
                , System.currentTimeMillis()
            )

            db.setValue(nachricht).addOnSuccessListener {
                Log.d("MESSAGE", "Message send success")
            }.addOnFailureListener{
                Log.e("MESSAGE","Msg1 send failed ${it.message}")
            }

            db_2.setValue(nachricht).addOnSuccessListener {
                Log.d("MESSAGE", "Second Message send success")

                activity_chat_new_message.text.clear()
                activity_chat_chatview.scrollToPosition(adapter.itemCount-1)
            }.addOnFailureListener{
                Log.e("MESSAGE","Msg2 send failed ${it.message}")
            }
        }
        else
        {
            Log.e("MESSAGE", "Person: ${person?.uid} , current_person: ${current_person?.uid}")
            Log.e("MESSAGE","Message send, Person is null")
        }
    }
}
