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
}