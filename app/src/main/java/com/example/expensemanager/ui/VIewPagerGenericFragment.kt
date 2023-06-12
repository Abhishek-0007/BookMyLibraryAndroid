package com.example.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expensemanager.R
import com.example.expensemanager.databinding.FragmentVIewPagerGenericBinding

class VIewPagerGenericFragment(private val check : Int) : Fragment() {
    private lateinit var binding : FragmentVIewPagerGenericBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVIewPagerGenericBinding.inflate(layoutInflater, container, false)
        when(check){
            1 -> {
                binding.desc.visibility = View.VISIBLE
                binding.author.visibility = View.GONE
                binding.review.visibility = View.GONE
            }
            2 -> {
                binding.desc.visibility = View.GONE
                binding.author.visibility = View.VISIBLE
                binding.review.visibility = View.GONE
            }
            else -> {
                binding.desc.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.review.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}