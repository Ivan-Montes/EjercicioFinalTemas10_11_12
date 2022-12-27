package obc.Ejercicio10_11_12;

import obc.Ejercicio10_11_12.entities.Laptop;
import obc.Ejercicio10_11_12.repos.LaptopRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Ejercicio10_11_12_Application {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(Ejercicio10_11_12_Application.class, args);
		LaptopRepository laptopRepository = (LaptopRepository) context.getBean("laptopRepository");
		Laptop laptop01 = new Laptop(null,"HP","220G5",4.3);
		Laptop laptop02 = new Laptop(null,"Dell","Optiplex330",8.3);
		laptopRepository.save(laptop01);
		laptopRepository.save(laptop02);

		System.out.println("\n###### Fin de main alcanzado ######");
	}

}
