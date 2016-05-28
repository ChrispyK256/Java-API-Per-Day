// File Name SendEmail.java

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;



public class APIEmailMain
{
   public static String objectName;
   public static String apiHyperlink;
   private final static String FILENAME = "C:\\Users\\Fitzgerald\\workspace\\API Email\\Java API Links.txt";
   
   public static void main(String [] args) throws IOException, MessagingException
   {    
       GoogleMail gm = new GoogleMail();
      // Recipient's email ID needs to be mentioned.
      String to = "chrispyk256@gmail.com";

      // Sender's email ID needs to be mentioned
      String from = "web@gmail.com";

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      //Properties properties = System.getProperties();

      // Setup mail server
      //properties.setProperty("mail.smtp.host", host);
      Properties props = new Properties();
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");
      props.put("mail.imap.auth.mechanisms", "XOAUTH2");

      // Get the default Session object.
      //Session session = Session.getDefaultInstance(properties);
      Session session = Session.getInstance(props,
              new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("chrispyk256", "ssbPikaP0wrr");
                }
              });
      Store store = session.getStore("smtp");
      /**
       * I left off here, I'll need to follow the 1.5.5 procedure below
       *            https://java.net/projects/javamail/pages/OAuth2
       * and here
       *            https://developers.google.com/identity/protocols/OAuth2?csw=1
       * to have a python script give me the oauth2 token below.  Blaaaaah.
       * 
       * Alternatively, you could work with the GoogleMail class to fix up the provider, and then get the security working.
       */
      store.connect("smtp.gmail.com", "chrispyk256", oauth2_access_token);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         
         //Randomly Select an Object out of the API
         selectAPI();
         
         // Set Subject: header field
         message.setSubject("Today's Random API Object is... " + generateSubject());

         // Now set the actual message
         message.setText(generateMessage());

         // Send message
        // gm.Send("Today's Random API Object is... " + generateSubject(), apiHyperlink);
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }

   private static String generateMessage() {
        return apiHyperlink;
    }
     
    private static String generateSubject() {
        return "API Of The Day: "+ objectName;
    }
    
    /*
     * Selects today's API Object to use
     */
    private static void selectAPI() throws IOException {
        // open file
        BufferedReader br = new BufferedReader(new FileReader(FILENAME));
        
        // randomly select API line from file
        int lineNum = (int) (Math.random() * 4024);
        
        // go to line in file
        for(int i = 0; i<lineNum; i++)
            br.readLine();
        
        String selectedLine = br.readLine();
        // set variables
        objectName = selectedLine.substring(selectedLine.indexOf('>',10) +4, selectedLine.indexOf("</a", 15)-4);
        apiHyperlink = "https://docs.oracle.com/javase/7/docs/api/" + selectedLine.substring(selectedLine.indexOf("href=")+6, selectedLine.indexOf(".html")+5);
        System.out.println(objectName + "  " + apiHyperlink);
        
    }
}