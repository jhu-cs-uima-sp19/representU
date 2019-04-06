# RepresentU

## Peyton Baur (pbaur1)
## Mia Boloix (mboloix1)
## Aubin Lohier (alohier1)

## Description
A JHU SGA “Countable” style app, which would lessen the gap between Hopkins students and their representatives. It would be similar to an Instagram-style social media application, but would only allow users with an active JHED to log in. This app would summarize the “Bills”, “Laws”, and “Issues” that SGA is tackling at the moment, in almost-real time, and allow students to give their feedback with a “Yay” or “Nay” style voting. The votes would be anonymous, but the comments would not be (in order to keep people accountable for what they write). We are assuming that SGA would cooperate and provide us with information, with the hopes that they will finally be able to reach the Hopkins population (who are generally apathetic).


## Student Account:
Username: **user@jhu.edu**
Password: **helloUser**

## SGA (Admin) Account:
Username: **admin@jhu.edu**
Password: **helloAdmin**

## 1st Sprint Goals
* Create Login UI
- Create a Login Activity
- Needs a function that checks for ‘@jhu.edu’
- Save passwords (?)
- Add in design elements
* Create SGA special account
- Create a special SGA account login 
* SGA side: Create an issue page 
- Create MainActivity with Nav Drawer
- Use Scrollable (or RecyclerView) List View
- Add in design 
* Create Issue object
- Has to be able to record votes for and votes against
- Text input for summary
* SGA side: Managing issues
- Allow SGA account to be able to create Issue objects
- Requires scrollable listview on a new Managing Issues Activity
- Need Add Issue Activity
- Object should have title field and summary field required to be filled. 

## 2nd Sprint Goals
* Full authentication
- Use JHU SIS to get JHED
* Archiving issues
- Add boolean field to Issue objects that stops them from being seen by normal accounts
- Create section in Managing Issue Activity that displays Archived issues.
* Edit issues
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

