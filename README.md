**Banking Application**
**Overview**
This project is a secure **Banking Application** developed using **Spring Boot**. The application allows users to manage their accounts, view transaction history, and receive transaction receipts in PDF format. The application leverages various technologies such as **iTextPDF** for PDF generation, **Spring Security** for secure authentication and authorization, and **Spring Repositories** for email notifications.

**Features**
**Transaction Receipt Generation:** Generate transaction receipts as PDFs using **iTextPDF**, making it easy for users to keep track of their financial activities.
**Email Notifications:** Integrated with **Spring Repositories** to send email notifications with PDF attachments to users, providing them with real-time updates on transactions.
**Secure Authentication and Authorization:** Implemented **Spring Security** to ensure secure user authentication and role-based access control, allowing users to access only authorized resources based on their roles.
**API Documentation:** Utilized **Swagger2** for generating comprehensive and easy-to-understand API documentation, enabling seamless integration and testing.

**Technologies Used**
**Spring Boot:** The core framework for building the banking application, providing the backbone for all features.
**iTextPDF:** A library used for generating PDF documents, specifically for creating transaction receipts.
**Spring Security:** Used to implement user authentication and role-based access control, ensuring data privacy and secure access.
**Spring Repositories:** For handling database operations and sending email notifications to users.
**Swagger2:** A tool for auto-generating API documentation to simplify API integration and testing.
**Java:** The primary programming language used to develop the backend logic of the application.

**Setup Instructions**
**Prerequisites**

Make sure you have the following installed on your system:

**JDK 11 or higher**
**Maven**
**MySQL or any other preferred database**

**Installation Steps**
**Clone the Repository**
Clone this repository to your local machine using the following command:


**git clone https://github.com/Shivshankar108/banking-application.git**
**Setup Database**
Configure your database (MySQL or other preferred databases) and update the **application.properties** file with your database credentials.

**Build the Application**
Navigate to the project folder and run the following command to build the application:

**mvn clean install**

**Run the Application**
Once the application is built, run it using the command:

**mvn spring-boot:run**
The application should now be running on **http://localhost:8080.**

**Email Configuration**
Ensure that your SMTP server is correctly configured to allow email notifications to be sent. You can set up the required SMTP settings in the **application.properties** file.

**Accessing Swagger API Documentation**
After running the application, you can access the Swagger API documentation at:

**http://localhost:8080/swagger-ui.html**
This provides detailed API documentation for integration and testing.

**Features in Detail**
**1. Transaction Receipt Generation**
The application allows users to generate transaction receipts in PDF format. When a transaction occurs, a receipt is generated using iTextPDF and attached to an email sent to the user. This ensures users can easily keep a record of their financial transactions.

**2. Email Notifications**
The system integrates with Spring Repositories to send email notifications to users. These emails include PDF transaction receipts, ensuring that users are updated about their transactions in real-time.

**3. Secure Authentication and Role-Based Authorization**
The application utilizes Spring Security to handle user authentication. Users can sign up, log in, and access their accounts securely. The system also supports role-based authorization, restricting access to specific resources based on the user's role (e.g., admin, user).

**4. Comprehensive API Documentation with Swagger2**
The application uses Swagger2 to auto-generate comprehensive API documentation. This enables other developers to easily understand how to integrate with the application and test its endpoints.
