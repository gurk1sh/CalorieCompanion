package se.umu.cs.guth0028.caloriecompanionapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodRepository
import java.util.*

class WelcomeViewModel : ViewModel() {

    private val userRepository = UserRepository.get()
    private val userIdLiveData = MutableLiveData<UUID>()

    private val user = userRepository.getUser()

    var userLiveData: LiveData<User?> =
        Transformations.switchMap(userIdLiveData) {
            userRepository.getUser()
        }

    fun loadUser(userId: UUID) {
        userIdLiveData.value = userId
    }

    fun addUser(user: User) {
        userRepository.addUser(user)
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    fun getIfUserCreated(userId: UUID) {
        userRepository.getIfUserCreated(userId)
    }

    fun getUser() : LiveData<User?> {
        return user
    }
}