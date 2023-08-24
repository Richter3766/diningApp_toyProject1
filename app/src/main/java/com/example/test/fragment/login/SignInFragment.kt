package com.example.test.fragment.login

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.test.helper.toyClient
import com.example.test.SubActivity
import com.example.test.databinding.FragmentSignInBinding
import com.example.test.model.LoginData
import com.example.test.services.AuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import com.example.test.R

class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var startGoogleLoginForResult: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences(R.string.loginData.toString(), Context.MODE_PRIVATE)

        checkLogin()

        auth = FirebaseAuth.getInstance()
        googleInit()

        binding.googleLoginBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startGoogleLoginForResult.launch(signInIntent)
        }

        return binding.root

    }
    private fun googleInit() {
        val default_web_client_id = R.string.google_client_id.toString()
//            getString(R.string.google_client_id); // Android id X

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(default_web_client_id)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        startGoogleLoginForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->

                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                            firebaseAuthWithGoogle(account.idToken!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e)
                        }
                    }
                    // Google Login Success
                } else {
                    Log.e(TAG, "Google Result Error ${result.data?.extras}")
                }
            }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user!!.getIdToken(true)
                        .addOnCompleteListener { tToken ->
                            if (tToken.isSuccessful) {
                                val idToken = tToken.result.token
                                // Send token to your backend via HTTPS
                                sendToken(idToken)
                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }

                    setUserData(user)
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI()
                }
            }
    }

    private fun updateUI() {
        // FirebaseUser 데이터에 따른 UI 작업
//        binding.userName.text = user?.displayName

//            원하는 Activity 로 이동도 가능
        requireActivity().run {
            startActivity(Intent(requireContext(), SubActivity::class.java))
            finish()
        }
    }
    companion object {
        const val TAG = "MainActivity"
    }

    private fun sendToken(idToken: String?) {
        val retrofit = toyClient.getInstance()
        val myAPI: AuthService = retrofit.create(AuthService::class.java)
        myAPI.verifyToken(idToken).enqueue(object : Callback<LoginData> {
            @SuppressLint("CommitPrefEdits")
            override fun onResponse(
                call: Call<LoginData>,
                response: Response<LoginData>
            ) {
                if(response.isSuccessful){
//                    response.body()?.let { newData ->
//                        Log.d(TAG, "Response: $newData")
//                    }
                    Log.d(TAG, "Response: ${response.body()?.result}")
//                    val customToken = response.body()?.result
                    // 얻은 토큰을 어디엔가에다 저장 후 백엔드 요청 시마다 함께 보내도록 함
                    // 로그인도 이 토큰을 기준으로 진행
                    // 받은 토큰이 성공적인 응답에서 온 것인지 확인 필요


                }else{
                    Log.d(TAG, "Response error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                Log.d(TAG, t.toString())
            }

        })
    }

    private fun checkLogin(){
        if(sharedPreferences.getBoolean("isLoggedIn", false)){
            updateUI()
        }
    }

    private fun setUserData(user: FirebaseUser?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("userName", user?.displayName)
        editor.putString("userEmail", user?.email)
        editor.apply()
    }
}