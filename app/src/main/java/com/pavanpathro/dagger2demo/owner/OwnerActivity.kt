package com.pavanpathro.dagger2demo.owner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.pavanpathro.dagger2demo.api.Constants
import com.pavanpathro.dagger2demo.api.models.Item
import com.pavanpathro.dagger2demo.api.models.Owner
import com.pavanpathro.dagger2demo.adapters.RepoAdapter
import com.pavanpathro.dagger2demo.repo.RepoActivity
import com.pavanpathro.dagger2demo.R
import com.pavanpathro.dagger2demo.application.GithubApplication
import kotlinx.android.synthetic.main.activity_profile.*

class OwnerActivity : AppCompatActivity(), OwnerView, RepoAdapter.OnItemClickListener, RepoAdapter.OnLoadMoreListener {
    private lateinit var ownerPresenterImpl: OwnerPresenterImpl
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ownerPresenterImpl = OwnerPresenterImpl(this)

        initRecyelerView()

        getIntentData()
    }

    private fun getIntentData() {
        val owner = intent.getParcelableExtra<Owner>(Constants.OWNER_DETAILS)

        textViewName.text = owner.login

        GithubApplication.picasso.load(owner.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageViewProfile)

        ownerPresenterImpl.getRepos(owner.login)
    }

    private fun initRecyelerView() {
        recyclerViewRepos.layoutManager = LinearLayoutManager(this)
        recyclerViewRepos.itemAnimator = DefaultItemAnimator()
        ViewCompat.setNestedScrollingEnabled(recyclerViewRepos, false)

        repoAdapter = RepoAdapter(false, recyclerViewRepos)
        repoAdapter.onItemClickListener = this
        repoAdapter.onLoadMoreListener = this
        recyclerViewRepos.adapter = repoAdapter
    }

    override fun attachRepos(items: List<Item>) {
        repoAdapter.updateItems(items)

        textViewFetching.visibility = View.GONE
        recyclerViewRepos.visibility = View.VISIBLE
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, RepoActivity::class.java)
        intent.putExtra(Constants.REPO_DETAILS, item)
        startActivity(intent)
    }

    override fun onLoadMore() {

    }
}
