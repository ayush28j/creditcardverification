package com.morr.morrassignment

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainViewModel : ViewModel() {
    fun validateCard(cardNumber: String): Boolean {
        if(cardNumber.length in 13..16) {
            /**card number must start with
             * 4 - Visa
             * 5 - MasterCard
             * 37 - AmericanExpress
             * 6 - Discover
             */
            if((cardNumber[0]=='4') || (cardNumber[0]=='5') || (cardNumber[0]=='6') || (cardNumber[0]=='3' && cardNumber[1]=='7')){
                var sumOfOdd = 0
                var sumOfEven = 0
                val revCardNumber = cardNumber.reversed()
                revCardNumber.forEachIndexed { index, c ->
                    if (index % 2 == 0) {
                        sumOfOdd += c.toString().toInt()
                    } else {
                        val doubledNumber = (c.toString().toInt() * 2)
                        if (doubledNumber.toString().length > 1) {
                            var doubledNumberSum = 0
                            doubledNumber.toString().forEach {
                                doubledNumberSum += it.toString().toInt()
                            }
                            sumOfEven += doubledNumberSum
                        } else
                            sumOfEven += doubledNumber
                    }
                }
                Log.d("CC", "odd: $sumOfOdd || even: $sumOfEven")
                return (sumOfEven + sumOfOdd) % 10 == 0
            }
            else
                return false
        }
        else{
            return false
        }
    }

    fun validateExpiry(expiry: String): Boolean {
        if(expiry.length!=5)
            return false
        val inputMonth = expiry.substring(0, 2).toInt() - 1
        val inputYear = ("20" + expiry.substring(3, 5)).toInt()
        val calendarInstance = Calendar.getInstance()
        val currentMonth = calendarInstance.get(Calendar.MONTH)
        val currentYear = calendarInstance.get(Calendar.YEAR)
        return if(currentYear > inputYear)
            false
        else{
            if(currentYear==inputYear){
                currentMonth <= inputMonth
            } else {
                true
            }
        }
    }

    fun validateCVV(cvv: String, cardNumber: String): Boolean {
        //For AMEX cards, cvv is of 4 digit, for others its 3
        //AMEX cards always start with 37
        if (cardNumber.length in 13..16) {
            return if (cardNumber.substring(0, 2) == "37")
                cvv.length == 4
            else
                cvv.length == 3
        }
        else
            return false
    }

    fun validateName(name: String): Boolean {
        if(name.isEmpty())
            return false
        val p: Pattern = Pattern.compile("^[ A-Za-z]+$")
        val m: Matcher = p.matcher(name)
        return m.matches()
    }
}