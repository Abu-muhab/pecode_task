package com.abumuhab.pecodetask.notificationpages

import android.app.NotificationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.abumuhab.pecodetask.R
import com.abumuhab.pecodetask.data.NotificationPage
import com.abumuhab.pecodetask.data.source.NotificationPagesRepository
import com.abumuhab.pecodetask.databinding.FragmentNotificationPageBinding
import kotlinx.coroutines.launch

class NotificationPageFragment(
    var page: NotificationPage
) : Fragment() {
    private lateinit var notificationPagesRepository: NotificationPagesRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentNotificationPageBinding>(
            layoutInflater,
            R.layout.fragment_notification_page,
            container,
            false
        )

        //get a reference to the notificationPages repository
        notificationPagesRepository = NotificationPagesRepository.getInstance(requireContext())

        binding.addButton.setOnClickListener {
            lifecycleScope.launch {
                notificationPagesRepository.addPage()
            }
        }

        binding.subtractButton.setOnClickListener {
            lifecycleScope.launch {
                notificationPagesRepository.removePage()
            }
        }

        binding.createNotificationButton.setOnClickListener {
            createNotificationChannel(requireContext())
            val builder = NotificationCompat.Builder(requireContext(), "npc")
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle("Chat heads active")
                .setContentText("Notification ${page.number}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            with(NotificationManagerCompat.from(requireContext())) {
                notify(page.number, builder.build())
            }
        }


        /**
         * Hide the remove button while on first fragment
         */
        if (page.number == 1) {
            binding.subtractButton.visibility = View.GONE
        }


        binding.pageIndicator.text = page.number.toString()

        return binding.root
    }
}