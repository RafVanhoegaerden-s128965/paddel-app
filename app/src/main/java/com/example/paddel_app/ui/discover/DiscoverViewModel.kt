import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.google.firebase.firestore.FirebaseFirestore

class DiscoverViewModel : ViewModel() {
    private val bookingsList = MutableLiveData<List<Booking>>()

    private val _courtLiveData = MutableLiveData<Court>()
    val courtLiveData: LiveData<Court> get() = _courtLiveData

    fun setBookingsList(bookings: List<Booking>) {
        bookingsList.value = bookings
    }
    fun getBookingsList(): LiveData<List<Booking>> {
        return bookingsList
    }

    fun fetchCourtInformation(courtId: String) {
        val db = FirebaseFirestore.getInstance()
        val courtsCollection = db.collection("courts")

        // Query court information for the specified courtId
        courtsCollection
            .document(courtId)
            .get()
            .addOnSuccessListener { courtSnapshot ->
                if (courtSnapshot.exists()) {
                    // Convert the court data to your Court model (replace Court::class.java with your actual model class)
                    val court = courtSnapshot.toObject(Court::class.java)

                    // Update LiveData with court data
                    if (court != null) {
                        _courtLiveData.postValue(court!!)
                    }
                } else {
                    Log.e("BookingDetailsViewModel", "Court not found for courtId: $courtId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("BookingDetailsViewModel", "Error fetching court information", e)
            }
    }
}