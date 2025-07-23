import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Blackjack {

	public static void main(String[] args) throws BlackjackException {
		Scanner sc = new Scanner(System.in);
		//creacion del objeto juego 
		Juego juego;
		int numeroJugadores;
		//Bucle + try-catch para manejar posibles errores en el ingreso del numero de usuarios 
		System.out.println("+------------------------------------+");
		System.out.println("+  Bienvenido al Juego de Blackjack  +");
		System.out.println("+------------------------------------+\n");
		System.out.println("Vamos a jugar!!!\n");
		while (true) {
			System.out.print("Ingresa el numero de jugadores que van a participar: ");
			try {
				numeroJugadores = sc.nextInt();
				sc.nextLine();
                //Verificacion de que el numero sea positivo y no muy grande
				if (numeroJugadores <= 0 || numeroJugadores > 10) {
					System.out.println("!Espera! El numero de jugadores debe ser un entero positivo o no pueden ser más de 10 jugadores. !Intenta de nuevo!");
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("!Incorrecto! Eso no parece un numero entero. Por favor, ingresa un numero valido.");
				sc.nextLine();
			} catch (Exception e) {
				System.out.println("Error, vuelve a intenter recuerda que debes ingresar un numero entero!");
				sc.nextLine();
			}
		}
		juego = new Juego(); 
		//Ciclo para ingresar el numero de jugadores
		for(int i = 1; i <= numeroJugadores; i++) {
			System.out.print("Participante numero " + i + ": ");
			String jugadoresIngresados;
			while (true) {
			    //Verificamos la entrada del usuario y atrapamos el posible error
				try {
					jugadoresIngresados = sc.nextLine();
					//En caso de ser vacio el nombre, pedimos que lo vuelva a ingresar!
					if(jugadoresIngresados.trim().isEmpty()) {
					    System.out.println("El nombre del participante no puede estar vacío. Por favor, ingresa un nombre válido.");
					    continue;
					}
					juego.agregarJugador(jugadoresIngresados);
					break;
				} catch (NoSuchElementException e) {
					System.out.println("No es un nombre de usuario valido, por favor solo letras");
				}
			}
		}

		System.out.println("\n!Todos los jugadores han sido registrados! !vamos a Jugar !");

		while(true) {
			juego.iniciarPartida();
			if(juego.volverAJugar()) break; // Se ajusta la logica para salir del bucle si no quiere jugar de nuevo
		}

		sc.close(); //Scanner se cierra aqui, al final del programa.
	}
}