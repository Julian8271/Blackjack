//Importamos las clases de arraylist y list para manejar las diferentes colecciones durante la ejecuciC3n
import java.util.Scanner;
import java.util.List;
//Nueva libreria para la el metodo reiniciarPartida para recorrer la lista con lo jugadores y verificar si tienen fichas suficientes
import java.util.Iterator;
import java.util.ArrayList;
import java.util.InputMismatchException;


public class Juego {
	//Se crea un atributo de tipo de las otras clases
	private Mazo mazo;
	private List<Jugador> jugadores;
	private Crupier crupier;
	private Scanner sc;
	//Constructor de la clase juego
	public Juego() {
		//Se inicializan este atributos llamando los constructores de cada clase
		this.mazo = new Mazo();
		this.jugadores = new ArrayList<>();
		this.crupier = new Crupier();
		this.sc = new Scanner(System.in);
	}
	//Agrega el numero de jugadores que queramos en el main
	public void agregarJugador(String name) {
		jugadores.add(new Jugador (name));
	}
	//Metodo principal que llama los otros metodos para que inicie la partida.
	public void iniciarPartida() throws BlackjackException {
		try {
			//Empezamos con las apuestas de cada jugador
			manejarApuestas();
			//Barajamos las cartas
			mazo.barajar();
			//Repartimos cartas a los jugadores
			repartirCartasIniciales();
			//Logica implementada para apostar seguro en caso de que la mano del crupier tenga un AS en su carta visible
			if(crupier.getMano().get(1).getValor().equals("As")) {
				int opcionSegura = 0;
				for (Jugador jug : jugadores) {
					//Manejamos una excepcion para evitar errores al rendirse
					while(true) {
						try {
							System.out.println("\n" + jug.getNombre() + ", el crupier muestra un As. ¿Quieres seguro? (1: SI / 2: NO): ");
							opcionSegura = sc.nextInt();
							if(opcionSegura < 1 || opcionSegura > 2) {
								System.out.println("Por favor ingresa un numero que corresponda a  uno de las opciones validas. (1 : SI / 2: NO)");
								continue;
							}
							sc.nextLine();//Consumimos el salto de linea 
							break;
						} catch(InputMismatchException e) {
							System.out.println("!Incorrecto! Debes seleccionar 1 para Si o 2 para No");
							sc.nextLine();
						}
					}
					if(opcionSegura == 1) {
						while (true) {
							try {
								//Numero a apostar
								System.out.println("¿Cuantas fichas quieres apostar como seguro? (Max " + jug.getFichas() + "): ");
								int seguro = sc.nextInt();
								sc.nextLine();//Consumimos el salto de linea
								//Nos aseguramos de que la apuesta segura este dentro del rango
								if(seguro < 0 || seguro > jug.getFichas()) {
									System.out.println("La cantidad de seguro no es valida. Debe ser entre 0 y " + jug.getFichas() + ". Intenta de nuevo.");
									continue;
								}
								jug.apostarSeguro(seguro);
								break;
							} catch (InputMismatchException e) {
								System.out.println("!Error! Debes ingresar un nC:mero para el seguro. Intenta de nuevo.");
								sc.next(); //Limpiamos el buffer
							}
						}
					}
				}
			}
			if(!crupier.getMano().get(1).getValor().equals("As")) {
				System.out.println("\nLa carta no oculta del Crupier es: "+crupier.getMano().get(1));
			}
			//Vamos con el turno de cada jugador que este en el juego
			turnoJugadores();
			//Turno del crupier
			turnoCrupier();
			//Aca determinamos los ganadores segun los resultados obtenidos anteriormente
			determinarGanadores();
			mostrarResultadosFichas();
		} catch (BlackjackException e) {
			System.err.println(e.getMessage());
			reiniciarPartida();
		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
		}
	}
	//Reparte las dos primeras cartas a cada juagdor y al crupier
	private void repartirCartasIniciales() {
		for(Jugador jugador :  jugadores) {
			jugador.recibirCarta(mazo.sacarCarta());
			jugador.recibirCarta(mazo.sacarCarta());
		}
		crupier.recibirCartaOculta(mazo.sacarCarta());
		crupier.recibirCarta(mazo.sacarCarta());
	}
	//Metodo para manejar las apuestas
	private void manejarApuestas() throws BlackjackException {
		for(Jugador jug : jugadores) {
			System.out.println("\n----------------------------------");
			System.out.println("Jugador: " + jug.getNombre());
			System.out.println("Fichas disponibles: " + jug.getFichas());

			int apuesta = -1;
			//ciclo para la apuesta y manejo de errores en caso de que el usuario ingrese algo mal
			while(apuesta <= 0 || apuesta > jug.getFichas()) {
				System.out.print("Cuanto quieres apostar? (1-" + jug.getFichas() + "): ");
				try {
					apuesta = sc.nextInt();
					sc.nextLine(); //Limpiar buffer
				} catch(Exception e) {
					sc.nextLine(); //Limpiar buffer en caso de error
					System.out.println("Por favor ingresa un numero valido.");
					continue;
				}

				if(apuesta <= 0) {
					System.out.println("La apuesta debe ser mayor que 0.");
				} else if(apuesta > jug.getFichas()) {
					System.out.println("No tienes suficientes fichas.");
				}
			}
			jug.apostar(apuesta);
		}
	}



