package com.gda.criminalintent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleText;
    private Button mDateBtn;
    private Button mTimeBtn;
    private CheckBox mSolvedChBox;
    private Button mReportBtn;
    private Button mSuspectBtn;
    private ImageButton mCallSuspect;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    public static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_DIALOG = "date_picker_dialog";
    private static final String TIME_PICKER_DIALOG = "time_picker_dialog";

    private static final int DATE_REQUEST_CODE = 0;
    private static final int TIME_REQUEST_CODE = 1;
    private static final int SUSPECT_REQUEST_CODE = 2;
    private static final int CONTACTS_PERSMISSION_REQUEST_CODE = 3;
    private static final int REQUEST_PHOTO = 4;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        UUID crimeId = (UUID) args.getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        Log.d("mPhotoFile", mPhotoFile.toString() + " exists: " + (mPhotoFile.exists()? "yes" : "no"));

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:

                // TODO: udapt for twoplane mode
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().setResult(CrimeListFragment.CRIME_RESULT_REMOVED);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleText = view.findViewById(R.id.crimeTitle);
        mTitleText.setText(mCrime.getTitle());
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateBtn = view.findViewById(R.id.crime_date);
        updateDate();
        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
//                datePickerFragment.setTargetFragment(CrimeFragment.this, DATE_REQUEST_CODE);
//                FragmentManager fragmentManager = getFragmentManager();
//                datePickerFragment.show(fragmentManager, DATE_PICKER_DIALOG);

                // TODO: choose method based on device type

                Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getDate());
                startActivityForResult(intent, DATE_REQUEST_CODE, null);

            }
        });

        mTimeBtn = view.findViewById(R.id.crime_time);
        mTimeBtn.setText(R.string.time_picker_title);
        mTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, TIME_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), TIME_PICKER_DIALOG);
            }
        });


        mSolvedChBox = view.findViewById(R.id.crime_solved);
        mSolvedChBox.setChecked(mCrime.isSolved());
        mSolvedChBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                updateCrime();
            }
        });

        mReportBtn = view.findViewById(R.id.crime_report);
        mReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);

                ShareCompat.IntentBuilder ib = ShareCompat.IntentBuilder.from(getActivity());
                ib.setSubject(getString(R.string.crime_report_subject));
                ib.setText(getCrimeReport());
                ib.setType("text/plain");
                ib.startChooser();
            }
        });

        mSuspectBtn = view.findViewById(R.id.crime_suspect);
        final Intent pickSuspect = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
//        Add to disable suspect button (simulate not finding contacts app)
//        pickSuspect.addCategory(Intent.CATEGORY_HOME);
        mSuspectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickSuspect, SUSPECT_REQUEST_CODE);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectBtn.setText(mCrime.getSuspect());
        }

        PackageManager pm = getActivity().getPackageManager();
        if (pm.resolveActivity(pickSuspect, pm.MATCH_DEFAULT_ONLY) == null) {
            mSuspectBtn.setEnabled(false);
        }

        mCallSuspect = view.findViewById(R.id.call_suspect);
        mCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suspectNumber = getSuspectNumber();
                if (suspectNumber == null) {
                    Log.d("PHONEEEEEEEEEEE", " is null");
                    return;
                }
                Intent callSuspect = new Intent(Intent.ACTION_DIAL);
                callSuspect.setData(Uri.parse("tel:" + suspectNumber));
                startActivity(callSuspect);
            }
        });
        updateCallButton();

        mPhotoButton = view.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(pm) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
//            Uri uri = Uri.parse(mPhotoFile.toString());
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.gda.criminalintent.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = view.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoFile.exists()) {
                    CrimePhotoFragment fragment = new CrimePhotoFragment(mPhotoFile);
                    fragment.show(getFragmentManager(), "crime_photo");
                } else {
                    Toast.makeText(getActivity(), "Sorry! No crime photo taken!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final ViewTreeObserver viewTreeObserver = mPhotoView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("onGlobalLayout", "ooooooooooooo");
                updatePhotoView();
            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("PermissionsResult", "request_code" + requestCode);
        switch (requestCode) {
            case CONTACTS_PERSMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), "Access to contacts denied!", Toast.LENGTH_SHORT).show();
                    Log.d("PermissionsResult", "Denied");
                } else {
                    Toast.makeText(getActivity(), "Access to contacts given! Try again.", Toast.LENGTH_SHORT).show();
                    Log.d("PermissionsResult", "allowewd");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private String getSuspectNumber() {
        long suspectId = mCrime.getSuspectId();
        Log.d("Suspect ID: ", "" + suspectId);

        int permission = getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
                    CONTACTS_PERSMISSION_REQUEST_CODE);
            Log.d("PHONEEEEEEEEEEEEEEE", "NO PERMISSION");
            return null;
        }

        final String[] query = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = getActivity().getContentResolver().query
                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 query,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                 new String[] {"" + suspectId},
                null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Log.d("PHONEEEEEEEEEEEEEEE", "NO data in cursor");
                return null;
            }
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            long _id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            long contact_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            Log.d("PHONEEEEEEEEEEEEEEE", phone);
            Log.d("_ID", Long.toString(_id));
            Log.d("CONTACT_ID", Long.toString(contact_id));
            return phone;

        } finally {
            cursor.close();
        }
    }

    private void updateCallButton() {
        if (mCrime.getSuspect() == null) {
            mCallSuspect.setVisibility(View.INVISIBLE);
        } else {
            mCallSuspect.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == DATE_REQUEST_CODE && data != null) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE_EXTRA);
            if (date != null) {
                mCrime.setDate(date);
                updateDate();
                updateCrime();
            }
        } else if (requestCode == TIME_REQUEST_CODE && data != null) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);
            Date date = mCrime.getDate();
            date.setHours(hour);
            date.setMinutes(minute);
            updateDate();
        } else if (requestCode == SUSPECT_REQUEST_CODE && data != null) {
            Uri conatctUri = data.getData();

            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID};

            Cursor cursor = getActivity().getContentResolver().query(conatctUri,
                    queryFields, null, null, null);

            try {
                if (cursor.getCount() == 0) {
                    return;
                }

                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                mCrime.setSuspectId(cursor.getLong(1));
                Log.d("SUSPECT ID", "" + cursor.getLong(1));
                updateCrime();
                mSuspectBtn.setText(suspect);
                updateCallButton();

            } finally {
                cursor.close();
            }

        } else if (requestCode == REQUEST_PHOTO) {
            updateCrime();
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateBtn.setText(mCrime.getDate().toString());
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    public void updatePhotoView() {
        Log.d("updatePhotoView", "TADAAAAAAAAAAAAAAA");
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap image = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),
                    mPhotoView.getWidth(), mPhotoView.getHeight());
            mPhotoView.setImageBitmap(image);
        }

    }

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

}
