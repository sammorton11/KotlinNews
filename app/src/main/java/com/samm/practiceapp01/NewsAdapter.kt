package com.samm.practiceapp01

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samm.practiceapp01.models.Articles
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList = ArrayList<Articles>()
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
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
        val formattedDate = formatDate(newsItem.publishedAt)
        holder.date.text = formattedDate
        // News Image
        loadNewsImage(holder, newsItem, imageWidth, imageHeight)
        holder.card.setOnClickListener { view ->
            openWebViewFragment(view, imageUrl)
        }
    }

    // Size of the data structure
    override fun getItemCount(): Int {
        return newsList.size
    }

    // Update the old list with a new list
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

// Load an image into the image view in the news card
fun loadNewsImage(
    holder: NewsAdapter.ViewHolder,
    newsItem: Articles,
    width: Int,
    height: Int
){
    val imageUrl: String = newsItem.urlToImage
    val newsImage: ImageView = holder.newsImage
    val context: Context = holder.newsImage.context

    Glide.with(context)
        .load(imageUrl)
        .override(width, height) // resizing
        .centerCrop()
        .into(newsImage)
}

fun formatDate(input: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("MM-dd-yyyy - hh:mm a", Locale.US)
    val date = inputFormat.parse(input)

    return outputFormat.format(date)
}
