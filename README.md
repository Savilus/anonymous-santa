<div align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="InteliJ" title="InteliJ"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108891-d86b6220-e232-423a-bf5f-90903e6887c3.png" alt="Visual Studio Code" title="Visual Studio Code"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192158954-f88b5814-d510-4564-b285-dff7d6400dad.png" alt="HTML" title="HTML"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183898674-75a4a1b1-f960-4ea9-abcb-637170a00a75.png" alt="CSS" title="CSS"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183897015-94a058a6-b86e-4e42-a37f-bf92061753e5.png" alt="React" title="React"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png" alt="TypeScript" title="TypeScript"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183568594-85e280a7-0d7e-4d1a-9028-c8c2209e073c.png" alt="Node.js" title="Node.js"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201470-f6d56780-adec-11eb-8f7c-e70e376cfd07.png" alt="Spring" title="Spring"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/190229463-87fa862f-ccf0-48da-8023-940d287df610.png" alt="Lombok" title="Lombok"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" alt="MySQL" title="MySQL"/></code>
</div>

# Anonymous Santa

### Introduce

This project is a full-featured restful web application designed to help users create and manage Santa Claus or other events in which we want to anonymously buy another person a gift.

The main part allows users:
- Register new account
- Log in your account and choose the preferred gifts you want to get.
- Create event what you want. You have to fill fields: name, date, number of people, budget and currency (All the fields have validation). You can create and join multiple events simultaneously.
- You can add people to these events, even if they are not register. The app will send email invitations for you to the people you've added.
- Invitees can join the event after creating an account, using a special code that will be sent to the email you provided.
- Once all invited participants join the resulting event, a drawing for gifts can be held. After the drawing, each participant will receive an email notification of the drawing held. After entering the site, the user will see the person to whom he or she is to buy a gift and what gift he or she prefers. No one knows from whom they will receive a gift, which allows you to remain anonymous and surprise the other person on the day when the gifts will be handed out.
- After all you can easily delete the event you want.

### Running the application
***

**1.** Clone the source code from Github:

Frontend repository:
````
https://github.com/mkolodziejczyk92/anonymous-santa-fe
````
Backend repository:
````
https://github.com/Savilus/anonymous-santa
````

**2.** Before starting the application for the first time, add an SQL database named 
`````anonymous-saunta`````.

**3.** Before first start type in VSC `````npm i````` to install everything you need after that start your frontend application through terminal typing `````npm start`````. 

**4.** Now start backend application from `````AnonymousSantaApplication.java````` class.

**5.**  Open ````http://localhost:3000```` in your browser. Frontend and backend will communicate with each other.

**6.** Create an account and enjoy!

#

***
![frontPage](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20frtont%20page.png?raw=true)
![signUp](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20sign%20up.png?raw=true)
![joinToTheEvent](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20join%20to%20the%20event.png?raw=true)
![participants](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20participants%20and%20events.png?raw=true)
![email](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20email%20title.png?raw=true)
![events](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20buy%20gift%20for.png?raw=true)
![deleteEvent](https://github.com/Savilus/anonymous-santa/blob/master/anonymousSanta%20ss/santa%20delete%20event.png?raw=true)
***

Created by 
* Marcin Kołodziejczyk
* Jakub Łanoszka

