package workspace.android.twitternameswitcher

import android.app.Application
import workspace.android.twitternameswitcher.repository.Repository
import workspace.android.twitternameswitcher.repository.http.HttpService


class App : Application() {

    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()
        HttpService.init(this)
        repository = Repository(this)
    }
}