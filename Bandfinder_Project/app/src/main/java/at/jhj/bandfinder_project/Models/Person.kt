package at.jhj.bandfinder_project.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
                   val uid: String ="",
                   val name: String = "",
                   var ort: String = "",
                   val profilbildUrl: String = "",
                val instrument: String= ""
):Parcelable