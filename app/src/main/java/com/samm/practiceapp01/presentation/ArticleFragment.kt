package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samm.practiceapp01.R
import com.samm.practiceapp01.core.ViewUtility
import com.samm.practiceapp01.domain.models.Articles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleFragment : Fragment(), NewsAdapter.OnCardClick {

    private val viewUtility = ViewUtility()
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToTopButton: FloatingActionButton
    private lateinit var layoutManager: LayoutManager
    private lateinit var errorMessageTV: TextView
    private var pageNumber = 1
    private lateinit var navController: NavController

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_article, container, false)
        val activity = requireActivity() // activity associated with this fragment

        recyclerView = view.findViewById(R.id.news_list)
        searchField = view.findViewById(R.id.search)
        progressBar = view.findViewById(R.id.progress_bar)
        backToTopButton = view.findViewById(R.id.back_to_top_button)
        errorMessageTV = view.findViewById(R.id.error_message_text_view)

        layoutManager = LinearLayoutManager(activity)
        backToTopButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
        recyclerView.scheduleLayoutAnimation()
        adapter = NewsAdapter(requireContext(), this)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        newsViewModel.viewModelScope.launch(Dispatchers.Main) {
            observeCacheData(newsViewModel)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        navControllerSetup()
        setUpRecyclerView(layoutManager)

        val sharedPref = getPreferences(requireContext())
        val savedValue = sharedPref.getString("SEARCH_FIELD_VALUE", "")
        searchField.setQuery(savedValue, false)
        val menuHost: MenuHost = requireActivity()
        viewUtility.hideViewsWhenScrolled(recyclerView, searchField, backToTopButton)

        backToTopButton.setOnClickListener {
            viewUtility.scrollToTop(recyclerView, backToTopButton)
        }

        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val editor = sharedPref.edit()
                if (query != null) {
                    editor.putString("SEARCH_FIELD_VALUE", query)
                    editor.apply()
                    fetchNewsData(pageNumber, searchField.query.toString())
                }
                return false
            }
            // Don't need this so just return false
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Action Menu -- Add a refresh button
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.action_bar_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.clear -> {
                        newsViewModel.deleteAllAlertDialog(requireContext()){
                            newsViewModel.clearCache()
                            adapter.clearList()
                        }
                        true
                    }
                    R.id.scrollToBottom -> {
                        viewUtility.scrollToBottom(recyclerView, adapter)
                        true
                    }
                    R.id.contact -> {
                        navController.navigate(R.id.contactInfoFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        recyclerView.isVerticalScrollBarEnabled = true
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    private fun navControllerSetup() {
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpRecyclerView(layoutManager: LayoutManager){
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = layoutManager
        } else {
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
        }
        recyclerView.adapter = adapter
    }

    private fun fetchNewsData(page: Int, search: String) {

        if (search.isEmpty()) {
            Toast.makeText(activity, "Search term is missing", Toast.LENGTH_LONG).show()
        } else {
            newsViewModel.fetchArticles(search, page)
            getState(newsViewModel)
            viewUtility.hideKeyboard(activity)

        }
    }

    private fun getState(viewModel: NewsViewModel) {
        if (!recyclerView.isComputingLayout) {
            viewModel.getState(viewLifecycleOwner, progressBar, errorMessageTV, adapter, recyclerView)
        }
    }

    private fun observeCacheData(viewModel: NewsViewModel){
        if (!recyclerView.isComputingLayout) {
            viewModel.getDbFlow(adapter, recyclerView)
        }
    }

    override fun onCardClick(article: Articles?) {
        val bundle = Bundle()
        if (article != null) {
            bundle.putString("url", article.url)
        }
        navController.navigate(R.id.webViewFragment2, bundle)
    }
}