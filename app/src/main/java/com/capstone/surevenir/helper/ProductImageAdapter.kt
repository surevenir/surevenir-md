package com.capstone.surevenir.helper

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ProductImageAdapter : TypeAdapter<List<String>>() {
    override fun write(out: JsonWriter, value: List<String>?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.beginArray()
        value.forEach { out.value(it) }
        out.endArray()
    }

    override fun read(reader: JsonReader): List<String> {
        val images = mutableListOf<String>()
        reader.beginArray()
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "url" -> images.add(reader.nextString())
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
            } else {
                images.add(reader.nextString())
            }
        }
        reader.endArray()
        return images
    }
}