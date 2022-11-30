package util.bd

import exercicis.EX3_2.Coordenadas
import exercicis.EX3_2.PuntGeo
import exercicis.EX3_2.Ruta
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


class GestionarRutesBD {
    var urlRutes = ""
    var st1 :Statement
    var st2 :Statement
    val con :Connection

    init {
        this.urlRutes = "jdbc:sqlite:Rutes.sqlite"
        this.con = DriverManager.getConnection(this.urlRutes)
        this.st1 = con.createStatement()
        this.st2 = con.createStatement()

        val sentSQL = "CREATE TABLE IF NOT EXISTS RUTES(" +
                "num_r INTEGER CONSTRAINT cp_rut PRIMARY KEY, " +
                "nom_r TEXT, " +
                "desn INTEGER, " +
                "desn_ac INTEGER " +
                ")"

        val sentSQL2 = "CREATE TABLE IF NOT EXISTS PUNTS(" +
                "num_r INTEGER, " +
                "num_p INTEGER, " +
                "nom_p TEXT, " +
                "latitud real, " +
                "longitud real, " +
                "CONSTRAINT cp_pun primary key(num_r, num_p) " +
                "foreign key (num_r) references RUTES " +
                ")"

        st1.executeUpdate(sentSQL)
        st2.executeUpdate(sentSQL2)
    }

    fun close() {
        con.close()
    }

    fun inserir(r: Ruta) {
        val ultimoNUmeroRutes = "SELECT MAX(num_r) FROM RUTES"
        val rsruta = st1.executeQuery(ultimoNUmeroRutes)
        val num_r = rsruta.getInt(1) + 1
        println(num_r)
        val nomR = r.nom
        val desnivell = r.desnivell
        val desnac = r.desnivellAcumulat
        val pr= "INSERT INTO RUTES VALUES ($num_r, '$nomR', $desnivell, $desnac)"
        st1.executeUpdate("INSERT INTO RUTES VALUES ($num_r, '$nomR', $desnivell, $desnac)")
        val ultimoNUmeroPunts= "SELECT MAX(num_p) FROM PUNTS"
        val rspunts = st1.executeQuery(ultimoNUmeroPunts)
        var num_p = rspunts.getInt(1)

        for (r in r.llistaDePunts) {
            num_p++
            val nom_p = r.nom
            val latitud = r.coord.latitud
            val longitud = r.coord.longitud

            st2.executeUpdate("INSERT INTO PUNTS VALUES ($num_r, $num_p, '$nom_p', $latitud, $longitud)")
        }


    }

    fun buscar(i: Int) : Ruta{
        val buscaRuta = "SELECT *  FROM RUTES WHERE num_r = '$i'"
        val resultadoR = st1.executeQuery(buscaRuta)
        var ruta : Ruta

        val nomR = resultadoR.getString(2)
        val desnivell = resultadoR.getInt(3)
        val desnAc= resultadoR.getInt(4)
        var list= mutableListOf<PuntGeo>()

        val buscaPunts = "SELECT *  FROM PUNTS WHERE num_r = '$i'"
        val resultadoP = st2.executeQuery(buscaPunts)

        while (resultadoP.next()) {
            list.add(PuntGeo(resultadoP.getString(3), Coordenadas(resultadoP.getDouble(4), resultadoP.getDouble(5))))
        }

        ruta = Ruta(nomR, desnivell, desnAc, list)
        return ruta

    }

    fun llistat(): ArrayList<Ruta> {
        var lista = arrayListOf<Ruta>()
        val sentencia = "SELECT MAX(num_r) FROM RUTES"
        val ultimoNumRuta = st1.executeQuery(sentencia)

        for (i in 1 until ultimoNumRuta.getInt(1) + 1) {
            lista.add(buscar(i))

        }
        return lista
    }

    fun borrar(e: Int) {
        st1.executeUpdate("DELETE FROM RUTES WHERE num_r = $e")
        st2.executeUpdate("DELETE FROM PUNTS WHERE num_r = $e")
    }

}