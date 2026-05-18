package com.example.learningassistantapp;

import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.contract.TaskResultContracts;
import org.json.JSONObject;



public class UpgradeFragment extends Fragment {

    private PaymentsClient paymentsClient;
    private ActivityResultLauncher<Task<PaymentData>> paymentDataLauncher;

    public UpgradeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentDataLauncher = registerForActivityResult(
                new TaskResultContracts.GetPaymentDataResult(),
                result -> {
                    int statusCode = result.getStatus().getStatusCode();

                    switch (statusCode) {
                        case CommonStatusCodes.SUCCESS:
                            PaymentData paymentData = result.getResult();

                            if (paymentData != null) {
                                Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case CommonStatusCodes.CANCELED:
                            Toast.makeText(requireContext(), "Payment cancelled.", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(requireContext(), "Payment failed.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Elements
        Button starterUpgradeButton = view.findViewById(R.id.upgrade_button_starter);
        Button intermediateUpgradeButton = view.findViewById(R.id.upgrade_button_intermediate);
        Button advancedUpgradeButton = view.findViewById(R.id.upgrade_button_advanced);
        Button backButton = view.findViewById(R.id.upgrade_button_back);
        // Build Payments in TestEnv
        paymentsClient = PaymentsUtil.createPaymentsClient(requireContext());

        // Listeners
        starterUpgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment("4.99");
            }
        });

        intermediateUpgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment("9.99");

            }
        });

        advancedUpgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment("19.99");
            }
        });

        // Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).popBackStack();
            }
        });

    }

    private void requestPayment(String price) {
        JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);

        if (paymentDataRequestJson == null) {
            Toast.makeText(requireContext(), "Payment request failed.", Toast.LENGTH_SHORT).show();
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());

        if (request == null) {
            Toast.makeText(requireContext(), "Invalid payment request.", Toast.LENGTH_SHORT).show();
            return;
        }

        Task<PaymentData> paymentDataTask = paymentsClient.loadPaymentData(request);

        paymentDataTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<PaymentData>() {
            @Override
            public void onComplete(Task<PaymentData> task) {
                paymentDataLauncher.launch(task);
            }
        });
    }

}