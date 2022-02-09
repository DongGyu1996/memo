package com.example.memo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_memo.view.*
import kotlin.math.log

//어댑터 생성
class MyAdapter(val context : Context,
                var list : List<MemoEntity>,
                var onDeleteListener: OnDeleteListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    //어댑터를 생성할 땐 3가지 필요
    //아이템의 총 개수 반환
    override fun getItemCount(): Int {
        return list.size

    }

    //1. view가 없는 경우 inflate 하여 뷰 홀더 생성?
    // 공부해야겠다 뭔소린건지..
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false)
        return MyViewHolder(itemView)
    }
    //레이아웃과 뷰를 이어주는 것..
    // 공부한다.. 어렵다..
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //list = 1, 2, 3...
        val memo = list[position]
        holder.memo.text = memo.memo
        holder.root.setOnLongClickListener(object : View.OnLongClickListener{
            //길게 클릭 시 삭제 리스너
            override fun onLongClick(p0: View?): Boolean {
                onDeleteListener.onDeleteListener(memo)
                return true
            }
        })
    }
    // 이너 내부 클래스는 외부클래스의 멤버 참조 가능
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val memo = itemView.textview_memo //텍스트값
        val root = itemView.root //constraint 뷰
    }

}