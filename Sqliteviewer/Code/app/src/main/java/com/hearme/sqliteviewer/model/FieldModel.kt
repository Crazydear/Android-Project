package com.hearme.sqliteviewer.model

class FieldModel {
    var fieldName: String? = null
    var fieldType: String? = null
    var notNull = 0
    var pk = 0
    var def: String? = null

    constructor()
    constructor(fieldName: String, fieldType: String, notNull: Int, pk: Int, def: String) {
        this.fieldName = fieldName
        this.fieldType = fieldType
        this.notNull = notNull
        this.pk = pk
        this.def = def
    }

    val headerName: String
        get() = fieldName + " (" + fieldType + ")"
}