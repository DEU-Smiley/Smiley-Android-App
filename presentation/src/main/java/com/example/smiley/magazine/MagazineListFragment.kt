package com.example.smiley.magazine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.magazine.model.Magazine
import com.example.domain.magazine.model.MagazineList
import com.example.smiley.R
import com.example.smiley.common.extension.addFragment
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.addFragmetFullScreen
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.start
import com.example.smiley.common.extension.stop
import com.example.smiley.common.extension.visible
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.databinding.FragmentMagazineListBinding
import com.example.smiley.magazine.adapter.MagazineListAdapter
import com.example.smiley.magazine.viewmodel.MagazineListState
import com.example.smiley.magazine.viewmodel.MagazineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MagazineListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MagazineListFragment : Fragment() {

    private var _bind: FragmentMagazineListBinding? = null
    private val bind: FragmentMagazineListBinding get() = _bind!!

    private val magazineVm: MagazineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_magazine_list, container, false)

        observe()
        requestData()
        initRecyclerView()

        return bind.root
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeMagazineState()
            }
        }
    }

    private suspend fun observeMagazineState(){
        magazineVm.state.collect { state ->
            when(state){
                is MagazineListState.Init -> {
                    bind.sflShimmerLayout.start()
                }
                is MagazineListState.SuccessLoad -> {
                    bind.sflShimmerLayout.stop()

                    val adapter = bind.rvMagazineList.adapter as MagazineListAdapter
                    adapter.changeDataSet(state.magazineList)
                }
                is MagazineListState.Error -> {
                    bind.sflShimmerLayout.stop()

                    requireActivity().showConfirmDialog(
                        title = "매거진 조회 오류",
                        content = state.message,
                        subContent = "계속해서 오류가 발생한다면, SMILEY 팀에게 문의해주세요.",
                    )
                }
            }
        }
    }

    private fun requestData(){
        magazineVm.getAllMagazines()
    }

    private fun initRecyclerView(){
        bind.rvMagazineList.apply {
            adapter = MagazineListAdapter(
                MagazineList(arrayListOf()),
                magazineClickListener
            )

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private val magazineClickListener = object : OnItemClickListener<Magazine> {
        override fun onItemClicked(view: View, data: Magazine) {
            val magazineFragment = MagazineDetailFragment.newInstance(data.contentUrl)

            this@MagazineListFragment.addFragmetFullScreen(magazineFragment)
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
        @JvmStatic
        fun newInstance() = MagazineListFragment()
    }
}