package at.jhj.bandfinder_project.Models

data class Nachricht(val msg_id: String = ""
                     ,val from_id: String = ""
                     ,val to_id: String = ""
                     ,val msg: String = ""
        ,val timestmp: Long = 0
)