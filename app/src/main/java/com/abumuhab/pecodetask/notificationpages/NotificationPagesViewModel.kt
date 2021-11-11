package com.abumuhab.pecodetask.notificationpages

import android.util.Log
import androidx.lifecycle.*
import com.abumuhab.pecodetask.data.NotificationPage
import com.abumuhab.pecodetask.data.source.NotificationPagesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class NotificationPagesViewModel(notificationPagesRepository: NotificationPagesRepository) :
    ViewModel() {
    val pageAddedEvent = MutableLiveData<NotificationPage>()
    val pageRemovedEvent = MutableLiveData<NotificationPage>()
    var currentPages: List<NotificationPage>? = null

    val observerNotificationPages: LiveData<List<NotificationPage>> =
        notificationPagesRepository.observePages().map {
            Log.i("OBSERVING", "observer")
            /**
             * if page list is empty,add the first page
             */
            if (it.isEmpty()) {
                viewModelScope.launch { notificationPagesRepository.addPage() }
            }

            /**
             * calc difference between old and new pages to determine add or remove event
             */
            if (currentPages != null) {
                if (it.size > currentPages!!.size) {
                    pageAddedEvent.value = it.last()
                } else if (currentPages!!.size > it.size) {
                    pageRemovedEvent.value = currentPages!!.last()
                }
            }
            currentPages = it
            it
        }

}

@Suppress("UNCHECKED_CAST")
class NotificationPagesViewModelFactory(
    private val notificationPagesRepository: NotificationPagesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (NotificationPagesViewModel(notificationPagesRepository) as T)
}