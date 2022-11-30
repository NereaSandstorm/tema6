package exercicis.EX3_2

import java.io.FileInputStream
import java.io.Serializable

class Ruta (var nom: String?, var desnivell: Int?, var desnivellAcumulat: Int?, var llistaDePunts: MutableList<PuntGeo> = mutableListOf<PuntGeo>()): Serializable{
    companion object {
        private const val serialVersionUID: Long = 1
    }

    fun addPunt(p: PuntGeo){
        llistaDePunts.add(p)
    }

    fun getPunt(i: Int): PuntGeo {
        return llistaDePunts.get(i)
    }

    fun getPuntNom(i: Int): String {
        return llistaDePunts.get(i).nom
    }

    fun getPuntLatitud(i: Int): Double {
        return llistaDePunts.get(i).coord.latitud
    }

    fun getPuntLongitud(i: Int): Double {
        return llistaDePunts.get(i).coord.longitud
    }

    fun size(): Int {
        return llistaDePunts.size
    }

    fun mostrarRuta() {
        // Aquest és el mètode que heu d'implementar vosaltres


        var contador = 0

        println("Ruta: " + nom)
        println("Desnivel: " + desnivell)
        println("Desnivel acumulado: " + desnivellAcumulat)
        println("Tiene " + size() + " puntos")
        for (e in llistaDePunts) {
            contador++
            println("Punto $contador: "+ getPuntNom(contador-1)+"("+ getPuntLatitud(contador-1)+", "+getPuntLongitud(contador-1)+")")
        }

        println()
    }
}