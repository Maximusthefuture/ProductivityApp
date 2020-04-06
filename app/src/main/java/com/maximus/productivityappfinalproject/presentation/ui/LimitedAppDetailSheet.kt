package com.maximus.productivityappfinalproject.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.maximus.productivityappfinalproject.R
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_app_details.view.*

class LimitedAppDetailSheet: BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.bottom_sheet, container, false)
        view.limit_in_hour.setOnClickListener {
            Toast.makeText(requireContext(), "THIS IS CHIP", Toast.LENGTH_LONG).show()
        }
        return view
    }

    companion object {
        const val TAG = "BottomSheet"
    }






}