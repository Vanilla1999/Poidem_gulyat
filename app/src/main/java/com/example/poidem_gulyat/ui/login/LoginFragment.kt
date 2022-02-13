package com.example.poidem_gulyat.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.FragmentLoginBinding
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.*

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
@AndroidEntryPoint
class LoginFragment() : Fragment(R.layout.fragment_login), CoroutineScope {
    private val navController : NavController by lazy { findNavController() }
    private val binding: FragmentLoginBinding by viewBinding()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

//    private val token: TokensRepository by inject()
//    private val time: TimeDataSource by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.relativeLayout1.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
              //  navController.navigate(R.id.blankFragmentFlex)
              //  startNewActivity( MainActivity::class.java)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }
        })
        binding.buttonLogin.setOnClickListener {
            launch {
                withContext(Dispatchers.Main) {
                    binding.relativeLayout1.transitionToEnd()
                }
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