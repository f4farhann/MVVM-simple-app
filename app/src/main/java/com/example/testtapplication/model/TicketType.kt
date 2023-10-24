import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TicketType(
    val typeId: String,
    val description: String,
    val createdOn: String,
    val updatedOn: String?
) {
    fun getCreatedOnDate(): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        return try {
            dateFormat.parse(createdOn)
        } catch (e: ParseException) {
            e.printStackTrace()
            // You can return a default date or throw a custom exception here
            null
        }
    }

}
