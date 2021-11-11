package com.abumuhab.pecodetask.notificationpages


import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.abumuhab.pecodetask.R
import com.abumuhab.pecodetask.data.source.NotificationPagesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationPageActivity : AppCompatActivity() {
    private var pager: ViewPager2? = null
    private lateinit var notificationPagesRepository: NotificationPagesRepository
    private lateinit var viewModel: NotificationPagesViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()

        //get a reference to the notificationPages repository
        notificationPagesRepository = NotificationPagesRepository.getInstance(application)


        //setup viewModel
        val viewModelFactory = NotificationPagesViewModelFactory(notificationPagesRepository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(NotificationPagesViewModel::class.java)


        //observe page added event
        viewModel.pageAddedEvent.observe(this) {
            it?.let {
                pager?.adapter?.notifyDataSetChanged()
                lifecycleScope.launch {
                    /**
                     * Add a delay to give time for adapter to register changes before animating to added fragment
                     */
                    delay(200)
                    pager?.setCurrentItem(it.number, true)
                }
            }
        }

        //observer page removed event
        viewModel.pageRemovedEvent.observe(this) {
            it?.let {
                /**
                 * Navigate user away from fragment if user is on fragment to be deleted
                 */
                if (pager!!.currentItem == it.number - 1) {
                    pager!!.setCurrentItem(it.number - 2, true)
                }

                lifecycleScope.launch {
                    delay(200)
                    pager?.adapter?.notifyDataSetChanged()
                }

                /**
                 * Remove notification if active for removed fragment
                 */
                val notificationManager: NotificationManager =
                    application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(it.number)

            }
        }


        viewModel.observerNotificationPages.observe(this) {
            /**
             * The notificationPages change is only used once when the pager adapter hasn't been setup.
             * Subsequent changes are handled by pageRemovedEvent and pageAddedEvent
             */
            if (pager == null) {
                pager = findViewById(R.id.pager)
                val pagerAdapter = ScreenSliderPageAdapter(this, viewModel)
                pager!!.adapter = pagerAdapter
            }
        }
    }
}

class ScreenSliderPageAdapter(
    fa: FragmentActivity,
    private val viewModel: NotificationPagesViewModel
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        viewModel.currentPages?.let {
            return it.size
        }
        return 0
    }

    override fun createFragment(position: Int): Fragment {
        return NotificationPageFragment(viewModel.currentPages!![position])
    }
}

