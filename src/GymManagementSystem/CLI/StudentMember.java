package GymManagementSystem.CLI;
import org.bson.Document;

public class StudentMember extends DefaultMember{
    public static String memberType = "School";
    private String schoolName;

    public String getSpecial() {
        return schoolName;
    }
    public static String getMemberType(){
        return memberType;
    }
    public String getSchoolName(){
        return schoolName;
    }

    public void setSchoolName(String schoolName){

        this.schoolName = schoolName;

    }
    public boolean search(String searchTerm){
        if (super.getMemberName().equalsIgnoreCase(searchTerm)) return true;
        else if (super.getMembershipDate().equalsIgnoreCase(searchTerm)) return true;
        else if (String.valueOf(super.getMembershipNum()).equals(searchTerm)) return true;
        else if (schoolName.equals(searchTerm)) return true;
        else if (memberType.equalsIgnoreCase(searchTerm)) return true;
        else return false;
    }
    @Override
    public String toString() {
        return getMembershipNum()+" - "+getMemberName()+" - School Member - "+schoolName
                +" - "+getMembershipDate();
    }
}
