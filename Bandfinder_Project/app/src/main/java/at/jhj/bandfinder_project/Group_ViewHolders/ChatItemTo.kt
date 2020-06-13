package at.jhj.bandfinder_project.Group_ViewHolders

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import at.jhj.bandfinder_project.R
import kotlinx.android.synthetic.main.chat_to_item_group_view.view.*


class ChatItemTo( val msg: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_item_group_view
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_item_group_view_TextView.text = msg
    }
}