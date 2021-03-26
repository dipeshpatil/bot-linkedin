package DataModel;

public class People {
    private String id;
    private String name;
    private String bio;
    private String location;

    public People(String id, String name, String bio, String location) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
