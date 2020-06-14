package at.jhj.bandfinder_project.Group_ViewHolders

import at.jhj.bandfinder_project.Models.Person
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import at.jhj.bandfinder_project.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_from_item_group_view.view.*
import kotlinx.android.synthetic.main.chat_to_item_group_view.view.*


class ChatItemTo( val msg: String, val person: Person?): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_item_group_view
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_item_group_view_TextView.text = msg

        if(person != null) {
            val uri = person.profilbildUrl
            Picasso.get().load(uri).into(viewHolder.itemView.chat_to_item_group_view_image)
        }
    }
}