# test_task

MySQL db is used, so remember to update properties file.
Also update jwt secret in properties file.
To start you need to add ROLE_USER to 'roles' table in db.

To register: use post method to /register, provide login and password in JSON body.
To get token: use post method to /auth, provide login and password you used for registration

To delete a contact you need to specify contact name(which is unique per user)
Update is also requires contact name. Since we use put method to update, previous phones and emails will be deleted if you specify new ones in JSON body.

Also some extra tasks were performed
-       Application can be run using Docker
-       Swagger documentation added to the project
-       Added ability to export/import contacts (GET and POST methods to /contacts/import and /contacts/export)


