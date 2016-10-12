// Daniel Hernandez
// 10/24/15
// CSC 311
// Project 2

/*  
    Project 2: Analytics of web broswer history. This code fully implements all
    options and requirements as stated in the Project2.pdf file for this
    assignment. The program uses a logins.txt file to keep track of user login
    credentials and also writes to this file when new users register. Each user
    is assigned a <username>.txt file, where <username> is the actual username
    of the user, at the time they begin to enter websites. The text file will
    contain all the websites the user enters and is used in options 4 - 7 to
    perform the required operations.
*/

// Required imports, java.io was used for file operations.
import java.util.*;
import java.io.*;

public class webAnalytics
{
    public static void main(String[] args) // begin main method (6 lines)
    {
        webAnalytics web = new webAnalytics(); //default constructor called
        
        web.showMenu(); // This is where the fun starts.
    } // end main method
    
    /*  Displays the menu to the user. Accepts the user's input and if the input
        is valid, that option will be executed from the switch-case block. If
        an invalid option is entered, the menu will be displayed again and the
        user will be asked for input again. This repeats until the input is
        valid. */
    
    public void showMenu() // begin showMenu() method
    {
        Scanner input = new Scanner(System.in);
        
        // Display menu to user and take input.
        System.out.println("Select an option by typing its number and hitting enter:"
                         + "\n1. Register and create user login."
                         + "\n2. Login"
                         + "\n3. Visit a website."
                         + "\n4. Display browsing history in chronological order."
                         + "\n5. Display browsing history in reverse chronological order."
                         + "\n6. Display the number of times each website is visited."
                         + "\n7. Display the most visited website."
                         + "\n8. Logout or end the program.");
        
        System.out.print("> ");
        
        // test that input is at least a number, not a string or character.
        try
        {
            optionEntered = input.nextInt();
            System.out.println();
        }
        catch (InputMismatchException e)
        {
        }
        
        // execute option chosen by user of use default case for all other input
        switch(optionEntered)
        {
            case 1:
            {
                registerUser(); // Option 1: Register user
                break;
            }
            
            case 2:
            {
                loginUser(); // Option 2: Login
                break;
            }
            
            case 3:
            {
                visitWebsites(); // Option 3: visit websites
                break;
            }
            
            case 4:
            {
                browsingHistory(); // Option 4: view browsing history
                break;
            }
            
            case 5:
            {
                reverseBrowsingHistory(); // Option 5: view reverse browser history
                break;
            }
            
            case 6:
            {
                websiteCount(); // Option 6: count website visits
                break;
            }
            
            case 7:
            {
                mostVisitedWebsite(); // Option 7: view most visited website
                break;
            }
            
            case 8:
            {
                userLogout(); // Option 7: Logout the user or end the program
                break;
            }
            default:
            {
                // when input is invalid this block executes.
                
                System.out.println("\nInvalid option selected. Please try again.\n");
                showMenu();
                break;
            }
        }
    } // end showMenu()
    
    /*  This method will open the logins.txt file that stores all ther users'
        username and password. If the file does not exist, it will be created. */
    
