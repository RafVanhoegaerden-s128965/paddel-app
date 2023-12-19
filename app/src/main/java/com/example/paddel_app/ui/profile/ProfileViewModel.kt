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

class ProfileViewModel : ViewModel() {

    //region PrivateVariables
    private val _db = FirebaseFirestore.getInstance()
    private val _currentUser = FirebaseAuth.getInstance().currentUser

    private val _userName = MutableLiveData<String>()

    private val _bestHand = MutableLiveData<Hand?>()
    private val _courtPosition = MutableLiveData<CourtPosition?>()
    private val _matchType = MutableLiveData<MatchType?>()
    private val _preferredTime = MutableLiveData<PreferredTime?>()
    //endregion

    //region PublicVariables
    val userName: MutableLiveData<String> get() = _userName
    val bestHand: MutableLiveData<Hand?> get() = _bestHand
    val courtPosition: MutableLiveData<CourtPosition?> get() = _courtPosition
    val matchType: MutableLiveData<MatchType?> get() = _matchType
    val preferredTime: MutableLiveData<PreferredTime?> get() = _preferredTime
    //endregion

    fun setUser(user: User) {
        Log.d("ProfileViewModel.SetUserName", "User: ${user.firstName} ${user.lastName}")
        val userName = "${user.firstName.capitalize()} ${user.lastName.capitalize()}"
        _userName.value = userName
        _bestHand.value = user.bestHand
        _courtPosition.value = user.courtPosition
        _matchType.value = user.matchType
        _preferredTime.value = user.preferredTime
    }


    //region Update
    fun updateBestHand(bestHand: Hand) {
        // Get user document
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Data which needs to be updated
        val data = mapOf(
            "bestHand" to bestHand
        )

        // Update data
        docRef.update(data).addOnSuccessListener {
            // Call the success callback
            Log.d("ProfileViewModel", "Best hand updated successfully")
        }
            .addOnFailureListener { e ->
                // Call the failure callback
                Log.e("ProfileViewModel.BestHand", "Error getting document: $e")

            }

        // Set data value
        _bestHand.value = bestHand
    }

    fun updateCourtPosition(courtPosition: CourtPosition) {
        // Get user document
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Data which needs to be updated
        val data = mapOf(
            "courtPosition" to courtPosition
        )

        // Update data
        docRef.update(data).addOnSuccessListener {
            // Call the success callback
            Log.d("ProfileViewModel", "Court Position updated successfully")
        }
            .addOnFailureListener { e ->
                // Call the failure callback
                Log.e("ProfileViewModel.CourtPosition", "Error getting document: $e")
            }

        // Set data value
        _courtPosition.value = courtPosition
    }

    fun updateMatchType(matchType: MatchType) {
        // Get user document
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Data which needs to be updated
        val data = mapOf(
            "matchType" to matchType
        )

        // Update data
        docRef.update(data).addOnSuccessListener {
            // Call the success callback
            Log.d("ProfileViewModel", "Match Type updated successfully")
        }
            .addOnFailureListener { e ->
                // Call the failure callback
                Log.e("ProfileViewModel.MatchType", "Error getting document: $e")
            }

        // Set data value
        _matchType.value = matchType
    }

    fun updatePreferredTime(preferredTime: PreferredTime) {
        // Get user document
        val docRef = _db.collection("user").document(_currentUser!!.uid)

        // Data which needs to be updated
        val data = mapOf(
            "preferredTime" to preferredTime
        )

        // Update data
        docRef.update(data).addOnSuccessListener {
            // Call the success callback
            Log.d("ProfileViewModel", "Preferred Time updated successfully")
        }
            .addOnFailureListener { e ->
                // Call the failure callback
                Log.e("ProfileViewModel.PreferredTime", "Error getting document: $e")
            }

        // Set data value
        _preferredTime.value = preferredTime
    }
    //endregion
}
