individual-project-anay-naik

Name: Anay Dilip Naik

SJSU ID: 015217358

Project Description:
The purpose of this project is to create a Java application that will maintain a static database on its own. The changes to the data would not persist if we ran the software again. This application should allow users to purchase inventory items, with amounts limited per item type. User can buy the items among three different categories. They are:
Essentials, Luxury, Misc
When the user input criterion is met, a bill amount should be calculated; otherwise, an error notice should be displayed, indicating the products with wrong values.

Requirements:
Eclipse IDE

Instructions:
Go to repo anaydilipnaik and clone the repository or download the zip file.
Open the zipped folder or the entire folder in eclipse by navigating to File -> Open.
After opening the project go to Billing.java and run the project.
Make sure the java version used is compatible.
Once the code is executed, you can check the output in output.csv file which is created and if an error is occurred, you can check it in errors.txt.

Design Patterns:
Factory pattern is used for writing different content to different output. An Outputter interface defines the writing operations whereas the classes implementing this interface extend the functionality. An OutputterFactory class creates new instances of the output methods based on the context provided by client
Strategy pattern is used for handling the different processing needs of each .CSV file while being processed. Based on the file, there is a different strategy used, and this decouples the loading functionality based on strategy and how it is consumed downstream
