package `in`.eoninfotech.eontechnician.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.adapters.ServiceRequestAdapter
import `in`.eoninfotech.eontechnician.databinding.ActivityServiceRequestBinding
import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.helper.DateRangePickerHelper
import `in`.eoninfotech.eontechnician.responses.ClientDetails
import `in`.eoninfotech.eontechnician.responses.ClientLocationDetail
import `in`.eoninfotech.eontechnician.responses.MainClientList
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import `in`.eoninfotech.eontechnician.view.ServiceRequestUiState
import `in`.eoninfotech.eontechnician.viewModel.ServiceRequestViewModel
import `in`.eoninfotech.eontechnician.viewModel.ViewModelClientLocation
import `in`.eoninfotech.eontechnician.viewModel.ViewModelMainClient
import `in`.eoninfotech.eontechnician.viewModel.ViewModelSubClient
import jakarta.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ServiceRequestActivity : AppCompatActivity() {

    @Inject
    lateinit var prefManager: SharedPreferenceManager
    @Inject
    lateinit var appPrefs: AppPreferences
    @Inject
    lateinit var locationPrefs: LocationPrefs
    @Inject
    lateinit var sharedPref: SharedPreferenceManager
    @Inject
    lateinit var telephonyManager: TelephonyManager
    @Inject
    lateinit var chk: CheckConnection
    lateinit var binding: ActivityServiceRequestBinding
    lateinit var viewModelMainClient: ViewModelMainClient
    lateinit var viewmodelSubClient: ViewModelSubClient
    lateinit var viewmodelClientLocation: ViewModelClientLocation

    lateinit var serviceRequestViewModel : ServiceRequestViewModel

    private val mainclientList: ArrayList<MainClientList> = ArrayList<MainClientList>()

    private val subClientList: ArrayList<ClientDetails> = ArrayList<ClientDetails>()
    private val mainClientDetail = ArrayList<String?>()

    private val subClientDetail = ArrayList<String?>()
    private var adapter: ArrayAdapter<String?>? = null

    var startDateLong: Long? = null
    var endDateLong: Long? = null

    var apiStartDate: String? = null
    var apiEndDate: String? = null
    var mainClientId: String? = null

    private var name: String? = ""
    private var clientId: String? = ""
    private var id_dist: String? = ""
    private var server_name: String? = ""
    private var db_name: String? = ""
    private var usrname: String?=""
    private var zone: String?=""
    private val locationDetail = ArrayList<String?>()
    private val locationList: ArrayList<ClientLocationDetail> = ArrayList<ClientLocationDetail>()
    private lateinit var serviceAdapter: ServiceRequestAdapter
    private var isLoading = false
    private var isLastPage = false
    private val pageSize = 10


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityServiceRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        binding.filterLay.isVisible = false

        usrname = sharedPref.getUsername()
        zone = sharedPref.getZone()

        name = intent.getStringExtra("activity_type")

        binding.etDateRange.apply {
            isFocusable = false
            isClickable = true
            isCursorVisible = false
        }

        setDefaultTodayDate()

        initViewModels()
        setupRecyclerView()
        observeServiceRequests()
        fetchServiceRequests()

        if (chk.isConnected()) {
            fetchMainClients()
        } else {
            chk.showConnectionErrorDialog()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (name.equals("I")) {
                title = "Installation Request "
                // If you want a more premium look, set elevation to 0
            } else {
                title = "Removal Request"
                // If you want a more premium look, set elevation to 0
            }
            elevation = 0f

        }
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            }
        )

        binding.swipeRefresh.setOnRefreshListener { fetchServiceRequests(loadMore = true) }

        binding.btnSearch.setOnClickListener {
            binding.filterLay.visibility = View.GONE
            serviceRequestViewModel.fetchServiceRequestDetails(
                username = usrname,
                zone = zone,
                activityType = name,
                mainCustomer = mainClientId,
                subCustomer = clientId,
                location = id_dist,
                date = apiStartDate,
                loadMore = false
            )
        }

        binding.btnClearFilter.setOnClickListener {

            mainClientId = ""
            clientId = ""
            id_dist = ""
            apiStartDate = ""
            apiEndDate = ""

            // Reset UI fields
            binding.mainClient.setSelection(0)
            binding.newInClients.setSelection(0)
            binding.newInLocations.setSelection(0)
            binding.etDateRange.setText("")
            binding.filterLay.visibility = View.GONE

            // Optionally reload default data
            serviceRequestViewModel.fetchServiceRequestDetails(
                username = usrname,
                zone = zone,
                activityType = name,
                mainCustomer = mainClientId,
                subCustomer = clientId,
                location = id_dist,
                date = apiStartDate,
                loadMore = false
            )
        }

        binding.etDateRange.setOnClickListener {

            DateRangePickerHelper.showDateRangePicker(
                supportFragmentManager,
                startDateLong,
                endDateLong
            ) { display, apiStart, apiEnd, startLong, endLong ->

                binding.etDateRange.setText(display)

                apiStartDate = apiStart
                apiEndDate = apiEnd

                startDateLong = startLong
                endDateLong = endLong
            }
            //openDateRangePicker()
        }
        binding.dateRangeLayout.setEndIconOnClickListener {

            binding.etDateRange.text = null

            startDateLong = null
            endDateLong = null

            apiStartDate = null
            apiEndDate = null
        }
        addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.filter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                Log.d("FILTER", "Filter clicked")
                if (menuItem.itemId == R.id.menu_filter) {
                    binding.filterLay.isVisible = !binding.filterLay.isVisible
                    return true
                }
                return false
            }

        }, this, Lifecycle.State.RESUMED)

        binding.mainClient.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                var i = i
                if (i == 0) {
                    return
                } else {
                    i = i - 1
                }
                mainClientId = mainclientList.get(i).getClient_Id().toString()
                fetchSubClients()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.newInClients.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                if (i == 0) return
                val client: ClientDetails = subClientList.get(i - 1)!!
                clientId = client.getClient_Id().toString()
                id_dist = client.getId_dist()
                server_name = client.getServer_name()
                db_name = client.getDb_name()
                fetchLocations()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

