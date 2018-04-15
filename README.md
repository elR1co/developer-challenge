# Developer Challenge

## Deploy
```
# Build the project and docker images
mvn package docker:build

# Run containers
docker-compose up

# Access app
http://localhost
```

## Architecture

The survey application is composed of an **Angular 4 Front End** served by **NGINX** and a **Spring Boot REST Backend** with embedded **Tomcat**. To avoid Cross-Origin HTTP requests restriction, NGINX is used as a proxy between the Front End and Back End applications. In development mode, we use webpack proxying support as **ng start** command is an alias to **ng serve --proxy-config proxy.conf.json** where proxy.conf.json file defines the proxy configuration.
**MySQL** is used as database to store our surveys data.

This architecture is used to make a clean separation between specific Front End developement of HTML, CSS, Javascript, which involves different skills and persons compared to Back End Development with server side languages like Java or Python.

**Angular 4** was chosen for its component-oriented approach (with much similarities to ReactJS) and the possibility to develop in **Typescript** which offers the advantage to code statically-typed Javascript, cool to detect errors way before you run your application. Angular 4 offers also a complete dev environment with integration with **npm** and **webpack** to handle dependencies, packaging, testing and live coding.

**Bootstrap 4** is used as CSS framework to provide a mobile-first design. **Plotly.js** is used for the pie chart generation.

**Spring Boot** can be considered as the Java equivalent of Django : a framework for quick development of REST functionalities with Spring-MVC and Object-Relational mapping with **JPA** (which uses **Hibernate** as implementation) and **Spring Data** project to provide the boilerplate code to access the data repositories.

Considering our need to have a schema because of the constraints of our data model, **Mysql** was chosen.

## Data Model

We can have N surveys in our database, and each survey has N questions, and to simplify, we have considered that one question can only be linked to one survey.

So we have a **one to many** relationship between Survey and Question.

Similarly, a question can have several proposed answers, and for simplication, one prefered answer can only be associated to one question (in consequence to one survey).

So we have also a **one to many** relationship between Question and ProposedAnswer.

As a matter of simplication, users don't have to log in to use the application. So we don't have a User entity. However, to get user's answers, we have a UserSurveyAnswer entity, which is linked to ProposedAnswer via a join entity UserAnswer. Indeed, a user can choose several proposed answers, and a proposed answer can be chosen by several users. So, we have a **many to many** relationship between UserSurveyAnswer and ProposedAnswer. We will use this UserSurveyAnswer to store also the user survey answer metadatas (IP Address, Browser, etc).

![Alt text](data-model.jpg?raw=true "Data Model")

## Database initialization

The database is initialized at start of Back-End application with survey data in Spring Boot main **Application** class. 

Please note that for demo purposes the database DDL is set to 'create' mode, meaning the database is re-created each time the Back-End application is re-started. To change that, you need to modify the property **spring.jpa.hibernate.ddl-auto** in **application.yml** file :

```
spring:
  jpa:
    hibernate:
      ddl-auto: create
```


## REST API

The REST backend provides the given API links :

- **<code>GET</code> api/surveys** retrieved all surveys
- **<code>GET</code> api/surveys/:surveyId** retrieve a survey via its id
- **<code>POST</code> api/surveys/:surveyId/answers** allows to save answers related to a survey
- **<code>GET</code> api/surveys/:surveyId/results/:answerId** retrieve the results of a survey according to the answers the user posted (The consumer provides the surveyId and answerId, the API will retrieve the user answers and use the calculation engine to provide the results)

All API links are defined in **SurveyController** class.

```
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
...

@RequestMapping(value = "", method = GET)
public Iterable<Survey> getSurveys() {
    return surveyRepository.findAll();
}

@RequestMapping(value = "/{surveyId}", method = GET)
public Survey getSurvey(@PathVariable Long surveyId) {
    return surveyRepository.findOne(surveyId);
}
```

## Front-End Workflow

Basically, the Front-end defines a workflow that the user has to follow to perform correctly the survey filling. Indeed, he cannot access directly the results page if he has not filled the questionnaire completely before. So, we have to find a way to "guide" the user to the right workflow : welcome -> fill questionnaire -> result

**For a matter of simplication (and time), the default url hard-codes the id of the only survey provided in the specifications.** To extend the application, a new parent component should be created to display the list of available surveys for example.

The Front-End uses the concept of routes provided by Angular router (same concept with ReactJS). And each route is dedicated to a given component. When the user access the default page, which is the survey page, the navbar displays all possible children routes of the workflow. So, for the default route **/survey/1**, the children routes are :

- **/start**
- **/questions** 
- **/result**

```
const routes: Routes = [
  { path: '', redirectTo: 'survey/1', pathMatch: 'full' },
  { path: 'survey/:surveyId',
    component: SurveyComponent,
    children: [
      {path: '', redirectTo: 'start', pathMatch: 'full' },
      { path: 'start', component: StartComponent },
      { path: 'questions', component: QuestionsComponent, canActivate: [WorkflowGuard] },
      { path: 'result', component: ResultComponent, canActivate: [WorkflowGuard] }
    ]
  }
];
```

### 1. /start

