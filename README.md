# RepresentU

## Peyton Baur (pbaur1)
## Mia Boloix (mboloix1)
## Aubin Lohier (alohier1)

## Description
An application for JHU students seeking a voice. This app, styled after a popular app named “Countable”, would lessen the gap between Hopkins students and their representatives. Issues presented by SGA are posted in a feed, as posts. It would be similar to an Instagram-style social media application, but would only allow users with an active JHED to log in. This app would summarize the “Bills”, “Laws”, and “Issues” that SGA is tackling at the moment, in almost-real time, and allow students to give their feedback with a “Yay” or “Nay” style voting. The votes would be anonymous, but the comments would not be (in order to keep people accountable for what they write). We are assuming that SGA would cooperate and provide us with information, with the hopes that they will finally be able to reach the Hopkins population (who are generally apathetic).


## Student Account:
Username: **user@jhu.edu**
Password: **helloUser**

## SGA (Admin) Account:
Username: **admin@jhu.edu**
Password: **helloAdmin**

## 1st Sprint Goals
* Create Login UI (DONE)
  - Create a Login Activity (DONE)
  - Needs a function that checks for ‘@jhu.edu’ (DONE)
  - Save passwords (?) (Waiting for authentication, next sprint)
  - Add in design elements (DONE)
* Create SGA special account (DONE)
  - Create a special SGA account login (DONE)
* SGA side: Create an issue page (DONE)
  - Create MainActivity with Nav Drawer (DONE)
  - Use Scrollable (or RecyclerView) List View (DONE)
  - Add in design (DONE)
* Create Issue object (DONE)
  - Has to be able to record votes for and votes against (DONE)
  - Text input for summary (DONE)
* SGA side: Managing issues (DONE)
  - Allow SGA account to be able to create Issue objects (DONE)
  - Requires scrollable listview on a new Managing Issues Activity (DONE)
  - Need Add Issue Activity (DONE)
  - Object should have title field and summary field required to be filled. (DONE)

After our first sprint, our app allows a user to sign-in as an admin via the admin login information, or a user via the user login information. When entering the app as an admin, one is able to view the current issues in the database on thier feed. Clickling on an issue allows them to see the title and the summary of the issue in addition to the number of "yay" and "nay" votes. Admins cannot vote. Once back at the feed, an admin can open up the navagation drawer and select "Manage Issues" to see that issues posted in a listview. Clicking on an issue brings you to the Edit Issue activity, which is created but not fully functional. Going back to the Manage Issues page, clicking the "Add Issue" button launches the activity that allows admins to add an issue. This page is fully functional and even requires that both the title and summary fields are filled in order for the data to be added. Loging in as a user, one can see all the issues posted on their feed. Clicking on an issue allows a user to vote Yay or Nay. We have not yet implemented the restriction that users can only vote once on each issue or the "Unvoting" function becuase this would require boolean values saved in our database for each user and specific issue. This has been saved for the next sprint. 

## 2nd Sprint Goals
* Full authentication
  - Use JHU SIS to get JHED
* Archiving issues
  - Add boolean field to Issue objects that stops them from being seen by normal accounts
  - Create section in Managing Issue Activity that displays Archived issues.
* Edit issues (ALMOST DONE)
  - Give SGA admin acct power to edit issues. Probably needs a new activity.
* Meet your SGA
  - Add in Meet your SGA screen/Activity
  - Probably need SGA object type
* Stats page after voting
  - Create Stats Activity
  - Use Java library to display graphs of Issue voting data
* Comment sections
  - Create list field for issues and add comments into list
  - Create Add comment activity with appropriate ui elements.
* Logout

## If Time
* Settings & Notifications
* Share button

