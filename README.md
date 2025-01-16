f## wallet-api
Using this application, the users can perform the following operations: 

● Register a profile;

● Login;

● Create a wallet;

● View a wallet balance;

● Deposit money to a wallet;

● Withdraw money from a wallet;

## TECH STACK:

● **Frontend**: Bootstrap, JavaScript, HTML, CSS, JavaScript

● **Backend**: Java, Spring Boot, Spring Data JPA, Hibernate/JPA, MySQL, Thymeleaf, RESTful API

## Overview of the API

/home - the home page, where users are introduced to the application and prompted to register or log in to their account:
![image](https://github.com/user-attachments/assets/7884c997-afd5-42cf-93d2-3ba57d1de534)

/register - the registration form which the users should fill if they want to use the application:
![image](https://github.com/user-attachments/assets/077d5c30-e8aa-4537-ae83-d1a979c07cd9)
![image](https://github.com/user-attachments/assets/5978c0ec-2c4e-43f5-88c5-3132c3c2d9c8)

/login - the users should enter their email and password for accessing the application:
![image](https://github.com/user-attachments/assets/8f105d57-aac4-4df7-83c0-b710695fcca5)

/myProfile - If the user has no wallets created, they will see the notification message:
![image](https://github.com/user-attachments/assets/66f89658-281a-4dd5-9390-62ef630f35ba)

/createWallet - the user opens this page by clicking the "Create Wallet" button:
![image](https://github.com/user-attachments/assets/575f7bf0-be4f-4505-98cf-08ad4ea09bae)

Here the user can choose a wallet name, currency and to create it:
![image](https://github.com/user-attachments/assets/57affa4a-979f-418b-b45b-ec3efa6f866f)

/myProfile - Here users can view all their wallets:
![image](https://github.com/user-attachments/assets/1d258692-0900-428d-a907-c15860e7253a)

/walletDetails - By clicking on "Open for detailed view", the users can see the balance in the chosen wallet:
![image](https://github.com/user-attachments/assets/ea2a0df9-a301-40ed-a745-2b2d7e872216)

When the user want to deposit money into their account, they must click the green "deposit" button and fill in the data correctly, then click the blue "deposit" button:
![image](https://github.com/user-attachments/assets/d9d4e211-ba9d-4767-8211-89a33d14065a)

If the transaction is successful, the user will receive a message:
![image](https://github.com/user-attachments/assets/36e205e5-960a-432b-b79f-97673c890694)

And the page will be automatically reloaded to visualize the change in the wallet balance:
![image](https://github.com/user-attachments/assets/6e624bdd-1650-4007-b17f-0a64c034e48d)

If the user wants to withdraw money, he must click on the red "Withdraw" button and enter the desired amount. In case the user wants to withdraw more money than they have available, they will receive a notification that the withdrawal was unsuccessful and they must try again:
![image](https://github.com/user-attachments/assets/60765652-784a-4cae-ab25-50c4df751f46)

![image](https://github.com/user-attachments/assets/4ff3b493-aaef-4bb9-946f-45796468b72e)


In case of a successfully entered amount, the user receives a confirmation message:
![image](https://github.com/user-attachments/assets/ee9d411a-ebd1-4c76-9186-5d53341c10e3)
![image](https://github.com/user-attachments/assets/328ff49e-721d-4c53-8b0e-292fc4c2f5bc)
![image](https://github.com/user-attachments/assets/cde21129-2358-47be-b05a-1908a5b9b153)

By pressing the gray "Back" button, the user returns to the previous page:
![image](https://github.com/user-attachments/assets/ebfe3aa8-6e97-43e0-b329-ce9e4a9780c0)

/login?logout - By clicking the "Logout" button, the user successfully logs out of their account and is redirected to the login form:
![image](https://github.com/user-attachments/assets/c15a6386-3f09-49da-9db2-45b905de645e)









