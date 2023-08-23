package com.example.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.test.databinding.ActivityMainBinding
import com.example.test.fragment.login.SignInFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val manager: FragmentManager = supportFragmentManager
    private val ft: FragmentTransaction = manager.beginTransaction()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ft.add(R.id.signInView, SignInFragment())
    }
}