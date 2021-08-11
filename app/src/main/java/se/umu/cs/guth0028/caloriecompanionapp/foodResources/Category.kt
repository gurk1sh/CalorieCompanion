package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import androidx.room.*
import java.util.*

@Entity
data class Category (@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "") {

    /*
    * Represents a category that is used in database
    * */

    override fun toString(): String {
        return name
    }

}