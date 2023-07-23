package com.example.smiley.main.home

import android.os.Bundle
import android.util.Log
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
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.bluetooth.viewmodel.BluetoothDataState
import com.example.smiley.bluetooth.viewmodel.BluetoothViewModel
import com.example.smiley.common.extension.addFragment
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.visible
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.common.utils.NotifyManager
import com.example.smiley.databinding.FragmentHomeBinding
import com.example.smiley.magazine.MagazineDetailFragment
import com.example.smiley.magazine.MagazineListFragment
import com.example.smiley.main.home.adapter.TimeLineAdapter
import com.example.smiley.main.home.adapter.TimeLineItem
import com.example.smiley.main.home.viewmodel.HomeViewModel
import com.example.smiley.main.home.viewmodel.TimeLineState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _bind: FragmentHomeBinding?=null
    private val bind:FragmentHomeBinding get() = _bind!!
    private val bluetoothVm: BluetoothViewModel by viewModels({requireActivity()})
    private val homeVm: HomeViewModel by viewModels()

    private var notifyFlag:Boolean = false

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        observe()
        initView()
        initTimeLineView()
        requestData()

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTouchEffectToAllViews(view as ViewGroup)
    }

    /**
     * 클릭 가능한 모든 뷰에 반투명 효과 적용
     */
    private fun applyTouchEffectToAllViews(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child.isClickable) {
                child.setOnTouchListener(TransparentTouchListener())
            }

            if (child is ViewGroup) {
                applyTouchEffectToAllViews(child)
            }
        }
    }

    private fun initView(){
        bind.llMagazineDetailBtn.setOnClickListener {
            (requireActivity() as AppCompatActivity)
                .addFragmentToFullScreen(MagazineListFragment.newInstance())
        }
    }

    private fun requestData(){
        homeVm.getTimeLineData()
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeBluetoothState()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeTimeLineState()
            }
        }
    }

    private suspend fun observeBluetoothState(){
        bluetoothVm.dataState.collect { state ->
            when (state) {
                is BluetoothDataState.Init -> Unit
                is BluetoothDataState.ReceiveData -> {
                    Log.d("플로우", "${state.wearFlag}")
                    if (state.wearFlag && !notifyFlag) {
                        NotifyManager.sendNotification(
                            requireContext(),
                            NotifyManager.WEARING_NOTIFY_ID,
                            "Smiley",
                            "교정기를 착용 중입니다."
                        )
                    }

                    notifyFlag = state.wearFlag
                }
            }
        }
    }

    private suspend fun observeTimeLineState(){
        homeVm.timeLineState.collect { state ->
            when(state){
                is TimeLineState.Init -> {
                    with(bind.sflShimmerLayout){
                        startShimmer()
                        visible()
                    }
                }
                is TimeLineState.SuccessLoad -> {
                    Log.d("매거진 조회 성공", "${state.timeLine}")
                    with(bind.sflShimmerLayout){
                        stopShimmer()
                        gone()
                    }
                    val adapter = bind.rvTimelineView.adapter as TimeLineAdapter
                    adapter.changeDataSet(state.timeLine as ArrayList<TimeLineItem>)
                }
                is TimeLineState.Error -> {
                    Log.e("HomeFragment", state.message)
                }
                is TimeLineState.ShowToast -> {
                    requireContext().showToast(state.message)
                }
            }
        }
    }

    /**
     * 타임라인 초기화
     */
    private fun initTimeLineView(){
        bind.rvTimelineView.apply {
            adapter = TimeLineAdapter(arrayListOf()).apply {
                setMagazineClickListener(magazineClickListener)
            }

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private val magazineClickListener = object : OnItemClickListener<Magazine> {
        override fun onItemClicked(view: View, data: Magazine) {
            val magazineFragment = MagazineDetailFragment.newInstance(data.contentUrl)
            (requireActivity() as AppCompatActivity)
                .addFragmentToFullScreen(magazineFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}