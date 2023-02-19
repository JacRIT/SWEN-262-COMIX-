import java.util.Scanner;

/*
 * This class takes the Comic objects that are supplied from 
 * CSVComicReader and insertes them into the database. 
 * 
 * THIS IS ONLY MEANT TO BE RUN AT THE START OF THE PROJECT 
 * OR IF SOMETHING GOES HORRIBLY WRONG
 * 
 */

public class CSVImporter {
    








    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Type \"I know what I'm doing, and I'm prepared for the consequences of my actions\" to proceed: ");
        String pass = reader.nextLine() ;
        reader.close();
        if (pass.compareTo("I know what I'm doing, and I'm prepared for the consequences of my actions") != 0) {
            System.out.println("Consult your team members first!");
            return ;
        }

        
        //Start process here

    }
}
