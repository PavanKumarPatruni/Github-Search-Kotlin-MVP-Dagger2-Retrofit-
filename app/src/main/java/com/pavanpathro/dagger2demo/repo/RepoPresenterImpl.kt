package com.pavanpathro.dagger2demo.repo

import android.content.Context
import com.pavanpathro.dagger2demo.application.GithubApplication
import com.pavanpathro.dagger2demo.R
import com.pavanpathro.dagger2demo.api.models.Owner
import retrofit2.Callback
import retrofit2.Response

class RepoPresenterImpl(private var context: Context) : RepoPresenter {

    private var repoView = context as RepoView

    override fun getContributors(ownerName: String, repoName: String) {

        GithubApplication.service.getContributors(ownerName, repoName).enqueue(object: Callback<List<Owner>> {
            override fun onFailure(call: retrofit2.Call<List<Owner>>, t: Throwable) {
                repoView.changeMessage(context.getString(R.string.no_contributors))
            }

            override fun onResponse(call: retrofit2.Call<List<Owner>>, response: Response<List<Owner>>) {
                if (response.body() != null) {
                    repoView.attachContributors(response.body()!!)
                } else {
                    repoView.changeMessage(context.getString(R.string.no_contributors))
                }
            }

        })

    }

}
