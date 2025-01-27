package com.example.smiley.main.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.hospital.model.SimpleHospital
import com.example.domain.magazine.model.Magazine
import com.example.domain.youtube.model.YoutubeVideo
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.bluetooth.viewmodel.BluetoothDataState
import com.example.smiley.bluetooth.viewmodel.BluetoothViewModel
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.invisible
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.setWearTimeMode
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.stop
import com.example.smiley.common.extension.visible
import com.example.smiley.common.listener.FragmentVisibilityListener
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.utils.NotifyManager
import com.example.smiley.common.utils.decorutils.SideSpaceDecoration
import com.example.smiley.common.view.BaseFragment
import com.example.smiley.databinding.FragmentHomeBinding
import com.example.smiley.databinding.LayoutCommonAppBarBinding
import com.example.smiley.hospital.HospitalMapFragment
import com.example.smiley.magazine.MagazineDetailFragment
import com.example.smiley.magazine.MagazineListFragment
import com.example.smiley.main.banner.adapter.AutoScrollBannerAdapter
import com.example.smiley.main.home.adapter.magazine.RecentMagazineAdapter
import com.example.smiley.main.home.adapter.partner.PartnerListAdapter
import com.example.smiley.main.home.adapter.youtube.YoutubeListAdapter
import com.example.smiley.main.home.viewmodel.HomeFragmentState
import com.example.smiley.main.home.viewmodel.HomeViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(), FragmentVisibilityListener {

    private var _bind: FragmentHomeBinding?=null
    private val bind: FragmentHomeBinding get() = _bind!!
    private val appBarBinding: LayoutCommonAppBarBinding by lazy {
        LayoutCommonAppBarBinding.bind(bind.clParent)
    }

    private val bluetoothVm: BluetoothViewModel by viewModels({requireActivity()})
    private val homeVm: HomeViewModel by viewModels()
    private val bannerAdapter: AutoScrollBannerAdapter by lazy {
        context?.let {
            AutoScrollBannerAdapter(it)
        }!!
    }

    private var notifyFlag:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        observe()
        initView()
        initCalendarView()
        initStatusChart()
        addClickEvent()

        initBannerView()
        initMagazineListView()
        initPartnerListView()
        initYoutubeListView()

        requestData()

        return bind.root
    }

    override fun onStart() {
        super.onStart()
        onShowFragment()
    }

    private fun initView(){
        bind.tvTodayMagazine.setOnClickListener {
            (requireActivity() as AppCompatActivity)
                .addFragmentToFullScreen(MagazineListFragment.newInstance())
        }

        App.user?.let {
            bind.tvNickname.text = "${it.name}님"
        }
    }

    /**--------------------------------------------------------------------------------------------*/
    private val today = LocalDate.now()
    private fun initCalendarView(){
        val currentMonth = YearMonth.now()
        val endMonth = currentMonth.plusMonths(1)
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        initWeekCalendar(currentMonth, endMonth, daysOfWeek(firstDayOfWeek))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initWeekCalendar(startMonth: YearMonth, endMonth: YearMonth, daysOfWeek: List<DayOfWeek>){
        with(bind.wcvWeekWearStatus){
            dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
                override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)
                override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                    container.day = data
                    bindDate(
                        date = data.date,
                        container = container,
                        isSelectable = data.position == WeekDayPosition.RangeDate
                    )
                }
            }

            setup(
                startMonth.atStartOfMonth(),
                endMonth.atEndOfMonth(),
                daysOfWeek.first()
            )
            scrollToWeek(today)
            setOnTouchListener { _, _ -> true }
        }
    }

    /**
     * @param cnt Int 날짜 셀의 착용 단계를 표현 (추후 시간 데이터로 바꿔야함)
     */
    private fun bindDate(date: LocalDate, container: WeekDayViewContainer, isSelectable: Boolean){
        with(container) {
            tvDayOfWeek.text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            tvDay.text = "${date.dayOfMonth}"
            setDot()
        }
    }

    inner class WeekDayViewContainer(view: View): ViewContainer(view){
        lateinit var day: WeekDay
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvDay: TextView = view.findViewById(R.id.tvDay)
        val dotLayout: LinearLayout = view.findViewById(R.id.ll_dot_layout)

        fun setEnabled(isEnabled: Boolean){
            if(isEnabled){
                this.tvDay.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black1_20))
            } else {
                this.tvDay.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray5_CB))
            }
        }

        @TestOnly
        fun setDot(){
            val dotCnt = 1..4
            val dots = dotLayout.children.toList()
            val cnt = dotCnt.random()

            for (i in dots.indices){
                if(i < cnt){
                    dots[i].visible()
                } else {
                    dots[i].invisible()
                }
            }
        }
    }
    /**--------------------------------------------------------------------------------------------*/

    private fun initStatusChart(labelList: ArrayList<String> = arrayListOf(
        "14일", "15일", "16일", "17일", "18일", "19일", "오늘"
    )){
        context ?: return

        with(bind){
            val xAxis = lcWeekStatusChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                isGranularityEnabled = true
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        val index = value.toInt() - 1
                        return if(index >= 0 && index < labelList.size){
                            labelList[index]
                        } else {
                            ""
                        }
                    }
                }

                // 하단 라벨 텍스트 설정
                typeface = Typeface.create(ResourcesCompat.getFont(requireContext(), R.font.pretendard_semibold), Typeface.NORMAL)
                textSize = 12f
                textColor = ContextCompat.getColor(requireContext(), R.color.gray3_82)

                // 세로 점선 설정 (그리드 라인)
                setDrawGridLines(true)
                enableGridDashedLine(15f, 10f, 0f)
                gridLineWidth = 1.5f
                gridColor = ContextCompat.getColor(requireContext(), R.color.gray6_F5)

                // 기타
                setDrawAxisLine(false)
                yOffset = 15f
            }

            val yLeftAxis = lcWeekStatusChart.axisLeft.apply {
                xOffset = 15f
                axisMaximum = 1440f
                axisMinimum = -200f // 최솟값이 0일때 Min을 0으로 설정하면 그래프가 얇아지는 현상 발생 (최소값에 여유를 두어 해결)

                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            lcWeekStatusChart.axisRight.isEnabled = false

            // 차트 범례
            val legend = lcWeekStatusChart.legend
            legend.isEnabled = false
        }

        initChartData()
    }

    private fun initChartData(
        entries: ArrayList<Entry> = arrayListOf(
            Entry(1f, 705f),
            Entry(2f, 1000f),
            Entry(3f, 0f),
            Entry(4f, 1201f),
            Entry(5f, 150f),
            Entry(6f, 0f),
            Entry(7f, 3f),
        )
    ) {
        context ?: return

        val lineDataSet = LineDataSet(entries, null).apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER // 곡선으로 설정
            color = ContextCompat.getColor(requireContext(), R.color.primary_normal_light)
            lineWidth = 2f

            setDrawValues(false) // 점마다 값 표시를 제거
            setDrawHighlightIndicators(false) // 눌렀을 때 하이라이트 제거

            /* 인디케이터 세팅 */
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.primary_normal_light))
            circleHoleColor = Color.WHITE
            circleRadius = 5.5f
            circleHoleRadius = 4f
        }

        val lineData = LineData(lineDataSet)

        with(bind.lcWeekStatusChart){
            data = lineData
            isDoubleTapToZoomEnabled = false
            description = Description().apply { text = "" }
            extraBottomOffset = 20f // 하단 x축 라벨 잘림 방지 여백
            resetViewPortOffsets()
        }
    }

    /**--------------------------------------------------------------------------------------------*/
    private fun addClickEvent(){
        bind.tvFamousDentalHospital.setOnClickListener {
            this.addFragmentToFullScreen(HospitalMapFragment.newInstance())
        }

        bind.tvFindNearByHosptial.setOnClickListener {
            this.addFragmentToFullScreen(HospitalMapFragment.newInstance())
        }

        bind.ivFindHospitalBanner.setOnClickListener {
            this.addFragmentToFullScreen(HospitalMapFragment.newInstance())
        }
    }

    private fun requestData(){
        homeVm.getMagazineList(5)
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
                    appBarBinding.setWearTimeMode(state.wearFlag)
                }
            }
        }
    }

    private suspend fun observeHomeState(){
        homeVm.state.collect { state ->
            when(state){
                is HomeFragmentState.Init -> Unit

                is HomeFragmentState.PartnerHospital -> {
                    handlePartnerHospital(state.hospitals)
                }
                is HomeFragmentState.RecommendVideo -> {
                    handleRecommendVideo(state.youtubeList)
                }

                is HomeFragmentState.RecentMagazine -> {
                    handleMagazine(state.magazine)
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

    private fun handleMagazine(magazines: ArrayList<Magazine>){
        bind.sflRecentMagazine.stop()

        val adapter = bind.rvRecentMagazine.adapter as RecentMagazineAdapter
        adapter.updateDataSet(magazines)
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

    private fun initBannerView(){
        context ?: return

        bind.vpBanner.apply {
            adapter = bannerAdapter
            // 코루틴으로 자동 스크롤 처리
            lifecycleScope.launch {
                var current = 0
                while (true) {
                    delay(3000)

                    this@apply.setCurrentItem((current++ % bannerAdapter.itemCount), true)
                }
            }
        }
    }

    private fun initMagazineListView(){
        context ?: return

        bind.rvRecentMagazine.apply {
            adapter = RecentMagazineAdapter(
                context,
                arrayListOf(),
                magazineClickListener
            )

            setHasFixedSize(true)
            addItemDecoration(SideSpaceDecoration(context, 10))
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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

    override fun onShowFragment() {
        super.onShowFragment()
        appBarBinding.setWearTimeMode()
    }

    private val magazineClickListener = object : OnItemClickListener<Magazine> {
        override fun onItemClicked(view: View, data: Magazine) {
            val magazineFragment = MagazineDetailFragment.newInstance(data.contentUrl)
            this@HomeFragment.addFragmentToFullScreen(magazineFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}