package com.example.smiley.main.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.bluetooth.fragment.BluetoothSearchFragment
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.setBasicMode
import com.example.smiley.common.listener.FragmentVisibilityListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.common.view.BaseFragment
import com.example.smiley.databinding.FragmentProfileBinding
import com.example.smiley.databinding.LayoutCommonAppBarBinding
import com.example.smiley.hospital.HospitalMapFragment
import com.example.smiley.hospital.HospitalSearchFragment
import com.example.smiley.magazine.MagazineListFragment
import com.example.smiley.medicine.MedicineSearchFragment


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment(), FragmentVisibilityListener {

    private var _bind:FragmentProfileBinding?=null
    private val bind:FragmentProfileBinding get() = _bind!!
    private val appBarBinding: LayoutCommonAppBarBinding by lazy { LayoutCommonAppBarBinding.bind(bind.clBaseLayout) }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        initView()
        addClickEventToMenus()

        return bind.root
    }

    override fun onStart() {
        super.onStart()
        onShowFragment()
    }

    private fun initView(){
        App.user?.let {
            bind.tvUserNicknameView.text = it.name
            bind.tvUserBirthDayView.text = it.birthDate
        }
    }
    /**
     * 프로필 화면의 각 메뉴에 클릭 이벤트를 지정하는 메소드
     */
    private fun addClickEventToMenus(){
        with(bind){
            menuMedicineExamine.setOnClickListener {
                this@ProfileFragment.addFragmentToFullScreen(MedicineSearchFragment())
            }

            menuDeviceSetting.setOnClickListener{
                this@ProfileFragment.addFragmentToFullScreen(BluetoothSearchFragment())
            }

            menuMagazine.setOnClickListener {
                this@ProfileFragment.addFragmentToFullScreen(MagazineListFragment.newInstance())
            }

            menuSearchHospital.setOnClickListener {
                this@ProfileFragment.addFragmentToFullScreen(HospitalSearchFragment())
            }

            menuNotifySetting.setOnClickListener {
                this@ProfileFragment.addFragmentToFullScreen(HospitalMapFragment())
            }
        }
    }

    override fun onShowFragment() {
        appBarBinding.setBasicMode()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}