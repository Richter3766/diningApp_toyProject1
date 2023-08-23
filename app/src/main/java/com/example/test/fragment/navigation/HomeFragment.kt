package com.example.test.fragment.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.MainActivity
import com.example.test.R
import com.example.test.SubActivity
import com.example.test.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sharedPreferences = requireContext().getSharedPreferences(R.string.loginData.toString(), Context.MODE_PRIVATE)

        setUserData()
        binding.logoutBtn.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            updateUI()
        }
        return binding.root
    }

    private fun setUserData(){
        val name = "${sharedPreferences.getString("userName", "없음")}님"
        val email = "${binding.homeUserEmail.text} ${sharedPreferences.getString("userEmail", "nothing")}"

        binding.homeUserName.text = name
        binding.homeUserEmail.text = email
    }

    private fun updateUI(){
        requireActivity().run {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            finish()
        }
    }
}