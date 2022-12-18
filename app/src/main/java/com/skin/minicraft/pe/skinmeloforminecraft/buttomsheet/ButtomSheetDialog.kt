package com.skin.minicraft.pe.skinmeloforminecraft.buttomsheet

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skin.minicraft.pe.skinmeloforminecraft.R
import com.skin.minicraft.pe.skinmeloforminecraft.databinding.FragmentButtomSheetDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ButtomSheetDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentButtomSheetDialogBinding
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

//        val layoutParams = binding.bottomSheet.layoutParams
//        layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
//        binding.bottomSheet.layoutParams = layoutParams

        mBottomSheetBehavior?.skipCollapsed = true
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentButtomSheetDialogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ButtomSheetDialog().apply {}

        var TAG = ButtomSheetDialog::class.java.simpleName
    }

    /**
     * set bottom sheet behavior and state
     */
    private fun setBottomSheetAndCallBackBottomSheetBehaviour() {


        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        //callback
        mBottomSheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetCollapsed()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }



    fun isExpendCollapse(){
        if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetExpand()
        } else {
            bottomSheetCollapsed()
        }
    }

    private fun bottomSheetExpand() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun bottomSheetCollapsed() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }



}