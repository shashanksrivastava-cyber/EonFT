package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dagger.hilt.android.AndroidEntryPoint;
import in.eoninfotech.eontechnician.activity.ServiceRequestActivity;
import in.eoninfotech.eontechnician.databinding.FragmentLiveStatusNewBinding;
import in.eoninfotech.eontechnician.databinding.ServiceRequestDashboardBinding;
import in.eoninfotech.eontechnician.di.SharedPreferenceManager;
import in.eoninfotech.eontechnician.helper.CheckConnection;
import in.eoninfotech.eontechnician.viewModel.ServiceCountViewModel;
import jakarta.inject.Inject;


@AndroidEntryPoint
public class ServiceRequestFragment extends Fragment {

    @Inject
    SharedPreferenceManager sharedPref;

    @Inject
    CheckConnection checkConnection;

    private ServiceRequestDashboardBinding binding;
    private ServiceCountViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = ServiceRequestDashboardBinding.inflate(inflater, container, false);

        initUI();
        initViewModels();

        return binding.getRoot();
    }

    private void initViewModels() {

        viewModel = new ViewModelProvider(this).get(ServiceCountViewModel.class);

        observeViewModel();

        loadDashboard();
    }

    private void loadDashboard() {
        binding.progressBar.setVisibility(View.VISIBLE);
        try {

            if (checkConnection != null && checkConnection.isConnected()) {

                String username = sharedPref != null ? sharedPref.getUsername() : "";
                String zone = sharedPref != null ? sharedPref.getZone() : "";

                viewModel.getServiceRequest(username, zone);

            } else {

                showMessage("No internet connection");

                setDefaultValues();

            }

        } catch (Exception e) {

            e.printStackTrace();
            setDefaultValues();

        }
    }

    private void observeViewModel() {

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {

            if (binding == null) return;

            if (Boolean.TRUE.equals(isLoading)) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);

            } else {

                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

            }

        });

        viewModel.getServiceCount().observe(getViewLifecycleOwner(), data -> {

            if (!isAdded() || binding == null) return;

            try {

                if (data != null) {

                    String total = !TextUtils.isEmpty(data.total_count)
                            ? data.total_count : "0";

                    String install = !TextUtils.isEmpty(data.install_pending_count)
                            ? data.install_pending_count : "0";

                    String removal = !TextUtils.isEmpty(data.removal_pending_count)
                            ? data.removal_pending_count : "0";

                    binding.tvTotalPendingCount.setText(total);
                    binding.tvInstallCount.setText(install);
                    binding.tvRemovalCount.setText(removal);

                } else {

                    setDefaultValues();

                }

            } catch (Exception e) {

                e.printStackTrace();
                setDefaultValues();

            }

        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {

            if (!isAdded()) return;

            if (!TextUtils.isEmpty(message)) {

                showMessage(message);

            }

            setDefaultValues();

        });
    }

    private void setDefaultValues() {

        if (binding == null) return;

        binding.tvTotalPendingCount.setText("0");
        binding.tvInstallCount.setText("0");
        binding.tvRemovalCount.setText("0");
    }

    private void showMessage(String message) {

        try {

            if (getContext() != null && !TextUtils.isEmpty(message)) {

                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void initUI() {

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDashboard();
            }
        });

        binding.cardPendingInstall.setOnClickListener(v -> {

            try {

                if (getActivity() != null) {

                    Intent intent = new Intent(getActivity(), ServiceRequestActivity.class);
                    intent.putExtra("activity_type", "I");
                    startActivity(intent);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        });

        binding.cardPendingRemoval.setOnClickListener(v -> {

            try {

                if (getActivity() != null) {

                    Intent intent = new Intent(getActivity(), ServiceRequestActivity.class);
                    intent.putExtra("activity_type", "R");
                    startActivity(intent);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboard();
    }
}
