package walter.teamcowboy.types;

/**
 * User class that describes a TeamCowboy user defined here: http://api.teamcowboy.com/v1/docs/#id.blexxo-7dss62
 * <p>
 * TODO: Add a json initializer to set the existing AuthUser.
 * <p>
 * TODO: make this extend AuthUser.
 * TODO: account for user options.
 */
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String emailAddress1;
    private String phone1;

    public User(int userId, String firstName, String lastName, String displayName, String emailAddress1,
            String phone1) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.emailAddress1 = emailAddress1;
        this.phone1 = phone1;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress1() {
        return emailAddress1;
    }

    public String getPhone1() {
        return phone1;
    }
}
