package de.gothaer.tiere;

import java.util.Objects;

public class Schwein {

    public static final int DEFAULT_GEWICHT = 10;
    private String name;
    private int gewicht;

    public Schwein() {
        this("Nobody");
    }

    public Schwein(String name) {
        setName(name);
        setGewicht(DEFAULT_GEWICHT);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        if(name == null || "Elsa".equals(name)) throw new IllegalArgumentException("Ungueltiger Name");
        this.name = name;
    }

    public int getGewicht() {
        return gewicht;
    }

    private void setGewicht(int gewicht) {
        this.gewicht = gewicht;
    }
    public void fuettern() {
        setGewicht(getGewicht() + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schwein schwein = (Schwein) o;
        return gewicht == schwein.gewicht && Objects.equals(name, schwein.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gewicht);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Schwein{");
        sb.append("name='").append(name).append('\'');
        sb.append(", gewicht=").append(gewicht);
        sb.append('}');
        return sb.toString();
    }
}
