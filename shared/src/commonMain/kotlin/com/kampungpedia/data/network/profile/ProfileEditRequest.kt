package com.kampungpedia.data.network.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileEditRequest  (
    var name: String?,
    var photo: ByteArray?
){
     class Builder{
         private var name: String? = null
         private var photo: ByteArray? = null

         fun name(name: String) = apply { this.name = name }
         fun photo(bytes: ByteArray) = apply { this.photo = bytes }
         fun build() = ProfileEditRequest(name, photo)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ProfileEditRequest

        if (name != other.name) return false
        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (photo?.contentHashCode() ?: 0)
        return result
    }
}