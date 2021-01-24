package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.HomeRepository
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.viewmodel.HomeModelFactory
import com.apps.bacon.mydiabetes.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = AppDatabase.getInstance(requireContext())
        val repository = HomeRepository(database)
        val factory = HomeModelFactory(repository)
        val homeViewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)

        homeViewModel.currentTag.observe(viewLifecycleOwner, { selectedTab ->
            if(homeViewModel.getProductsByTag(selectedTab).hasObservers())
                homeViewModel.getProductsByTag(selectedTab).removeObservers(viewLifecycleOwner)

            if(selectedTab == 0){
                homeViewModel.getAll().observe(viewLifecycleOwner, {
                    initRecyclerView(it)
                })
            }else{
                homeViewModel.getProductsByTag(selectedTab).observe(viewLifecycleOwner, {
                    initRecyclerView(it)
                })
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initRecyclerView(data: List<Product>){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter(data, this@HomeFragment)
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
        startActivity(intent)
    }
}