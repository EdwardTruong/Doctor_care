Hi there, If you see this GitHub account, it means there is an issue with my main account, but don't worry as the code is still being updated here, just a bit less frequently. I will soon update my main account and code on this account.


# Medical Appointment 

This is a project using Spring Boot version 3.2.2 with Spring Security for authorization along with JWT for user authentication.




## Knowledges

 - Spring MVC
 - Spring Boot Restful API
 - Spring Security
 - Jwt



## Functions

Login : 
When a user logs in, a JWT is generated. And when the user uses any feature, the JWT will be checked to authorize or deny that request

    
    //LoginRequest
    {
    "username":"user_01@gmail.com",
    "password":"123"
    }
    // Bearer Token : eyJhbGciOiJIUzI1NiJ9.
    eyJzdWIiOiJ0cnVvbmdlZDQ2QGdtYWlsLmNvbSIsImlhdCI6MTcyNDc2Mzc2NywiZXhwIjoxNzI0NzY0NjY3fQ.
    2NxtiCQSrOd8Za5yeGrtHOLUADqoP_5f9b3zA-lmGcw  



Request password: 
    Send email have register already & input password and re-password
    

    // @RequestParam("email") String email
    http://localhost:8080/api/home/send-email?email=user_01@gmail.com
    


User needs to confirm by jwt in their email before changing password: 

    
    //@RequestBody ChangePasswordRequest request , @RequestParam("key")
    {
    "password":"123456",
    "rePassword":"123456"
    }

    
Find information by key words: 
    
    {
	"idDoctor":"1",
	"reason":"Đi khám",
	"idSchedule":"1",
	"time":"08:00-09:00"
    }         

CRUD : User - Doctor - Schedules - Session - Statuses.

Find : 
    
     @GetMapping :  http://localhost:8080/api/home/top-specialization
     @GetMapping :  http://localhost:8080/api/home/top-clinics
     
     With : doc-specialization : @RequestParam("name") String specialization,@RequestParam("date") LocalDate date
     @GetMapping :  http://localhost:8080/api/home/doc-specialization?name=khoa nhi&date=2024-10-09
        
        


    With : getSchedule : @RequestBody ScheduleInfoRequest
     @GetMapping :  http://localhost:8080/api/home/getSchedule 
      
     {
         "searchString":"Đà Nẵng",
        "date":"2024-10-09",
        "maxPrice":"200",
        "minPrice":""
     }
    
And other features are currently under development(upload images, paied, receipt ,ect...)

    


    
    








 
    

## Roadmap

- [Get requirements](https://docs.google.com/document/d/1rRGRPzctYN_VPQ6LlEGjV-zyYfBlH0BOaVfRPepasfY/edit)
- [Build database](https://docs.google.com/document/d/16r2h6WFY7xSB444rZJmGde6QlaU_1iuslSGL6BGEYFM/edit)
- [Design layers](https://docs.google.com/document/d/1A-9NATqidxQOA7ORxwgMNxguGBmIaUbtVRPGOgeFvuQ/edit)

 


## Authors



- [@TruongTNFx](https://www.github.com/TruongTNFx)







## 🛠 Skills

- Tool : Post man
- Configuration layer for Jwt.
- Exceptions Handle.
- Sending email(helper.addAttachment(...))
    








    