//    Observe UI State (Activity / Fragment)
//
//    Now your UI has only one observer.

    private fun observeServiceRequests() {

        binding.swipeRefresh.setRefreshing(false)

        lifecycleScope.launchWhenStarted {

            serviceRequestViewModel.uiState.collect { state ->

                when (state) {

                    is ServiceRequestUiState.Idle -> {
                        // do nothing
                    }

                    is ServiceRequestUiState.Loading -> {

                        binding.progressBar.isVisible = true
                        binding.rvRequests.visibility = View.GONE
                        binding.tvEmptyView.visibility = View.GONE
                    }

                    is ServiceRequestUiState.Success -> {

                        binding.progressBar.isVisible = false
                        binding.rvRequests.visibility = View.VISIBLE
                        binding.tvEmptyView.visibility = View.GONE

                        serviceAdapter.submitList(state.data)
                    }

                    is ServiceRequestUiState.Empty -> {

                        binding.progressBar.isVisible = false
                        binding.rvRequests.visibility = View.GONE
                        binding.tvEmptyView.visibility = View.VISIBLE
                        binding.tvEmptyView.text = state.message
                    }

                    is ServiceRequestUiState.Error -> {

                        binding.progressBar.isVisible = false
                        binding.rvRequests.visibility = View.GONE
                        binding.tvEmptyView.visibility = View.VISIBLE
                        binding.tvEmptyView.text = state.message
                    }
                }
            }
        }
    }

    private fun fetchServiceRequests(loadMore: Boolean = false) {
        if (isLoading) return

        isLoading = true

        serviceRequestViewModel.fetchServiceRequestDetails(
            username = usrname,
            zone = zone,
            activityType = name,
            mainCustomer = mainClientId,
            subCustomer = clientId,
            location = id_dist,
            date = apiStartDate,
            loadMore = loadMore
        )
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceRequestAdapter(name) { item ->

            val intent = Intent(
                this@ServiceRequestActivity,
                ServiceRequestDetails::class.java
            )

            intent.putExtra("request_id", item.req_no)
            intent.putExtra("activity_type", name)

            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)

        binding.rvRequests.layoutManager = layoutManager
        binding.rvRequests.adapter = serviceAdapter


        binding.rvRequests.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0) return

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                val shouldPaginate = !isLoading &&
                        !isLastPage &&
                        (visibleItemCount + firstVisibleItem) >= totalItemCount - 2

                if (shouldPaginate) {

                    fetchServiceRequests(loadMore = true)

                }
            }
        })
    }

    private fun fetchLocations() {
        observeViewModelsLocation()
    }

    private fun fetchSubClients() {
        observeViewModelsSubClient()
    }

    private fun fetchMainClients() {
        observeViewModels()
    }

    private fun initViewModels() {
        viewModelMainClient = ViewModelProvider(this).get(ViewModelMainClient::class.java)
        viewmodelSubClient = ViewModelProvider(this).get(ViewModelSubClient::class.java)
        viewmodelClientLocation = ViewModelProvider(this).get(ViewModelClientLocation::class.java)
        serviceRequestViewModel = ViewModelProvider(this).get(ServiceRequestViewModel::class.java)

    }


    private fun setDefaultTodayDate() {

        val today = Calendar.getInstance().time

        val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val displayDate = displayFormat.format(today)

        apiStartDate = apiFormat.format(today)
        apiEndDate = apiFormat.format(today)

        binding.etDateRange.setText("$displayDate - $displayDate")

        startDateLong = today.time
        endDateLong = today.time
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun observeViewModels() {

        viewModelMainClient.getMainClientRepository().observe(this) { response ->

            if (response == null) {
                Log.d("TAG", "Null response from server")
                return@observe
            }

            if (response.getType() == 1) {

                mainclientList.clear()
                mainclientList.addAll(response.getMain_client_list())

                mainClientDetail.clear()
                mainClientDetail.add("SELECT CLIENT")

                for (client in mainclientList) {
                    mainClientDetail.add(client.getClient_Name())
                }

                adapter = ArrayAdapter(
                    this,
                    R.layout.simple_custom_spinner_item,
                    mainClientDetail
                )
                adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.mainClient.setAdapter(adapter)

            } else {
                mainClientDetail.clear()
                mainClientDetail.add("NO DATA AVAILABLE")

            }
        }
    }


    private fun observeViewModelsSubClient() {
        viewmodelSubClient.getSubClientRepository(mainClientId)
            .observe(this) { response ->
                if (response == null) {
                    Log.d("TAG", "Null response from server")
                    return@observe
                }

                if (response.getType() == 1) {
                    subClientList.clear()
                    subClientList.addAll(response.clientList)
                    subClientDetail.clear()
                    subClientDetail.add("SELECT CLIENT")
                    for (client in response.getClientList()) {
                        subClientDetail.add(client.getClient_Name())
                    }
                    adapter = ArrayAdapter<String?>(
                        this,
                        R.layout.simple_custom_spinner_item,
                        subClientDetail
                    )
                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.newInClients.setAdapter(adapter)
                } else {
                    subClientDetail.clear()
                    subClientDetail.add("NO DATA AVAILABLE")
                }

            }
    }

    private fun observeViewModelsLocation() {
        viewmodelClientLocation.getClientLocationRepository(id_dist, server_name, db_name)
            .observe(this) { response ->
                if (response == null) {
                    Log.d("TAG", "Null response from server")
                    return@observe
                }
                if (response.getType() == 1) {
                    locationList.clear()
                    locationList.addAll(response.getClientLoc())
                    locationDetail.clear()
                    locationDetail.add("SELECT LOCATION")
                    for (loc in locationList) {
                        locationDetail.add(loc.getLoc_Name())
                    }
                    adapter = ArrayAdapter<String?>(
                        this,
                        R.layout.simple_custom_spinner_item,
                        locationDetail
                    )
                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.newInLocations.setAdapter(adapter)
                } else {
                    locationDetail.clear()
                    locationDetail.add("NO DATA AVAILABLE")
                }
            }
    }
}