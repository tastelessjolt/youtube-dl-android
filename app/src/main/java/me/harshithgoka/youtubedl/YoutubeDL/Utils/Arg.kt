package me.harshithgoka.youtubedl.YoutubeDL.Utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by harshithg on 17/1/18.
 */


class Arg : JSONObject {
    constructor() : super()

    constructor(s: String) : super() {
        try {
            this.put(VAL, s)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    constructor(i: Int) : super() {
        try {
            this.put(VAL, i)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    constructor(a: JSONArray) : super() {
        try {
            this.put(VAL, a)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    companion object {
        const val VAL = "val"
    }
}