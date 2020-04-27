package com.andy.apparch;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.andy.arch.viewmodel.ViewModelHelper;

public class ActivityDemo extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ViewModelHelper.createViewModel(this, DemoVM.class);
    }

    public static class DemoVM extends ViewModel {
        @Override
        protected void onCleared() {
            new Throwable().printStackTrace();
        }
    }
}
