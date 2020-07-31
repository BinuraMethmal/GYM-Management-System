package GymManagementSystem.CLI;


import java.io.Serializable;
import java.util.Calendar;

public class Date implements Serializable {
    private int[] date = new int[3];
    //private int currentYear;

    public Date(String dateOfStart){

        //currentYear = Calendar.getInstance().get(Calendar.YEAR);

        try{
            String [] tempDate = dateOfStart.split("/"); // Split year to DD,MM,YYYY

            // Check year has 4 character
            if (tempDate[2].length() !=4){
                System.out.println("** Invalid Date **\nPlease Check your Year!");
                throw new IllegalArgumentException();

            } else {
                // Covert Strings to Integers
                date[0] = Integer.parseInt(tempDate[0]); // DD (Date)
                date[1] = Integer.parseInt(tempDate[1]); // MM (Month)
                date[2] = Integer.parseInt(tempDate[2]); // YYYY (Year)
            }

            // Check user's month has 0 to max 31 days
            if (date[0]>31 || date[0]<1) {
                if (date[0]>31){
                    System.out.println("** Invalid Date **" +
                            "\nMonth should have only maximum 31 Days. Please Check your Date!");
                }else {
                    System.out.println("** Invalid Date **" +
                            "\nMonth should on above 0 Days. Please Check your Date!");
                }
                throw new IllegalArgumentException();

            // Check user's year has 1 to 12 months
            }else if (date[1]>12 || date[1]<1){
                if(date[1]>12){
                    System.out.println("** Invalid Date **" +
                            "\nYear should have only maximum 12 Months. Please Check your Month!");;
                } else{
                    System.out.println("** Invalid Date **" +
                            "\nYour month should on above 0 Months. Please Check your Month!");;
                }
                throw new IllegalArgumentException();
            }

            /* Make year period
                Added 100 years ago period */
            else if (date[2]<1920){
                    System.out.println("Invalid Date **" +
                            "\n Your year is not in acceptable range . Please Check your Year.");
                    throw new IllegalArgumentException();
            }

        // Make Date format error
        } catch (Exception e){
            System.out.println("** Date Format ERROR **");
            throw new IllegalArgumentException();
        }
    }

    // Get date class
    public String getDate(){
        return date[0]+"/"+date[1]+"/"+date[2];
    }
}
