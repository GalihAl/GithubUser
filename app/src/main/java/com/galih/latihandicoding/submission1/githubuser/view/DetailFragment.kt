package com.galih.latihandicoding.submission1.githubuser.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.galih.latihandicoding.submission1.githubuser.adapter.ViewPageDetailAdaptor
import com.galih.latihandicoding.submission1.githubuser.databinding.FragmentDetailBinding
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_row_github_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {
    private lateinit var _binding : FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = _binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDetailBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<GithubUser>(DetailActivity.EXTRA_PERSON) as GithubUser
        _binding.tvUserNameReceiver.text = user.userName
        _binding.tvNameReceiver.text = user.name
        _binding.tvLocationReceiver.text = user.adress
        _binding.tvCompanyReceiver.text = user.company
        _binding.tvFollowerValueReceiver.text = user.followers.toString()
        _binding.tvFollowingValueReceiver.text = user.following.toString()
        _binding.tvRepositoryValueReceiver.text = user.repository.toString()
        Glide.with(this@DetailFragment).load(user.avatar).into(_binding.photoProfileDetail)
    }
}