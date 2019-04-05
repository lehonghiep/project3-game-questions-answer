package com.elomath.pro.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class AnswerConvertor : AttributeConverter<MutableList<String>, String> {
    override fun convertToEntityAttribute(dbData: String?): MutableList<String>? {
        if (dbData == null) {
            return mutableListOf()
        }
        val data: String = dbData.trim()
        return Gson().fromJson<MutableList<String>>(data)
    }

    override fun convertToDatabaseColumn(attribute: MutableList<String>?): String? {
        if (attribute == null || attribute.isEmpty())
            return null
        return Gson().toJson(attribute)
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

}