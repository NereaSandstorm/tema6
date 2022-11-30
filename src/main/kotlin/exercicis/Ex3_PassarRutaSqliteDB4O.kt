package exercicis

import com.db4o.Db4oEmbedded
import util.bd.GestionarRutesBD

fun main(args: Array<String>) {
    var gestionarRutesBD = GestionarRutesBD()
    val bd = Db4oEmbedded.openFile("Rutes.db4o")
    var lista= gestionarRutesBD.llistat()

    for (e in lista) {
        bd.store(e)
    }
    bd.close();

}