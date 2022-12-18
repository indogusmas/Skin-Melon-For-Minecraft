package com.skin.minicraft.pe.skinmeloforminecraft

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.skin.minicraft.pe.skinmeloforminecraft.base.BaseActivity
import com.skin.minicraft.pe.skinmeloforminecraft.buttomsheet.BottomSheetFullFragment
import com.skin.minicraft.pe.skinmeloforminecraft.databinding.ActivityShowButtomSheetMainBinding


class ShowButtomSheetMainActivity : BaseActivity(), AdapterItem.OnClikcListener,BottomSheetFullFragment.OnShowAds {

    private lateinit var binding: ActivityShowButtomSheetMainBinding
    private var actionBarHeight: Int = 0
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapterItem: AdapterItem

    companion object{
        var TAG = ShowButtomSheetMainActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowButtomSheetMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLicense()
        mainViewModel = MainViewModel()
        val id = getString(R.string.craft_id)
        val export = getString(R.string.export)
        mainViewModel.setItem(id,export)
        adapterItem = AdapterItem(java.util.ArrayList<ItemSkin>(),this)
        binding.rv.adapter = adapterItem
        binding.rv.layoutManager = GridLayoutManager(this,3)
        binding.rv.hasFixedSize()

        actionBarHeight = with(TypedValue().also {this.theme.resolveAttribute(android.R.attr.actionBarSize, it, true)}) {
            TypedValue.complexToDimensionPixelSize(this.data, resources.displayMetrics)
        }
        initObServer()

        binding.swipe.setOnRefreshListener {
            Handler().postDelayed(Runnable {
                mainViewModel.setItem(id,export)
            },2000)
        }
    }

    private fun initObServer() {
        mainViewModel.item.observe(this, Observer {
            when(it.baseResponseStatus){
                BaseResource.BaseResponseStatus.STATUS_1_SUCCESS -> loadItem(it.data)
                BaseResource.BaseResponseStatus.STATUS_2_ERROR -> error(it.message)
                BaseResource.BaseResponseStatus.STATUS_3_LOADING -> loadingState()
            }
        })
    }
    fun  loadItem(item : List<ItemSkin>){
       binding.swipe.isRefreshing = false
        adapterItem.setItems(item)
    }

    fun loadingState(){
       binding.swipe.isRefreshing = true
    }
    fun error(message : String){
       binding.swipe.isRefreshing = false
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun onClickItem(item: ItemSkin?) {
        showAds()
        val dialog = BottomSheetFullFragment().newInstance(actionBarHeight,item)
            dialog.show(supportFragmentManager, BottomSheetFullFragment.TAG);
    }

    override fun callAds() {
        showAds()
    }


}