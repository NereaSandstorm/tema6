package exercicis

import com.db4o.Db4oEmbedded
import exercicis.EX3_2.Ruta

fun main(args: Array<String>) {
    val bd = Db4oEmbedded.openFile("Rutes.db4o")
    val patro = Ruta(null, null, null)
    val llista = bd.queryByExample<Ruta>(patro)
    for (e in llista) {
        println("${e.nom}: ${e.llistaDePunts.size}")
    }
    bd.close();
}

