package com.example.mysololife.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef

class ContentRVAdapter(val context : Context,
                       val items : ArrayList<ContentModel>,
                       val keyList : ArrayList<String>,
                       val bookmarkIdList : MutableList<String>)
    : RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {



    // 전체 아이템을 가져와서 이쪽에서 하나씩 넣어줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)

        Log.d("ContentRVAdapter", keyList.toString())
        Log.d("ContentRVAdapter", bookmarkIdList.toString())

        return Viewholder(v)
    }

    // 아이템에 있는 내용물들을 Viewholder class에 넣을 수 있게 연결해줌
    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {
        holder.bindItem(items[position], keyList[position])
    }

    // 전체 아이템 개수가 몇 개인지
    override fun getItemCount(): Int {
        return items.size
    }

    // content_rv_item.xml 파일에 만들어놓은 아이템에 데이터를 받아와서 넣어주는 역할
    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item : ContentModel, key : String) {

            itemView.setOnClickListener {   // 아이템 클릭하면
                Toast.makeText(context, item.title, Toast.LENGTH_LONG).show() // 각각의 타이틀 메시지 뜨도록
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent) // ContentShowActivity으로 이동
            }

            val contenttitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if(bookmarkIdList.contains(key)) { // 북마크에 대한 정보를 가지고 있냐 (있을 때)
                bookmarkArea.setImageResource(R.drawable.bookmark_color) // 가지고 있으면 black
            } else { // 북마크에 대한 정보 없을 때
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            bookmarkArea.setOnClickListener {   // 북마크를 클릭하면
                Log.d("ContentRVAdpter", FBAuth.getUid())
                Toast.makeText(context, key, Toast.LENGTH_LONG).show()

                //FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue("Good")

                if(bookmarkIdList.contains(key)){
                    // 북마크가 있을 때
                    //bookmarkIdList.remove(key) // 북마크 동적으로 삭제
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .removeValue()

                } else {
                    // 북마크가 없을 때
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .setValue(BookmarkModel(true))   // FBAuth.getUid() 안에 키값(contentid)를 집어넣는다
                }

            }

            contenttitle.text = item.title
            Glide.with(context)
                .load(item.imageUrl) // 이미지 url 정보가 아이템에 있다
                .into(imageViewArea) // 이미지 url을 imageViewArea에 집어넣는다
        }
    }
}