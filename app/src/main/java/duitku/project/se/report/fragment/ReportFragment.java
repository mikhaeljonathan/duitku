package duitku.project.se.report.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import duitku.project.se.interfaces.UIView;

public class ReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UIView reportFragmentView = new ReportFragmentView(inflater, container, this);
        reportFragmentView.setUpUI();
        return reportFragmentView.getView();
    }

}
