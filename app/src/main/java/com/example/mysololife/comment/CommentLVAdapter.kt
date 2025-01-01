package com.example.mysololife.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mysololife.R

class CommentLVAdapter(val commentList: MutableList<CommentModel>) : BaseAdapter() {

    // 댓글 목록의 크기 반환
    override fun getCount(): Int {
        return commentList.size
    }

    // 지정된 위치의 댓글 데이터를 반환
    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    // 각 항목의 고유 ID를 반환 (여기서는 위치를 ID로 사용)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 리스트뷰의 각 항목에 데이터를 바인딩
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        // 댓글 제목을 텍스트뷰에 설정
        val title = view?.findViewById<TextView>(R.id.titleArea)
        title!!.text = commentList[position].commentTitle

        // 댓글 작성 시간을 텍스트뷰에 설정
        val time = view?.findViewById<TextView>(R.id.timeArea)
        time!!.text = commentList[position].commentCreatedTime

        // 완성된 뷰 반환
        return view!!
    }
}