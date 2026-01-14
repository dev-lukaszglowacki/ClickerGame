package com.example.clicker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clicker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.generateTarget()

        binding.startButton.setOnClickListener {
            viewModel.start()
            binding.startButton.isEnabled = false
            binding.stopButton.isEnabled = true
        }

        binding.stopButton.setOnClickListener {
            viewModel.stop()
            binding.stopButton.isEnabled = false
            binding.startButton.isEnabled = true
        }

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.timerText.observe(viewLifecycleOwner) {
            binding.timerText.text = it
        }

        viewModel.resultText.observe(viewLifecycleOwner) {
            binding.resultText.text = it
        }

        viewModel.targetText.observe(viewLifecycleOwner) {
            binding.targetTimeText.text = it
        }

        viewModel.scoreText.observe(viewLifecycleOwner) {
            binding.scoreText.text = it
        }

        viewModel.bestScoreText.observe(viewLifecycleOwner) {
            binding.bestScoreText.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
