package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samm.practiceapp01.R
import com.samm.practiceapp01.domain.models.Articles
import com.samm.practiceapp01.util.Constants.imageHeight
import com.samm.practiceapp01.util.Constants.imageWidth
import com.samm.practiceapp01.util.ViewUtility

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList = ArrayList<Articles>()
    private val viewUtility = ViewUtility()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitle: TextView = itemView.findViewById(R.id.news_title)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        val card: CardView = itemView.findViewById(R.id.card_view)
        val date: TextView = itemView.findViewById(R.id.article_date)

    }

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

        holder.newsTitle.text = newsItem.title
        holder.newsDescription.text = newsItem.description
        val formattedDate = newsItem.publishedAt?.let { viewUtility.formatDate(it) }
        holder.date.text = formattedDate

        // News Image
        viewUtility.loadNewsImage(holder, newsItem, imageWidth, imageHeight)

        // Open Website in web view fragment
        holder.card.setOnClickListener { view ->
            if (imageUrl != null) {
                viewUtility.openWebViewFragment(view, imageUrl)
            }
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
    @SuppressLint("NotifyDataSetChanged")
    fun clearList(){
        newsList.clear()
        notifyDataSetChanged()
    }
}
