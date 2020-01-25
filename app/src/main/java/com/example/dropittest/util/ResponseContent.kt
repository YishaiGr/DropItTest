package com.example.dropittest.util

data class ResponseContent<out T>(val content: T?, var errorMessage: String? = null)