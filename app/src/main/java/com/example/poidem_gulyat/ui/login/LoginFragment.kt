package com.example.poidem_gulyat.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.App
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.FragmentLoginBinding
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.di.loginActivity.DaggerLoginFragmentComponent
import com.example.poidem_gulyat.di.loginActivity.LoginFragmentComponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.utils.startNewActivity
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.*
import javax.inject.Inject

import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class LoginFragment() : Fragment(R.layout.fragment_login), CoroutineScope {
    @Inject
    lateinit var factory: FactoryLogin
    private val viewModelLogin by viewModels<LoginViewModel> { factory }
    private val navController: NavController by lazy { findNavController() }
    private val binding: FragmentLoginBinding by viewBinding()
    lateinit var appComponent: LoginFragmentComponent
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    //    private val token: TokensRepository by inject()
//    private val time: TimeDataSource by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent = DaggerLoginFragmentComponent.factory().create(App.appComponentMain)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        initListeners()
        binding.relativeLayout1.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                //  navController.navigate(R.id.blankFragmentFlex)
                requireActivity().finish()
                requireActivity().startNewActivity(MainActivity::class.java)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {

            }
        })
    }

    private fun initListeners() {
        lifecycleScope.launchWhenResumed {
            viewModelLogin.loginStateFlow.collect {
                when (it) {
                    is Response.Success -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireActivity(),
                                "Авторизация успешная",
                                Toast.LENGTH_LONG)
                                .show()
                        }
                        viewModelLogin.saveAccessTokens(
                            it.value!!.user.access_token!!,
                            it.value.user.refresh_token!!)
                    }
                    is Response.Failure -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireActivity(),
                                "Biometry not supported",
                                Toast.LENGTH_LONG).show()
                            binding.relativeLayout1.transitionToEnd()
                        }
                    }
                    is Response.Loading -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireActivity(),
                                "Проходит авторизация",
                                Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun initButton() {
        //
        binding.buttonLogin.setOnClickListener {
            launch {
                viewModelLogin.login(binding.mail.text.toString().trim(),
                    binding.password1.text.toString().trim())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }

    companion object {
    }
}