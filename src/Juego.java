import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


public class Juego {
	//Se crea un atributo de tipo de las otras clases
	private Mazo mazo;
	private List<Jugador> jugadores;
	private Crupier crupier;
	private Scanner sc;
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
	public void iniciarPartida() {
		mazo.barajar();
		repartirCartasIniciales();
		turnoJugadores();
		turnoCrupier();
		determinarGanadores();
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
	//Logica principal del juego
	private void turnoJugadores() {
		for(Jugador jugador : jugadores) {
			System.out.println("\n---Turno de "+jugador.getNombre() + " ---");
			//Ciclo para las decisiones de cada jugador si plantan o piden mas
			while(true) {
				System.out.println("Tus cartas: "+jugador.mostrarMano()+" |Puntaje: "+jugador.getPuntaje());
				System.out.println("B?Pedir (1) p Plantarse (2)?");
				int opcion = sc.nextInt();
				//Segun su decision se ejecuta algo
				if(opcion == 1) {
					jugador.recibirCarta(mazo.sacarCarta());
					System.out.println("Sacaste: "+jugador.getMano().get(jugador.getMano().size()-1));
					//Se comprueba si el puntaje se sobrepaso o puede seguir jugando
					if(jugador.volo()) {
						System.out.println("Te pasaste de 21, Fin del turno");
						break;
					}

				} else {
					//Se planto
					break;
				}
			}
		}
	}
	//Turno del crupier y se toma la logica de juego de su clase
	private void turnoCrupier() {
		System.out.println("\n--Turno del Crupier ---");
		crupier.jugarTurno(mazo);
	}
	//Metodo para determinar quien gano!!!
	private void determinarGanadores() {
		System.out.println("\n=== RESULTADOS ==");
		int puntajeCrupier = crupier.getPuntaje();
		boolean crupierSePasC3 = crupier.volo();
		//Ciclo para mostrarnos los enfrentamientos
		for(Jugador jugador : jugadores) {
			int puntajeJugador = jugador.getPuntaje();
			System.out.print(jugador.getNombre() + ": " + puntajeJugador + " vs Crupier: " + puntajeCrupier + " -> ");
			if(jugador.volo()) {
				System.out.println("Perdiste (te pasaste).");
			} else if (crupierSePasC3) {
				System.out.println("!Ganaste! (Crupier se paso).");
			} else if(puntajeJugador > puntajeCrupier) {
				System.out.println("!Ganaste!");
				//Creo que este mensaje esta mal jajaja
			} else if(puntajeJugador == puntajeCrupier) {
				System.out.println("Empate (gana el crupier).");
			} else {
				System.out.println("Empate (gana el crupier).");
			}
		}
		//Comparacion entre juagdores 
		//Solo si hay mas de dos jugadores, sin contar al crupier 
		if (jugadores.size() >= 2) {
        System.out.println("\n--- Comparación entre Jugadores ---");
        for (int i = 0; i < jugadores.size() - 1; i++) {
            //Como los elementos del ArrayList son jugadores instanciamos objetos de tipo jugador 
            Jugador jugador1 = jugadores.get(i);
            //El siguiente en la ArrayList
            Jugador jugador2 = jugadores.get(i + 1);
            
            System.out.print(jugador1.getNombre() + ": " + jugador1.getPuntaje() + " vs " + 
                            jugador2.getNombre() + ": " + jugador2.getPuntaje() + " -> ");
            //Logica para comparar a los jugadores 
            //Parecida a la de crupier y jugador 
            if (jugador1.volo() && jugador2.volo()) {
                System.out.println("Ambos se pasaron.");
            } else if (jugador1.volo()) {
                System.out.println(jugador2.getNombre() + " gana.");
            } else if (jugador2.volo()) {
                System.out.println(jugador1.getNombre() + " gana.");
            } else if (jugador1.getPuntaje() > jugador2.getPuntaje()) {
                System.out.println(jugador1.getNombre() + " gana.");
            } else if (jugador1.getPuntaje() < jugador2.getPuntaje()) {
                System.out.println(jugador2.getNombre() + " gana.");
            } else {
                System.out.println("Empate.");
            }
        }
    }
	}
}