package com.example.picksy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.snackbar.Snackbar

class TrackActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReferenceOrders: DatabaseReference
    private lateinit var mDatabaseReferenceOrdersServer: DatabaseReference

    private lateinit var alertDialog: AlertDialog.Builder

    private lateinit var userId: String
    private lateinit var status: String
    private lateinit var desdate: String
    private lateinit var deldate: String
    private lateinit var orderDate: String
    private lateinit var keyUpdate: String

    private lateinit var mTextView1: TextView
    private lateinit var mTextView2: TextView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mButton: Button
    private lateinit var mToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        mButton = findViewById(R.id.bt_tarcker_cancle)
        mTextView1 = findViewById(R.id.tv_tracker_detail)
        mTextView2 = findViewById(R.id.tv_tracker_detail_date)
        mSeekBar = findViewById(R.id.seekBar)

        alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("No Internet Connection")
            .setMessage("We cannot detect any internet connection. Please check your internet connection and try again.")
            .setPositiveButton("Retry") { _, _ ->
                showSnack(isNetworkAvailable())
            }
            .setNegativeButton("Close") { dialog, _ ->
                finish()
                onBackPressed()
                Toast.makeText(applicationContext, "You clicked on NO", Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }

        showSnack(isNetworkAvailable())

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        userId = mFirebaseUser.uid
        mDatabaseReferenceOrders = mFirebaseDatabase.getReference("Customer").child(userId).child("Orders")

        val intent = intent
        keyUpdate = intent.getStringExtra("key") ?: ""
        status = intent.getStringExtra("status1") ?: ""
        orderDate = intent.getStringExtra("odate") ?: ""
        desdate = intent.getStringExtra("disdate") ?: ""
        deldate = intent.getStringExtra("deldate") ?: ""

        when (status) {
            "Cancle" -> {
                mSeekBar.progress = 0
                mTextView1.text = "Order Cancelled"
                mButton.visibility = View.INVISIBLE
            }
            "NotDeliver" -> {
                mSeekBar.progress = 33
                mTextView1.text = "Order Placed"
                mTextView2.text = orderDate
                mButton.visibility = View.VISIBLE
            }
            "Dispatch" -> {
                mSeekBar.progress = 66
                mTextView1.text = status
                mTextView2.text = desdate
                mButton.visibility = View.VISIBLE
            }
            "Delivered" -> {
                mSeekBar.progress = 99
                mTextView1.text = status
                mTextView2.text = deldate
                mButton.visibility = View.INVISIBLE
            }
        }

        mButton.setOnClickListener {
            mDatabaseReferenceOrders = mFirebaseDatabase.getReference("Customer").child(userId).child("Orders").child(keyUpdate)
            mDatabaseReferenceOrders.removeValue()
            mDatabaseReferenceOrdersServer = mFirebaseDatabase.getReference("CompanyOrder").child(userId).child(keyUpdate)
            mDatabaseReferenceOrdersServer.child("status").setValue("Cancle")
            mToast = Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT)
            mToast.show()
//            val intent1 = Intent(this, CartActivity::class.java)
//            startActivity(intent1)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showSnack(isConnected: Boolean) {
        if (!isConnected) {
            val message = "Sorry! Not connected to internet"
            alertDialog.show()
            val color = Color.RED
            val parentView: View = findViewById(android.R.id.content)
            val snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
            val sbView = snackbar.view
            val textView: TextView = sbView.findViewById(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(color)
            snackbar.show()
        }
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance()?.setConnectivityListener(this)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }
}