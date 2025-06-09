🌱 Agri-Energy Connect Platform 🔋
ASP.NET Core C# SQL Server Bootstrap

Connecting Farmers with Energy and Agricultural Services
A centralized platform that bridges the gap between agricultural producers and energy solutions

Watch Demo on YouTube
Overview
The Agri-Energy Connect Platform is a prototype web application designed to connect farmers with energy and agricultural services through a centralized system. This enterprise-level solution demonstrates advanced software development practices and implements role-based functionality for Farmers and Employees. The platform serves as a digital ecosystem where farmers can manage their agricultural products while employees can oversee farmer registrations and monitor product listings.

Key Features
Role-Based Authentication: Secure login system with separate interfaces for Farmers and Employees
Farmer Management: Employees can add and manage farmer accounts
Product Management: Farmers can add and showcase their agricultural products
Advanced Filtering: Search products by date range, farmer name, or product category
Responsive Design: User-friendly interface built with Bootstrap and Razor views
User Roles
The platform supports two primary user roles:

Employee: System administrators who can:

Register new farmers into the system
View products from all farmers
Filter products by date range, farmer name, or category
Farmer: Agricultural producers who can:

Manage their product listings
Add new products to the marketplace
View their own product listings
Technology Stack
Framework: ASP.NET Core 8.0 MVC
Frontend: HTML5, CSS3, JavaScript, Bootstrap
Backend: C#, Entity Framework Core
Database: Microsoft SQL Server Express/LocalDB
Authentication: ASP.NET Core Identity
Architecture: Layered MVC architecture with Repository Pattern
Prerequisites
Before you begin, ensure you have the following installed:

Essential Software Requirements
Visual Studio 2022 or later
.NET 8.0 SDK or later
SQL Server Express or LocalDB
Entity Framework Core tools
Additional Requirements
Git: For version control and cloning the repository
Internet Connection: Required for downloading NuGet packages
Setup Instructions
1. Clone the Repository
Open a command prompt or terminal and execute the following commands:

git clone https://github.com/ST10407732/St10407732_Prog7311_part2.git
cd St10407732_Prog7311_part2
2. Database Configuration
Follow these steps to set up your database:

Step 2.1: Create the Database
Open SQL Server Management Studio (SSMS)
Connect to your SQL Server instance
Right-click on "Databases" in the Object Explorer
Select "New Database..."
Enter AgriConnectDb as the database name
Click "OK" to create the database
Step 2.2: Update Connection String
Open the appsettings.json file in the project root directory
Locate the ConnectionStrings section
Update the DefaultConnection to match your SQL Server instance name:
"ConnectionStrings": {
  "DefaultConnection": "Server=(localdb)\\mssqllocaldb;Database=AgriConnectDb;Trusted_Connection=True;MultipleActiveResultSets=true"
}
Replace (localdb)\\mssqllocaldb with your actual SQL Server instance name if you're not using LocalDB.

Note for SQL Server Authentication: If you're using SQL Server Authentication instead of Windows Authentication, update your connection string accordingly:

"DefaultConnection": "Server=YourServerName;Database=AgriConnectDb;User Id=YourUsername;Password=YourPassword;MultipleActiveResultSets=true;TrustServerCertificate=True"
3. Apply Database Migrations
This step will create all necessary database tables and seed initial data:

Option A: Using Visual Studio Package Manager Console
Open the solution file (AgriEnergyPlatform.sln) in Visual Studio
Open the Package Manager Console from: Tools > NuGet Package Manager > Package Manager Console
Ensure the Default project is set to the main project
In the console, execute the following commands:
Add-Migration InitialCreate
Update-Database
Wait for the commands to complete - you should see "Done." message
Option B: Using Command Line Interface (CLI)
Navigate to the project directory in your terminal
Execute the following commands:
dotnet ef migrations add InitialCreate
dotnet ef database update
Wait for the commands to complete successfully
4. Install Required NuGet Packages
The project depends on several NuGet packages. If they aren't automatically restored:

Using Visual Studio:
Right-click on the solution in Solution Explorer
Select "Restore NuGet Packages"
Wait for the process to complete
Using Command Line:
dotnet restore
5. Build and Run the Application
Option A: Using Visual Studio
Open the solution file (AgriEnergyPlatform.sln) in Visual Studio
Build the solution: Press Ctrl+Shift+B or select Build > Build Solution
Run the application: Press F5 or click the green "Play" button
Your default browser should open automatically with the application running
Option B: Using Command Line
dotnet build
dotnet run
The application should now be running at the localhost address specified in your browser.

