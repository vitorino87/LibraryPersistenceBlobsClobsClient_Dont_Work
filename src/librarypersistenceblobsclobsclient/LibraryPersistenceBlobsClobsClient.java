/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypersistenceblobsclobsclient;

import com.tutorialspoint.entity.Books;
import com.tutorialspoint.entity.Publisher;
import com.tutorialspoint.stateless.LibraryPersistenceBlobsClobsBeanRemote;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author tiago.lucas
 */
public class LibraryPersistenceBlobsClobsClient {

    BufferedReader brConsoleReader = null;
    Properties props;
    InitialContext ctx;

    {
        props = new Properties();
        try {
            props.load(new FileInputStream("jndi.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ctx = new InitialContext(props);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LibraryPersistenceBlobsClobsClient ejbTester = new LibraryPersistenceBlobsClobsClient();
        ejbTester.testBlobClob();
    }

    private void showGUI() {
        System.out.println("*****************************");
        System.out.println("Welcome to Book Store");
        System.out.println("*****************************");
        System.out.print("Options\n1. Add Book\n2. Exit\nEnter Choice: ");

    }

    private void testBlobClob() {
        try {
            int choice = 1;
            LibraryPersistenceBlobsClobsBeanRemote libraryBean
                    = (LibraryPersistenceBlobsClobsBeanRemote) ctx.lookup("LibraryPersistenceBlobsClobsBean/remote");
            while (choice != 2) {
                String bookName;
                showGUI();
                String strChoice = brConsoleReader.readLine();
                choice = Integer.parseInt(strChoice);
                if (choice == 1) {
                    System.out.print("Enter book name: ");
                    bookName = brConsoleReader.readLine();
                    String xml = "<book><name>" + bookName + "</name></book>";
                    Books book = new Books();
                    book.setName(bookName);
                    byte[] imageBytes = {0x32, 0x32, 0x32, 0x32, 0x32,
                        0x32, 0x32, 0x32,
                        0x32, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32,
                        0x32, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32, 0x32
                    };
                    String aux="";
                    for(int i=0;i<imageBytes.length;i++){
                        aux+=imageBytes[i];
                    }
                    System.out.println(aux);
                    System.out.println(xml);
                    Publisher publisher = new Publisher("Tiago","UFABC");
                    book.setPublisher(publisher);
                    book.setImage(imageBytes);
                    book.setXml(xml);

                    libraryBean.addBook(book);
                } else if (choice == 2) {
                    break;
                }
            }
            List<Books> booksList = libraryBean.getBooks();
            System.out.println("Book(s) entered so far: " + booksList.size());
            int i = 0;
            for (Books book : booksList) {
                System.out.println((i + 1) + ". " + book.getName());
                byte[] imageByts = book.getImage();
                if (imageByts != null) {
                    System.out.println("image bytes: [");
                    for (int j = 0; j < imageByts.length; j++) {
                        System.out.println("0x" + String.format("%x", imageByts[j] + " "));
                    }
                    System.out.println("]");
                }
                System.out.println(book.getXml());
                i++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (brConsoleReader != null) {
                    brConsoleReader.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
