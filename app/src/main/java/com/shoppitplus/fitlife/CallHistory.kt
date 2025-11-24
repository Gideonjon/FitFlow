package com.shoppitplus.fitlife

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoppitplus.fitlife.adapter.CallHistoryAdapter
import com.shoppitplus.fitlife.databinding.FragmentCallHistoryBinding
import com.shoppitplus.fitlife.db.PhoneCall
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CallHistory() : Fragment() {
    private var _binding: FragmentCallHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CallHistoryAdapter
    private val REQUEST_CALL_LOG = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCallHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CallHistoryAdapter()
        binding.recyclerCallHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCallHistory.adapter = adapter

        checkPermission()
    }

    private fun checkPermission() {
        val context = requireContext()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CALL_LOG),
                REQUEST_CALL_LOG
            )
        } else {
            loadCallLogs()
        }
    }

    private fun loadCallLogs() {
        val callList = ArrayList<PhoneCall>()

        val cursor = requireContext().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.let {
            val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)

            while (it.moveToNext()) {
                val name = it.getString(nameIdx) ?: "Unknown"
                val number = it.getString(numberIdx) ?: ""
                val type = it.getInt(typeIdx)
                val date = it.getLong(dateIdx)

                val formattedTime = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                    .format(Date(date))

                val callType = when (type) {
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }

                callList.add(PhoneCall(name, number, callType, formattedTime))
            }
            it.close()
        }

        adapter.submitList(callList)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL_LOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCallLogs()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


