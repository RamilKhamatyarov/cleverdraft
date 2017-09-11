package ru.rkhamatyarov.cleverdraft.model

import com.orm.SugarRecord

import java.util.Date
import kotlin.properties.Delegates

/**
 * Created by Asus on 02.09.2017.
 */

class Note : SugarRecord {
    private var id: Int = -1
    var title: String? = null
    var content: String? = null
    var createdDate: Date? = null
    var modifiedDate: Date? = null

    constructor() {}

    constructor(id: Int, title: String, content: String, createdDate: Date, modifiedDate: Date) {
        this.id = id
        this.title = title
        this.content = content
        this.createdDate = createdDate
        this.modifiedDate = modifiedDate
    }

    constructor(content: String, createdDate: Date) {
        this.content = content
        this.createdDate = createdDate
    }


}
