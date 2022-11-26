package cz.mg.crisp;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;

public @Entity class TestClass implements TestObject {
    public static final long SIZE = 16;

    private int id;
    private Float decimal;
    private String text;
    private TestClass inner;
    private TestPointer<TestClass> next;

    public TestClass() {
        super();
    }

    @Value
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Optional @Value
    public Float getDecimal() {
        return decimal;
    }

    public void setDecimal(Float decimal) {
        this.decimal = decimal;
    }

    @Required @Value
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Mandatory @Part
    public TestClass getInner() {
        return inner;
    }

    public void setInner(TestClass inner) {
        this.inner = inner;
    }

    @Optional @Part
    public TestPointer<TestClass> getNext() {
        return next;
    }

    public void setNext(TestPointer<TestClass> next) {
        this.next = next;
    }
}
