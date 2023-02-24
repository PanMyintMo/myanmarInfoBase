package com.pan.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.pan.mvvm.R
import com.pan.mvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainBinding

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


        binding.navView.setNavigationItemSelectedListener {

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {

                R.id.save -> {
                    Toast.makeText(this@MainActivity, "Save", Toast.LENGTH_SHORT).show()

                }
                R.id.nav_home -> {
                    Toast.makeText(this@MainActivity, "Home", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this@MainActivity, "rttot", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}