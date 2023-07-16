package com.example.smiley.magazine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.databinding.FragmentMagazineDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MagazineDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MagazineDetailFragment : Fragment() {
    private val SAVED_URL: String? = null
    private var magazineUrl: String? = null

    private var _bind:FragmentMagazineDetailBinding?=null
    private val bind:FragmentMagazineDetailBinding get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            magazineUrl = it.getString(SAVED_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_magazine_detail, container, false)

        initWebView()

        return bind.root
    }

    private fun initWebView(){
        with(bind.wvMagazineView.settings){
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            allowContentAccess = true
        }
        bind.wvMagazineView.loadUrl(
            magazineUrl!!
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MagazineDetailFragment.
         */
        @JvmStatic
        fun newInstance(url: String) =
            MagazineDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(SAVED_URL, url)
                }
            }
    }
}