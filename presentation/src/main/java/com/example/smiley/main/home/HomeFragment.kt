package com.example.smiley.main.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.magazine.model.Magazine
import com.example.smiley.R
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.FragmentHomeBinding
import com.example.smiley.main.adapter.TimeLineAdapter
import com.example.smiley.main.adapter.TimeLineItem
import com.example.smiley.main.adapter.ViewObject
import com.example.smiley.main.adapter.ViewType
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var _bind: FragmentHomeBinding?=null
    private val bind:FragmentHomeBinding get() = _bind!!

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

        initTimeLineView()

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTouchEffectToAllViews(view as ViewGroup)
    }

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

    private fun initTimeLineView(){
        val items = arrayListOf<TimeLineItem>()

        items.apply {
            add(
                TimeLineItem(
                    ViewType.MAGAZINE_OBJECT.name,
                    ViewObject.MagazineObject(
                        "스마일리 매거진이 드리는 교정 정보입니다.",
                        Magazine(
                            id = 1,
                            author = "test",
                            title = "치아 교정 시,\n주의해야 할 사항은?",
                            subTitle = "치아 교정시 주의사항",
                            thumbnail = ByteArrayOutputStream().run {
                                val bitmap =
                                    (resources.getDrawable(R.drawable.mock_magazine_thumb_1) as BitmapDrawable).bitmap
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                                toByteArray()
                            },
                            likes = 923,
                            viewCount = 1023,
                            mainContent = null
                        )
                    )
                )
            )

            add(
                TimeLineItem(
                    ViewType.TEXT_OBJECT.name,
                    ViewObject.TextObject(
                        "오후에 매거진을 2개 읽었습니다."
                    )
                )
            )
            add(
                TimeLineItem(
                    ViewType.MAGAZINE_OBJECT.name,
                    ViewObject.MagazineObject(
                        "앉아서 5분, 예쁜 미소를 유지시켜주는 차이 관리 방법",
                        Magazine(
                            id = 1,
                            author = "test",
                            title = "예쁜 미소를 유지 시켜주는\n치아 관리 방법!",
                            subTitle = "치아 관리 방법",
                            thumbnail = ByteArrayOutputStream().run {
                                val bitmap =
                                    (resources.getDrawable(R.drawable.mock_magazine_thumb_2) as BitmapDrawable).bitmap
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                                toByteArray()
                            },
                            likes = 923,
                            viewCount = 1023,
                            mainContent = null
                        )
                    )
                )
            )
        }

        bind.timelineView.apply {
            adapter = TimeLineAdapter(items)
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
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