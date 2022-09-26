package hello;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	@RequestMapping("/")
	public String home() {
		InetAddress ip;
		String hostname;
		try {
			ip = Inet4Address.getLocalHost();
			hostname = ip.getHostName();
			System.out.println("Your current IP address : " + ip.getHostAddress());
			System.out.println("Your current Hostname : " + hostname);
			return hostname + " " + ip.getHostAddress();
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}

		return "Hello Docker World";
	}

	public static void main(String[] args) {
		SpringApplication.run(hello.Application.class, args);
	}


}
