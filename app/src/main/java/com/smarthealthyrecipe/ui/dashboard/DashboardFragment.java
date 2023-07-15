package com.smarthealthyrecipe.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smarthealthyrecipe.R;
import com.smarthealthyrecipe.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;
    private ActivityResultLauncher<Intent> cameraLauncher;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("DashboardFragment", "in launcher");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Photo was taken successfully
                        Intent data = result.getData();
                        // Process the photo data
                        Bundle extras = data.getExtras();
                        Bitmap photoBitmap = (Bitmap) extras.get("data");
                        // Do something with the photo
                        ImageView imageView = requireView().findViewById(R.id.imageView);
                        imageView.setImageBitmap(photoBitmap);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.takePictureButton.setOnClickListener(this);
        binding.enterButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.takePictureButton) {
            Log.d("DashboardFragment", "takePictureButton clicked");
            if (hasCameraPermission()) {
                Log.d("DashboardFragment", " has camera permission");
                launchCamera();
            } else {
                Log.d("DashboardFragment", " Does not have camera permission");
                requestCameraPermission();
            }
        } else if (view.getId() == R.id.enterButton) {
            // Dialog code goes here
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Text");

            // Inflate the custom layout for the EditText
            View editView = getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
            final EditText editText = editView.findViewById(R.id.editText);
            builder.setView(editView);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String enteredText = editText.getText().toString();
                    Toast.makeText(getActivity(), "Entered Text: " + enteredText, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("cameraIntent", " cameraIntent " + cameraIntent);
//        cameraLauncher.launch(cameraIntent);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        }
        else{
            Log.d("cameraIntent", " in else " );
            Toast.makeText(requireContext(), "No camera app found on the device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission is granted, proceed with camera access
                launchCamera();
            } else {
                // Camera permission is denied, show a message or handle the denial case
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