	//Logica principal del juego
	// En el metodo turnoJugadores():
	private void turnoJugadores() {
		for (Jugador jugador : jugadores) {
			System.out.println("\n--- Turno de " + jugador.getNombre() + " ---");

			// Mostrar mano inicial
			System.out.println("Tus cartas: " + jugador.mostrarMano() + " | Puntaje: " + jugador.getPuntaje());
			// Opcion de rendirse (solo en las dos primeras cartas)
			if(jugador.getMano().size() == 2) {
				int opcionRendirse = 0;
				///Bloque de codigo para manejar errores algo ingresar los datos el usuario
				while(true) {
					try {
						System.out.println("¿Quieres rendirte? (1: SI / 2: NO)");
						opcionRendirse = sc.nextInt();
						//confirmamos que que la entrada sea una de las dos opciones
						if(opcionRendirse < 1 || opcionRendirse > 2) {
							System.out.println("Por favor ingresa un numero que corresponda a  uno de las opciones validas. (1 : SI / 2: NO)");
							continue;
						}
						sc.nextLine();
						break;
						//Manejo de errores
					} catch (InputMismatchException e) {
						System.out.println("Por favor ingresa un numero entero que corresponda a una de las dos opciones validas. (1: SI / 2: NO)");
						sc.nextLine();//Limpiamos el buffer
					}
				}
				//condicion en la cual se rinde el jugador y recupera la mitad de su apuesta
				if(opcionRendirse == 1) {
					jugador.rendirse();
					System.out.println("Te rendiste. Recuperas la mitad de tu apuesta.");
					continue;
				}
			}
			// Opcion de dividir
			if (jugador.puedeDividir()) {
				int opcionSplit = 0;
				//Bloque de codigo para manejar errores algo ingresar los datos el usuario
				while(true) {
					System.out.print("Quieres dividir? (1: SI / 2: No): ");
					try {

						opcionSplit = sc.nextInt();
						//Confirmamos una de las dos opciones
						if(opcionSplit < 1 || opcionSplit > 2 ) {
							System.out.println("Por favor ingresa un numero que corresponda a una de las dos opciones. (1: SI / 2: NO)");
							continue;
						}
						sc.nextLine();
						break;
					} catch (InputMismatchException e) {
						System.out.println("Por favor ingresa un numero entero que corresponda a una de las dos opciones. (1: SI / 2: NO)");
						sc.nextLine();
					}
				}
				if (opcionSplit == 1) {
					//Llamamos el metodo para dividir las manos
					jugador.dividir();
					System.out.println("!Cartas divididas! Ahora tienes dos manos:");
					System.out.println("Mano 1: " + jugador.getMano().get(0));
					System.out.println("Mano 2: " + jugador.getSegundaMano().get(0));

					// Jugar primera mano
					jugarMano(jugador, false);

					// Jugar segunda mano
					jugarMano(jugador, true);

					continue; // Saltar al siguiente jugador
				}
			}

			// Si no dividie, jugar normal
			jugarMano(jugador, false);
		}
	}

// Nuevo metodo auxiliar para manejar una mano.
	private void jugarMano(Jugador jugador, boolean esSegundaMano) {
		//Creamos una lista para manejar la mano en caso de que condicion de segunda mano se cumpla o si no jugamos con la original
		List<Carta> mano = esSegundaMano ? jugador.getSegundaMano() : jugador.getMano();
		//Hacemos lo mismo con una condicion para obtener el puntaje de la mano con la cual se esta jugando
		int puntaje = esSegundaMano ? jugador.getPuntajeSegundaMano() : jugador.getPuntaje();

		//Aca creamos el string con las cartas de la segunda mano del jugador
		StringBuilder cartasStr = new StringBuilder();
		for (Carta carta : mano) {
			cartasStr.append(carta.toString()).append(" ");
		}
		System.out.println("Cartas de tu " + (esSegundaMano ? "segunda mano" : "mano principal") + ": " + cartasStr.toString().trim());
		//Logica de la segunda mano para recibir cartas o plantarse igual a la mano original
		while (true) {
			System.out.print("Pedir carta (1) o Plantarse (2)? ");
			int opcion = 0;
			while(true) {
				//Uso de try y catch para atrapar posible error
				try {
					opcion = sc.nextInt();
					sc.nextLine();
					if(opcion < 1 || opcion > 2) {
						System.out.println("Por favor selecciona una opcion valida! 1: Pedir Cartas 2: Platarse");
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println("Debes ingresar un numero entero que corresponda a las dos opciones. 1: Pedir Cartas 2: Platarse");
					sc.nextLine();
				}
			}
			//En caso de que la opcion sea uno hace toda la logica para recibir una nueva carta
			if (opcion == 1) {
				Carta nuevaCarta = mazo.sacarCarta();
				//bloque if-else para añadir y calcular el puntaje de la mano correspondiente
				if (esSegundaMano) {
					jugador.recibirCartaSegundaMano(nuevaCarta);
					puntaje = jugador.getPuntajeSegundaMano();
				} else {
					jugador.recibirCarta(nuevaCarta);
					puntaje = jugador.getPuntaje();
				}
				System.out.println("Sacaste: " + nuevaCarta + " Tu puntaje es: "+(esSegundaMano ? jugador.getPuntajeSegundaMano() : jugador.getPuntaje()));

				if ((esSegundaMano && jugador.segundaManoVolo()) || (!esSegundaMano && jugador.volo())) {
					System.out.println("!Te pasaste de 21 en esta mano!");
					break;
				}
				//En caso de dos se termina el ciclo
			} else {
				break;
			}
		}
	}
	//metodo para verificar si los jugadores quieren otra ronda!
	public boolean volverAJugar() {
		String continuar;
		while (true) {
			System.out.println("Nueva Ronda? (S/N)");
			try {
				continuar = sc.nextLine();
				if(!continuar.equalsIgnoreCase("S") && !continuar.equalsIgnoreCase("N")) {
					System.out.println("Ingresa una una opcion correcta de las dos. (S: SI / N: NO)");
					continue;
				}
				break;

			} catch (InputMismatchException e) {
				System.out.println("Ingresa una letra por favor.");
			}
		}
		/*En caso de que la condicion sea verdadera este bloque de codigo
		se ejecutara limpiando las manos de cada jugador, ademas de verificar si cuentan con los recursos para apostar.
		*/
		if (continuar.equalsIgnoreCase("S") ) {
			//Se usa un for comun ya que se necesita acceder a los indices del ArrayList para eliminar a dicho jugador.
			for(int i = 0; i < jugadores.size(); i++) {
				//Limpiamos la mano de cada jugador para la nueva ronda
				jugadores.get(i).getMano().clear();
				//En caso de que el jugador no tenga mas fichas no podra seguir jugando
				if(jugadores.get(i).getFichas() <= 0) {
					System.out.println(jugadores.get(i).getNombre() +" no puede seguir jugando ya que no tiene fichas suficientes, continuando sin el.");
					jugadores.remove(i);
					i--;
				}
			}
			//Tambien limpiamos la mano del crupier para la nueva ronda.
			crupier.getMano().clear();
			//retornamos false para que la condicion se siga ejecutando en el main
			return !continuar.equalsIgnoreCase("S");
			//No mas rondas
		} else {
			return true;
		}

	}
	//Turno del crupier y se toma la logica de juego de su clase
	private void turnoCrupier() {
		System.out.println("\n--Turno del Crupier ---");
		crupier.jugarTurno(mazo);
	}

	//Metodo para determinar quien gano!!!
	//Quitamos la comparación con los otros jugadores, ya que en el blackjack original esto no se hace en el modo de jeugo clasico 
	private void determinarGanadores() {
		System.out.println("\n=== RESULTADOS ===");
		//Obtenemos el puntaje del crupier
		int puntajeCrupier = crupier.getPuntaje();
		//verificamos si este se paso o no
		boolean crupierSePaso = crupier.volo();
		//En caso de que tenga blackjack
		boolean crupierBlackjack = crupier.tieneBlackjack();
		//se verifica si los jugadores tienen mano o no
		for(Jugador jug : jugadores) {
			if(jug.getMano().isEmpty()) {
				continue;
			}
		}

		for(Jugador jugador : jugadores) {
			//En caso de que la mano este basia porque se retiro se pasa
			if(jugador.getMano().isEmpty()) continue;
			//Logica de pago por el pago seguro
			if(crupierBlackjack && jugador.getSeguro() > 0) {
				int pagoSeguro = jugador.getSeguro() * 2;
				jugador.ganarFichas(pagoSeguro);
				System.out.println(jugador.getNombre() + " gana " + pagoSeguro + " por el seguro");
			}
		}
		for (Jugador jugador : jugadores) {
			//Procesar primera mano
			determinarResultadoMano(jugador, false, puntajeCrupier, crupierSePaso);

			//Procesar segunda mano (si existe)
			if (!jugador.getSegundaMano().isEmpty()) {
				determinarResultadoMano(jugador, true, puntajeCrupier, crupierSePaso);
			}
		}
	}

	private void determinarResultadoMano(Jugador jugador, boolean esSegundaMano,
	                                     int puntajeCrupier, boolean crupierSePaso) {
		//Logica para tenerminar el ganador del juego teniendo en cuenta todos las condiciones o acciones durante el juego
		//Verificamos el puntaje de la mano con la que se este jugando
		int puntaje = esSegundaMano ? jugador.getPuntajeSegundaMano() : jugador.getPuntaje();
		//Verificamos si se paso el jugador en alguna manoo
		boolean volo = esSegundaMano ? jugador.segundaManoVolo() : jugador.volo();

		System.out.print(jugador.getNombre() + " (" + (esSegundaMano ? "Mano Split" : "Mano Principal") +
		                 "): " + puntaje + " vs Crupier: " + puntajeCrupier + " -> ");

		if (volo) {
			System.out.println("Perdiste (te pasaste)");
		} else if (crupierSePaso) {
			System.out.println("!Ganaste! (Crupier se paso)");
			if (esSegundaMano) {
				jugador.ganarApuestaSegundaMano();
			} else {
				jugador.ganarApuesta();
			}
		} else if (puntaje > puntajeCrupier) {
			System.out.println("!Ganaste!");
			if (esSegundaMano) {
				jugador.ganarApuestaSegundaMano();
			} else {
				jugador.ganarApuesta();
			}
		} else if (puntaje == puntajeCrupier) {
			System.out.println("Empate");
			if (esSegundaMano) {
				jugador.empateApuestaSegundaMano();
			} else {
				jugador.empateApuesta();
			}
		} else {
			System.out.println("Perdiste");
		}
	}
	//Metodo para mostrar el saldo de los particpantes al finalizar la ronda.
	private void mostrarResultadosFichas() {
		System.out.println("\n=== SALDO FINAL ===");
		for (Jugador jugador : jugadores) {
			System.out.println(jugador.getNombre() + ": " + jugador.getFichas() + " fichas");
		}
	}

	public void reiniciarPartida() throws BlackjackException {
		//Limpiar manos de todos los jugadores y crupier
		for (Jugador jugador : jugadores) {
			//Aca nos aseguramos de que todos los datos receteados para volver a iniciar y evitar problemas
			jugador.getMano().clear();
			jugador.getSegundaMano().clear(); //Si se dividio la mano
			jugador.setPuntaje(0);
			jugador.setPuntajeSegundaMano(0);
			jugador.setApuestaActual(0);
			jugador.setApuestaSegundaMano(0);
			jugador.setSeguro(0); //Reiniciar seguro
		}
		crupier.getMano().clear();
		crupier.setPuntaje(0);

		//Verificar jugadores sin fichas
		//Usamos iterator ya que investigando es mas eficiente que usar un bloque de ciclos ya que evita errores.
		Iterator<Jugador> iterator = jugadores.iterator();
		while (iterator.hasNext()) {
			Jugador jugador = iterator.next();
			if (jugador.getFichas() <= 0) {
				System.out.println(jugador.getNombre() + " ha sido eliminado (sin fichas).");
				iterator.remove();
			}
		}

		//Validar que queden jugadores
		if (jugadores.isEmpty()) {
			throw new BlackjackException("No hay jugadores activos. El juego ha terminado.");
		}

		//Reiniciar mazo si tiene menos de 10 cartas
		if (mazo.tamañoMazo() < 10) {
			mazo = new Mazo();
			System.out.println("!Nuevo mazo barajado!");
		}

		//Barajar de nuevo
		mazo.barajar();
	}
}