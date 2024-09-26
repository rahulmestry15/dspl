package com.example.dspl.ui.theme

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dspl.network.UserRepository
import com.example.dspl.ui.theme.events.dsplEvents
import com.example.dspl.ui.theme.model.Task
import com.example.dspl.ui.theme.model.UserTasks
import com.example.dspl.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DsplUserNavigationViewModel(val appContext: Application) : AndroidViewModel(appContext) {

    private val repository = UserRepository()
    val userList = ArrayList<UserTasks>()
    private var eventStates = SingleLiveEvent<dsplEvents>()
    var title = MutableLiveData<String>()
    var subTitle = MutableLiveData<String>()
    var titleAlign = MutableLiveData(View.TEXT_ALIGNMENT_TEXT_START)
    var isSubTitleVisible = MutableLiveData(false)
    var toolbarVisiblity = MutableLiveData(true)
    var startIconLogo = MutableLiveData<Drawable?>()
    var startIconClickListener = MutableLiveData<View.OnClickListener>()
    var useContainer = MutableLiveData(false)
    var endIconLogo = MutableLiveData<Drawable?>()

    init {
        userList.clear()
    }


    fun getState(): SingleLiveEvent<dsplEvents> {
        return eventStates
    }
    fun setStartIcon(icon: Drawable?, onClickListener: View.OnClickListener) {
        startIconLogo.value = icon
        startIconClickListener.value = onClickListener
    }
    fun setUseContainer(value:Boolean) {
        useContainer.value = value
    }
    fun setTitle(mTitle: String?) {
        title.value = mTitle ?: ""
    }
    fun setSubTitle(mTitle: String?) {
        subTitle.value = mTitle ?: ""
    }
    fun setEndIcon(icon: Drawable?) {
        endIconLogo.value = icon
    }

    fun fetchUsers( handleResponse: (response: ArrayList<Task>?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response: Response<ArrayList<Task>> = repository.getUsers()
            if (response.isSuccessful) {
                handleResponse(response.body())
            } else {
                Toast.makeText(appContext, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}