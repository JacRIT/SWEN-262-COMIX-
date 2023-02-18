package model;

public class Creator {
    private final int id ;
    private final String first_name ;
    private final String last_name ;

    public Creator(int id, String first_name, String last_name) {
        this.id = id ;
        this.first_name = first_name ;
        this.last_name = last_name ;
    }

    @Override
    public String toString() {
        return "Creator [name=" + first_name + " " + last_name + "]";
    }


}

