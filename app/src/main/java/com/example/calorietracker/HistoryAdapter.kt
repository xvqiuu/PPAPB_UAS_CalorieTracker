import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.History
import com.example.calorietracker.Menu
import com.example.calorietracker.databinding.ItemHistoryBinding

typealias OnClickUpdate = (History) -> Unit
class HistoryAdapter (private val listHistorys: List<History>,
                      private val onClickUpdate : OnClickUpdate) :
    RecyclerView.Adapter<HistoryAdapter.ItemHistoryViewHolder>(){

    inner class ItemHistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: History) {
            with(binding) {
                txtNamaMakanan.text = data.nama
                txtJumlahKalori.text = data.jumlah
                txtJamMakan.text = data.waktu

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id", data.id)
                    bundle.putString("waktu", data.waktu)
                    bundle.putString("nama", data.nama)
                    bundle.putString("takaran", data.takaran)
                    bundle.putString("jumlah", data.jumlah)

                    Toast.makeText(itemView.context, "You clicked on ${data.nama}",
                        Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemHistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),parent,
            false)
        return ItemHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listHistorys.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ItemHistoryViewHolder, position: Int) {
        holder.bind(listHistorys[position])
    }
}