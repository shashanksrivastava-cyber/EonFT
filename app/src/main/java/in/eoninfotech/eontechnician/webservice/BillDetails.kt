package `in`.eoninfotech.eontechnician.webservice

data class BillDetails(
    @JvmField val bill_no: String,
    @JvmField val bill_amt: String,
    @JvmField val bill_date: String,
    @JvmField val app_amt: String,
    @JvmField val app_date: String,
    @JvmField val rej_date: String,
    @JvmField val canc_date: String,
    @JvmField val rec_date: String,
    @JvmField val status: String,
    @JvmField val remarks: String,
    @JvmField val action_by: String,

){

}
