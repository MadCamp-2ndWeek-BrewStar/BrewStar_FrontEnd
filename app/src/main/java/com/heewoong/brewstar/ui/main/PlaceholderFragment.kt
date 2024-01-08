package com.heewoong.brewstar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.heewoong.brewstar.R
import com.heewoong.brewstar.databinding.FragmentNavigationBinding
import com.heewoong.brewstar.tab1
import com.heewoong.brewstar.tab2

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentNavigationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        val root = binding.root

        val textView: TextView = binding.sectionLabel
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: Int): Fragment {
            return when (sectionNumber) {
                1 -> tab1() // 첫 번째 탭에는 현재 프래그먼트를 반환
                2 -> tab2() // 두 번째 탭에는 tab2 프래그먼트를 반환
                else -> PlaceholderFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}