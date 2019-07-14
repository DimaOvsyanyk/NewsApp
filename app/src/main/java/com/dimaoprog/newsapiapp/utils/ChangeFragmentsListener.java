package com.dimaoprog.newsapiapp.utils;

import androidx.fragment.app.Fragment;

public interface ChangeFragmentsListener {

    void openFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack);
}
