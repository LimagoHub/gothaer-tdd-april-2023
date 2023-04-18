package de.gothaer.service;

import de.gothaer.dependency.Dependency;

public class MyServiceUsingDependencyImpl {

    private final Dependency dependency;

    public MyServiceUsingDependencyImpl(final Dependency dependency) {
        this.dependency = dependency;
    }

    public void myFirstMethod() {

        String myString = "Hallo";
        myString = myString.toUpperCase();
        dependency.foo(myString);
        dependency.foo(myString);
    }

    public int mySecondMethod() {

        return dependency.bar() + 5;
    }

    public int myThirdMethod(String test) {

        return dependency.foobar(test.toUpperCase()) + 5;
    }

    // Dummy (Hohle Fachlogik, muss nur complieren)
    // Stub (Dependency -> Stellvertreter, echtes Verhalten simuliert)
    // Mock (wie Stub, kann mit Assertion scheitern)
    // Spy (Dekorator -> Proxy zum Messen von echten Aufruf)
    // Fake (echte Klasse in ihren Verhalten manipulieren, nicht möglich in Mockito)
}
