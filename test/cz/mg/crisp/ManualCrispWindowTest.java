package cz.mg.crisp;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.crisp.services.CrispWindowFactory;

public @Test class ManualCrispWindowTest {
    public static void main(String[] args) {
        System.out.print("Running " + ManualCrispWindowTest.class.getSimpleName() + " ... ");
        new ManualCrispWindowTest().test();
        System.out.println("OK");
    }

    private final @Mandatory TestMetadataFactory testMetadataFactory = TestMetadataFactory.getInstance();
    private final @Mandatory CrispWindowFactory crispWindowFactory = CrispWindowFactory.getInstance();

    private ManualCrispWindowTest() {
    }

    private void test() {
        TestClass referencedObject = new TestClass();
        referencedObject.setId(22222);
        referencedObject.setDecimal(2.2f);
        referencedObject.setText("Second");
        referencedObject.setInner(null);
        referencedObject.setNext(new TestPointer<>());

        TestClass innerObject = new TestClass();
        innerObject.setId(77777);
        innerObject.setDecimal(7.7f);
        innerObject.setText("Inner");
        innerObject.setInner(null);
        innerObject.setNext(null);

        TestClass rootObject = new TestClass();
        rootObject.setId(11111);
        rootObject.setDecimal(null);
        rootObject.setText("First");
        rootObject.setInner(innerObject);
        rootObject.setNext(new TestPointer<>(referencedObject));

        crispWindowFactory.create(testMetadataFactory, rootObject);
    }
}
