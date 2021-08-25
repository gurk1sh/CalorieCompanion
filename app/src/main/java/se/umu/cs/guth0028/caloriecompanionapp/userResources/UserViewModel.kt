package se.umu.cs.guth0028.caloriecompanionapp.userResources

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository.get()
    val userIdLiveData = userRepository.getUserWithoutID()

    val userLiveData = userRepository.getUserWithoutID()

    fun addUser(user: User) {
        userRepository.addUser(user)
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    fun updateUserGoal(user: User) {
        userRepository.updateUserGoal(user)
    }

    fun updateUserCalories(user: User) {
        userRepository.updateUserCalories(user)
    }
}