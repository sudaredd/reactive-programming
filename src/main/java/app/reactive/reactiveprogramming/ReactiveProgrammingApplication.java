package app.reactive.reactiveprogramming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class ReactiveProgrammingApplication {

	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(ReactiveProgrammingApplication.class, args);
	}

}
