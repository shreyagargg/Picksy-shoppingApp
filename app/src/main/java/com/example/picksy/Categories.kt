package com.example.picksy

class Categories {
    private var _name: String = ""
    private var _color: String = ""
    private var _icon: String = ""
    private var _brief: String = ""
    private var _id: Int = 0

    constructor(name: String, color: String, icon: String, brief: String, id: Int) {
        this.name = name
        this.color = color
        this.icon = icon
        this.brief = brief
        this.id = id
    }

    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    var color: String
        get() = _color
        set(value) {
            _color = value
        }

    var icon: String
        get() = _icon
        set(value) {
            _icon = value
        }

    var brief: String
        get() = _brief
        set(value) {
            _brief = value
        }

    var id: Int
        get() = _id
        set(value) {
            if (value >= 0) {
                _id = value
            } else {
                println("ID cannot be negative")
            }
        }
}