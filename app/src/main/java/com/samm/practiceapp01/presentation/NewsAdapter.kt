package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samm.practiceapp01.R
import com.samm.practiceapp01.core.Constants.imageHeight
import com.samm.practiceapp01.core.Constants.imageWidth
import com.samm.practiceapp01.core.ViewUtility
import com.samm.practiceapp01.domain.models.Articles

class NewsAdapter(private val context: Context, private val cardClick: OnCardClick) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList = ArrayList<Articles>()
    private val viewUtility = ViewUtility()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var newsTitle: TextView = itemView.findViewById(R.id.news_title)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        val card: CardView = itemView.findViewById(R.id.card_view)
        val date: TextView = itemView.findViewById(R.id.article_date)
        val author: TextView = itemView.findViewById(R.id.news_author)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.news_item, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        val imageUrl = newsItem.urlToImage
        val formattedDate = newsItem.publishedAt?.let { viewUtility.formatDate(it) }
        val newsImage: ImageView = holder.newsImage

        holder.newsTitle.text = newsItem.title
        holder.newsDescription.text = newsItem.description
        holder.date.text = formattedDate
        holder.author.text = newsItem.author

        Glide.with(context)
            .load(imageUrl)
            .override(imageWidth, imageHeight) // resizing
            .centerCrop()
            .into(newsImage)

        holder.card.setOnClickListener {
            cardClick.onCardClick(newsItem)
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

    interface OnCardClick {
        fun onCardClick(article: Articles?)
    }
}
