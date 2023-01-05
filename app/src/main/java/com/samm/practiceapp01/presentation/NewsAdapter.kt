package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samm.practiceapp01.R
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.util.Utility

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList = ArrayList<Articles>()
    private val utility = Utility()
    private val imageWidth: Int = 1000
    private val imageHeight: Int = 800

    // ViewHolder class that holds the views that the data is adapted to
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitle: TextView = itemView.findViewById(R.id.news_title)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        val card: CardView = itemView.findViewById(R.id.card_view)
        val date: TextView = itemView.findViewById(R.id.article_date)

    }

    // Create and return the inflated view item that is placed in the Recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.news_item, parent, false)

        return ViewHolder(view)
    }

    // Bind Data to the views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        val imageUrl = newsItem.url

        // Title
        holder.newsTitle.text = newsItem.title

        // Description
        holder.newsDescription.text = newsItem.description

        // Date text
        val formattedDate = utility.formatDate(newsItem.publishedAt)
        holder.date.text = formattedDate

        // News Image
        utility.loadNewsImage(holder, newsItem, imageWidth, imageHeight)

        // Open Website in web view fragment
        holder.card.setOnClickListener { view ->
            openWebViewFragment(view, imageUrl)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNews(articles: List<Articles>) {
        newsList.clear()
        newsList.addAll(articles)
        notifyDataSetChanged()
    }

    // swaps the article fragment out for the webview fragment passing the url into the
    // newInstance function.
    private fun openWebViewFragment(view: View, url: String) {
        val activity = view.context as AppCompatActivity
        val myFragment = WebViewFragment.newInstance(url)
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, myFragment)
            .addToBackStack(null)
            .commit()
    }
}
