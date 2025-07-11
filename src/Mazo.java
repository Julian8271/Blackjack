import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    //Creamos una lista de objetos de tipo de tipo carta ya que cada objeto de tipo carta es una sola carta 
    private List<Carta> cartas;

    public Mazo() {
        //Los convertimos a una ArrayList ya que este solo puede contener objetos, ademas de que su tamaño puede incrementar o decrecer
        cartas = new ArrayList<>();
        inicializarMazo();
    }

    private void inicializarMazo() {
        //Creamos arreglos con los tipos de cartas y los valores que estas puedan tomar 
        String[] palos = {"Corazones", "Picas", "Diamantes", "Treboles"};
        String[] valores = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; 
        //Ciclos anidados para asignar los valores y tipo a cada carta.
        for (String palo : palos) {
            for (String valor : valores) {
                cartas.add(new Carta(palo, valor));
            }
        }
    }
    //Metodo que desordena el ArrayList, como si se estuviera barajando
    public void barajar() {
        Collections.shuffle(cartas); 
    }
    //Aca se verifica que el mazo este lleno, ademas de que se saca la carta elegida 
    public Carta sacarCarta() {
        if (cartas.isEmpty()/*Verifica si la lista esta vacia en cada de que no se inicialice el mazo*/) {
            throw new IllegalStateException("¡El mazo está vacío!");
        }
        return cartas.remove(0); 
    }
    public int tamañoMazo() {
        return cartas.size();
    }
}