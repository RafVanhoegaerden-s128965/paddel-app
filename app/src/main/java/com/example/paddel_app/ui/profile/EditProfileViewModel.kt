package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileViewModel : ViewModel() {

    private val _db = FirebaseFirestore.getInstance()
    private val _currentUser = FirebaseAuth.getInstance().currentUser

    private val _user = MutableLiveData<User>()
    val user: MutableLiveData<User> get() = _user

    // Methode om het gebruikersprofiel bij te werken
    fun updateUserProfile(firstName: String?, lastName: String?, email: String?, birthDate: String?, gender: String?) {
        // Update lokale gebruikersgegevens
        _user.value = _user.value?.copy(
            firstName = firstName ?: _user.value?.firstName.orEmpty(),
            lastName = lastName ?: _user.value?.lastName.orEmpty(),
            email = email ?: _user.value?.email.orEmpty(),
            birthDate = birthDate ?: _user.value?.birthDate.orEmpty(),
            gender = gender ?: _user.value?.gender.orEmpty()
        )

        // Update het profiel in Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (!email.isNullOrBlank()) {
            currentUser?.verifyBeforeUpdateEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("EditProfileViewModel", "E-mail succesvol bijgewerkt in Firebase Authentication")

                        // Update de weergavenaam in Firebase Authentication
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName("${firstName.orEmpty()} ${lastName.orEmpty()}")
                            .build()

                        currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    Log.d("EditProfileViewModel", "Weergavenaam succesvol bijgewerkt in Firebase Authentication")
                                } else {
                                    Log.e("EditProfileViewModel", "Fout bij het bijwerken van de weergavenaam in Firebase Authentication", profileTask.exception)
                                }
                            }
                    } else {
                        Log.e("EditProfileViewModel", "Fout bij het bijwerken van het e-mailadres in Firebase Authentication", task.exception)
                    }
                }
        }

        // Update de gebruiker in Firestore
        val docRef = _db.collection("user").document(_currentUser!!.uid)
        val data = mutableMapOf<String, Any?>()

        if (!firstName.isNullOrBlank()) {
            data["firstName"] = firstName
        }

        if (!lastName.isNullOrBlank()) {
            data["lastName"] = lastName
        }

        if (!email.isNullOrBlank()) {
            data["email"] = email
        }

        if (!birthDate.isNullOrBlank()) {
            data["birthDate"] = birthDate
        }

        if (!gender.isNullOrBlank()) {
            data["gender"] = gender
        }

        docRef.update(data)
            .addOnSuccessListener {
                Log.d("EditProfileViewModel", "Gebruiker succesvol bijgewerkt in Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("EditProfileViewModel", "Fout bij het bijwerken van de gebruiker in Firestore", e)
            }
    }
}
