package Model.JavaObjects;

public class Signature {

    private int id ;
    private String name ;
    private boolean authenticated ;
    
    public Signature(int id, String name, boolean authenticated) {
        this.id = id;
        this.name = name;
        this.authenticated = authenticated;
    }
    public Signature(int id, String name) {
        this.id = id;
        this.name = name;
        this.authenticated = false;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    @Override
    public String toString() {
        return "Signature [id=" + id + ", name=" + name + ", authenticated=" + authenticated + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (authenticated ? 1231 : 1237);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Signature other = (Signature) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (authenticated != other.authenticated)
            return false;
        return true;
    }
    
}