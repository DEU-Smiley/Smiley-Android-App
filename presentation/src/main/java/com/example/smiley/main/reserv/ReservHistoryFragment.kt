package com.example.smiley.main.reserv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.hospital.model.Hospital
import com.example.domain.reservation.model.Reserv
import com.example.domain.reservation.model.ReservList
import com.example.smiley.R
import com.example.smiley.databinding.FragmentReservHistoryBinding
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservHistoryFragment : Fragment() {
    private var _bind: FragmentReservHistoryBinding?=null
    private val bind:FragmentReservHistoryBinding get() = _bind!!

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv_history, container, false)

        initRecylcerView()

        return bind.root
    }

    private fun initRecylcerView(){
        /** 테스트 데이터 */
        val reservs = arrayListOf<Reserv>()
        repeat(30){
            reservs.add(
                Reserv(
                    id = 1,
                    LocalDateTime.of(2023, 5, (Math.random()*29+1).toInt(), (Math.random()*23+1).toInt(), 30),
                    "메모",
                    hospitalName = "개금다나아내과의원",
                    hospitalAddress = "부산시 부산진구 가야대로"
                )
            )
        }

        val comparator = compareBy<Reserv> { it.reservDate }
        reservs.sortWith(comparator)
        reservs.reverse()

        bind.reservHistoryView.apply {
            adapter = ReservHistoryAdapter(ReservList(reservs))

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
         * @return A new instance of fragment ReservHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}