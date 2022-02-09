package com.example.memo

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(), OnDeleteListener {

    lateinit var db : MemoDatabase //늦은 초기화 변수
    var memoList : List<MemoEntity> = listOf<MemoEntity>() //리스트 콜렉션<> = listof<>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // db 변수 설정 MemoDatabase 클래스 내 getInstance 클래스 함수 저장
        db = MemoDatabase.getInstance(this)!!
        //버튼 클릭 시 이벤트 발생
        button_add.setOnClickListener {
            // memo 변수에 MemoEntity 값 전송
            val memo = MemoEntity(null, edittext_memo.text.toString())
            // 초기값은 공백으로
            edittext_memo.setText("")
            // insertMemo 함수에 값 전송
            insertMemo(memo)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        //삭제
        getAllMemos()
    }

    //1. Insert Data
    //2. Get Data
    //3. Delete Data

    //메모 삽입
    fun insertMemo(memo : MemoEntity){
        //1. MainThread vs WorkerThread(Background Thread)
        //asyncTask 객체 생성
        val insertTask = object : AsyncTask<Unit, Unit, Unit>(){
            // AsyncTask 사용시 선언해야하는 것
            // doInBackground , onPostExecute
            // 여기는 실체 처리되어야할 코드부분이며 백그라운드로 실행됨.
            // AsyncTask는 비동기 처리를 할 수 있도록 스레드와 핸들러 기능을 하나의 클래스에 합쳐놓은 것.
            override fun doInBackground(vararg p0: Unit?) {
                //db 변수에 DAO 인터페이스에 insert
                db.memoDAO().insert(memo)
            }
            //잘 처리 되면 onPostExecute 실행
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        insertTask.execute()
    }

    fun getAllMemos(){
        val getTask = (object : AsyncTask<Unit,Unit,Unit>(){
            // 쿼리 실행?
            override fun doInBackground(vararg p0: Unit?) {
                memoList = db.memoDAO().getAll()
            }
            // 정상 작동 한다면, setRecyclerView
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }).execute()

        //getTask.execute()
    }

    // 삭제 기능
    fun deleteMemo(memo: MemoEntity){
        val deleteTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        deleteTask.execute()
    }
    // 리사이클러뷰 사용하기 위해선 어댑터 필요
    fun setRecyclerView(memoList : List<MemoEntity>){
        //id.adapter
        recyclerView.adapter = MyAdapter(this, memoList,this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}