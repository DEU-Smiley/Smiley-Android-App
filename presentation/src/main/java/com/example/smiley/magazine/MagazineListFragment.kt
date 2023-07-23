package com.example.smiley.magazine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.magazine.model.MagazineList
import com.example.smiley.R
import com.example.smiley.databinding.FragmentMagazineListBinding
import com.example.smiley.magazine.adapter.MagazineListAdapter
import com.example.smiley.main.home.adapter.TimeLineAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MagazineListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MagazineListFragment : Fragment() {

    private var _bind: FragmentMagazineListBinding? = null
    private val bind: FragmentMagazineListBinding get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_magazine_list, container, false)
        // Inflate the layout for this fragment

        initRecyclerView()

        return bind.root
    }

    private fun initRecyclerView(){
        bind.rvMagazineList.apply {
            adapter = MagazineListAdapter(MagazineList(arrayListOf()))

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MagazineListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = MagazineListFragment()
    }
}