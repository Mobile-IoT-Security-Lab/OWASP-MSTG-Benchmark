MASTG-TEST0006
Trigger vulnerability:

In the provided example, the vulnerability can be triggered by the following sequence of actions:

- Launch the application and register to the form, observe the EditTexts field labeled "Username" and "Password".

-Upon selecting the EditTexts field to enter a password and a username, the keyboard pops up for user input.

- Since the input type for the EditTexts field (sensitiveEditText) is inadvertently overwritten in the MainActivity.java file to InputType.TYPE_CLASS_TEXT, keyboard caching is enabled.

As a result, even though the developer intended for the input field to accept sensitive information like passwords, the keyboard cache will store the entered characters, potentially compromising the security of the application.

Subsequently, if the device is accessed by an unauthorized user or if there are any security breaches, the stored keyboard cache could be exploited to retrieve sensitive information entered by previous users, including passwords.




