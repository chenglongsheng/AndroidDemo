package cn.cls.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private val data = mutableListOf<Bean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        val listView: ListView = findViewById(R.id.lv)
        val beanAdapter = BeanAdapter(data, this)
        listView.adapter = beanAdapter
        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id -> Log.e("cls", "onItemClick: 点击了$position") }
    }

    private fun setData() {
        for (i in 1..100) {
            data.add(Bean("Bean${i}"))
        }

    }
}