import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Game

class DiscoverViewModel : ViewModel() {
    private val bookingsList = MutableLiveData<List<Booking>>()
    private val gamesList = MutableLiveData<List<Game>>()

    fun setGamesList(games: List<Game>){
        gamesList.value = games
    }
    fun getGamesList():LiveData<List<Game>>{
        return gamesList
    }

    fun setBookingsList(bookings: List<Booking>) {
        bookingsList.value = bookings
    }
    fun getBookingsList(): LiveData<List<Booking>> {
        return bookingsList
    }
}