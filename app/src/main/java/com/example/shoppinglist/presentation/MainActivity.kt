package com.example.shoppinglist.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.presentation.adapter.ShoppListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var shopListAdapter: ShoppListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.shopLists = it
        }
        binding.buttonAddShopItem.setOnClickListener {
            val intent = ShopItemActivity.moveAdd(this)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        shopListAdapter = ShoppListAdapter()
        binding.apply {
            with(recyclerview) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = shopListAdapter
                recycledViewPool.setMaxRecycledViews(
                    ShoppListAdapter.VIEW_TYPE_ENABLED,
                    ShoppListAdapter.MAX_POOL_SIZE
                )
                recycledViewPool.setMaxRecycledViews(
                    ShoppListAdapter.VIEW_TYPE_DISABLED,
                    ShoppListAdapter.MAX_POOL_SIZE
                )
            }
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.recyclerview)
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.shopLists[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            val intent = ShopItemActivity.modeEdit(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
}

