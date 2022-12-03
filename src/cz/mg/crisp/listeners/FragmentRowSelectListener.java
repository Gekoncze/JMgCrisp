package cz.mg.crisp.listeners;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.Fragment;

public @Utility interface FragmentRowSelectListener {
    void onFragmentRowSelected(@Optional Fragment fragment, int i);
}
