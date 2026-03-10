package `in`.eoninfotech.eontechnician.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.activity.ServiceRequestDetails
import `in`.eoninfotech.eontechnician.databinding.ServiceRequestAdapterBinding
import `in`.eoninfotech.eontechnician.responses.ServiceCountDetailResponse

class ServiceRequestAdapter(
    private val activityType: String?,
    private val onViewDetailsClick: (ServiceCountDetailResponse) -> Unit
) : RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder>() {

    private val list = mutableListOf<ServiceCountDetailResponse>()

    fun submitList(newList: List<ServiceCountDetailResponse>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ServiceRequestAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ServiceRequestAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.binding.apply {

            tvRequestNo.text = "REQ #${item.req_no}"

            tvClientName.text = item.main_customer

            tvPlant.text = item.sub_customer

            tvLocation.text = item.location

            tvDate.text = item.request_date

            //tvRemarks.text = item.re ?: "-"

            // STATUS
            val status = item.status?.uppercase()

            tvStatus.text = status

            when (status) {

                "COMPLETED" -> {

                    statusBadge.setCardBackgroundColor(
                        Color.parseColor("#E8F5E9")
                    )

                    tvStatus.setTextColor(
                        Color.parseColor("#2E7D32")
                    )
                }

                "PENDING", "REJECTED" -> {

                    statusBadge.setCardBackgroundColor(
                        Color.parseColor("#FFEBEE")
                    )

                    tvStatus.setTextColor(
                        Color.parseColor("#C62828")
                    )
                }
            }

            // INSTALL / REMOVAL TEXT

            val totalReq = item.total_req ?: 0
            val pendingReq = item.pending_req ?: 0

            val typeText =
                if (activityType == "I") "installation"
                else "removal"

            tvInstallCountBadge.text =
                "$pendingReq out of $totalReq $typeText pending"

            cardRoot.setOnClickListener {

                onViewDetailsClick(item)

            }

        }

    }

    override fun getItemCount(): Int = list.size
}