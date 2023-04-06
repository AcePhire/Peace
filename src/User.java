public class User {
    private String ID;
    private String hashedPassword;
    private String firstName;
    private String lastName;

    public User(String ID, String firstName, String lastName, String hashedPassword){
        this.ID = ID;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Getters and setters

    public String getID() {
        return ID;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
