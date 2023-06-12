package com.example.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.MyEvent
import com.example.expensemanager.ViewModels.LiveDataViewModel
import com.example.expensemanager.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    private val viewModel: RetrofitHelper by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.back.setOnClickListener { findNavController().navigateUp() }

        binding.submit.setOnClickListener {
            viewModel.url.value = binding.url.text.toString()
        }

        return binding.root
    }
}