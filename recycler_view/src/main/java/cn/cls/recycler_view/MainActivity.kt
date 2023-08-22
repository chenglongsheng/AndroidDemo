package cn.cls.recycler_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {

    private val data: MutableList<Bean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 1..100) {
            data.add(Bean("Bean${i}"))
        }
        val adapter = BeanAdapter(data, this)
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = adapter

        // 设置布局
//        val layoutManager = LinearLayoutManager(this)
//        val layoutManager = GridLayoutManager(this, 2)
        // 瀑布流
        val layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        recyclerView.layoutManager = layoutManager

        adapter.setOnRecyclerItemClickListener(object : BeanAdapter.OnRecyclerItemClickListener {
            override fun onRecyclerItemClick(position: Int) {
                Log.e("cls", "onRecyclerItemClick: $position")
            }
        })

    }
}