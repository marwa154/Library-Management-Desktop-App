# **BibliothequeFX - Library Management System**

##  **Project Overview**
A JavaFX desktop application for managing library operations including members, documents, loans, and returns.

##  **Project Structure**
```
BibliothequeFX/
├── src/
│   └── com/bibliotheque/app/
│       ├── Main.java                      # Application entry point
│       ├── controllers/                   # JavaFX Controllers
│       │   ├── MainMenuController.java
│       │   ├── AbonneListController.java
│       │   ├── AddAbonneController.java
│       │   ├── EditAbonneController.java
│       │   ├── DocumentListController.java
│       │   ├── AddDocumentController.java
│       │   └── EditDocumentController.java
│       ├── models/                        # Data Models
│       │   ├── Abonne.java               # Member model
│       │   ├── Document.java             # Document model
│       │   └── Exemplaire.java           # Copy model
│       ├── dao/                          # Data Access Objects
│       │   ├── AbonneDAO.java
│       │   └── DocumentDAO.java
│       └── config/                       # Configuration
│           ├── DatabaseConnection.java
│           └── TestConnection.java
├── database_setup.sql                    # Database creation script
├── lib/                                  # External libraries (create this folder)
│   └── mysql-connector-java-8.0.xx.jar
└── README.md
```

##  **Quick Start Guide**

### **Step 1: Install Prerequisites**
1. **Java JDK 17+** - [Download](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
2. **JavaFX SDK 21** - [Download](https://gluonhq.com/products/javafx/)
3. **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
4. **MySQL Workbench** (optional) - [Download](https://dev.mysql.com/downloads/workbench/)

### **Step 2: Clone/Download the Project**
```bash
git clone [your-repository-url]
cd BibliothequeFX
```

### **Step 3: Setup MySQL Database**

#### ** Using MySQL Workbench**
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Create a new schema:
```sql
CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;
```
4. Open and execute `database_setup.sql`


### **Step 4: Add MySQL Connector**

1. **Download MySQL Connector/J** from [here](https://dev.mysql.com/downloads/connector/j/)
2. Select "Platform Independent" version
3. Extract the ZIP file
4. Copy `mysql-connector-java-8.0.xx.jar` to the `lib/` folder (create if doesn't exist)

### **Step 5: Configure Database Connection**

Edit `src/com/bibliotheque/app/config/DatabaseConnection.java`:

```java
public class DatabaseConnection {
    // Update these values according to your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque_db";
    private static final String USERNAME = "root";      // Your MySQL username
    private static final String PASSWORD = "password";  // Your MySQL password
    
    // ... rest of the code remains the same
}
```

##  **IDE Setup**

### **For Eclipse Users:**

1. **Import Project:**
   ```
   File → Import → Existing Projects into Workspace
   → Select project folder → Finish
   ```

2. **Add MySQL Connector:**
   ```
   Right-click project → Build Path → Configure Build Path
   → Libraries → Add External JARs
   → Select mysql-connector-java-8.0.xx.jar from lib folder
   → Apply and Close
   ```

3. **Configure JavaFX:**
   ```
   Run → Run Configurations → Java Application
   → Arguments → VM arguments:
   --module-path "path/to/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml
   ```

### **For IntelliJ IDEA Users:**

1. **Open Project:**
   ```
   File → Open → Select project folder
   ```

2. **Setup JDK:**
   ```
   File → Project Structure → Project
   → Set Project SDK to Java 17
   → Set Project language level to 17
   ```

3. **Add Dependencies:**
   ```
   File → Project Structure → Modules → Dependencies
   → + → JARs or directories
   → Add both:
     1. JavaFX SDK lib folder
     2. mysql-connector-java-8.0.xx.jar
   ```

##  **Running the Application**

### **Method 1: Run from IDE**
1. Open `Main.java` in `src/com/bibliotheque/app/`
2. Click Run (▶) button

### **Method 2: Command Line**
```bash
# Navigate to project root
cd BibliothequeFX

# Compile and run (adjust paths as needed)
javac --module-path "path/to/javafx-sdk/lib" \
      --add-modules javafx.controls,javafx.fxml \
      -cp "lib/mysql-connector-java-8.0.xx.jar" \
      -d out src/com/bibliotheque/app/**/*.java

java --module-path "path/to/javafx-sdk/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "out:lib/mysql-connector-java-8.0.xx.jar" \
     com.bibliotheque.app.Main
```

##  **Testing the Connection**

Run the test program to verify database connectivity:

```java
// In your IDE, run:
src/com/bibliotheque/app/config/TestConnection.java
```

**Expected output:** `"Connexion à la base de données établie avec succès!"`

##  **Troubleshooting**

| Problem | Solution |
|---------|----------|
| "No suitable driver found" | Ensure MySQL connector JAR is in classpath |
| "Access denied for user" | Verify MySQL credentials in DatabaseConnection.java |
| JavaFX not loading | Check VM arguments and JavaFX SDK path |
| ClassNotFoundException | Rebuild project and refresh dependencies |

##  **Database Schema**

### **Main Tables:**
- **abonne**: Library members (CIN, name, address, phone)
- **document**: Books/resources (title, author, type, ISBN)
- **exemplaire**: Physical copies of documents
- **pret**: Loan transactions and history

### **Sample Queries:**
```sql
-- View all members
SELECT * FROM abonne;

-- View all documents
SELECT * FROM document;

-- View active loans
SELECT * FROM pret WHERE date_retour IS NULL;
```

##  **Features Implemented**

### **Member Management:**
- ✅ Add new members
- ✅ Edit existing members
- ✅ Delete members
- ✅ Search members
- ✅ View member details

### **Document Management:**
- ✅ Add new documents
- ✅ Edit document information
- ✅ Delete documents
- ✅ Search catalog
- ✅ Manage copies

### **Loan Management:**
- ✅ Check out documents
- ✅ Return documents
- ✅ View loan history
- ✅ Calculate late fees

##  **Development Tools**

- **Language:** Java 17
- **GUI Framework:** JavaFX 21
- **Database:** MySQL 8.0
- **IDE:** Eclipse/IntelliJ IDEA
- **Version Control:** Git

##  **Development Team**

- **Marwa Saidi** 
- **Ameni Ben Hassine** 

