package com.gda.criminalintent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class CrimePhotoFragment extends DialogFragment {
    private File mCrimePhoto;

    public CrimePhotoFragment(File photoFile) {
        mCrimePhoto = photoFile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crime_photo, null);

        ImageView imageView = view.findViewById(R.id.crime_photo);
        Bitmap bitmap = PictureUtils.getScaledBitmap(mCrimePhoto.getPath(), getActivity());
        imageView.setImageBitmap(bitmap);

        return view;
    }
}
