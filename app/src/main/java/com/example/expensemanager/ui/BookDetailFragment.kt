package com.example.expensemanager.ui

import android.R
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.expensemanager.Adapter.ViewPagerAdapter
import com.example.expensemanager.databinding.FragmentBookDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class BookDetailFragment : Fragment() {
    private lateinit var binding : FragmentBookDetailBinding
    private var bookImage = ""
    private var bookAuthor = ""
    private var bookTitle = ""
    private var y = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailBinding.inflate(layoutInflater, container, false)
        bookImage = arguments?.getString("bookImage").toString()
        bookAuthor = arguments?.getString("bookAuthor").toString()
        bookTitle = arguments?.getString("bookTitle").toString()
        val x = arguments?.getBundle("x")
        y = arguments?.getString("y").toString()
        binding.bookName.setText(bookTitle)
        binding.bookAuthor.setText(bookAuthor)

        val fade = Fade()
        fade.excludeTarget(R.id.statusBarBackground, true)
        fade.excludeTarget(R.id.navigationBarBackground, true)

        // below methods are used for adding
        // enter and exit transition.

        // below methods are used for adding
        // enter and exit transition.
        activity?.window?.setEnterTransition(fade)
        activity?.window?.setExitTransition(fade)
//        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        binding.back.setOnClickListener { findNavController().navigateUp() }
        binding.pager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
        binding.tabLayout.setSelected(true)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when(position){
                0 -> {tab.text = "Description"}
                1 -> {tab.text = "Reviews"}
                2 -> {tab.text = "Similarities"}
            }
        }.attach()
        val theImage = GlideUrl(
            bookImage, LazyHeaders.Builder()
                .addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build()
        )
        theImage.let {
            Glide.with(binding.root.context)
                .load(theImage)
                .error(bookImage)
                .into(binding.image)
        }
        return binding.root
    }
}