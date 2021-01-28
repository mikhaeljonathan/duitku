package com.example.duitku.account;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.category.fragment.ViewCategoriesActivity;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.database.DuitkuDbHelper;
import com.example.duitku.firebase.FirebaseJobService;
import com.example.duitku.firebase.FirebaseReader;
import com.example.duitku.firebase.FirebaseWriter;
import com.example.duitku.passcode.PasscodeActivity;
import com.example.duitku.user.User;
import com.example.duitku.user.UserController;
import com.example.duitku.welcome.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.example.duitku.account.UpgradePremiumActivity.BACKUP_FIRESTORE;

public class AccountFragment extends Fragment {

    private View view;
    private User user;

    private Button passcodeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        user = new UserController(getActivity()).getUser();

        setUpButtons();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        user = new UserController(getActivity()).getUser();

        setUpBanner();
        setUpPasscodeBtn();
    }

    private void setUpBanner() {
        TextView nameTV = view.findViewById(R.id.account_name_textview);
        nameTV.setText(user.getUser_name());

        TextView emailTV = view.findViewById(R.id.account_email_textview);
        emailTV.setText(user.getUser_email());

        ImageView premiumImageView = view.findViewById(R.id.account_premium_imageview);
        if (user.getUser_status().equals(UserEntry.TYPE_REGULAR)) {
            premiumImageView.setVisibility(View.GONE);
        } else {
            premiumImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpButtons() {
        setCategoriesBtn();
        setUpUpgradePremiumButton();
        setUpAddFeedbackButton();
        setUpSignOutButton();
    }

    private void setCategoriesBtn() {
        Button categoriesBtn = view.findViewById(R.id.account_categories_btn);
        categoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewCategoriesActivity = new Intent(getActivity(), ViewCategoriesActivity.class);
                startActivity(viewCategoriesActivity);
            }
        });
    }

    private void setUpPasscodeBtn() {
        passcodeBtn = view.findViewById(R.id.account_set_passcode_btn);

        final String passcode = user.getUser_passcode();

        if (passcode != null) {
            passcodeBtn.setText("Remove Passcode");
        } else {
            passcodeBtn.setText("Set Passcode");
        }

        passcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passcode != null) {
                    showRemoveConfirmationDialog();
                } else {
                    Intent setPasscodeIntent = new Intent(getActivity(), PasscodeActivity.class);
                    setPasscodeIntent.putExtra("Flag", "SET");
                    startActivity(setPasscodeIntent);
                }
            }
        });

    }

    private void showRemoveConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Remove Passcode Confirmation");
        alertDialogBuilder.setMessage("Are you sure to remove the passcode?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removePasscode();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void removePasscode() {
        user.setUser_passcode(null);
        new UserController(getActivity()).updateUser(user);
        setUpPasscodeBtn();
    }

    private void setUpUpgradePremiumButton() {
        Button upgradePremiumBtn = view.findViewById(R.id.account_upgrade_premium_btn);
        upgradePremiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upgradePremiumIntent = new Intent(getActivity(), UpgradePremiumActivity.class);
                startActivity(upgradePremiumIntent);
            }
        });
    }

    private void setUpAddFeedbackButton() {
        Button addFeedbackBtn = view.findViewById(R.id.account_add_feedback_btn);
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFeedbackIntent = new Intent(getActivity(), AddFeedbackActivity.class);
                startActivity(addFeedbackIntent);
            }
        });
    }

    private void setUpSignOutButton() {
        Button signOutBtn = view.findViewById(R.id.account_sign_out_btn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FirebaseWriter(getActivity()).writeAll();
                showConfirmationSignOutDialog();
            }
        });
    }

    private void showConfirmationSignOutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Sign out Confirmation");
        alertDialogBuilder.setMessage("Are you sure to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void signOut() {
        cancelBackup();

        GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build())
                .signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Intent loginIntent = new Intent(getActivity(), WelcomeActivity.class);
                            startActivity(loginIntent);
                            getActivity().finish();
                        }
                    }
                });

        new DuitkuDbHelper(getActivity()).dropAllTables();
    }

    public void cancelBackup() {
        // ini buat cancel backup
        JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BACKUP_FIRESTORE);
    }

}