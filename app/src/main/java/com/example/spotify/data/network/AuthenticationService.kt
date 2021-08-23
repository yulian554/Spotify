package com.example.spotify.data.network

import com.example.spotify.ui.view.SplashScreenActivity.Companion.prefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

open class AuthenticationService {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = FirebaseDatabase.getInstance()

    fun saveNameAndPhotoWhenStartingAssignment() {
        databaseReference = database?.reference!!.child(auth.currentUser?.uid!!)
        val userReference = databaseReference?.child(auth.currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prefs.saveNameUser(snapshot.child("Name").value.toString())
                prefs.savePhotoUser(snapshot.child("imageProfile").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun saveDataUser(saveNameDb: String, saveSurnameDb: String) {
        databaseReference = database?.reference!!.child(auth.currentUser?.uid!!)
        val currentUSerDb = databaseReference?.child(auth.currentUser?.uid!!)
        currentUSerDb?.child("Name")?.setValue(saveNameDb)
        currentUSerDb?.child("Surname")?.setValue(saveSurnameDb)
        currentUSerDb?.child("imageProfile")?.setValue("")
    }
}

