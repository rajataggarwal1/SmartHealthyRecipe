package com.smarthealthyrecipe.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smarthealthyrecipe.R;
import com.smarthealthyrecipe.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("DashboardFragment", "Before Photo captured successfully");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d("DashboardFragment", "Photo captured successfully");
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
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                Log.d("DashboardFragment", "takePictureButton clicked -- resolveActivity");
                cameraLauncher.launch(cameraIntent);
                Log.d("DashboardFragment", "takePictureButton clicked -- after Camera Launcher");
            }
        }
        else if(view.getId() == R.id.enterButton){
            // Dialog code goes here
            Log.d("DashboardFragment", "enterButton");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Text");

            // Inflate the custom layout for the EditText
            View editView = getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
            final EditText editText = editView.findViewById(R.id.editText);
            builder.setView(editView);

            Log.d("DashboardFragment", "enterButton-2");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String enteredText = editText.getText().toString();
                    Log.d("DashboardFragment", "enterButton  text" + enteredText);
                    Toast.makeText(getActivity(), "Entered Text: " + enteredText, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);
            Log.d("DashboardFragment", "enterButton  after" );
            AlertDialog dialog = builder.create();
            dialog.show();
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardFragment.this);
//        builder.setTitle("Enter Text");
//
//        // Create an EditText view and set it as the dialog's content
//        final EditText editText = new EditText(DashboardFragment.this);
//        builder.setView(editText);
//
//        // Set positive and negative buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String enteredText = editText.getText().toString();
//                // Do something with the entered text
//                // For example, display it in a Toast message
//                Toast.makeText(MainActivity.this, "Entered Text: " + enteredText, Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//
//        // Create and show the AlertDialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
