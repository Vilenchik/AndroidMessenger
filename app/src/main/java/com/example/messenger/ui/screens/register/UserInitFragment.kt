package com.example.messenger.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.messenger.R
import com.example.messenger.database.*
import com.example.messenger.utilits.AppValueEventListener
import com.example.messenger.utilits.restartActivity
import com.example.messenger.utilits.showToast
import kotlinx.android.synthetic.main.fragment_user_init.*

class UserInitFragment(val phone: String) : Fragment(R.layout.fragment_user_init) {


    override fun onStart() {
        super.onStart()
        UserRun()
    }

    fun UserRun() {

        val uid = AUTH.currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_ID] = uid
        dateMap[CHILD_PHONE] = phone

        REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
            .addListenerForSingleValueEvent(AppValueEventListener {

                if (!it.hasChild(CHILD_USERNAME)) {
                    dateMap[CHILD_USERNAME] = uid
                }

                REF_DATABASE_ROOT.child(
                    NODE_PHONES
                ).child(phone).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(
                            NODE_USERS
                        ).child(uid).updateChildren(dateMap)
                            .addOnSuccessListener {
                                app_init_btn.setOnClickListener { restartActivity() }
                            }
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
            })


    }


}