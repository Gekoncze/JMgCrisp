package cz.mg.crisp;

import cz.mg.annotations.classes.Test;
import cz.mg.c.CLibrary;
import cz.mg.c.CObject;
import cz.mg.c.CPointer;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.services.ReferencePositionService;
import cz.mg.crisp.ui.CrispWindow;

public @Test class ManualCrispWindowTest {
    public static void main(String[] args) {
        System.out.print("Running " + ManualCrispWindowTest.class.getSimpleName() + " ... ");

        test();

        System.out.println("OK");
    }

    private static void test() {
        CLibrary.load();

        TestClass secondTestObject = new TestClass(22222);
        secondTestObject.value = 2;

        TestClass firstTestObject = new TestClass(11111);
        firstTestObject.value = 1;
        firstTestObject.next = new CPointer<>(secondTestObject.getAddress(), TestClass.SIZE, TestClass::new);

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

        Reference reference2 = new Reference();
        reference2.setSource(firstFragment);
        reference2.setTarget(secondFragment);
        reference2.setSelected(false);

        Scene scene = new Scene();
        scene.getFragments().addLast(firstFragment);
        scene.getFragments().addLast(secondFragment);
        scene.getReferences().addLast(reference);
        scene.getReferences().addLast(reference2);

        firstFragment.setSelected(true);
        ReferencePositionService.getInstance().computePositionsForSelectedFragmentReferences(scene);
        firstFragment.setSelected(false);

        CrispWindow window = new CrispWindow();
        window.getScenePanel().setScene(scene);
        window.setVisible(true);
    }

    public static class TestClass extends CObject {
        public static final long SIZE = 16;

        private int value;
        private CPointer<TestClass> next;

        public TestClass(long address) {
            super(address);
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public CPointer<TestClass> getNext() {
            return next;
        }

        public void setNext(CPointer<TestClass> next) {
            this.next = next;
        }
    }
}