This is the welcome page of the survey. We load the survey from the REST backend, using the surveyId provided by the parent route. We store the survey into a singleton service **WorkflowDataService** in order to be accessed by the other routes of the workflow. When the user clicks on 'Start' button, we navigate to the next route of the workflow.

### 2. /questions

This is the questionnaire. It is divided in several steps which match the number of questions. The questions are provided by the **WorkflowDataServide** which stored them in the previous step. The **QuestionComponent** handle itself the state of the page, depending on the selected question, as it is possible for the user to go back to previous question or to advance to the next one. 

When the user selects an answer in the form, automatic data-binding happens under the hood with a var in the component which stores the chosen answers in order to be sent to the REST server for saving at the end. When the REST server has successfully saved the answers, the component receives an answerId as a Promise which is then stored by WorkflowDataService to be accessed by next step.

```
this.route.parent.params.subscribe(params => {
	let surveyId = params['surveyId'];
	this.answerService.postAnswers(surveyId, Object.values(this.answers)).then(
	  answer => {
	    this.workflowDataService.setAnswerId(answer.id);
	    this.router.navigate(['../result'], { relativeTo: this.route });
	  }
	);
});
```

### 3. /result

This is where we display the survey result. The **ResultComponent** uses the answerId provided by WorkflowDataService to access the results provided by calculation engine on server side. The results are received as a Promise and given to **Plotly** Javascript library to display a pie chart. When the user clicks on 'Terminate', the data stored in WorkflowDataService is cleared, and we go back to the welcome step.

## Workflow Guard

In order to "guide" the user, we need to prevent access to urls/steps the user has not achieved yet. This is the role of the concept of **Guard** provided by Angular, which could also be used for authentication/authorisation.

```
@Injectable()
export class WorkflowGuard implements CanActivate {

  constructor(private router: Router, private workflowService: WorkflowStepService, private myRoute: ActivatedRoute) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    let path: string = route.routeConfig.path;

    return this.verifyWorkFlow(path);
  }

  verifyWorkFlow(path) : boolean {
    console.log("Entered '" + path + "' path.");

    // If any of the previous steps is invalid, go back to the first invalid step
    let firstPath = this.workflowService.getFirstInvalidStep(path);
    if (firstPath.length > 0) {
      console.log("Redirected to '" + firstPath + "' path which it is the first invalid step.");
      let path = `${firstPath}`;
      this.router.navigate([this.router.url], path);
      return false;
    }

    return true;
  }
}
```

When a user starts a workflow, if he tries to click on navbar links to questions or result, nothing happens, as he is prevented by the guard. Same thing if he tries to enter the url in the navigator.

However, when he successfully access the result step, all the links are then accessible until he clicks on 'Terminate' which reactivates the guard.

## Calculation Engine

The **SurveyCalculationEngine** uses the **weighted mean formula** to calculate the user political leanings, according to his answers. Each question has a weight, and each proposed answer has a score. The score can be from min = 0 (0% right) to max = 1 (100% right). 

Example of calculation :

``` 
User has provided the given answers [(x, y),...] 
with x = weight and y = score : 
[(2, 1), (1, 0.6), (1, 0.4)]

result = sum(weight*score)/sum(weight)
		= (2*1 + 1*0.6 + 1*0.4) / (2 + 1 + 1)
		= 0.75
		
=> 75% right, 25% left

``` 

## User Survey Answer Metadatas

Every time a user chooses answers, the metadatas can be retrieved on server side and stored in the database. 
To perform this task, we use Spring MVC to retrieve **User-Agent** header. Then, it is parsed by a library called **browscap4j** which uses a big CSV file (~100MB) to indentify the related browser type, device, etc. This functionality is disabled by default as the big CSV loading takes several seconds on startup. But it can be enabled in the application.yml file with the property **browscap.enabled**. Please note that when enabling this feature, around 20 seconds are necessary to fully load the CSV file and init Browscap parser. Memory limits are also necessary to be increased on the docker container (max 2G is sufficient) and on the JVM (min 1G, max 2G). 

```
BrowserCapabilities browserCapabilities = browscap.lookup(userAgent);
userSurveyAnswer.setBrowser(browserCapabilities.getBrowser());
userSurveyAnswer.setBrowserType(browserCapabilities.getBrowserType());
userSurveyAnswer.setDeviceBrandName(browserCapabilities.getDeviceBrandName());
userSurveyAnswer.setDeviceCodeName(browserCapabilities.getDeviceCodeName());
userSurveyAnswer.setDeviceName(browserCapabilities.getDeviceName());
userSurveyAnswer.setDeviceType(browserCapabilities.getDeviceType());
userSurveyAnswer.setPlatform(browserCapabilities.getPlatform());
userSurveyAnswer.setPlatformMaker(browserCapabilities.getPlatformMaker());
userSurveyAnswer.setPlatformVersion(browserCapabilities.getPlatformVersion());
```

## Docker images packaging

Docker images for both Front-End and Back-End are built via **docker-maven-plugin**. For the Front-End, the **frontend-maven-plugin** is used to trigger npm to generate Angular optimized Javascript files. We use a nginx image as base for the Front-End, a java8 image as base for the Back-End, and a mysql one for the db. The docker-compose describes the dependencies between the different services. 













 












