package com.morr.morrassignment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.morr.morrassignment.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Setting up dataBinding and viewModel
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel

        setUpListeners()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUpListeners() {

        val expiry = binding.expiry.editText!!
        //A watcher to auto-format expiry
        expiry.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = expiry.text
                if(text.length == 1){
                    if(text.toString() != "1" && text.toString() != "0" && text.toString()!="/"){
                        expiry.setText("0$text/")
                        expiry.setSelection(expiry.text.length)
                    }
                }
                else if(text.length==2){
                    if(!text.contains('/')) {
                        if (text.toString().toInt() <= 12) {
                            expiry.setText("$text/")
                            expiry.setSelection(expiry.text.length)
                        }
                        else{
                            expiry.text.clear()
                        }
                    }
                }
                else if(text.length==3){
                    if((text[0].toString() + text[1].toString()).toInt()>12){
                        expiry.text.clear()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.submitPayment.setOnClickListener {
            var flag = 1
            val cardNumber = binding.cardNumber.editText?.text.toString()
            val expiryDate = binding.expiry.editText?.text.toString()
            val cvv = binding.cvv.editText?.text.toString()
            val firstName = binding.firstName.editText?.text.toString()
            val lastName = binding.lastName.editText?.text.toString()
            binding.cardNumber.error = ""
            binding.expiry.error=""
            binding.cvv.error=""
            binding.firstName.error=""
            binding.lastName.error=""
            if (!viewModel.validateCard(cardNumber)) {
                flag = 0
                binding.cardNumber.error = "Invalid or unsupported Card"
            }
            if (!viewModel.validateExpiry(expiryDate)) {
                flag = 0
                binding.expiry.error = "Invalid expiry"
            }
            if (!viewModel.validateCVV(cvv, cardNumber)) {
                flag = 0
                binding.cvv.error = "Invalid Security Code"
            }
            if (!viewModel.validateName(firstName)) {
                flag = 0
                binding.firstName.error = "Required/Invalid!"
            }
            if (!viewModel.validateName(lastName)) {
                flag = 0
                binding.lastName.error = "Required/Invalid!"
            }
            if (flag == 1) {
                submitPayment(cardNumber, expiryDate, cvv, firstName, lastName)
            }
        }
    }

    private fun submitPayment(
        cardNumber: String,
        expiry: String,
        cvv: String,
        firstName: String,
        lastName: String
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Payment Successful!")
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

}