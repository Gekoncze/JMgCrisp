package cz.mg.crisp;

import cz.mg.annotations.classes.Test;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.services.ReferencePositionService;
import cz.mg.crisp.ui.CrispWindow;

public @Test class ManualCrispWindowTest {
    public static void main(String[] args) {
        System.out.print("Running " + ManualCrispWindowTest.class.getSimpleName() + " ... ");
        test();
        System.out.println("OK");
    }

    private static void test() {
        TestClass secondTestObject = new TestClass();
        secondTestObject.setId(22222);
        secondTestObject.setDecimal(2.2f);
        secondTestObject.setText("Second");
        secondTestObject.setInner(null);
        secondTestObject.setNext(new TestPointer<>());

        TestClass innerTestObject = new TestClass();
        innerTestObject.setId(77777);
        innerTestObject.setDecimal(7.7f);
        innerTestObject.setText("Inner");
        innerTestObject.setInner(null);
        innerTestObject.setNext(null);

        TestClass firstTestObject = new TestClass();
        firstTestObject.setId(11111);
        firstTestObject.setDecimal(null);
        firstTestObject.setText("First");
        firstTestObject.setInner(innerTestObject);
        firstTestObject.setNext(new TestPointer<>(secondTestObject));

        Fragment firstFragment = new Fragment();
        firstFragment.setObject(firstTestObject);
        firstFragment.setPosition(new LocalPoint(16, 16));
        firstFragment.setSize(new LocalVector(128, 92));
        firstFragment.setSelected(false);

        Fragment secondFragment = new Fragment();
        secondFragment.setObject(secondTestObject);
        secondFragment.setPosition(new LocalPoint(16 + 128 + 64, 16));
        secondFragment.setSize(new LocalVector(128, 92));
        secondFragment.setSelected(false);

        Reference reference = new Reference();
        reference.setSource(firstFragment);
        reference.setTarget(secondFragment);
        reference.setSelected(false);
        reference.setBegin(new LocalPoint());
        reference.setEnd(new LocalPoint());

        Reference reference2 = new Reference();
        reference2.setSource(firstFragment);
        reference2.setTarget(secondFragment);
        reference2.setSelected(false);
        reference2.setBegin(new LocalPoint());
        reference2.setEnd(new LocalPoint());

        Scene scene = new Scene();
        scene.getFragments().addLast(firstFragment);
        scene.getFragments().addLast(secondFragment);
        scene.getReferences().addLast(reference);
        scene.getReferences().addLast(reference2);

        firstFragment.setSelected(true);
        ReferencePositionService.getInstance().computePositionsForSelectedFragmentReferences(scene);
        firstFragment.setSelected(false);

        CrispWindow window = new CrispWindow(new Metadata(TestMetadataFactory.getInstance()));
        window.getScenePanel().setScene(scene);
        window.setVisible(true);
    }
}
