package cz.mg.crisp;

import cz.mg.annotations.classes.Test;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.services.FragmentFactory;
import cz.mg.crisp.ui.CrispWindow;

public @Test class ManualCrispWindowTest {
    public static void main(String[] args) {
        System.out.print("Running " + ManualCrispWindowTest.class.getSimpleName() + " ... ");
        test();
        System.out.println("OK");
    }

    private static void test() {
        TestMetadataFactory testMetadataFactory = TestMetadataFactory.getInstance();
        FragmentFactory fragmentFactory = FragmentFactory.getInstance();

        Metadata metadata = new Metadata();
        metadata.setMetadataFactory(testMetadataFactory);

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

        Fragment fragment = fragmentFactory.create(metadata, firstTestObject);
        Scene scene = new Scene();
        scene.getFragments().addLast(fragment);

        CrispWindow window = new CrispWindow(metadata);
        window.getScenePanel().setScene(scene);
        window.setVisible(true);
    }
}
