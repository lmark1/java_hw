package hw0036479998.android.fer.hr.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This class stores form properties.
 *
 * Created by lmark on 29.6.2017..
 */

public class FormEntry implements Serializable {

    @SerializedName("avatar_location")
    private String avatarLocation;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone_no")
    private String phoneNumber;

    @SerializedName("email_sknf")
    private String email;

    @SerializedName("spouse")
    private String spouse;

    @SerializedName("age")
    private Integer age;

    /**
     * @return Returns avatar location.
     */
    public String getAvatarLocation() {
        return avatarLocation;
    }

    /**
     * @return Returns last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return Retursn first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return Returns age.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @return Returns email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set new age.
     *
     * @param age New age.
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Set avatar loacation.
     *
     * @param avatarLocation Avatar location.
     */
    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
    }

    /**
     * Set new email.
     *
     * @param email New email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set new first name.
     *
     * @param firstName New first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set new last name.
     *
     * @param lastName New last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set new phone number.
     *
     * @param phoneNumber Phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set new spouse.
     *
     * @param spouse New spouse.
     */
    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    /**
     * @return Returns spouse.
     */
    public String getSpouse() {
        return spouse;
    }

    /**
     * @return Returns phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

}
