package at.jhj.bandfinder_project.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.jhj.bandfinder_project.Group_ViewHolders.ChatItemFrom
import at.jhj.bandfinder_project.Group_ViewHolders.ChatItemTo
import at.jhj.bandfinder_project.Models.Person
import at.jhj.bandfinder_project.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val person = intent.getParcelableExtra<Person>("PERSON")
        supportActionBar?.title = "Chat mit ${person.name}"

        activity_chat_btn_send_message.setOnClickListener {

        }
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatItemFrom("dd"))
        adapter.add(ChatItemTo("d"))

        activity_chat_chatview.adapter = adapter
    }

    private fun sendMsg()
    {
        
    }
}
