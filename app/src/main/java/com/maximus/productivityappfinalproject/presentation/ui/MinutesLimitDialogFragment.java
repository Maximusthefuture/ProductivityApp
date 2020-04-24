package com.maximus.productivityappfinalproject.presentation.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl;

public class MinutesLimitDialogFragment extends DialogFragment {

    private NumberPicker mNumberPicker;
    private MinutesLimitDialogListener mListener;
    private SharedPrefManagerImpl mSharedPrefManager;


    public interface MinutesLimitDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, NumberPicker numberPicker);
        void onDialogNegativeClick(DialogFragment dialog, NumberPicker numberPicker);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mSharedPrefManager = new SharedPrefManagerImpl(getContext());

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.number_picker, null);
                mNumberPicker = dialogView.findViewById(R.id.number_picker);
                mNumberPicker.setMinValue(0);
                mNumberPicker.setMaxValue(59);


        builder.setMessage(R.string.set_hourly_limit)


                .setPositiveButton(R.string.positive_button_title, (dialog, which) -> {
                    mListener.onDialogPositiveClick(MinutesLimitDialogFragment.this, mNumberPicker);
//                    mNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
//                        Toast.makeText(requireContext(), newVal, Toast.LENGTH_SHORT).show();
//                    });
                    mSharedPrefManager.setTimeLimitChanged(true);
                })
                .setNegativeButton(R.string.negative_button_title, (dialog, which) ->
                        mListener.onDialogNegativeClick(MinutesLimitDialogFragment.this, mNumberPicker))
                .setView(dialogView);
        return builder.create();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (MinutesLimitDialogListener) getParentFragment();
        } catch (ClassCastException ex) {
            throw new ClassCastException( " must implement NoticeDialogListener");
        }
    }
}
