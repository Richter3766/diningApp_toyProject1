package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.test.databinding.ActivitySubBinding
import com.example.test.fragment.login.SignInFragment
import com.example.test.fragment.navigation.DiningFragment
import com.example.test.fragment.navigation.HomeFragment
import com.example.test.fragment.navigation.MyPageFragment

class SubActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)

        setContentView(binding.root)
        replaceFragment(homeFragment)
        setNavigationListener()
    }

    private fun setNavigationListener(){
        binding.mainNavi.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.diningFragmentBtn -> replaceFragment(diningFragment)
                R.id.homeFragmentBtn -> replaceFragment(homeFragment)
                R.id.myPageFragmentBtn -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment, "fragment")
        ft.commitNow()
    }

    companion object{
        val homeFragment = HomeFragment()
        val diningFragment = DiningFragment()
        val myPageFragment = MyPageFragment()
    }
}