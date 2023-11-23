import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // Gebruik MutableLiveData om de waarde dynamisch te kunnen wijzigen
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    // Functie om de gebruikerse-mail in te stellen
    fun setUserEmail(email: String) {
        _email.value = email
    }
}