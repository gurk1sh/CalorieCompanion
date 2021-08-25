package se.umu.cs.guth0028.caloriecompanionapp.userResources

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class User(@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var age: Int = 0, var gender: String = "", var weight: Float = 0f, var length: Float = 0f, var activityLevel: String = "",var goal: String = "Maintain", var calories: Float = 0f) :
    Parcelable {
}