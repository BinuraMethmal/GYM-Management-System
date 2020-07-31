package GymManagementSystem.CLI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.*;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;

import javax.xml.bind.DatatypeConverter;

public class MyGymManager extends Application implements GymManager{

    public static ArrayList<DefaultMember> members = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws IOException {
        loadFromDB();
        System.out.println("<<<<<<<<< Welcome to Gym Manager >>>>>>>>>");

        while (true){

            Scanner sc = new Scanner(System.in);


            // Current Member count
            System.out.println("Current Member count: "+ members.size());
            System.out.print("\n");

            // Labels for every input options
            System.out.println("Enter your Option.");
            System.out.println("'A' for Add new member. ");
            System.out.println("'D' for Delete member. ");
            System.out.println("'P' for Display all members.");
            System.out.println("'S' for Sort members.");
            System.out.println("'E' for Export data into File.");
            System.out.println("'L' for View List of members in GUI.");
            System.out.println("'Q' for Quit.");
            System.out.print("Your Option: ");

            // Take Input option
            String option = sc.nextLine().toUpperCase();
            System.out.println("\n");

            switch (option){
                case "A":
                    addMember();
                    break;
                case "D":
                    deleteMember();
                    break;
                case "P":
                    displayMembers();
                    break;
                case "S":
                    sortData();
                    break;
                case "E":
                    saveData();
                    break;
                case "L":
                    listOfMembers();
                    break;
                case "Q":
                    if (members.size()==100){
                        System.out.println("** No Data to Save **");
                    } else {

                        MongoClient mongoClient = new MongoClient("localhost", 27017);
                        MongoDatabase mongoDatabase = mongoClient.getDatabase("GYM_Database");
                        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Members");
                        BasicDBObject document = new BasicDBObject();
                        mongoCollection.deleteMany(document);

                        Document memberInfo = new Document("_id","1stDoc")
                                .append("count",DefaultMember.getMemberCount())
                                .append("memberNo",DefaultMember.getMemberNum());
                        mongoCollection.insertOne(memberInfo);

                        for (DefaultMember member :members){
                            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
                            outputStream.writeObject(member);
                            outputStream.flush();

                            memberInfo = new Document("title","Member")
                                    .append("_id", member.getMembershipNum())
                                    .append("name",member.getMemberName())
                                    .append("member", DatatypeConverter.printBase64Binary(byteOutputStream.toByteArray()));

                            mongoCollection.insertOne(memberInfo);
                            byteOutputStream.close();
                            outputStream.close();
                        }
                    }
                    System.exit(0);
                    break;
            }
        }
    }
    private void loadFromDB() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        //mongoClient.setPortBindings(List.of("27017:27017"));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("GYM_Database");
        MongoCollection<Document> collection = mongoDatabase.getCollection("Members");
        long done = mongoDatabase.getCollection("Members").countDocuments();
        if (done == 0) {
            System.out.println("\n----------- ** No Member details in Database ** -----------");
        }else{
            FindIterable<Document> dataCollection = collection.find();
            for (Document doc : dataCollection){
                if(doc.get("_id").equals("1stDoc")){
                    DefaultMember.setMemberCount(doc.getInteger("count"));
                    DefaultMember.setMemberNum(doc.getInteger("memberNo"));
                }else{
                    String record = doc.getString("member");
                    byte[] recordByte = DatatypeConverter.parseBase64Binary(record);
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(recordByte);
                    ObjectInputStream objectInputStream;
                    DefaultMember defaultMember;
                    try {
                        objectInputStream = new ObjectInputStream(byteInputStream);
                        defaultMember = (DefaultMember) objectInputStream.readObject();
                        members.add(defaultMember);
                        //System.out.println(defaultMember);
                        byteInputStream.close();
                        objectInputStream.close();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error occurred in data import");
                    }
                }
            }
        }
    }

    public void addMember(){

        // Check member list Reached max or not
        if (DefaultMember.getMemberCount()==100){
            System.out.println("Maximum member count is 100, It was already reached.");
            return;
        }

        String name;
        Date date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String sclName;
        int age=0;

        while (true){
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter - ");
            System.out.println("'N' for Normal Members");
            System.out.println("'S' for Student Members");
            System.out.println("'O' for Senior Members (Over aged 60).");
            System.out.println("'B' for Back.");

            System.out.print("Your Option: ");
            String option = sc.nextLine().toUpperCase();
            System.out.println("\n");

            if (option.equalsIgnoreCase("N") || option.equalsIgnoreCase("S") ||
                    option.equalsIgnoreCase("O")){

                // Enter member name
                System.out.print("Enter Full Name: ");
                name = sc.nextLine();

                System.out.print("Enter Membership start date (Format - 'DD/MM/YYYY'): ");
                String startDate = sc.nextLine();

                // Check date is in right format
                try{
                    date = new GymManagementSystem.CLI.Date(startDate);
                } catch (IllegalArgumentException e){
                    continue;
                }

                // Include information to DefaultMember class
                if (option.equalsIgnoreCase("N")){
                    DefaultMember member = new DefaultMember();
                    member.setMemberName(name);
                    member.setMembershipDate(date);
                    members.add(member);

                // Input school name & Include information to DefaultMember class and StudentMember class
                } else if (option.equalsIgnoreCase("S")){

                    StudentMember member = new StudentMember();
                    System.out.print("Enter your school: ");
                    sclName= sc.nextLine();

                    member.setMemberName(name);
                    member.setMembershipDate(date);
                    member.setSchoolName(sclName);
                    members.add(member);

                // Input age & Include information to DefaultMember class and Over60Member class
                } else if (option.equalsIgnoreCase("O")) {

                    Over60Member member = new Over60Member();
                    while (true) {

                        System.out.print("Enter your current age: ");
                        try {
                            age = sc.nextInt();
                            if (age < 60 || age > 100) throw new InputMismatchException();
                        } catch (InputMismatchException e) {
                            System.out.println("** Invalid Age. Please try again **");
                            continue;
                        }
                        member.setAge(age);
                        break;
                    }

                    member.setMemberName(name);
                    member.setMembershipDate(date);
                    members.add(member);

                } else {
                    try {
                        main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    public void deleteMember(){
        if (members.size()==0){
            System.out.println("** No date entered to Delete **");
            return;
        }

        while (true) {
            Scanner sc = new Scanner(System.in);
            int membershipNo ;
            DefaultMember deleteMember = null;
            System.out.print("Enter membership number (ID) to delete: ");

            // Check membershipNo is Integer or not
            try {
                membershipNo = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("** Integer Required! **");
                continue;
            }

            // Assign DefaultMember class memberNum in members arraylist into deleteMember using for loop
            for (DefaultMember member : members) {
                if (member.getMembershipNum()==membershipNo){
                    deleteMember = member;
                }
            }

            // Give final decision to user for deleting process
            if (deleteMember!=null){
                System.out.println(deleteMember);
                System.out.print("Are you sure to delete this member? (Y/N) ");
                String booleanOption = sc.next().toUpperCase();
                if (booleanOption.equalsIgnoreCase("Y")){
                    members.remove(deleteMember);
                    System.out.println("Deleted - "+ deleteMember.getMemberName());
                    DefaultMember.memberCountDecrement();
                }else {
                    System.out.println("** Deletion Canceled **");
                }
            }else {
                System.out.println("No record found to delete");
            }
            break;
        }
    }

    public void displayMembers(){
        // Check member list Reached max or not
        if (members.size()==0){
            System.out.println("* No data to print *");
            return;
        }

        // Print DefaultMember class info in members arraylist
        for (DefaultMember member:members){
            System.out.println(member);

        }
    }


    // Sort Data in Acceding method
    public void sortData(){
        if (members.size()==0){
            System.out.println("** No date entered to Sort **");
            return;
        }

        ArrayList<DefaultMember> temp = new ArrayList<>(members);
        for (int j=0; j<members.size();j++){
            for (int i=0; i<members.size();i++){
                if (temp.get(j).compareTo(temp.get(i))<0){
                    DefaultMember swapMe = temp.get(i);
                    temp.set(i,temp.get(j));
                    temp.set(j,swapMe);
                }
            }
        }
        for (DefaultMember member : temp){
            System.out.println(member);
        }
    }

    // Display Member list from GUI
    public void listOfMembers(){
        Stage stage = new Stage();
        TableView<DefaultMember> table = new TableView<>();
        TableColumn<DefaultMember, String> memberNum = new TableColumn<>("Member Number");
        memberNum.setCellValueFactory(new PropertyValueFactory<>("membershipNum"));
        memberNum.setStyle("-fx-alignment: CENTER;");
        TableColumn<DefaultMember, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        nameCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<DefaultMember, String> dateCol = new TableColumn<>("Membership Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));
        dateCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<DefaultMember, String> typeCol = new TableColumn<>("Member Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        typeCol.setStyle("-fx-alignment: CENTER;");
        TableColumn<DefaultMember, String> specialCol = new TableColumn<>("Age / School");
        specialCol.setCellValueFactory(new PropertyValueFactory<>("special"));
        specialCol.setStyle("-fx-alignment: CENTER;");
        table.getColumns().setAll(memberNum,nameCol,dateCol,typeCol,specialCol);
        table.setPrefWidth(600);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getItems().setAll(members);
        table.setStyle("-fx-pref-rows: 100");

        TextField searchField = new TextField();
        Button searchBtn =  new Button("Search");
        searchBtn.setOnAction(event -> {
            String searchMe = searchField.getText();
            searchField.clear();
            System.out.println(searchMe);
            if (searchMe.equals("")){
                table.getItems().setAll(members);
            }else{
                ArrayList<DefaultMember> tempArray = new ArrayList<>();
                for (DefaultMember member : members){
                    if (member.search(searchMe)){
                        tempArray.add(member);
                    }
                }
                table.getItems().setAll(tempArray);
            }
        });
        Label label = new Label("GYM MANAGEMENT SYSTEM");
        label.setStyle("-fx-font-size: 20");
        HBox search = new HBox(label,searchField,searchBtn);
        search.setSpacing(10);
        search.setStyle("-fx-padding: 10");
        VBox mainContainer = new VBox(search,table);
        //mainContainer.setSpacing(10);

        Scene scene = new Scene(mainContainer);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void saveData(){

        if (members.size()==0){
            System.out.println("** No Data Entered to Save **");
            return;
        }
        try {
            BufferedWriter print = new BufferedWriter(
                    new FileWriter("src\\GymManagementSystem\\ExportFiles\\GymManagerOutput.txt", false));
            print.write("<<<<<<<<< Welcome to Gym Manager >>>>>>>>>\n\n");
            for (DefaultMember member : members){
                print.write(member.toString()+ "\n");
            }
            print.close();
            System.out.println("** File Exported **");
        } catch (IOException e) {
            System.out.println("Exception occurred when writing");
        }
    }

    public static void main(String[] args)  {
        launch();
    }
}
