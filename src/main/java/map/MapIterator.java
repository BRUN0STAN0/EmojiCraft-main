package map;

import java.util.Iterator;

/**
 * Iterator Pattern: Il pattern Iterator consente di accedere agli elementi di un oggetto collezione in modo sequenziale senza esporre la sua rappresentazione interna.
 * L'interfaccia MapIterator estende Iterator per permettere l'iterazione sui componenti della mappa.
 * Questo pattern separa la logica di iterazione dalla struttura dati, rendendo il codice più flessibile e riutilizzabile.
 *
 * Motivo dell'utilizzo:
 * - Consente di navigare tra i componenti della mappa senza esporre la struttura interna.
 * - Facilita l'estensione e la manutenzione della logica di iterazione.
 */
public interface MapIterator extends Iterator<MapComponent> {

    
}
