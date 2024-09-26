package com.example.dspl.ui.theme.screen.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dspl.databinding.DsplListLayoutBinding
import com.example.dspl.ui.theme.DsplUserNavigationViewModel
import com.example.dspl.ui.theme.adapter.UserListAdapter
import com.example.dspl.ui.theme.model.Task
import com.example.dspl.ui.theme.model.UserTasks
import com.fnoplay.utility.base.getViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//First screen of Activity open via Navgraph
class DsplUserkListFragment : Fragment() {

    //sharedViewModel we can use entire activity
    private val navViewModel: DsplUserNavigationViewModel by activityViewModels()
    private lateinit var binding: DsplListLayoutBinding
    lateinit var adapter: UserListAdapter
    var isDatEmpty = false
    var isBackGround = false

    //Viewmodel of fragment
    lateinit var viewModel: DsplListViewModel

    //initialize binding and viewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DsplListLayoutBinding.inflate(layoutInflater)
        activity?.let {
            viewModel = getViewModel { DsplListViewModel(it.application, it) }
        }
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navViewModel.userList.clear()

        setupRecyclerView()
        setListener()
        binding.homeView.setOnClickListener {
            getUserList()
        }
    }


    override fun onStart() {
        super.onStart()
        getUserList()
    }

    fun setListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // dx is the horizontal scroll amount, dy is the vertical scroll amount
                if (dy > 0) {
                    // User scrolled downwards
                    Log.d("ScrollListener", "Scrolling down")
                    //if scroll down to bottom then calling the API for another data
                    //   getUserList()
                } else if (dy < 0) {
                    // User scrolled upwards
                    Log.d("ScrollListener", "Scrolling up")
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Check the current scroll state
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("ScrollListener", "RecyclerView stopped scrolling")
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()
    }

    //getList Api of Pagination on scroll
    fun getUserList() {
        if (isBackGround){
            return
        }
        navViewModel.userList.clear()
        isBackGround = true
        navViewModel.fetchUsers() {
            lifecycleScope.launch(Dispatchers.Main) {
                if (it?.size == 0) {
                    isDatEmpty = true
                    return@launch
                }

                navViewModel.userList.addAll(groupTasksByUserId(it!!))
                adapter.refreshList(navViewModel.userList)
                isBackGround = false
            }
        }
    }

    fun groupTasksByUserId(tasks: ArrayList<Task>): ArrayList<UserTasks> {
        val groupedTasks = tasks.groupBy { it.userId } // Group tasks by userId
        val userTasksList = ArrayList<UserTasks>() // Create an empty ArrayList

        // Populate the ArrayList with UserTasks objects
        groupedTasks.forEach { (userId, userTasks) ->
            userTasksList.add(
                UserTasks(
                    userId,
                    ArrayList(userTasks)
                )
            ) // Add each UserTasks object to the list
        }

        return userTasksList
    }



    //Initialize the recycle view with adapter
    fun setupRecyclerView() {
        adapter = UserListAdapter(requireActivity(), arrayListOf())
        val manager = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.setAdapter(adapter)
        adapter.onItemClick = {

        }
    }

}