package workspace.android.twitternameupdater.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.TwitterCore
import workspace.android.twitternameupdater.views.models.MainViewModel
import workspace.android.twitternameupdater.R
import workspace.android.twitternameupdater.repository.Preset
import workspace.android.twitternameupdater.views.models.viewModel


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var model : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = this.viewModel()

        showInfo()

        model.liveCurrentName().observe(this, Observer<String> {
            current_name.text = it
            Toast.makeText(this, "Name updated with Success!", Toast.LENGTH_SHORT).show()
        })

        live_save_button.setOnClickListener {
            val name = live_text.text.toString()
            model.saveName(name, Preset.LIVE)
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        }

        live_button.setOnClickListener {
            val name = live_text.text.toString()
            if(!isNameEmpty(name)){
                model.postName(name)
            }
            model.saveName(name, Preset.LIVE)
        }

        offline_save_button.setOnClickListener {
            val name = offline_text.text.toString()
            model.saveName(name, Preset.OFFLINE)
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        }

        offline_button.setOnClickListener {
            val name = offline_text.text.toString()
            if(!isNameEmpty(name)){
                model.postName(name)
            }
            model.saveName(name, Preset.OFFLINE)
        }

        logout_button.setOnClickListener {
            TwitterCore.getInstance().sessionManager.clearActiveSession()
            model.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showInfo() {
        val name = intent.getStringExtra("name")
        model.liveCurrentName().postValue(name)
        val picture = intent.getStringExtra("picture")
        loadPicture(picture)

        live_text.setText(model.getName(Preset.LIVE))
        offline_text.setText(model.getName(Preset.OFFLINE))
    }

    private fun loadPicture(picture : String?) {
        if(picture.isNullOrBlank()){
            Log.d(TAG, "Picture is null")
            return
        }
        Picasso.get().load(picture).into(profile_picture)
    }

    private fun isNameEmpty(name : String) : Boolean {
        if(name.isEmpty()){
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

}
