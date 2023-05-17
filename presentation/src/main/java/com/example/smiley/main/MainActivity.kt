package com.example.smiley.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.databinding.ActivityMainBinding
import com.example.smiley.main.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private var _bind:ActivityMainBinding? = null
    private val bind:ActivityMainBinding get() = _bind!!

    private var homeFragment: HomeFragment? = null

    private var fragmentMap: HashMap<Int, Fragment?>? = hashMapOf(
        R.id.menu_home to homeFragment,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initBottomNavibar()
    }

    private fun initBottomNavibar() {
        bind.navbar.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_home -> changetFragment(R.id.menu_home)
                    R.id.menu_timetable -> changetFragment(R.id.menu_timetable)
                    R.id.menu_lab -> changetFragment(R.id.menu_lab)
                    R.id.menu_profile -> changetFragment(R.id.menu_profile)
                    else -> changetFragment(R.id.menu_home)
                }

                true
            }
            selectedItemId = R.id.menu_home
        }
    }

    private fun changetFragment(fragmentId: Int){
        fragmentMap?.let {
            it.forEach{ (key, value) ->
                if(key == fragmentId){
                    if(value == null){
                        it[key] = fragmentFactory(fragmentId)
                        addFragment(it[key])
                    }
                    showFragment(value)
                }
                else hideFragment(value)
            }
        }
    }

    private fun fragmentFactory(fragmentId: Int): Fragment {
        return when (fragmentId) {
            R.id.menu_home -> HomeFragment()
            R.id.menu_timetable -> HomeFragment()
            R.id.menu_lab -> HomeFragment()
            R.id.menu_profile -> HomeFragment()
            else -> HomeFragment()
        }
    }

    // Fragment 변경
    private fun<T: Fragment> addFragment(fragment: T?) {
        // 이전 버전까지 호환 가능하도록 getSupportFragmentManager() 사용
        fragment?.let {
            supportFragmentManager
                .beginTransaction() // 프래그먼트 변경을 위한 트랜잭션을 시작
                .add(R.id.frame_layout, it) // FrameLayout에 전달 받은 프래그먼트로 교체
                .commit() // 변경 사항 적용
        }
    }

    private fun<T: Fragment> showFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .show(it)
                .commit()
        }
    }

    private fun<T: Fragment> hideFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .hide(it)
                .commit()
        }
    }
}