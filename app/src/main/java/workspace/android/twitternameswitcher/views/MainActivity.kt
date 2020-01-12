package workspace.android.twitternameswitcher.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import android.graphics.BitmapFactory
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import workspace.android.twitternameswitcher.views.models.MainViewModel
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.views.models.viewModel
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var model : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = this.viewModel()
        model.httpOauthConsumer = intent.getSerializableExtra("consumer") as CommonsHttpOAuthConsumer
        loadImage()

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
            model.repo.logout()
            finish()
        }
    }

    private fun loadImage() {
        val url = URL(model.currentUser.getString("profile_image_url"))
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        profile_picture.setImageBitmap(bmp)
    }

}
