package com.example.dspl.ui.theme

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.dspl.R
import com.example.dspl.databinding.DsplUserActivityBinding
import com.example.dspl.ui.theme.screen.dashboard.DsplUserkListFragment

class DsplUserNavigationActivity : AppCompatActivity() {

    private lateinit var binding: DsplUserActivityBinding
    lateinit var navController: NavController
    //sharedViewModel Entire Activity
    private val viewModel: DsplUserNavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setBinding view of activity
        binding = DsplUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setGraph()
        getCurrentFragment()

    }
    // initialise navgraph for the activity
    private fun setGraph() {
        navController.setGraph(R.navigation.nav_graph, intent.extras)
    }

    // for getting current fragment
    fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when (getCurrentFragment()) {
            is DsplUserkListFragment-> {
                finish()
            }


            else -> {
                navController.popBackStack()
            }
        }
    }

}