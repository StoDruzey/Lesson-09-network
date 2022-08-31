package com.example.lesson09network

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.example.lesson09network.databinding.FragmentFirstBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class FragmentFirst : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = requireNotNull(_binding)
//to request and check permisssion:
//    private val launcher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//
//    }

    private var currentRequest: Call<List<User>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFirstBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val githubApi = retrofit.create<GithubApi>()

        currentRequest = githubApi
            .getUsers(10, 50)
            .apply {
                enqueue(object : Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        if (response.isSuccessful) {
                            val users = response.body() ?: return
                            binding.imageView.load(users[5].avatarUrl)
                        } else {
                            handleException(HttpException(response))
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        if (!call.isCanceled) {
                            handleException(t)
                        }
                    }
                })
            }
//to check permission: method checkSelfPermission returns int. It is necessary to compare with constant of PackageManager
        ContextCompat
            .checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        with(binding) {
            button.setOnClickListener {
//to request permisssion:
//                launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentRequest?.cancel()
        _binding = null
    }

    private fun handleException(t: Throwable) {

    }
}