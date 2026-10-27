package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Logica del centro de impresion.
 *
 * Una cola normal (LinkedList / ArrayDeque) es FIFO: sale primero el que llego primero.
 * Aqui usamos una COLA CON PRIORIDAD: sale primero el de MENOR numero de prioridad,
 * aunque haya llegado despues. Se declara con la interfaz Queue para usar
 * los mismos metodos de cualquier cola: offer (encolar), peek (ver frente), poll (desencolar).
 */
public class GestorImpresiones {

    /** Criterio de orden: menor numero = mayor prioridad (1 antes que 3 y 5). */
    private static final Comparator<TrabajoImpresion> POR_PRIORIDAD =
            Comparator.comparingInt(TrabajoImpresion::getPrioridad);

    private Queue<TrabajoImpresion> colaTrabajos;
    private int capacidadMaxima;

    public GestorImpresiones(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.colaTrabajos = new PriorityQueue<>(POR_PRIORIDAD);
    }

    public boolean registrarTrabajo(TrabajoImpresion trabajo) {
        if (trabajo == null) {
            return false;
        }
        if (codigoInvalido(trabajo.getCodigo())) {
            return false;
        }
        if (existeTrabajo(trabajo.getCodigo())) {
            return false;                       // duplicado
        }
        if (colaTrabajos.size() >= capacidadMaxima) {
            return false;                       // cola llena
        }
        return colaTrabajos.offer(trabajo);     // encolar
    }

    public boolean existeTrabajo(String codigo) {
        if (codigoInvalido(codigo)) {
            return false;
        }
        for (TrabajoImpresion t : colaTrabajos) {   // recorrer no modifica la cola
            if (t.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public TrabajoImpresion consultarSiguienteTrabajo() {
        return colaTrabajos.peek();             // ver el frente SIN retirarlo (null si vacia)
    }

    public TrabajoImpresion procesarSiguienteTrabajo() {
        TrabajoImpresion trabajo = colaTrabajos.poll();   // retirar el frente (null si vacia)
        if (trabajo != null) {
            trabajo.setEstado("PROCESADO");
        }
        return trabajo;
    }

    public int contarTrabajosPendientes() {
        return colaTrabajos.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaTrabajos.size();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<TrabajoImpresion> listarTrabajos() {
        // El recorrido de una PriorityQueue NO esta ordenado (es un heap),
        // por eso se copia a una lista nueva y se ordena. La cola no se toca.
        List<TrabajoImpresion> lista = new ArrayList<>(colaTrabajos);
        lista.sort(POR_PRIORIDAD);
        return lista;
    }

    private boolean codigoInvalido(String codigo) {
        return codigo == null || codigo.isBlank();
    }
}
