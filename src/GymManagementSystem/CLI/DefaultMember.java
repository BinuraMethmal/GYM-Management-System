package GymManagementSystem.CLI;

import java.io.Serializable;

public class DefaultMember implements Serializable {
    public static String memberType = "Normal";
    public static int memberCount = 0;
    public static int memberNum =0;
    private int membershipNum;
    private String memberName;
    private Date membershipDate;

    public  DefaultMember(){
        memberCount = memberCount + 1;
        memberNum = memberNum + 1;
        membershipNum = memberNum;
    }
    public static String getMemberType(){
        return memberType;
    }

    public String getSpecial() {
        return "NA";
    }

    public String getMembershipDate() {
        return membershipDate.getDate();
    }

    public void setMembershipDate(Date membershipDate) {
        this.membershipDate =membershipDate;
    }

    public static int getMemberNum(){
        return memberNum;
    }

    public static void setMemberNum(int membershipNum){
        memberNum = membershipNum;
    }

    public void setMemberName(String memberName){
        this.memberName=memberName;
    }

    public String getMemberName(){
        return memberName;
    }

    public static int getMemberCount(){
        return memberCount;
    }

    public static void setMemberCount(int newMemberCount){
        memberCount = newMemberCount;
    }

    public int getMembershipNum() {
        return membershipNum;
    }

    public int compareTo(DefaultMember member){
        return memberName.compareToIgnoreCase(member.getMemberName());
    }

    public static void memberCountDecrement(){
        memberCount--;
    }

    public boolean search(String searchTerm){
        if (memberName.equalsIgnoreCase(searchTerm)) return true;
        else if (membershipDate.getDate().equalsIgnoreCase(searchTerm)) return true;
        else if (String.valueOf(membershipNum).equals(searchTerm)) return true;
        else if (memberType.equalsIgnoreCase(searchTerm)) return true;
        else return false;
    }

    @Override
    public String toString() {
        return membershipNum+" - "+memberName+" - Normal Member - "+membershipDate.getDate();
    }

}