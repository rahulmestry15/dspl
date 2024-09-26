package com.example.dspl.ui.theme.events

//sealed class for obserer for navigating the data from view model and fragments
sealed class dsplEvents {
    object ShowProgress : dsplEvents()
    class TOAST(val message: String?) : dsplEvents()
    class shwoImage(val url: String?) : dsplEvents()
}