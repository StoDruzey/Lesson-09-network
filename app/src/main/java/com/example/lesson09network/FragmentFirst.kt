package com.example.lesson09network

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
                            binding.imageView.load(users[6].avatarUrl)
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
        with(binding) {

//                toolbar.menu
//                toolbar.inflateMenu(R.menu.menu_toolbar) //for processing menu toolbar
//                toolbar.setNavigationOnClickListener {
//                    findNavController().navigateUp() //for processing button "back"
//                }
            //for processing click on toolbar menu
//            toolbar.setOnMenuItemClickListener {
//                if (it.itemId == R.id.action_search) {
//                    Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
//                    true
//                } else {
//                    false
//                }
//            }
            //for processing of search menu toolbar
//            toolbar
//                .menu
//                .findItem(R.id.action_search)
//                .actionView
//                .let { it as SearchView }
//                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                })

            button.setOnClickListener {


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