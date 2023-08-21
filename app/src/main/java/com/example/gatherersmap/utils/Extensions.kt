package com.example.gatherersmap.utils

import android.annotation.SuppressLint
import java.util.Base64


@SuppressLint("NewApi")
fun ByteArray.toBase64(): String =
    String(Base64.getEncoder().encode(this))
