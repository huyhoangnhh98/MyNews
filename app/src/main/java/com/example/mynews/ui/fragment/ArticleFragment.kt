package com.example.mynews.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mynews.R
import com.example.mynews.ui.NewsActivity
import com.example.mynews.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment(R.layout.fragment_article) {

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_article, container, false)
//    }
    lateinit var viewModel: NewsViewModel

    //get argument from navigation (step 1)
    val args : ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        //get the article pass as agrument (step 2)
            var article = args.article

        //display article in web view
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url.toString())
        }

        //set floatingButton click to save article
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Save Successfully", Snackbar.LENGTH_LONG).show()
        }

    }
}