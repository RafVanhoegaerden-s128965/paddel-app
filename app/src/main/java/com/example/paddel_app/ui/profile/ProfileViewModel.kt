package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.enum.CourtPosition
import com.example.paddel_app.enum.Hand
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.enum.PreferredTime
import com.example.paddel_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

// Remove user argument to FIX crash!!!!
class ProfileViewModel(/*private val user: User*/) : ViewModel() {

    //region PrivateVariables
    private val _db = FirebaseFirestore.getInstance()
    private val _user = MutableLiveData<User>()

    private val _userName = MutableLiveData<String>()
    private val _bestHand = MutableLiveData<Hand>()
    private val _courtPosition = MutableLiveData<CourtPosition>()
    private val _matchType = MutableLiveData<MatchType>()
    private val _preferredTime = MutableLiveData<PreferredTime>()
    //endregion

    //region PublicVariables
    val userName: MutableLiveData<String> get() = _userName
    val bestHand:MutableLiveData<Hand> get() = _bestHand
    val courtPosition:MutableLiveData<CourtPosition> get() = _courtPosition
    val matchType:MutableLiveData<MatchType> get() = _matchType
    val preferredTime:MutableLiveData<PreferredTime> get() = _preferredTime
    //endregion


    //region Functions

    // TODO fix Function if user is getted
    fun setUserName(user : User){
        Log.d("ProfileViewModel.SetUserName", "User: ${user.firstName} ${user.lastName}")

        val userName = "${user.firstName} ${user.lastName}"

        _userName.value = userName
    }

    //region Update
    fun updateBestHand(userId: String, bestHand : Hand){
        // TODO Add logic to add to firebase
//        val docRef = db.collection("user").document(userId)

//        val data = mapOf(
//            "bestHand" to bestHand
//        )
//
//        docRef.update(data)
//            .addOnSuccessListener {
//            // Update successful
//            Log.e("Update BestHand", "Best Hand: ${bestHand}")
//        }
//            .addOnFailureListener { e ->
//                // Handle the error
//                println("Error updating document: $e")
//            }
    }

    fun updateCourtPosition(userId: String, courtPosition: CourtPosition){
        // TODO Add logic to add to firebase
    }

    fun updateMatchType(userId: String, matchType: MatchType){
        // TODO Add logic to add to firebase
    }

    fun updatePreferredTime(userId: String, preferredTime: PreferredTime){
        // TODO Add logic to add to firebase
    }
    //endregion

    //endregion

    fun getUser(callback: (User?) -> Unit) {
        // Create Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Get userId
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("currentUser", "user: ${user.email.toString()}")
        }
        val userId = user!!.uid

        // Get document
        val docRef = db.collection("user").document(userId)

        // Get the User object
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Convert Firestore document to User object
                val user = document.toObject<User>()
                callback(user)
            } else {
                callback(null)
            }
        }.addOnFailureListener { e ->
            Log.e("MainActivity.User", "Error getting document: $e")
            callback(null)
        }
    }
}