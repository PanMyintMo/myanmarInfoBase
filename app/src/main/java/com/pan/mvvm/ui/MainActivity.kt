package com.pan.mvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.pan.mvvm.R
import com.pan.mvvm.databinding.ActivityMainBinding
import com.pan.mvvm.fragments.LoginFragment
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainBinding


    @Inject
    lateinit var tokenManager: TokenManager
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()

    private var isDarkMode = false // keep track of the current mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(R.drawable.myanlogo)
        actionBar?.setDisplayShowTitleEnabled(false)


        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        //for navHeader
        val id = tokenManager.getId()
        if (id != null) {
            myanfobaseViewModel.getUserLoginDetailResponse(id.toString())
        }

        fetchUserLoginDetailResponse()

        binding.navView.setNavigationItemSelectedListener {

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {

                R.id.save -> {
                    Toast.makeText(this@MainActivity, "Save", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivity(intent)
                }

                R.id.theme -> {
                    toggleNightMode()
                }
                R.id.logout -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Confirm exit?")
                        .setMessage("Do you want to log out from this app?")
                        .setNegativeButton("No") { _, _ ->
                            Snackbar.make(binding.root, "Declined", Snackbar.LENGTH_SHORT).show()
                        }
                        .setPositiveButton("Yes") { _, _ ->
                            val intent = Intent(this@MainActivity, LoginFragment::class.java)

                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()


                        }
                        .show()
                }

                else -> {
                    Toast.makeText(this@MainActivity, "rttot", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        // set the initial mode
        isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun toggleNightMode() {

        isDarkMode = !isDarkMode
        if (isDarkMode) {
            // Switch to night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Switch to day mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        recreate() // recreate the activity to apply the new theme
    }

    private fun fetchUserLoginDetailResponse() {
        myanfobaseViewModel.getLoginDetailResponseLiveData.observe(this) { userLoginDetailResponse ->
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            val navHeader = navigationView.getHeaderView(0)
            val nvEmail = navHeader.findViewById(R.id.user_maile) as TextView
            val nvName = navHeader.findViewById(R.id.userName) as TextView
            val navProfile = navHeader.findViewById(R.id.navProfile) as ImageView

            // for navigation header's email and user name
            nvEmail.text = userLoginDetailResponse.email
            nvName.text = userLoginDetailResponse.username

            // check if the profile picture list is not empty before accessing its elements
            if (userLoginDetailResponse.profilePicture.isNotEmpty()) {
                Glide.with(this).load(userLoginDetailResponse.profilePicture[0].filePath)
                    .into(navProfile)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.createPost -> {
                showCreateNewPost()
                return true
            }
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreateNewPost() {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }
}