package com.example.smiley.main.stats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.common.base.NetworkError
import com.example.domain.stats.model.Exp
import com.example.domain.stats.model.Stats
import com.example.smiley.R
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.databinding.FragmentStatsBinding
import com.example.smiley.main.stats.adapter.CalendarAdapter
import com.example.smiley.main.stats.adapter.ExpGridAdapter
import com.example.smiley.main.stats.adapter.ExpListAdapter
import com.example.smiley.main.stats.viewmodel.StatsFragmentState
import com.example.smiley.main.stats.viewmodel.StatsViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.abs


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StatsFragment : Fragment() {

    /* 캘린더 관련 변수 (리팩토링 필요) */
    class CalendarDate(var day: String, var date: LocalDate)

    val itemList = arrayListOf<CalendarDate>()
    private val calendarAdapter by lazy {
        CalendarAdapter(itemList)
    }
    var selectorPosition:Int = itemList.size - 1

    private var _bind: FragmentStatsBinding? = null
    private val bind:FragmentStatsBinding get() = _bind!!
    private val statsVm: StatsViewModel by viewModels()
    private val pieChartColors by lazy {
        arrayListOf(
            resources.getColor(R.color.primary_normal),
            resources.getColor(R.color.primary_dark),
            resources.getColor(R.color.purple1_5C),
            resources.getColor(R.color.mint),
            resources.getColor(R.color.purple2_BE),
        )
    }

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        observe()
        statsVm.requestTodayStats()
        initSeekBar()

        initCalendarView()
        calendarEventAdder()

        return bind.root
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                statsVm.state.collect{ state ->
                    when(state){
                        is StatsFragmentState.Init -> Unit
                        is StatsFragmentState.IsLoading -> Unit
                        is StatsFragmentState.SuccessStats -> {
                            handleSuccess(state.stats)
                        }
                        is StatsFragmentState.Error -> {
                            handleError(state.error)
                        }
                        is StatsFragmentState.ShowToast -> Unit
                    }
                }
            }
        }
    }

    private fun handleSuccess(stats:Stats){
        with(bind){
            tvTotalExp.text = "+${DecimalFormat("#,###").format(stats.totalExp)} exp"
        }

        initMission(stats)
        initExpPieChart(stats.exp)
        initExpGridView(stats.exp)
        initExpListView(stats.exp)
    }

    private fun handleError(error:NetworkError){
        requireActivity().showConfirmDialog(
            title = "착용 통계 조회 오류",
            content = error.message,
            subContent = "(code: ${error.code}, ${error.error})",
        )
    }

    private fun initExpListView(exp:List<Exp>){
        with(bind.expListView){
            adapter = ExpListAdapter(exp)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initMission(stats: Stats){
        with(bind){
            tvDailyTime.text = "${stats.wearTime / 60}H ${stats.wearTime % 60}m"
            dailyTimeSeekbar.setProgress(
                stats.wearTime,
                true
            )

            tvTargetTime.text = "${stats.wearTime / 60}H ${stats.wearTime % 60}m"
            targetTimeSeekbar.setProgress(
                stats.wearTime,
                true
            )
            tvMagazineCount.text = "${stats.readMagazineCnt}개 읽음"
            magazineCountSeekbar.setProgress(
                stats.readMagazineCnt,
                true
            )
        }
    }

    private fun initExpPieChart(exp:List<Exp>){
        // 경험치 데이터 세팅
        val expList = mutableListOf<PieEntry>()
        exp.forEach {
            expList.add(PieEntry(it.exp.toFloat(), 0f))
        }

        val dataSet = PieDataSet(expList, "").apply {
            this.colors = pieChartColors
            this.setDrawValues(false)
            this.setDrawIcons(false)
        }

        val pieData = PieData(dataSet)

        with(bind.expPieChart){
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            holeRadius = 75f
            animateXY(500, 500)
            invalidate()
        }
    }

    private fun initExpGridView(exp:List<Exp>){
        val adapter = ExpGridAdapter(
            requireActivity(),
            exp,
            pieChartColors
        )

        bind.expGridView.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSeekBar(){
        with(bind){
            targetTimeSeekbar.setOnTouchListener { _, _ -> true }
            dailyTimeSeekbar.setOnTouchListener { _, _ -> true }
            magazineCountSeekbar.setOnTouchListener { _, _ -> true }
        }
    }


    /**
     * WeekCalendarView 초기화 메소드
     */
    private fun initCalendarView(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        bind.calendarView.layoutManager = layoutManager

        // 현재 달의 마지막 날짜
        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
        lastDayOfMonth.format(DateTimeFormatter.ofPattern("dd"))

        val today = LocalDate.now()

        /* NEED FIX 교정 시작일 부터 현재까지로 바꿔야함 */
        for (i in 1..today.monthValue) { // 1월부터 12월 까지
            for (j in 1..YearMonth.of(today.year, i).lengthOfMonth()) { // 해당 년도, 해당 월의 마지막 달까지 반복
                val localDate = LocalDate.of(today.year, i, j)
                val dayOfWeek: DayOfWeek = localDate.dayOfWeek // MONDAY, TUESDAY 같은 요일의 이름을 가져옴

                itemList.add(
                    CalendarDate(
                        dayOfWeek.toString().substring(0, 1), // 요일
                        localDate // 날짜 YYYY-MM-DD
                    )
                )
            }
        }

        // CalendarView Item 클릭 이벤트 리스너 등록
        calendarAdapter.onItemClickListener = object : CalendarAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                val item:CalendarDate = itemList[position]

                bind.selector.x = view.x + (view.paddingLeft / 2)

                // selectorPosition은 완전히 보이는 아이템의 Position으로부터 셀렉터가 얼만큼 떨어져있는지를 저장해둠
                selectorPosition = position - (bind.calendarView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                bind.missionToday.text = "${item.date.year}-${item.date.monthValue}-${item.date.dayOfMonth}"

                statsVm.requestTodayStats()
            }
        }

        bind.calendarView.adapter = calendarAdapter
        bind.calendarView.scrollToPosition(itemList.size - 1)
    }


    /**
     * 캘린더 리사이클러뷰 이벤트 등록 메소드
     * */
    private fun calendarEventAdder(){
        bind.calendarView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            @SuppressLint("SetTextI18n")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    bind.calendarView.post{ autoScroll() }

                    // 완전히 보이는 아이템의 Position + SelectorPosition
                    val item = itemList[
                            (bind.calendarView.layoutManager as LinearLayoutManager)
                                .findFirstCompletelyVisibleItemPosition() + selectorPosition
                    ]
                    bind.missionToday.text = "${item.date.year}-${item.date.monthValue}-${item.date.dayOfMonth}"
                    statsVm.requestTodayStats()
                }
            }
        })
    }

    private fun autoScroll() {
        lateinit var date: LinearLayout
        val calendar = bind.calendarView

        val xy = IntArray(2)
        var gap: Int
        var position: Int
        var minimumGap = -1

        for (i in 0 until calendar.childCount) {
            date = calendar.getChildAt(i) as LinearLayout // 리사이클러뷰 안의 날짜를 하나씩 가져옴

            date.getLocationInWindow(xy) // 해당 날짜의 절대 좌표 값

            position = xy[0] + (date.width) / 4  // (프레임의 넓이 + 날짜 뷰의 넓이) / 2 -> 중간으로 설정 됨 + x 좌표
            gap = position - calendar.width

            // 가장 가까운 그래프까지의 거리 차이를 저장
            if (minimumGap == -1 || abs(gap) < abs(minimumGap)) {
                minimumGap = gap
            }
        }

        calendar.smoothScrollBy(minimumGap, 0) // minimumGap 만큼 이동
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatsFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}