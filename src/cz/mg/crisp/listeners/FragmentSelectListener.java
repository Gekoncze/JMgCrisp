package cz.mg.crisp.listeners;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;

public @Utility interface FragmentSelectListener {
    void onFragmentSelected(@Optional Fragment fragment);
}