6. Verify Database Setup
To ensure your database has been correctly set up:

Open SQL Server Management Studio
Connect to your database server
Expand the AgriConnectDb database
Expand the "Tables" folder
Verify that all necessary tables have been created
If the tables don't exist, try repeating Step 3 of the setup process.

Using the Application
Authentication
Login Credentials
For accessing the employee portal, use the following credentials:

Email: employee@agri.com
Password: Employee123!
Login Process
Navigate to the Login page
Enter your email and password
Click the "Sign In" button to authenticate
If successful, you'll be redirected to the dashboard
If unsuccessful, an error message will display
For Employees
Employees have access to administrative features to manage farmers and view all products.

1. Managing Farmers
Add Farmer Profiles:

Navigate to "Add Farmer" section
Fill in the farmer details
Submit the form to register a new farmer
View Products from All Farmers:

Access "View Products" section
Apply filters:
Date Range: Select start and end dates
Farmer: Choose from registered farmers
Product Category: Filter by product type
For Farmers
Farmers can manage their product listings.

1. Managing Products
Add Products:

Navigate to "Add Product" section
Complete the product details form
Submit to add the product to the system
View Own Products:

Access "My Products" section
View a list of all products you've added
Database Schema
The application uses the following primary database tables:

Users Table
UserId: Primary key
Username: For login
Password: Securely stored
FirstName and LastName
Email: User's email address
Role: Either "Employee" or "Farmer"
Additional fields for farmers: FarmName, FarmLocation, FarmDescription
Products Table
ProductId: Primary key
Name: Product name
Category: Product category
ProductionDate: Date produced
Description: Detailed product description
Price: Product price
IsOrganic: Whether the product is organic
FarmerId: Foreign key to Users table
Troubleshooting Common Issues
Connection String Problems
Issue: "Cannot connect to database" error
Solution:
Verify your SQL Server instance name is correct
Ensure SQL Server is running
Check that the database exists
Verify your account has permission to access the database
Migration Errors
Issue: "No migrations applied" or "Pending migrations" warnings
Solution:
Run Update-Database in Package Manager Console
Check for errors in the migration files
For severe issues, you might need to delete the database and start fresh
Login Issues
Issue: Cannot log in with provided credentials
Solution:
Verify the email and password are correct (case-sensitive)
Check that the database contains the user record
Try using the default credentials: employee@agri.com / Employee123!
Extending the Application
Adding New Features
Create New Models:

Add new class files to the Models folder
Update ApplicationDbContext.cs to include new DbSet properties
Create Database Migrations:

Create a migration: Add-Migration MigrationName
Apply the migration: Update-Database
Add Controllers and Views:

Create new controller classes in the Controllers folder
Add new views in the Views folder
Code Attribution
Microsoft Identity Integration
Author: Andy Malone MVP
Link: Microsoft Identity Tutorial
Date Accessed: May 5, 2025
MVC Application Framework
Author: Microsoft
Link: ASP.NET Core MVC Tutorial
Date Accessed: May 6, 2025
Data Annotation Attributes
Author: Simplilearn
Link: Data Annotation in ASP.NET MVC
Date Accessed: May 6, 2025
ASP.NET App with SSMS Database
Author: Andy Malone MVP
Link: Database Connection Tutorial
Date Accessed: May 5, 2025
Web Development Resources
HTML Resources: w3schools HTML Tutorial
CSS Resources: w3schools CSS Tutorial
jQuery Resources: w3schools jQuery Tutorial
JavaScript Resources: w3schools JavaScript Tutorial
Date Accessed: May 8, 2025
Validation and Database Implementation
REGEX Validation: Tyler Woodward, Phone Validation Regex
Database Implementation: Microsoft, Working with SQL in ASP.NET Core
LINQ Implementation: Grant Riordan, How to Use LINQ
Date Accessed: May 8, 2025
📧 Contact
Developer: ST10407732
GitHub: ST10407732

Last Updated: May 21, 2025

About
No description, website, or topics provided.
Resources
 Readme
 Activity
Stars
 0 stars
Watchers
 1 watching
Forks
 0 forks
Report repository
Releases
No releases published
Packages
No packages published
Languages
C#
56.2%
 
HTML
39.3%
 
CSS
4.3%
 
JavaScript
0.2%
Footer
