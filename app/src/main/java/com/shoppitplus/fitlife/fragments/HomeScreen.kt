package com.shoppitplus.fitlife.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shoppitplus.fitlife.R
import com.shoppitplus.fitlife.adapter.HomePagerAdapter
import com.shoppitplus.fitlife.databinding.FragmentHomeScreenBinding
import java.util.Calendar

class HomeScreen : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        setupHeader()
        setupTabs()
        setupShareButton()

        return binding.root
    }

    private fun setupTabs() {
        val pagerAdapter = HomePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Contacts"
                1 -> "Call History"
                else -> "Exercises"
            }
        }.attach()
    }

    private fun setupHeader() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("user_name", "") ?: ""

        val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }

        binding.userName.text = "$greeting, $username 👋"
    }

    private fun setupShareButton() {
        binding.btnShareApp.setOnClickListener {
            val appLink = "https://drive.google.com/drive/folders/1DZZ_SZzrjUKk_xwcilodiShjerevDfjx?usp=sharing"

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this fitness app: $appLink")
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, "Share App"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
