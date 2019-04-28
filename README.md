Compile and run tests
--- 

    mvn clean install

Run
---

    mvn spring-boot:run
    
Build docker image 
---

	docker build -t orchestra:latest .

Run with docker
---

	docker run --network=host -p 8081:8081 orchestra:latest