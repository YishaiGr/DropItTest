package com.example.dropittest.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.dropittest.R
import com.example.dropittest.App
import kotlinx.android.synthetic.main.activity_drop_it.*
import kotlinx.android.synthetic.main.custom_title_layout.*
import javax.inject.Inject

class DropActivity : AppCompatActivity() {

    @Inject lateinit var dropViewModel: DropViewModel
    lateinit var dropComponent: DropComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_it)
        setSupportActionBar(toolbar)

        dropComponent = (application as App).appComponent.dropComponent().create()
        dropComponent.inject(this)

        setObservers()
        back_btn.setOnClickListener { onBackPressed() }
    }

    private fun setObservers(){
        dropViewModel.handleErrorLiveData.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                handleError(message)
            }
        })
        dropViewModel.enableBackButtonLiveData.observe(this, Observer {on ->
            enableBackButton(on)
        })
        dropViewModel.titleLiveData.observe(this, Observer { title ->
            app_title_tv.text = title
        })
        dropViewModel.enableLoadingLiveData.observe(this, Observer {on ->
            enableLoading(on)
        })
    }

    private fun enableLoading(on: Boolean){
        if(on){
            loading_lottie.visibility = View.VISIBLE
            (loading_lottie as LottieAnimationView).speed = 2.0f
            (loading_lottie as LottieAnimationView).playAnimation()
            disableViews(on)
        }
        else{
            loading_lottie.visibility = View.GONE
            (loading_lottie as LottieAnimationView).cancelAnimation()
            disableViews(on)
        }
    }

    private fun disableViews(on: Boolean){
        val layout: ConstraintLayout? =
            when(this.findNavController(R.id.nav_host_fragment).currentDestination?.id){
            R.id.contactFragment -> findViewById<TextView>(
                R.id.instruction_contact_tv
            ).parent as ConstraintLayout
            R.id.addressFragment -> findViewById<TextView>(
                R.id.instruction_address_tv
            ).parent as ConstraintLayout
            R.id.bagsFragment -> findViewById<Button>(
                R.id.save_bags_bags_btn
            ).parent as ConstraintLayout
            else -> null
        }
        layout?.let {
            for (child in layout.children)
                child.isEnabled = on.not()
        }
    }

    private fun enableBackButton(on: Boolean){
        if (on)
            back_btn.visibility = View.VISIBLE
        else
            back_btn.visibility = View.INVISIBLE
    }

    private fun handleError(message: String?){
        if (message!=null)
            showErrorFragment(message)
        else
            showErrorFragment(getString(R.string.some_error_occurred_message))
    }

    private fun showErrorFragment(message: String){
        dropViewModel.setErrorMessage(message)
        val navController = this.findNavController(R.id.nav_host_fragment)
        when(navController.currentDestination?.id){
            R.id.contactFragment -> navController.navigate(
                R.id.action_contactFragment_to_errorFragment
            )
            R.id.addressFragment -> navController.navigate(
                R.id.action_addressFragment_to_errorFragment
            )
            R.id.bagsFragment -> navController.navigate(
                R.id.action_bagsFragment_to_errorFragment
            )
        }
    }
}