package com.example.picksy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.DateFormat
import java.util.Date
import java.util.Random

class ProductDetailActivity : AppCompatActivity() /*, ConnectivityReceiver.ConnectivityReceiverListener
 */{

    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReferenceUserOrder: DatabaseReference
    private lateinit var mDatabaseReferenceUserDetail: DatabaseReference
    private lateinit var mDatabaseReferenceStatus: DatabaseReference
    private lateinit var mDatabaseReferenceServerOrder: DatabaseReference
    private lateinit var mDatabaseReferenceServerOrder2: DatabaseReference

    private lateinit var userId: String
    private lateinit var alertDialog: AlertDialog

    //    private var productSerial: ProductDesc? = null
    private lateinit var product: ProductObject

    private lateinit var mImageView: ImageView
    private lateinit var mProductName: TextView
    private lateinit var mProductPrice: TextView
    private lateinit var mProductDiscount: TextView
    private lateinit var mExpiryDate: TextView
    private lateinit var mDescription: TextView
    private lateinit var mOrder: Button

    private var account: UserAccount? = null
    private var status: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)  // Ensure this matches the XML file name

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

//        alertDialog = AlertDialog.Builder(this)
//            .setCancelable(false)
//            .setTitle("No Internet Connection")
//            .setMessage("We cannot detect any internet connection. Please check your internet connection and try again.")
//            .setPositiveButton("Retry") { _, _ -> showSnack(isNetworkAvailable()) }
//            .setNegativeButton("Close") { _, _ ->
//                finish()
//                onBackPressed()
//            }
//            .create()
//
//        showSnack(isNetworkAvailable())

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        mFirebaseUser =
            mFirebaseAuth.currentUser ?: throw IllegalStateException("FirebaseUser is null")
        userId = mFirebaseUser.uid

        mDatabaseReferenceStatus =
            mFirebaseDatabase.reference.child("Customer").child(userId).child("DetailPresent")
        mDatabaseReferenceUserDetail =
            mFirebaseDatabase.reference.child("Customer").child(userId).child("accountDetail")

        // Map XML components to Kotlin variables
        mImageView = findViewById(R.id.image)
        mProductName = findViewById(R.id.product)
        mProductPrice = findViewById(R.id.price)
        mProductDiscount = findViewById(R.id.discount)
        mExpiryDate = findViewById(R.id.expiryDate)
        mDescription = findViewById(R.id.desciption)
        mOrder = findViewById(R.id.addToCart)

//        val intent = intent
//        if (intent.hasExtra("ProductDetail")) {
//            productSerial = intent.getSerializableExtra("ProductDetail") as ProductDesc
//        }
//
//        product = ProductObject()
//        productSerial?.let {
//            product.apply {
//                discountPrecentage = it.discountPrecentage
//                expiryDate = it.expiryDate
//                productName = it.productName
//                mrpPrice = it.mrpPrice
//                discountPrice = it.discountPrice
//                description = it.description
//                url = it.url
//            }
//
//            Glide.with(mImageView.context)
//                .load(it.url)
//                .into(mImageView)
//
//            mProductName.text = product.productName
//            mProductPrice.text = product.discountPrice.toString()
//            mProductDiscount.text = product.discountPrecentage
//            mExpiryDate.text = product.expiryDate
//            mDescription.text = product.description
//
//            mDatabaseReferenceStatus.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    status = dataSnapshot.getValue(Boolean::class.java)
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {}
//            })
//
//            mOrder.setOnClickListener {
//                if (status == false) {
//                    // Uncomment and adjust the following line if you have AccountActivity
//                    // startActivity(Intent(this@ProductDetailActivity, AccountActivity::class.java))
//                } else {
//                    mDatabaseReferenceUserDetail.addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            account = dataSnapshot.getValue(UserAccount::class.java)
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {}
//                    })
//
//                    val mBuilder = AlertDialog.Builder(this@ProductDetailActivity)
//                    val mView = layoutInflater.inflate(R.layout.dialog_to_payment, null)
//
//                    val mEditText: EditText = mView.findViewById(R.id.et_amount_detail)
//                    val mRadioGroup: RadioGroup = mView.findViewById(R.id.rg_payment_detail)
//                    val mSubmit: Button = mView.findViewById(R.id.bt_submit_payment)
//                    val mCancel: Button = mView.findViewById(R.id.bt_cancel_payment)
//
//                    mBuilder.setView(mView)
//
//                    val dialog = mBuilder.create()
//                    dialog.show()
//
//                    mSubmit.setOnClickListener {
//                        val createOrder = CreateOrder()
//                        val amount = mEditText.text.toString()
//                        val methodOfPayment: String?
//                        val totalAmount: Int
//
//                        if (amount.isEmpty()) return@setOnClickListener
//
//                        val count = amount.toInt()
//                        if (count > 0) {
//                            val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
//
//                            createOrder.apply {
//                                pincode = account?.pincode
//                                landSmark = account?.landmark
//                                status = "NotDeliver"
//                                productName = product.productName
//                                this.amount = count
//                                customername = account?.name
//                                customerNo = account?.phoneno
//                                price = product.discountPrice
//                                totalCost = count * product.discountPrice
//                                deliverydate = "N"
//                                dispatchdate = "N"
//                                orderdate = currentDateTimeString
//                                userid = userId
//                                address = account?.address
//                                url = product.url
//                                paymentmethod = when (mRadioGroup.checkedRadioButtonId) {
//                                    R.id.rb_cod_detail -> "cod"
//                                    R.id.rb_online_detail -> "online"
//                                    else -> null
//                                }
//                                paymentstatus = if (paymentmethod == "online") "Y" else "N"
//                            }
//
//                            val random = Random().nextInt(99999999).toString()
//                            mDatabaseReferenceUserOrder = mFirebaseDatabase.reference.child("Customer").child(userId).child("Orders")
//                            mDatabaseReferenceUserOrder.child(random).setValue(createOrder)
//
//                            mDatabaseReferenceServerOrder2 = mFirebaseDatabase.reference.child("CompanyOrder").child(userId)
//                            mDatabaseReferenceServerOrder2.child(random).setValue(createOrder)
//
//                            mDatabaseReferenceServerOrder = mFirebaseDatabase.reference.child("Company").child("Orders").child(userId)
//                            mDatabaseReferenceServerOrder.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    val counter = dataSnapshot.getValue(Int::class.java) ?: -1
//                                    if (counter == -1) {
//                                        mDatabaseReferenceServerOrder.setValue(1)
//                                    } else {
//                                        mDatabaseReferenceServerOrder.setValue(counter + 1)
//                                    }
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {}
//                            })
//
//                            Toast.makeText(applicationContext, "Order successfully placed", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(applicationContext, "Order not placed", Toast.LENGTH_LONG).show()
//                        }
//                        dialog.dismiss()
//                    }
//
//                    mCancel.setOnClickListener {
//                        dialog.dismiss()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun isNetworkAvailable(): Boolean {
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }
//
//    private fun showSnack(isConnected: Boolean) {
//        if (!isConnected) {
//            val message = "Sorry! Not connected to internet"
//            alertDialog.show()
//            val color = Color.RED
//            val parentView = findViewById<View>(android.R.id.content)
//            val snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
//
//            val sbView = snackbar.view
//            val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//            textView.setTextColor(color)
//            snackbar.show()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        MyApplication.getInstance()?.setConnectivityListener(this)
//    }
//
//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showSnack(isConnected)
//    }
    }
}