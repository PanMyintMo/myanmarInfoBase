package com.pan.mvvm.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pan.mvvm.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*


class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("we started with our goal is we want to know you How to apply passport in our country Myanmar at that time our country situation is not good so many young people want to go out oversea so many people want to know \"How to do and get passport\". we want to share this kind of information so we started for information sharing website.")

            .setImage(R.drawable.myanlogo)
            .addItem(Element().setTitle("Version 6.2"))
            .addGroup("Connect with us")
            .addEmail("panmyintmo9@gmail.com")
            .addWebsite("https://www.myanfobase.com")
            .addFacebook("Pan")
            .addTwitter("pan")
            .addYoutube("pan")
            .addGitHub("pan")
            .addItem(getCopyRightsElement())
            .create()


        setContentView(aboutPage)

    }

    private fun getCopyRightsElement(): Element {
        val copyRightElement = Element()
        val copyrights =
            String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR))
        copyRightElement.title = copyrights
        copyRightElement.iconDrawable = R.drawable.about_icon_copy_right
        copyRightElement.autoApplyIconTint = true
        copyRightElement.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        copyRightElement.iconNightTint = android.R.color.white
        copyRightElement.gravity = Gravity.CENTER
        copyRightElement.setOnClickListener {
            Toast.makeText(this, copyrights, Toast.LENGTH_SHORT).show()
        }
        return copyRightElement
    }
}