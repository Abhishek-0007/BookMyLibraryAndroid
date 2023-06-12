package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.FragmentQrScreenBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.SeatResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QrScreenFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding : FragmentQrScreenBinding
    private var otp = ""
    private var genre = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrScreenBinding.inflate(layoutInflater, container, false)
        genre = arguments?.getString("genre").toString()
        codeScanner = CodeScanner(requireActivity(), binding.scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            Log.d("Scan res: ", it.text)
            activity?.runOnUiThread {
                Log.d("Scan res: ", it.text)
                otp = it.text
                checkAPI()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            Log.d("Scan error: ", it.message.toString())
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "error: ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Scan error: ", it.message.toString())

            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    fun checkAPI() {
        checkApiRepoVirtual().observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.loadingLayout.visibility = View.GONE
                    }, 1000)
                    ExtensionMethods().showBottomSheetAfterVirtual(requireContext(), "Virtual Library", otp)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR -> {
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }
    @SuppressLint
    fun checkApiRepoVirtual() : MutableLiveData<Resource<SeatResponse>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<SeatResponse>>()
        mutableLiveData.value = Resource<SeatResponse>().loading()
        api.addGenreToDB(otp, "example@example.com", genre).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<SeatResponse>().success(it)

                },
                {
                    mutableLiveData.value = Resource<SeatResponse>().error(it)
                }
            )

        return mutableLiveData
    }
}