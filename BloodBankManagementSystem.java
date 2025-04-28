import java.util.*;
import java.io.*;

public class BloodBankManagementSystem {

    static String username = "admin";
    static String password = "1234";
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Donor> donors = new ArrayList<>();

    public static void main(String[] args) {
        loadDonorsFromFile();
        welcomeScreen();
    }

    static void welcomeScreen() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║  BLOOD BANK MANAGEMENT SYSTEM ║");
        System.out.println("╚══════════════════════════════╝\n");

        loading("Loading Blood Bank Database");

        System.out.print("\nEnter username: ");
        String inputUsername = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String inputPassword = sc.nextLine().trim();

        if (login(inputUsername, inputPassword)) {
            loading("\nLogging you in");
            System.out.println("\n✨ Login successful! Welcome, " + username + "! ✨");
            mainMenu();
        } else {
            System.out.println("\n❌ Invalid login credentials. Access denied! ❌");
        }
    }

    static boolean login(String inputUsername, String inputPassword) {
        return inputUsername.equals(username) && inputPassword.equals(password);
    }

    static void mainMenu() {
        int choice = -1;
        do {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║    BLOOD BANK MANAGEMENT MENU     ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.println("1. Add Donor");
            System.out.println("2. View All Donors");
            System.out.println("3. Search Donor by Blood Group");
            System.out.println("4. Exit");
            System.out.println("5. Change Password");
            System.out.println("6. Delete Donor by Name and Contact");
            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); 
            } else {
                System.out.println("⚠️ Please enter a valid number!");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    addDonor();
                    break;
                case 2:
                    viewDonors();
                    break;
                case 3:
                    searchDonor();
                    break;
                case 4:
                    exitScreen();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    deleteDonor();
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Please try again!");
            }
        } while (choice != 4);
    }

    static void addDonor() {
        System.out.print("\nEnter Donor Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Donor Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Blood Group: ");
        String bloodGroup = sc.nextLine().trim();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine().trim();
        System.out.print("Enter Residence/Living Place: ");
        String livingPlace = sc.nextLine().trim();
        System.out.print("Enter District: ");
        String district = sc.nextLine().trim();

        Donor donor = new Donor(name, age, bloodGroup, contact, livingPlace, district);

        boolean exists = false;
        for (Donor d : donors) {
            if (d.name.equalsIgnoreCase(name) && d.contact.equals(contact)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            System.out.println("⚠️ Donor already exists with same Name and Contact!");
        } else {
            donors.add(donor);
            saveDonorsToFile();
            loading("\nSaving donor information");
            System.out.println("✅ Donor added and saved successfully!");
        }
    }

    static void viewDonors() {
        System.out.println("\n══════════ DONOR LIST ══════════");
        if (donors.isEmpty()) {
            System.out.println("No donors available!");
        } else {
            for (Donor d : donors) {
                System.out.println(d);
            }
        }
    }

    static void searchDonor() {
        System.out.print("\nEnter Blood Group to search: ");
        String bg = sc.nextLine().trim();
        boolean found = false;
        System.out.println("\n══════════ SEARCH RESULTS ══════════");
        for (Donor d : donors) {
            if (d.bloodGroup.equalsIgnoreCase(bg)) {
                System.out.println(d);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No donors found with blood group " + bg + "!");
        }
    }

    static void deleteDonor() {
        System.out.print("\nEnter Donor Name to delete: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Donor Contact Number: ");
        String contact = sc.nextLine().trim();
        boolean removed = donors.removeIf(d -> d.name.equalsIgnoreCase(name) && d.contact.equals(contact));
        if (removed) {
            saveDonorsToFile();
            loading("\nDeleting donor information");
            System.out.println("🗑️ Donor deleted successfully!");
        } else {
            System.out.println("❌ Donor not found with given Name and Contact Number!");
        }
    }

    static void changePassword() {
        System.out.print("\nEnter current password: ");
        String current = sc.nextLine().trim();
        if (current.equals(password)) {
            System.out.print("Enter new password: ");
            password = sc.nextLine().trim();
            loading("\nUpdating password");
            System.out.println("✅ Password changed successfully!");
        } else {
            System.out.println("❌ Incorrect current password!");
        }
    }

    static void exitScreen() {
        loading("\nClosing the Blood Bank System");
        System.out.println("\n🙏 Thank you for using Blood Bank Management System!");
        System.out.println("🩸 Every drop counts! 🩸\n");
        System.exit(0);
    }

    static void loading(String message) {
        System.out.print(message);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("\n⚠️ Loading interrupted!");
        }
    }

    @SuppressWarnings("unchecked")
    static void loadDonorsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("donors.txt"))) {
            donors = (ArrayList<Donor>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            donors = new ArrayList<>();
        }
    }

    static void saveDonorsToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("donors.txt"))) {
            out.writeObject(donors);
        } catch (IOException e) {
            System.out.println("❌ Error saving donors!");
        }
    }
}

class Donor implements Serializable {
    String name;
    int age;
    String bloodGroup;
    String contact;
    String livingPlace;
    String district;

    Donor(String name, int age, String bloodGroup, String contact, String livingPlace, String district) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.livingPlace = livingPlace;
        this.district = district;
    }

    @Override
    public String toString() {
        return "🩸 Name: " + name + ", Age: " + age + ", Blood Group: " + bloodGroup +
               ", Contact: " + contact + ", Residence/Living Place: " + livingPlace +
               ", District: " + district;
    }
}
