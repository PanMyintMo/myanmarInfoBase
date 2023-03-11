package com.pan.mvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.pan.mvvm.R
import com.pan.mvvm.databinding.ActivityMainBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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

                else -> {
                    Toast.makeText(this@MainActivity, "rttot", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }

    private fun fetchUserLoginDetailResponse() {
        myanfobaseViewModel.getLoginDetailResponseLiveData.observe(this) { userLoginDetailResponse ->
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            val navHeader = navigationView.getHeaderView(0)
            val nvEmail = navHeader.findViewById(R.id.user_maile) as TextView
            val nvName = navHeader.findViewById(R.id.userName) as TextView
            val navProfile=navHeader.findViewById(R.id.navProfile) as ImageView

            // for navigation header's email and user name
            nvEmail.text = userLoginDetailResponse.email
            nvName.text = userLoginDetailResponse.username
            Glide.with(this).load( userLoginDetailResponse.profilePicture[0].filePath).into(navProfile)

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}