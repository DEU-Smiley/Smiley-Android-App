package com.example.smiley.magazine.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.magazine.model.Magazine
import com.example.domain.magazine.model.MagazineList
import com.example.smiley.R
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubMagazineItemBinding

class MagazineListAdapter(
    private var magazineList: MagazineList,
    private val listener: OnItemClickListener<Magazine>? = null
): RecyclerView.Adapter<MagazineListAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: SubMagazineItemBinding): RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_magazine_item, parent, false)

        return ViewHolder(SubMagazineItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val magazine = magazineList.magazines[position]

        with(holder.bind){
            tvMagazineTitle.text = magazine.title
            tvSubTitle.text= magazine.subTitle
            tvAuthor.text = magazine.author
            tvViewCnt.text = "${magazine.viewCount}"
            tvLikeCnt.text = "${magazine.likes}"
            ivMagazineThumb.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    magazine.thumbnail, 0, magazine.thumbnail.size
                )
            )
        }

        // 터치 & 클릭 효과 적용
        with(holder.bind.cvContainer){
            setOnTouchListener(TransparentTouchListener())
            if(listener != null) {
                setOnClickListener {
                    listener.onItemClicked(
                        view = it,
                        data = magazine
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = magazineList.magazines.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataSet(magazineList: MagazineList){
        this.magazineList = magazineList
        notifyDataSetChanged()
    }
}