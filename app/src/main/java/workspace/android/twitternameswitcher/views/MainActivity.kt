package workspace.android.twitternameswitcher.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.TwitterCore
import workspace.android.twitternameswitcher.views.models.MainViewModel
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.views.models.viewModel


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var model : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = this.viewModel()

        showProfile()

        model.liveCurrentName().observe(this, Observer<String> {
            current_name.text = it
            Toast.makeText(this, "Name updated with Success!", Toast.LENGTH_SHORT).show()
        })

        live_button.setOnClickListener {
            val text = live_text.text.toString()
            if(text.isEmpty()){
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_LONG).show()
            }
            model.postName(text)
        }

        offline_button.setOnClickListener {
            val text = live_text.text.toString()
            if(text.isEmpty()){
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_LONG).show()
            }
            model.postName(offline_text.text.toString())
        }

        logout_button.setOnClickListener {
            TwitterCore.getInstance().sessionManager.clearActiveSession()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showProfile() {
        val name = intent.getStringExtra("name")
        model.liveCurrentName().postValue(name)
        val picture = intent.getStringExtra("picture")
        loadPicture(picture)
    }

    private fun loadPicture(picture : String?) {
        if(picture.isNullOrBlank()){
            Log.d(TAG, "Picture is null")
            return
        }
        Picasso.get().load(picture).into(profile_picture)
    }

}