    public File openLoginFile() // begin openLoginFile() method
    {
        try
        {
            File loginFile = new File("logins.txt"); // locate the file
            
            if(!loginFile.exists())
            {
                loginFile.createNewFile(); // create file if it doesnt exist.
            }
            
            return loginFile; // opens file
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    } //end openLoginFile()
    
    /*  This method will open the currently logged in user's text file that
        contains their browsing history. If the file doesnt exists, it will be
        created. */
    
    public File openUserFile() // begin openUserFile() method
    {
        try
        {
            // locate the file
            File userWebHistoryFile = new File(currentlyLoggedInUser + ".txt");
            
            if(!userWebHistoryFile.exists())
            {
                userWebHistoryFile.createNewFile(); // create the file if it does not exist.
            }
            
            return userWebHistoryFile; // opens the file
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    } // end openUserFile()
    
    /*  Allosw the user to register in order for them to log in. This method checks
        if the username chosen by the user is already taken by another user. The user
        is allowed 2 attempts to enter a unique username, otherwise a username is
        created for them using their last name and random numbers if necessary to
        meet the 8 character length requirement. The user is then asked to create
        a password. They will continue to prompted until their password is at least
        8 characters long.
    */
    
    public void registerUser() // begin registerUser() method
    {
        // required variables
        Scanner input = new Scanner(System.in);
        String line;
        String username;
        String usernameEntered;
        String password;
        String passwordEntered = null;
        String firstName;
        String lastName;
        boolean usernameExists;
        int registrationAttempts = 0;
        
        File loginFile = openLoginFile(); // open the logins.txt file
        
        // checks if a user is logged in and if so, they are returned to the menu.
        if(userLoggedIn == true)
        {
            System.out.println("In order for another user to register, you must logout first.");
            showMenu();
        }
        
        // if not logged in, proceed with registration.
        if(userLoggedIn == false)
        {
            System.out.println("Please enter your first and last name.");
            System.out.print("> ");
        
            String firstLastName = input.nextLine(); //prompted for first and last name.
            
            // do-while loop used for prompting the user for a username. Loop ends
            // when a unique username is entered or attemps exceed 2.
            do
            {
                usernameExists = false;
                
                System.out.println("\nPlease create a username.");
                System.out.print("> ");
                
                usernameEntered = input.nextLine(); // prompt for username
                
                // try-catch block reads the logins.txt file checking if the
                // username already exists.
                try
                {
                    // required to read the file properly
                    FileReader fr = new FileReader(loginFile);
                    BufferedReader br = new BufferedReader(fr);
                    
                    // while loop reads each line of file
                    while((line = br.readLine()) != null)
                    {
                        // each line contains 4 items, StringTokenizer seperates
                        // the items into tokens.
                        StringTokenizer st = new StringTokenizer(line);
                        
                        // each token stored in appropriate variable
                        username = st.nextToken();
                        password = st.nextToken();
                        firstName = st.nextToken();
                        lastName = st.nextToken();
                        
                        // notify user that the username already exists.
                        if(usernameEntered.equals(username))
                        {
                            System.out.println("\nSorry, this username already exists. Please try again.");
                            usernameExists = true;
                            registrationAttempts++;
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            while(usernameExists == true && registrationAttempts < 2);
            
            // if attempts exceeds 2, create a username for the user.
            if(usernameExists == true && registrationAttempts == 2)
            {
                // Gets the last name of the user form their input above.
                StringTokenizer st1 = new StringTokenizer(firstLastName);
                String firstName1 = st1.nextToken();
                String lastName1 = st1.nextToken();
                Random rng = new Random();
                
                System.out.println("Maximum number of attempts reached. You will now be given a username.\n");
                
                usernameEntered = lastName1; // username will begin with user's last name
                
                // Adds random digits to username until the username is
                // 8 characters long.
                while(usernameEntered.length() < 8)
                {
                    int num = rng.nextInt(10);
                    usernameEntered = usernameEntered + num;
                }
                
                System.out.println("Your username is: " + usernameEntered); // show new username
                
                usernameExists = false; // changes to false now that a username has been assigned to the user.
            }
            
            // Prompts the user to create a pssword, repeats until their passwrod is 8 characters long.
            if(usernameExists == false)
            {
                // Do-while prompting for password.
                do
                {
                    System.out.println("\nPlease create a password that is at least 8 characters long.");
                    System.out.print("> ");
                    
                    passwordEntered = input.nextLine();
                    
                    if(passwordEntered.length() < 8)
                    {
                        System.out.println("\nSorry, this password is too short. Please try again.");
                    }
                } while (passwordEntered.length() < 8);
            }
            
            if(usernameExists == false)
            {
                // writes the username and password to the logins.txt file
                try
                {   
                    FileWriter fw = new FileWriter(loginFile.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.append(usernameEntered + " " + passwordEntered + " " + firstLastName);
                    bw.newLine();
                    bw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
                System.out.println("You have successfully registered. You will now be logged in.");
                loginUserfromReg(usernameEntered, passwordEntered); // Automatically logs in the user.
            }
        }
    } // end registerUser()
    
    /*  This method is only called when a new user registers. It is used to
        automatically log them in for the first time. */
    
    public void loginUserfromReg(String username, String password) // begin loginUserfromReg(String, String) method
    {
        // required variables
        String usernameReceived = username;
        String passwordReceived = password;
        String usernameRead;
        String passwordRead;
        String line;
        String firstName;
        String lastName;
        
        File loginFile = openLoginFile(); // Opens the logins.txt file
        
        // Reads the file checking that the username and password entered are correct.
        try
        {
            FileReader fr = new FileReader(loginFile);
            BufferedReader br = new BufferedReader(fr);
            
            while((line = br.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line);
                usernameRead = st.nextToken();
                passwordRead = st.nextToken();
                firstName = st.nextToken();
                lastName = st.nextToken();
                
                if(usernameReceived.equals(usernameRead) && passwordReceived.equals(passwordRead))
                {
                    System.out.println("\nYou have successfully logged in, " + firstName + " " + lastName + "\n");
                    userLoggedIn = true;
                    currentlyLoggedInUser = usernameReceived;
                }
            }
            
            // added in case an error were to occur during the registration process.
            if(userLoggedIn == false)
            {
                System.out.println("\nThere was an error with registration and we were not able to log you in. "
                                  + "Please try to register again or login.");
                
                showMenu();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        // show the menu when logged in successfully.
        if(userLoggedIn == true)
        {
            showMenu();
        }  
    } // end loginUserFromReg(String, String)
    
    /*  This method prompts the user for their username and passwrod. The file
        logins.txt is opened and read line by line checking if the username and 
        password entered by the user match an entry in the file. If there is a
        match, the user is automatically logged in. Otherwise, they are asked
        to try again. After 3 failed attempts, the user is returned to the menu. */
    
    public void loginUser() // begin loginUser() method
    {
        // required variables
        Scanner input = new Scanner(System.in);
        String line;
        String username;
        String usernameEntered;
        String password;
        String passwordEntered;
        String firstName;
        String lastName;
        boolean loginSuccessful = false;
        int loginAttempts = 0;
        
        // do-while loop prompts for username and passwrod, then checks file.
        // 3 attempts are allowed.
        do
        {
            // checks if user is already is logged in.
            if(userLoggedIn == true)
            {
                System.out.println("You are already logged in.");
                continue;
            }
            
            loginSuccessful = false;
            
            // prompts for username and password
            System.out.println("Please enter your username.");
            System.out.print("> ");
            usernameEntered = input.nextLine();
        
            System.out.println("\nPlease enter your password.");
            System.out.print("> ");
            passwordEntered = input.nextLine();
        
            File loginFile = openLoginFile(); // opens logins.txt file
            
            // checks the file for a matching username and password combination
            try
            {
                FileReader fr = new FileReader(loginFile);
                BufferedReader br = new BufferedReader(fr);
            
                while((line = br.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(line);
                    username = st.nextToken();
                    password = st.nextToken();
                    firstName = st.nextToken();
                    lastName = st.nextToken();
                    
                    // Log the user in if a match is found.
                    if(usernameEntered.equals(username) && passwordEntered.equals(password))
                    {
                        System.out.println("\nYou have successfully logged in, " + firstName + " " + lastName + "\n");
                        loginSuccessful = true;
                        userLoggedIn = true;
                        currentlyLoggedInUser = usernameEntered;
                    }
                }
                
                // when login credentials are invalid or not found.
                if(loginSuccessful == false)
                {
                    System.out.println("\nEither the username or password is incorrect.\n");
                    loginAttempts++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        } while(loginSuccessful == false && userLoggedIn == false && loginAttempts < 3);
        
        // After 3 failed attempts, takes the user back to the menu.
        if(userLoggedIn == false && loginAttempts == 3)
        {
            System.out.println("Too many login attempts, returning you to the menu.");
            System.out.println();
            
            showMenu();
        }
        
        // If the user has logged in, show the menu.
        if(userLoggedIn == true)
        {
            showMenu();
        }
    } // end loginUser()
    
    /*  This method first opens the current user's text file. The user is then prompted
        to enter a webstie. After each input, it will be written to the file. This repeats
        until the user enters a $, which tells the program that the user is done entereing
        websites. */
    
    public void visitWebsites() // begin visitWebsites() method
    {
        // checks that the user is logged in first.
        if(currentlyLoggedInUser == null)
        {
            System.out.println("You must log in first to select this option.\n");
            showMenu();
        }
        
        // if they are logged in, proceed with this if block.
        if(currentlyLoggedInUser != null)
        {
            //required variables
            Scanner input = new Scanner(System.in);
        
            String webURL;
            websites = new String[CAPACITY];
            
            File usersWebHistory = openUserFile(); // opens the user's text file
            
            // do while loop will continue to execute until $ is entered.
            do
            {
                // prompts the user to type in a website.
                System.out.println("Please enter a website URL (in the format wwws.example.com) or $ to stop entering website URLs.");
                System.out.print("> ");
            
                webURL = input.nextLine();
             
                try
                {
                    // required to write to the file. True is added so that contents of file are not over written
                    FileWriter fw = new FileWriter(usersWebHistory.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    
                    // when $ is not entered, execute this block.
                    if(!(webURL.equals("$")))
                    {
                        bw.append(webURL); // writes the website entered to the file
                        bw.newLine(); // writes a newline to the file
                        bw.close(); // required for file writer to work properly
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } while(!(webURL.equals("$")) || currentlyLoggedInUser == null);
            
            // display the menu when the user enters $
            if(webURL.equals("$"))
            {
                showMenu();
            }
        }
    } // end visitWebsites()
    
    /*  This method will read the current user's text file, storing each line into
        an array and a queue. The array's only use right now is to add items
        to the queue. The queue will contain all the websites visited by the user
        including any duplicates. Then dequeue is used to display all the websites. */
    
    public void browsingHistory() // begin browsingHistory method
    {
        // makes sure that the user is logged in first.
        if(currentlyLoggedInUser == null)
        {
            System.out.println("You must be logged in to view your browsing history.\n");
            showMenu();
        }
        
        if(currentlyLoggedInUser != null)
        {
            File usersBrowsingHistory = openUserFile(); // open the user's text file
            
            // Variables that are required.
            String websiteRead;
            String line;
            websites = new String[CAPACITY];
            Q = new String[CAPACITY]; // Queue
            
            // Reads the file, line by line, adding each website to the array.
            // Then from the array, each item is enqueued to the queue.
            try
            {
                int i = 0;
                
                FileReader fr = new FileReader(usersBrowsingHistory);
                BufferedReader br = new BufferedReader(fr);
                
                // write each line to the array, then from the array to the queue.
                while((line = br.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(line);
                    websiteRead = st.nextToken();
                    
                    websites[i] = websiteRead; // From file to the array.
                    enqueue(websites[i]); // From the array to the queue.
                    i++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            // outputs the user's web history.
            System.out.println("Here is your complete web browsing history, in chronological orde:");
            
            while(!isEmpty())
            {
                System.out.println("\t" + dequeue()); // removes from queue and displays the website.
            }
            
            System.out.println();
            
            showMenu();
        }
    } // end browsingHistory()
    
    /*  This method reads the current user's text file, line by line, adding the websites to
        the websties[] array. To remove duplicates, a second array websites2[] is used.
        From websites[], each element that does not already exist in websites2[] is added
        to websites2[]. A stack called theStack is then used to reverse the order. So from
        websites2[], each element is pushed onto theStack. Then when displaying the browsing
        history, each item will be popped from theStack. */
    
    public void reverseBrowsingHistory() // begin reverseBrowsingHistory() method
    {
        // makes sure that user is logged in first.
        if(currentlyLoggedInUser == null)
        {
            System.out.println("You must be logged in to view your browsing history in reverse chronological order.\n");
            showMenu();
        }
        
        if(currentlyLoggedInUser != null)
        {
            // required variables
            File usersBrowsingHistory = openUserFile();
            String websiteRead;
            String line;
            String data;
            boolean webURLExists;
            websites = new String[CAPACITY];
            websites2 = new String[CAPACITY];
            theStack = new String[CAPACITY]; // Stack
            int elementInsert = 0;
            
            try
            {
                int i = 0;
                
                FileReader fr = new FileReader(usersBrowsingHistory);
                BufferedReader br = new BufferedReader(fr);
                
                while((line = br.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(line);
                    websiteRead = st.nextToken();
                    
                    websites[i] = websiteRead;
                    i++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            // writes websites from the array websites[] to the array websites2[]
            // as long as the current website element doesnt exist in websites2[]
            for(int k = 0; websites[k] != null; k++)
            {
                // get element from websites[] array
                webURLExists = false;
                data = websites[k];
                
                // check current element with elements in websites2[] array
                for(int l = 0; websites2[l] != null; l++)
                {   
                    if(data.equals(websites2[l]))
                    {
                        webURLExists = true; // if there is a match, dont add current element
                    }
                }
                
                // when there is not match, add the current element to the array websites2[]
                // then push tha element form websites2[] array into theStack.
                if(webURLExists == false && !data.equals(websites[k+1]))
                {
                    websites2[elementInsert] = data;
                    push(websites2[elementInsert]);
                    elementInsert++; // keeps track of where to insert an element into websites2[] array
                }
            }
            
            // Displays websites in reverse chornological order, without diplucates.
            System.out.println("Here is your web browsing history "
                             + "in reverse chronological order, with duplicates removed:");
            
            while(!(empty()))
            {
                System.out.println("\t" + pop()); // pop() used to get the websites from theStack.
            }
            
            System.out.println();
            
            showMenu();
        }
    } // end reverseBrowsingHistory()
    
    /*  This method performs the operations from reverseBrowsingHistory() method, so that part
        will not be explained in the comments for this method. Continuing from where the operations
        in reverseBrowingHistoryMethod() leave off, the websites from the array websites2[] array will
        be added into a 2 Dimensional array where the top row contains the websites and the
        bottom row contains how many times each website. With each element added to the top row, a loop
        will go through the original websites[] array looking for duplicates. When found, a counter is
        incremented. At the end the number of the counter is added to the bottom row corresponding to the current
        element. The counter is then reset at the beginning of the loop to count for the next element. */
    
    public void websiteCount() // begin websiteCount() method
    {
        // makes sure the user is logged in.
        if(currentlyLoggedInUser == null)
        {
            System.out.println("You must be logged in to view the number of times you visit each website.\n");
            showMenu();
        }
        
        if(currentlyLoggedInUser != null)
        {
            // required variables
            File usersBrowsingHistory = openUserFile();
            String websiteRead;
            String line;
            String data;
            boolean webURLExists;
            websites = new String[CAPACITY];
            websites2 = new String[CAPACITY];
            websitesCount = new String[CAPACITY][CAPACITY]; // 2 Dimensional array
            int elementInsert = 0;
            int j = 0;
            
            try
            {
                int i = 0;
                
                FileReader fr = new FileReader(usersBrowsingHistory);
                BufferedReader br = new BufferedReader(fr);
                
                while((line = br.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(line);
                    websiteRead = st.nextToken();
                    
                    websites[i] = websiteRead;
                    i++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            for(int k = 0; websites[k] != null; k++)
            {
                int websiteVisitCounter = 0; // the counter used to know how many duplicates occur for each website
                webURLExists = false;
                data = websites[k];
                
                for(int l = 0; websites2[l] != null; l++)
                {
                    if(data.equals(websites2[l]))
                    {
                        webURLExists = true;
                    }
                }
                
                if(webURLExists == false && !data.equals(websites[k+1]))
                {
                    // counter is used to check how many duplicates occur in the
                    // original array websites[]. After, the counter is inserted
                    // to websitesCount[1][] and it corresponds to how many
                    // duplicates occur for the current website.
                    for(int m = 0; websites[m] != null; m++)
                    {
                        if(data.equals(websites[m]))
                        {
                            websiteVisitCounter++;
                        }
                    }
                    
                    // insertion to the array and to the 2D array.
                    websites2[elementInsert] = data;
                    websitesCount[0][elementInsert] = data;
                    websitesCount[1][elementInsert] = Integer.toString(websiteVisitCounter);
                    elementInsert++;
                }
            }
            
            // Outputs how many times each website was visited. Reading from the 2D array
            System.out.println("Here are the number of times that you visited each website:");
            
            while(websitesCount[0][j] != null)
            {
                System.out.println("\t" + websitesCount[0][j] + " was visited " + websitesCount[1][j] + " times");
                j++;
            }
            
            System.out.println();
            
            showMenu();
        }
    } // end websiteCount()
    
    /*  This method contains all the operations from websiteCount() so they will not be
        explained in the comments of this method. Continuing from where websiteCount()
        leaves off, a variable num is used to keep track of the maximum number of vists for
        a website. A while loop goes through the 2D array, checking each number stored in the
        bottom row. Variable num is set to the number in the 2D array when it is higher than the
        current value in the variable num. At the end, go through the array again, looking for
        the match between num and the value sotred in websitesCount[1][]. When the match is found,
        the corresponding element websites[0][] and websites[1][] is displayed. */
    
    public void mostVisitedWebsite() // begin mostVisitedWebsite() method
    {
        if(currentlyLoggedInUser == null)
        {
            System.out.println("You must be logged in to view which website you have visited the most.\n");
            showMenu();
        }
        
        if(currentlyLoggedInUser != null)
        {
            // required variables
            File usersBrowsingHistory = openUserFile();
            String websiteRead;
            String line;
            String data;
            boolean webURLExists;
            websites = new String[CAPACITY];
            websites2 = new String[CAPACITY];
            websitesCount = new String[CAPACITY][CAPACITY];
            int elementInsert = 0;
            int numberFromArray; // number from 2D array
            int numberFromArray1; // number from 2D array when checking for match
            int num = 0; // used to keep track of highest number of visits for a website.
            int j = 0;
            int n = 0;
            
            try
            {
                int i = 0;
                
                FileReader fr = new FileReader(usersBrowsingHistory);
                BufferedReader br = new BufferedReader(fr);
                
                while((line = br.readLine()) != null)
                {
                    StringTokenizer st = new StringTokenizer(line);
                    websiteRead = st.nextToken();
                    
                    websites[i] = websiteRead;
                    i++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            for(int k = 0; websites[k] != null; k++)
            {
                int websiteVisitCounter = 0;
                webURLExists = false;
                data = websites[k];
                
                for(int l = 0; websites2[l] != null; l++)
                {
                    if(data.equals(websites2[l]))
                    {
                        webURLExists = true;
                    }
                }
                
                if(webURLExists == false && !data.equals(websites[k+1]))
                {
                    for(int m = 0; websites[m] != null; m++)
                    {
                        if(data.equals(websites[m]))
                        {
                            websiteVisitCounter++;
                        }
                    }
                    
                    websites2[elementInsert] = data;
                    websitesCount[0][elementInsert] = data;
                    websitesCount[1][elementInsert] = Integer.toString(websiteVisitCounter);
                    elementInsert++;
                }
            }
            
            // loop achecks for maximum number from the 2D array.
            while(websitesCount[1][j] != null)
            {
                // Integer.parseInt() required because we are reading from an array of String
                numberFromArray = Integer.parseInt(websitesCount[1][j]);
                
                // when num less than numberFromArray, set num equal to numberFromArray
                if(num < numberFromArray)
                {
                    num = numberFromArray;
                }
                
                j++;
            }
            
            // Output the most visited website.
            System.out.println("Here is the website you have visited the most:");
            
            // Loop goes through array checking for the matching number.
            while(websitesCount[1][n] != null)
            {
                numberFromArray1 = Integer.parseInt(websitesCount[1][n]);
                
                // when match is found, output the website and the number of visits/
                if(num == numberFromArray1)
                {
                    System.out.println("\t" + websitesCount[0][n] + " which was visited " + websitesCount[1][n] + " times");
                }
                
                n++;
            }
            
            System.out.println();
            
            showMenu();
        }
    } // end mostVisitedWebsite()
    
    /*  This method allows the ueer to logout or end the program. After the user is logged
        out, they can end the program or return to the menu to log in again. If not logged
        in, they can end the program or return to the menu.
    */
    public void userLogout() // begin userLogout() method
    {
        // when the user is not logged in, they still the option of ending the program
        // or returning to the menu.
        if(currentlyLoggedInUser == null)
        {
            Scanner input = new Scanner(System.in);

            System.out.println("You are not logged in.\n");
            
            System.out.println("To end the program, enter a $. Any other character entered will allow you "
                             + "or another user to log in.");
            System.out.print("> ");
            
            String option = input.nextLine();
            
            // end program when $ is entered.
            if(option.equals("$"))
            {
                System.out.println("\nEnding program.");
            }
            
            // anything else will return to the menu.
            if(!(option.equals("$")))
            {
                System.out.println();
                
                showMenu();
            }
        }
        
        // the current user is logged out. Variable currentlyLoggedInUser is set to null
        // and will be filled in with the username of the next user that logs in. Variable
        // userLoggedIn is set to false to indicate that no user is currently logged in.
        if(currentlyLoggedInUser != null)
        {
            Scanner input = new Scanner(System.in);
            
            currentlyLoggedInUser = null;
            userLoggedIn = false;
            
            System.out.println("You have succesfully logged out.\n");
            
            System.out.println("To end the program, enter a $. Any other character entered will allow you "
                             + "or another user to log in.");
            System.out.print("> ");
            
            String option = input.nextLine();
            
            //end program when $ is entered
            if(option.equals("$"))
            {
                System.out.println("\nEnding program.");
            }
            
            // anything else returns the logged out user to the menu.
            if(!(option.equals("$")))
            {
                System.out.println();
                
                showMenu();
            }
        }
    } // end userLogout()
    
    public boolean enqueue(Object obj)
    {
        if(isFull())
        {
            System.out.println("Queue is full, unable to add.");
            return false;
        }
        
        Q[r] = obj;
        r = (r + 1) % CAPACITY;
        count++;
        
        return true;
    }
    
    public Object dequeue()
    {
        if(isEmpty())
        {
            System.out.println("Queue is empty, unable to remove.");
            return null;
        }
        
        Object result = Q[f];
        Q[f] = null;
        f = (f + 1) % CAPACITY;
        count--;
        
        return result;
    }
    
    public boolean isEmpty()
    {
        return (r == f && count == 0);
    }
    
    public boolean isFull()
    {
        return (count == CAPACITY);
    }
    
    public void push(Object obj)
    {
        if(!full())
            theStack[sp++] = obj;
    }
    
    public Object pop()
    {
        if(!empty())
            return theStack[--sp];
        else
            return null;
    }
    
    public boolean empty()
    {
        if(sp == 0)
            return true;
        else
            return false;
    }
    
    public boolean full()
    {
        if(sp == CAPACITY)
            return true;
        else
            return false;
    }
    
    private static final int CAPACITY = 100;
    private int sp;
    private String[] websites;
    private String[] websites2;
    private String[][] websitesCount;
    private Object[] theStack;
    private Object[] Q;
    private int f = 0;
    private int r = 0;
    private int count = 0;
    private int optionEntered;
    private boolean userLoggedIn = false;
    private String currentlyLoggedInUser;
}