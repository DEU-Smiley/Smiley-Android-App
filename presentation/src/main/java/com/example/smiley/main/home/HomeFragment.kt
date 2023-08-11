package com.example.smiley.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.hospital.model.SimpleHospital
import com.example.domain.hospital.model.SimpleHospitalList
import com.example.domain.magazine.model.Magazine
import com.example.domain.youtube.model.YoutubeVideo
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.bluetooth.viewmodel.BluetoothDataState
import com.example.smiley.bluetooth.viewmodel.BluetoothViewModel
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.start
import com.example.smiley.common.extension.stop
import com.example.smiley.common.extension.visible
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.utils.NotifyManager
import com.example.smiley.common.view.BaseFragment
import com.example.smiley.databinding.FragmentHomeBinding
import com.example.smiley.magazine.MagazineDetailFragment
import com.example.smiley.magazine.MagazineListFragment
import com.example.smiley.main.home.adapter.partner.PartnerListAdapter
import com.example.smiley.main.home.adapter.timeline.TimeLineAdapter
import com.example.smiley.main.home.adapter.timeline.TimeLineItem
import com.example.smiley.main.home.adapter.youtube.YoutubeListAdapter
import com.example.smiley.main.home.viewmodel.HomeViewModel
import com.example.smiley.main.home.viewmodel.HomeFragmentState
import dagger.hilt.android.AndroidEntryPoint

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _bind: FragmentHomeBinding?=null
    private val bind: FragmentHomeBinding get() = _bind!!
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
        initPartnerListView()
        initYoutubeListView()
        requestData()

        return bind.root
    }


    private fun initView(){
        bind.llMagazineDetailBtn.setOnClickListener {
            (requireActivity() as AppCompatActivity)
                .addFragmentToFullScreen(MagazineListFragment.newInstance())
        }

        App.user?.let {
            bind.tvUserName.text = "${it.name}님"
        }
    }

    private fun requestData(){
        homeVm.getTimeLineData()
        homeVm.getNearByPartnerList(3)
        homeVm.getRecommendVideoList()
    }

    private fun observe(){
        repeatOnStarted {
            observeBluetoothState()
        }

        repeatOnStarted {
            observeHomeState()
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

    private suspend fun observeHomeState(){
        homeVm.state.collect { state ->
            when(state){
                is HomeFragmentState.Init -> Unit
                is HomeFragmentState.TimeLine -> {
                    handleTimeLine(state.timeLine)
                }
                is HomeFragmentState.PartnerHospital -> {
                    handlePartnerHospital(state.hospitals)
                }
                is HomeFragmentState.RecommendVideo -> {
                    handleRecommendVideo(state.youtubeList)
                }
                is HomeFragmentState.Error -> {
                    Log.e("HomeFragment", state.message)
                }
                is HomeFragmentState.ShowToast -> {
                    requireContext().showToast(state.message)
                }
            }
        }
    }

    private fun handleTimeLine(timelineItem: List<TimeLineItem>){
        bind.sflShimmerLayout.stop()

        val adapter = bind.rvTimelineView.adapter as TimeLineAdapter
        adapter.changeDataSet(timelineItem as ArrayList<TimeLineItem>)
    }

    private fun handlePartnerHospital(hospitals: List<SimpleHospital>){
        bind.sflPartnerShimmer.stop()

        val adapter = bind.rvFamousHospital.adapter as PartnerListAdapter
        adapter.updateDataSet(hospitals = hospitals as ArrayList<SimpleHospital>)
    }

    private fun handleRecommendVideo(youtubeList: ArrayList<YoutubeVideo>){
        bind.sflRecommendVideo.stop()

        val adapter = bind.rvRecommendVideo.adapter as YoutubeListAdapter
        adapter.updateDataSet(youtubeList = youtubeList)
    }

    /**
     * 타임라인 초기화
     */
    private fun initTimeLineView(){
        bind.rvTimelineView.apply {
            adapter = TimeLineAdapter(
                arrayListOf(),
                magazineClickListener
            )

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun initPartnerListView(){
        bind.rvFamousHospital.apply {
            adapter = PartnerListAdapter(
                arrayListOf()
            )

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun initYoutubeListView(){
        bind.rvRecommendVideo.apply {
            adapter = YoutubeListAdapter(
                requireActivity(),
                arrayListOf()
            )

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
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