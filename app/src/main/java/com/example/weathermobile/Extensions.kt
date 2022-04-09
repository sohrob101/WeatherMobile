package com.example.weathermobile

fun String?.isZipCodeValidated(): Boolean {
    return this?.length == 5 && this.all {it.isDigit()}
}