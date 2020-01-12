package workspace.android.twitternameswitcher.views.models

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import workspace.android.twitternameswitcher.App
import workspace.android.twitternameswitcher.repository.Repository

val Application.repository: Repository
    get() = (this as App).repository

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): T =
    ViewModelProvider(this,
        ViewModelFactory(this.application.repository)
    ).get(T::class.java)

class ViewModelFactory(val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository::class.java).newInstance(repo)
    }
}