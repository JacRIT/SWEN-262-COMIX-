package model;

public class Publisher {
    private final int id ;
    private final String name ;

    public Publisher(int id, String name) {
        this.id = id ;
        this.name = name ;
    }

    @Override
    public String toString() {
        return "Publisher [id=" + id + ", name=" + name + "]";
    }
}