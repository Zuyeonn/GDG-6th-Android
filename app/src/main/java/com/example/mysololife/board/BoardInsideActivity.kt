package com.example.mysololife.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.comment.CommentLVAdapter
import com.example.mysololife.comment.CommentModel
import com.example.mysololife.databinding.ActivityBoardInsideBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName  // 로그찍기 편하게 TAG 이용

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {  // boardSettingIcon 클릭하면, showDialog 실행된다.
            showDialog()
        }

        // 첫번째 방법에서 데이터를 받는 부분!
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time


        // 두번째 방법
        val key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener { // commentBtn을 클릭하면
            insertComment(key)
        }
        getCommentData(key)

        // CommentLVAdapter 어댑터 초기화
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter // ListView에 어댑터 연결
    }


    fun getCommentData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 기존 댓글 리스트를 초기화
                commentDataList.clear()

                // 데이터베이스의 모든 요소 순회
                for (dataModel in dataSnapshot.children) {
                    // 데이터 스냅샷을 CommentModel객체로 변환 후 리스트에 저장
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                // 데이터를 받아온 뒤에 어댑터에 동기화
                commentAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key : String){
        // - comment
        //    - BoardKey 게시글 고유 키
        //        - CommentKey
        //            - CommentData 댓글 내용

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(
                binding.commentArea.text.toString(),
                FBAuth.getTime()
            )) //commentArea에 있는 텍스트와 시간을 바인딩 이용해서 집어넣어준다.

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("") // 댓글 작성하고 댓글 창 초기화
    }


    private fun showDialog() {  // 다이얼로그를 띄움

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")


        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {   // editBtn 버튼을 누르면
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener { // removeBtn 버튼을 누르면
            FBRef.boardRef.child(key).removeValue()

            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getImageData(key : String) {

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromRB = binding.getImageArea

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            // 만약에 task가 성공적이면
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromRB)

            } else {  // 성공적이지 않을 때
                binding.getImageArea.isVisible = false

            }
        })
    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {  // 데이터가 존재하는 경우

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()  // 현재 로그인한 사용자 UID
                    val writerUid = dataModel?.uid  // 글쓴이 UID

                    if(myUid.equals(writerUid)){
                        // 본인이 쓴 글인 경우
                        Log.d(TAG, "내가 쓴 글")
                        // 아이콘 표시
                        binding.boardSettingIcon.isVisible = true

                    } else {
                        // 본인이 쓴 글이 아닌 경우
                        Log.d(TAG, "내가 쓴 글 아님")
                    }

                } catch (e : Exception) {  // 데이터가 없을 경우
                    Log.d(TAG, "삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}