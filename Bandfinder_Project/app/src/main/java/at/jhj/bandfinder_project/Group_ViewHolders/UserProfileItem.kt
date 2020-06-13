package at.jhj.bandfinder_project.Group_ViewHolders

import at.jhj.bandfinder_project.Models.Person
import at.jhj.bandfinder_project.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_message_group_view.view.*

class UserProfileItem(val person: Person): Item<ViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.activity_message_group_view
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //Lade den Text
        viewHolder.itemView.message_view_Name.text = person.name
        //Lade das Bild in die Image View
        Picasso.get().load(person.profilbildUrl).into(viewHolder.itemView.message_view_profilephoto)
    }

}