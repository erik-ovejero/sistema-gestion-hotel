package Clases;

public class ItemCombo {

    private int id;
    private String descripcion;

    public ItemCombo(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}