package workspace.android.twitternameswitcher.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import android.graphics.BitmapFactory
import android.util.Log
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore
import workspace.android.twitternameswitcher.views.models.MainViewModel
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.views.models.viewModel
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var model : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = this.viewModel()
        model.initConsumer(resources.getString(R.string.twitter_api_key), resources.getString(R.string.twitter_api_secret))

        val name = intent.getStringExtra("name")
        model.liveCurrentName().postValue(name)
        val picture = intent.getStringExtra("picture")
        loadPicture(picture)

        model.liveCurrentName().observe(this, Observer<String> {
            current_name.text = it
        })

        live_button.setOnClickListener {
            model.postName(live_text.text.toString())
        }

        offline_button.setOnClickListener {
            model.postName(offline_text.text.toString())
        }

        logout_button.setOnClickListener {
            finish()
        }
    }

    private fun loadPicture(picture : String?) {
        if(picture.isNullOrBlank()){
            Log.d(TAG, "Picture is null")
            return
        }
        Picasso.get().load(picture).into(profile_picture)
    }

}
