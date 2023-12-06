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

// Remove user argument to FIX crash!!!!
class ProfileViewModel(/*private val user: User*/) : ViewModel() {
  
class ProfileViewModel : ViewModel() {

    //region PrivateVariables
    private val _db = FirebaseFirestore.getInstance()
    private val _currentUser = FirebaseAuth.getInstance().currentUser

    private val _bestHand = MutableLiveData<Hand>()
    private val _courtPosition = MutableLiveData<CourtPosition>()
    private val _matchType = MutableLiveData<MatchType>()
    private val _preferredTime = MutableLiveData<PreferredTime>()
    //endregion

    //region PublicVariables
    
    // Functions
    fun setUserName(user: User) {
        Log.d("ProfileViewModel.SetUserName", "User: ${user.firstName} ${user.lastName}")

    val bestHand:MutableLiveData<Hand> get() = _bestHand
    val courtPosition:MutableLiveData<CourtPosition> get() = _courtPosition
    val matchType:MutableLiveData<MatchType> get() = _matchType
    val preferredTime:MutableLiveData<PreferredTime> get() = _preferredTime
    //endregion


    //region Functions

    //region Update
    fun updateBestHand(bestHand : Hand){
        // Get collection
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Map data
        val data = mapOf(
            "bestHand" to bestHand
        )

        // Update data
        docRef.update(data)
            .addOnSuccessListener {
            // Update successful
            Log.e("Update BestHand", "Best Hand: ${bestHand}")
        }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error updating document: $e")
            }
    }

    fun updateCourtPosition(courtPosition: CourtPosition){
        // Get collection
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Map data
        val data = mapOf(
            "courtPosition" to courtPosition
        )

        // Update data
        docRef.update(data)
            .addOnSuccessListener {
                // Update successful
                Log.e("Update BestHand", "Court Position: ${courtPosition}")
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error updating document: $e")
            }
    }

    fun updateMatchType(matchType: MatchType){
        // Get collection
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Map data
        val data = mapOf(
            "matchType" to matchType
        )

        // Update data
        docRef.update(data)
            .addOnSuccessListener {
                // Update successful
                Log.e("Update BestHand", "Match Type: ${matchType}")
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error updating document: $e")
            }
    }

    fun updatePreferredTime(preferredTime: PreferredTime){
        // Get collection
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Map data
        val data = mapOf(
            "preferredTime" to preferredTime
        )

        // Update data
        docRef.update(data)
            .addOnSuccessListener {
                // Update successful
                Log.e("Update BestHand", "PreferredTime: ${preferredTime}")
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error updating document: $e")
            }
    }
    //endregion

    //endregion
}
    fun getUserName(): String? {
        return _userName.value
    }

    fun updateBestHand(besthand: Hand) {
        // TODO Add logic to add to firebase
    }
}
