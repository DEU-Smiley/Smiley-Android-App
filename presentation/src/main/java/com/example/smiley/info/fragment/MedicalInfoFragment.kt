package com.example.smiley.info.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.common.extension.showViewThenCheckedChanged
import com.example.smiley.common.extension.showViewThenEnterPressed
import com.example.smiley.databinding.FragmentMedicalInfoBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind: FragmentMedicalInfoBinding

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_medical_info, container, false)

        addAnswerClickEvent()
        addKeyPressEventToEditText()

        return bind.root
    }

    private fun addAnswerClickEvent(){
        bind.apply {
            answer1Radio.showViewThenCheckedChanged(question2Layout, null)
            answer2Radio.showViewThenCheckedChanged(answer2SubQuestion, question3Layout)
            answer3Radio.showViewThenCheckedChanged(answer3SubQuestion, question4Layout)
        }
    }

    private fun addKeyPressEventToEditText() {
        bind.apply {
            answer2SubEditText.showViewThenEnterPressed(question3Layout, scrollView)
            answer3SubEditText.showViewThenEnterPressed(question4Layout, scrollView)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicalInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicalInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}