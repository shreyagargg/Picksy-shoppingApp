package com.example.picksy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemList = arrayListOf(
            Item(R.drawable.book, "Study Material"),
            Item(R.drawable.menu, "Item 1"),
            Item(R.drawable.menu, "Item 2"),
            Item(R.drawable.menu, "Item 3"),
            Item(R.drawable.menu, "Item 4"),
            Item(R.drawable.menu, "Item 5"),
            Item(R.drawable.menu, "Item 6"),
            Item(R.drawable.menu, "Item 7")
        )

        val recyclerViewBottom: RecyclerView = findViewById(R.id.recyclerViewBottom)
        recyclerViewBottom.layoutManager = GridLayoutManager(this,2)
        recyclerViewBottom.adapter = ItemAdapter(this,itemList)


        val recyclerViewTop: RecyclerView = findViewById(R.id.recyclerViewTop)
        recyclerViewTop.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTop.adapter = ItemAdapter(this,itemList)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.nav)

//        findViewById<ImageView>(R.id.menu).setOnClickListener {
////            drawerLayout.open()
//            drawerLayout.openDrawer(GravityCompat.START)
//        }

        // Set up navigation item selection
//        private fun loadFragment(fragment: Fragment) {
//            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container, fragment)
//            transaction.addToBackStack(null) // Optional: Add to back stack if you want back navigation
//            transaction.commit()
//        }

//        val fm = FragmentManager
//        navigationView.setNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.menu1 -> {
////                    startActivity(Intent(this, ProfileActivity::class.java))
////                    loadFragment(ProfileActivity)
////                    val intent = Intent(this, ProfileActivity::class.java)
////                    startActivity(intent)
//                    // Handle home navigation
//                }
//                R.id.menu2 -> {
//                    // Handle settings navigation
//                }
//            }
////            drawerLayout.close()
//            drawerLayout.closeDrawer(GravityCompat.START)
//            true
//        }

//        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
//            if (item.itemId == R.id.menu_chat) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frame_layout, chatFragment).commit()
//            }
//            if (item.itemId == R.id.menu_profile) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frame_layout, profileFragment).commit()
//            }
//            true
//        })


//        val iv: ImageView = findViewById(R.id.menu)
//        iv.setOnClickListener {
//            val intent = Intent(this, NavigationDrawer::class.java)
//            startActivity(intent)
//        }

        val profile = findViewById<ImageView>(R.id.profile)
        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

        }
        }
    }

