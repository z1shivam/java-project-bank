package root;

public class UserDetails {

    public String fullName;
    public String dob;
    public String address;
    public String pan;
    public String aadhar;
    public String phone;
    public boolean isMarried;
    public boolean isEmp;
    public String gender;
    public String email;
    public String username;
    public String password;
    public long accountNumber;

    public UserDetails(String fullName, String dob, String address,
            String pan, String aadhar, String phone, boolean isMarried, boolean isEmp,
            String gender, String email, String username, String password, long accountNumber) {
        this.fullName = fullName;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.aadhar = aadhar;
        this.pan = pan;
        this.isMarried = isMarried;
        this.isEmp = isEmp;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDetails{"
                + "fullName=" + fullName
                + ", dob='" + dob
                + ", address='" + address
                + ", pan='" + pan
                + ", aadhar='" + aadhar
                + ", phone='" + phone
                + ", isMarried='" + isMarried
                + ", isEmp='" + isEmp
                + ", gender='" + gender
                + ", email='" + email
                + ", username='" + username
                + ", password=" + password
                + ", accountNumber='" + accountNumber
                + '}';
    }
}
