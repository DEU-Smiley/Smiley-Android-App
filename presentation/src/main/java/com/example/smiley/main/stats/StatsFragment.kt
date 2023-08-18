package com.example.smiley.main.stats

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.common.base.NetworkError
import com.example.domain.stats.model.Exp
import com.example.domain.stats.model.Stats
import com.example.smiley.R
import com.example.smiley.common.extension.getNumberOfWeeks
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.view.BaseFragment
import com.example.smiley.databinding.FragmentStatsBinding
import com.example.smiley.databinding.LayoutStatsExpBinding
import com.example.smiley.databinding.LayoutStatsMissionBinding
import com.example.smiley.main.stats.adapter.ExpGridAdapter
import com.example.smiley.main.stats.adapter.ExpListAdapter
import com.example.smiley.main.stats.viewmodel.StatsFragmentState
import com.example.smiley.main.stats.viewmodel.StatsViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekScrollListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StatsFragment : BaseFragment() {

    private var _bind: FragmentStatsBinding? = null
    private val bind:FragmentStatsBinding get() = _bind!!
    private val missionBind: LayoutStatsMissionBinding by lazy { LayoutStatsMissionBinding.bind(bind.root) }
    private val expBind: LayoutStatsExpBinding by lazy { LayoutStatsExpBinding.bind(bind.root) }

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

    private var selectedDate:LocalDate? = LocalDate.now()
    private var prevContainer: WeekDayViewContainer? = null
    private val today = LocalDate.now()

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
        statsVm.requestStatToDate(today)

        initSeekBar()
        initCalendarView()

        return bind.root
    }

    private fun observe(){
        repeatOnStarted {
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

    private fun handleSuccess(stats:Stats){
        with(expBind){
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
        with(expBind.expListView){
            adapter = ExpListAdapter(exp)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initMission(stats: Stats){
        with(missionBind){
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

        with(expBind.expPieChart){
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

        expBind.expGridView.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSeekBar(){
        with(missionBind){
            targetTimeSeekbar.setOnTouchListener { _, _ -> true }
            dailyTimeSeekbar.setOnTouchListener { _, _ -> true }
            magazineCountSeekbar.setOnTouchListener { _, _ -> true }
        }
    }

    private fun initCalendarView(){
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(240)
        val endMonth = currentMonth.plusMonths(240)
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        initCalendarTitle()
        initWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek(firstDayOfWeek))
        initMonthCalendar(startMonth,endMonth, currentMonth, daysOfWeek(firstDayOfWeek))
    }

    private fun initCalendarTitle(){
        bind.includeCalendarTitleLayout.root.children
            .map { it as TextView }
            .forEachIndexed { idx, textView ->
                val dayOfWeek = daysOfWeek()[idx]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }
    }

    private fun initWeekCalendar(startMonth: YearMonth, endMonth: YearMonth, currentMonth: YearMonth, daysOfWeek: List<DayOfWeek>){
        with(bind.clWeekCalendar){
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

            weekScrollListener = mWeekScrollListener
            setup(
                startMonth.atStartOfMonth(),
                endMonth.atEndOfMonth(),
                daysOfWeek.first()
            )
            scrollToWeek(currentMonth.atDay(today.dayOfMonth))
        }
    }

    private fun initMonthCalendar(startMonth: YearMonth, endMonth: YearMonth, currentMonth: YearMonth, daysOfWeek: List<DayOfWeek>){
        with(bind.cvMonthCalendar){
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                // 새 컨테이너가 필요할 때만 호출
                override fun create(view: View): DayViewContainer = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.apply {
                        this.day = day
                        this.textView.text = "${day.date.dayOfMonth}"
                        this.textView.alpha = if (this.day.position == DayPosition.MonthDate) 1.0f else 0.6f
                        setEnabled(day.position == DayPosition.MonthDate)
                    }

                    // 해당 달에 포함된 주 수에 따라 날짜 셀 height 조절
                    val weeksInMonth = getNumberOfWeeks(day.date.year, day.date.monthValue)
                    val cellHeight = bind.cvMonthCalendar.height / weeksInMonth
                    val dayLayout = container.view.findViewById<ConstraintLayout>(R.id.cl_day_cell_layout)
                    dayLayout.updateLayoutParams {
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            cellHeight
                        )
                    }
                }
            }

            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }
    }

    private fun bindDate(date: LocalDate, container: WeekDayViewContainer, isSelectable: Boolean){
        with(container) {
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            tvDay.text = "${date.dayOfMonth}"
            tvDayOfWeek.text = dayName

            when {
                !isSelectable -> setEnabled(false)
                date == selectedDate -> {
                    prevContainer?.setEnabled(false)
                    prevContainer = this
                    setEnabled(true)
                }
                date == today -> {
                    prevContainer = this
                    setEnabled(true)
                }
                else -> setEnabled(false)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dateClicked(date: LocalDate) {
        statsVm.requestStatToDate(date)
        bind.tvYearMonth.text = String.format(
            resources.getString(R.string.date_year_month_kor),
            date.year,
            date.monthValue
        )

        selectedDate = date

        bind.cvMonthCalendar.notifyDateChanged(date)
        bind.clWeekCalendar.notifyDateChanged(date)
    }

    private val mWeekScrollListener = object : WeekScrollListener {
        override fun invoke(week: Week) {
            val firstDay = week.days[0].date

            bind.tvYearMonth.text = String.format(
                resources.getString(R.string.date_year_month_kor),
                firstDay.year,
                firstDay.monthValue
            )
        }
    }

    inner class WeekDayViewContainer(view: View): ViewContainer(view){
        lateinit var day: WeekDay
        val tvDayOfWeek: TextView = view.findViewById(R.id.tv_day_of_week)
        val tvDay: TextView = view.findViewById(R.id.tv_day)

        init {
            view.setOnClickListener {
                if(day.position == WeekDayPosition.RangeDate){
                    dateClicked(day.date)
                }
            }
        }

        fun setEnabled(isEnabled: Boolean){
            if(isEnabled){
                this.tvDayOfWeek.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black1_20))
                this.tvDay.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black1_20))
            } else {
                this.tvDayOfWeek.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray5_CB))
                this.tvDay.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray5_CB))
            }
        }
    }

    inner class DayViewContainer(view: View): ViewContainer(view){
        lateinit var day: CalendarDay
        val textView: TextView = view.findViewById(R.id.tv_day)

        init {
            view.setOnClickListener {
                Log.d("StatsFragment", "$day")
            }
        }

        fun setEnabled(isEnabled: Boolean){
            if(isEnabled) textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black1_20))
            else textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray5_CB))
        }
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