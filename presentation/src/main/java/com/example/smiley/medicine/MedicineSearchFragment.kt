package com.example.smiley.medicine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.smiley.R
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.visible
import com.example.smiley.databinding.FragmentMedicineSearchBinding
import com.example.smiley.medicine.adapter.MedicineFilterAdapter
import com.example.smiley.medicine.adapter.MedicineSelectAdapter
import com.example.smiley.medicine.viewmodel.MedicineViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicineSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicineSearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind: FragmentMedicineSearchBinding
    private lateinit var medicineFilterAdapter: MedicineFilterAdapter
    private lateinit var medicineSelectAdapter: MedicineSelectAdapter
    private val medicineVm:MedicineViewModel by viewModels()

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_search, container, false)

        initRecyclerView()
        initSearchEditText()

        return bind.root
    }

    /**
     * 리사이클러뷰 초기화
     */
    private fun initRecyclerView(){
        medicineFilterAdapter = MedicineFilterAdapter(medicineVm.getMedicineList()).apply {
            setOnItemClickListener(medicineItemClickListener)
        }

        medicineSelectAdapter = MedicineSelectAdapter(arrayListOf()).apply {
            registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()

                    if(itemCount == 0) bind.selectBtn.gone()
                    else bind.selectBtn.visible()
                }
            })
        }

        bind.searchResultView.apply {
            adapter = medicineFilterAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }


        bind.selectedView.apply {
            adapter = medicineSelectAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    /**
     * 의약품 검색 EditText 초기화
     */
    private fun initSearchEditText(){
        bind.searchEditText.addTextChangedListener(filterTextWatcher)
    }

    /**
     * 검색어 입력 체크 TextWatcher
     */
    private val filterTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            medicineFilterAdapter.filter.filter(p0)
        }
    }

    /**
     * 검색 결과 약품 클릭 이벤트 리스너
     */
    private val medicineItemClickListener = object : MedicineFilterAdapter.OnItemClickListener {
        override fun onItemClicked(position: Int, data: String) {
            medicineSelectAdapter.apply {
                addMedicine(data)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicinSearchFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicineSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}