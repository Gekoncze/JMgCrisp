package cz.mg.crisp.listeners;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.crisp.entity.model.Fragment;

public @Utility interface FragmentOpenListener {
    void onFragmentOpened(@Mandatory Fragment parent, int i);
}
