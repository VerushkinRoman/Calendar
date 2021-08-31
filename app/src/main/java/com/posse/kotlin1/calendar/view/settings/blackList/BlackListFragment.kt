package com.posse.kotlin1.calendar.view.settings.blackList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.posse.kotlin1.calendar.R
import com.posse.kotlin1.calendar.databinding.FragmentBlackListBinding
import com.posse.kotlin1.calendar.model.Friend
import com.posse.kotlin1.calendar.utils.*
import com.posse.kotlin1.calendar.viewModel.BlackListViewModel

private const val ARG_EMAIL = "Email"

class BlackListFragment : DialogFragment() {
    private var _binding: FragmentBlackListBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String
    private val viewModel: BlackListViewModel by viewModels()
    private lateinit var adapter: BlackListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(ARG_EMAIL)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlackListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowSize(this, WindowManager.LayoutParams.MATCH_PARENT)
        binding.blackListCard.listClose.setOnClickListener { dismiss() }
        adapter = BlackListRecyclerAdapter(
            mutableListOf()
        ) { viewModel.personSelected(it) }
        binding.blackListCard.listRecyclerView.adapter = adapter
        viewModel.getLiveData().observe(viewLifecycleOwner, { showFriends(it) })
        viewModel.refreshLiveData(email) { context?.showOfflineToast() }
        isCancelable = true
    }

    private fun showFriends(friends: Pair<Boolean, Set<Friend>>) {
        if (friends.first) {
            if (friends.second.isEmpty()) {
                binding.blackListCard.noData.show()
                binding.blackListCard.noDataText.putText(getString(R.string.empty_black_list))
                binding.blackListCard.listRecyclerView.disappear()
            } else {
                val friendsList = friends.second.toMutableList()
                friendsList.sortBy { it.name }
                adapter.setData(friendsList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String) =
            BlackListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                }
            }
    }
}