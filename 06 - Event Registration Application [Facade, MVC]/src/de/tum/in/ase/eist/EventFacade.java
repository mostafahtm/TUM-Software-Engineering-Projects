package de.tum.in.ase.eist;

public class EventFacade {

    EventClient eventClient;
    TokenGeneratorClient tokenGeneratorClient;

    //TODO L03P01 Facade Pattern 1.1: Implement the Constructor and initialize all attributes.
    public EventFacade() {
        this.tokenGeneratorClient = new TokenGeneratorClient();
        this.eventClient = new EventClient();
    }

    //TODO L03P01 Facade Pattern 1.2: Implement the registerEvent method.
    public void registerEvent(Employee employee) {
        if(employee.getRole().equals(Role.MANAGER)){
           String generatedToken = tokenGeneratorClient.generateToken(employee);
           employee.setToken(generatedToken);

           String response = eventClient.registerEvent(employee);
           if(response.equals("Registration is successful")){
            employee.setIsRegistered(true);
           }
           else {
            System.out.println("Registration failed");
           }
        }

        else {
            System.out.println("Only managers can register the event!");
        }
    }

    //TODO L03P01 Facade Pattern 1.3: Implement the sendPreferences method.
    public void sendPreferences(Employee employee, Event event) {
        if(employee.getIsRegistered()){
            employee.setEventPreferences(event);
            eventClient.recordPreferences(employee);
        }

        else{
            System.out.println("Employee should register to the event first!");
        }
    }

    public EventClient getEventClient() {
        return eventClient;
    }

    public void setEventClient(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    public TokenGeneratorClient getTokenGeneratorClient() {
        return tokenGeneratorClient;
    }

    public void setTokenGeneratorClient(TokenGeneratorClient tokenGeneratorClient) {
        this.tokenGeneratorClient = tokenGeneratorClient;
    }
}
