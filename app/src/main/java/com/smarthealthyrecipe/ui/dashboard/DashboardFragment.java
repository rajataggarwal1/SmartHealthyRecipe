package com.smarthealthyrecipe.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.smarthealthyrecipe.MainActivity;
import com.smarthealthyrecipe.R;
import com.smarthealthyrecipe.SharedViewModel;
import com.smarthealthyrecipe.databinding.FragmentDashboardBinding;

import java.util.HashSet;
import java.util.Set;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private SharedViewModel sharedViewModel;

    private FragmentDashboardBinding binding;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private static final String PREF_NAME = "ItemPrefs";
    private static final String KEY_ITEM_SET = "itemSet";

    private SharedPreferences sharedPreferences;
    private Set<String> itemSet;
    private OnItemAddedListener itemAddedListener;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        itemSet = sharedPreferences.getStringSet(KEY_ITEM_SET, new HashSet<>());

        sharedViewModel = ((MainActivity) requireActivity()).getSharedViewModel();

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

    private void onUserTextInput(String text) {
        sharedViewModel.setInputData(text);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button addButton = rootView.findViewById(R.id.enterButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return rootView;
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter Item");

        final EditText editText = new EditText(requireContext());
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredItem = editText.getText().toString();
                if (!enteredItem.isEmpty()) {
                    addItem(enteredItem);
                    Toast.makeText(requireContext(), "Item added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Please enter an item.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
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
            }}
//        } else if (view.getId() == R.id.enterButton) {
//            // Dialog code goes here
//        }
    }
    private void addItem(String item) {
        itemSet.add(item);
        saveItemsToSharedPreferences();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateHomeFragment(item);
        }
    }

    private void saveItemsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ITEM_SET, itemSet).apply();
    }

    // Interface to communicate with HomeFragment
    public interface OnItemAddedListener {
        void onItemAdded(String item);
    }

    // Method to set the itemAddedListener from HomeFragment
    public void setOnItemAddedListener(OnItemAddedListener listener) {
        itemAddedListener = listener;
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

