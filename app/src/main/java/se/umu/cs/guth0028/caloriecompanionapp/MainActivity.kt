package se.umu.cs.guth0028.caloriecompanionapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import se.umu.cs.guth0028.caloriecompanionapp.userResources.User
import se.umu.cs.guth0028.caloriecompanionapp.userResources.UserViewModel

class MainActivity : AppCompatActivity() {
private lateinit var user: User

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user = User()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) //get the navigation host fragment holding the current fragment
        val inflater = navHostFragment?.findNavController()?.navInflater //get the navigation host fragment inflater
        val graph = inflater?.inflate(R.navigation.nav_graph) //get the navigation graph

        userViewModel.userIdLiveData.observe( //Fetches a list of users
            this,
            { user ->
                user?.let {
                    if (user.isEmpty()) { //Check for any already existing user in the list, start welcomefragment if not
                        graph?.setStartDestination(R.id.welcomeFragment)
                    } else {
                        graph?.setStartDestination(R.id.dailySummaryFragment)
                    }
                    val navController = navHostFragment?.findNavController()
                    if (graph != null && savedInstanceState == null) {
                        navController?.setGraph(graph, intent.extras) //update the navigation graph with the new start destination
                    }
                    userViewModel.userIdLiveData.removeObservers(this) //unregister this observer to avoid this activity randomly changing the current fragment
                }
            }
        )
    }
}