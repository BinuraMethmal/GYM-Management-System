package GymManagementSystem.CLI;
import org.bson.Document;

public class Over60Member extends DefaultMember {
    public static String memberType = "Senior";
    private int age;

    public static String getMemberType(){
        return memberType;
    }
    public String getSpecial() {
        return String.valueOf(age);
    }
    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;

    }
    public boolean search(String searchTerm){
        if (super.getMemberName().equalsIgnoreCase(searchTerm)) return true;
        else if (super.getMembershipDate().equalsIgnoreCase(searchTerm)) return true;
        else if (String.valueOf(super.getMembershipNum()).equals(searchTerm)) return true;
        else if (String.valueOf(age).equals(searchTerm)) return true;
        else if (memberType.equalsIgnoreCase(searchTerm)) return true;
        else return false;
    }
    @Override
    public String toString() {
        return getMembershipNum()+" - "+getMemberName()+" - Senior Member - "+age
                +" - "+getMembershipDate();
    }
}
