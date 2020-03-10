package ckc.android.develophelp.lib;

public class Dog {
    private static final Dog ourInstance = new Dog();

    public static Dog getInstance() {
        return ourInstance;
    }

    private Dog() {
    }

    public int mAge = 10;

    public void setAge(int age) {
        mAge = age;
    }
}
