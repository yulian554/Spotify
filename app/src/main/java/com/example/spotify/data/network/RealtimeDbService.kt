package com.example.spotify.data.network

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.spotify.ui.view.SplashScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class RealtimeDbService{

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = FirebaseDatabase.getInstance()

    fun savePhotoRealtime() {
                databaseReference = database?.reference!!.child(auth.currentUser?.uid!!)
                val currentUSerDb = databaseReference?.child(auth.currentUser?.uid!!)
                currentUSerDb?.child("imageProfile")?.setValue(SplashScreenActivity.prefs.getPhotoUser())
    }

    fun getPhotoForShow(context: Context, setImage: CircleImageView, progress: MutableLiveData<Boolean>) {
        progress.value = true
        databaseReference = database?.reference!!.child(auth.currentUser?.uid!!)
        val userReference = databaseReference?.child(auth.currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                context.let {
                    Glide.with(context)
                        .load(snapshot.child("imageProfile").value.toString())
                        .into(setImage)
                }
                progress.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
