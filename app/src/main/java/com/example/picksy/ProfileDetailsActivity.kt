package com.example.picksy

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileDetailsActivity : AppCompatActivity(), View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private var userAccount: UserAccount? = null
    private var userId: String? = null

    private var alertDialog: AlertDialog? = null
    private var isSubmitted = false

    private lateinit var mEditTextName: EditText
    private lateinit var mEditTextAddress: EditText
    private lateinit var mEditTextPhoneNo: EditText
    private lateinit var mEditTextLandmark: EditText
    private lateinit var mEditTextCity: EditText
    private lateinit var mEditTextPincode: EditText

    private lateinit var mTextViewName: TextView
    private lateinit var mTextViewAddress: TextView
    private lateinit var mTextViewPhoneNo: TextView
    private lateinit var mTextViewLandmark: TextView
    private lateinit var mTextViewCity: TextView
    private lateinit var mTextViewPincode: TextView

    private lateinit var mLinearLayoutEditable: LinearLayout
    private lateinit var mLinearLayoutNonEditable: LinearLayout

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mDatabaseReferenceToUser: DatabaseReference
    private lateinit var mDatabaseReferenceCheckDetail: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("No Internet Connection")
            .setMessage("We cannot detect any internet connection. Please check your internet connection and try again.")
            .setPositiveButton("Retry") { _, _ -> showSnack(isNetworkAvailable()) }
            .setNegativeButton("Close") { dialog, _ ->
                finish()
                onBackPressed()
                dialog.cancel()
            }
            .create()

        showSnack(isNetworkAvailable())

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser ?: throw IllegalStateException("FirebaseUser is null")
        userId = mFirebaseUser.uid

        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReferenceToUser = mFirebaseDatabase.reference.child("Customer").child(userId!!).child("accountDetail")
        mDatabaseReferenceCheckDetail = mFirebaseDatabase.reference.child("Customer").child(userId!!).child("DetailPresent")

        // Initialize views
//        mLinearLayoutEditable = findViewById(R.id.ll_editable)
//        mLinearLayoutNonEditable = findViewById(R.id.ll_noneditable)

        mEditTextName = findViewById(R.id.name)
        mEditTextAddress = findViewById(R.id.address)
        mEditTextPhoneNo = findViewById(R.id.number)
        mEditTextLandmark = findViewById(R.id.landMark)
        mEditTextCity = findViewById(R.id.city)
        mEditTextPincode = findViewById(R.id.pinCode)

//        mTextViewName = findViewById(R.id.tv_name)
//        mTextViewAddress = findViewById(R.id.tv_address)
//        mTextViewPhoneNo = findViewById(R.id.tv_phone_number)
//        mTextViewLandmark = findViewById(R.id.tv_landmark)
//        mTextViewCity = findViewById(R.id.tv_city)
//        mTextViewPincode = findViewById(R.id.tv_pincode)

        findViewById<Button>(R.id.submit).setOnClickListener(this)

        mDatabaseReferenceCheckDetail.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bool = dataSnapshot.getValue(Boolean::class.java) ?: false
                isSubmitted = bool
                if (isSubmitted) {
                    mLinearLayoutEditable.visibility = View.INVISIBLE
                    mLinearLayoutNonEditable.visibility = View.VISIBLE

                    mDatabaseReferenceToUser.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            userAccount = dataSnapshot.getValue(UserAccount::class.java)
                            userAccount?.let {
                                updateUIWithUserData(it)
                            }
                            mDatabaseReferenceCheckDetail.setValue(true)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.submit -> {
                userAccount = UserAccount().apply {
                    name = mEditTextName.text.toString()
                    address = mEditTextAddress.text.toString()
                    phoneno = mEditTextPhoneNo.text.toString()
                    city = mEditTextCity.text.toString()
                    landmark = mEditTextLandmark.text.toString()
                    pincode = mEditTextPincode.text.toString()
                }
                mDatabaseReferenceToUser.setValue(userAccount)

                mLinearLayoutEditable.visibility = View.INVISIBLE
                mLinearLayoutNonEditable.visibility = View.VISIBLE

                mDatabaseReferenceToUser.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        userAccount = dataSnapshot.getValue(UserAccount::class.java)
                        userAccount?.let {
                            updateUIWithUserData(it)
                        }
                        mDatabaseReferenceCheckDetail.setValue(true)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showSnack(isConnected: Boolean) {
        val message = if (isConnected) "" else "Sorry! Not connected to internet"
        if (!isConnected) {
            alertDialog?.show()
            val parentView: View = findViewById(android.R.id.content)
            val snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(Color.RED)
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

    private fun updateUIWithUserData(userAccount: UserAccount) {
        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(userAccount.name).build()
        mFirebaseUser.updateProfile(userProfileChangeRequest)

//        mTextViewName.text = userAccount.name
//        mTextViewAddress.text = userAccount.address
//        mTextViewPhoneNo.text = userAccount.phoneno
//        mTextViewCity.text = userAccount.city
//        mTextViewPincode.text = userAccount.pincode
//        mTextViewLandmark.text = userAccount.landmark
    }
}
