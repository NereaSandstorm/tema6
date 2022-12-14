package exercicis

import java.awt.EventQueue
import java.awt.GridLayout
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.BoxLayout
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JTable
import javax.swing.JScrollPane
import com.db4o.Db4oEmbedded
import exercicis.EX3_2.PuntGeo
import exercicis.EX3_2.Ruta
import util.bd.GestionarRutesBD
import kotlin.system.exitProcess

class FinestraComplet : JFrame() {
    var llista = arrayListOf<Ruta>()
    var numActual = 0

    // DeclaraciĆ³ de la Base de Dades

    val qNom = JTextField(15)
    val qDesn = JTextField(5)
    val qDesnAcum = JTextField(5)
    val distancia = JTextField(5)
    val punts = JTable(1, 3)
    val primer = JButton(" << ")
    val anterior = JButton(" < ")
    val seguent = JButton(" > ")
    val ultim = JButton(" >> ")
    val tancar = JButton("Tancar")

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setTitle("JDBC: Visualitzar Rutes Complet")
        setLayout(GridLayout(0, 1))

        val p_prin = JPanel()
        p_prin.setLayout(BoxLayout(p_prin, BoxLayout.Y_AXIS))
        val panell1 = JPanel(GridLayout(0, 2))
        panell1.add(JLabel("Ruta:"))
        qNom.setEditable(false)
        panell1.add(qNom)
        panell1.add(JLabel("Desnivell:"))
        qDesn.setEditable(false)
        panell1.add(qDesn)
        panell1.add(JLabel("Desnivell acumulat:"))
        qDesnAcum.setEditable(false)
        panell1.add(qDesnAcum)
        panell1.add(JLabel("Distancia:"))
        distancia.setEditable(false)
        panell1.add(distancia)
        panell1.add(JLabel("Punts:"))

        val panell2 = JPanel(GridLayout(0, 1))
        punts.setEnabled(false)
        val scroll = JScrollPane(punts)
        panell2.add(scroll, null)

        val panell5 = JPanel(FlowLayout())
        panell5.add(primer)
        panell5.add(anterior)
        panell5.add(seguent)
        panell5.add(ultim)

        val panell6 = JPanel(FlowLayout())
        panell6.add(tancar)

        add(p_prin)
        p_prin.add(panell1)
        p_prin.add(panell2)
        p_prin.add(panell5)
        p_prin.add(panell6)
        pack()

        primer.addActionListener {
            // instruccions per a situar-se en la primera ruta, i visualitzar-la
            numActual = 0
            VisRuta()
        }
        anterior.addActionListener {
            // instruccions per a situar-se en la ruta anterior, i visualitzar-la
            if (!(numActual <= 0)) {
                numActual -= 1

            } else {
                numActual = 0
            }

            VisRuta()
        }
        seguent.addActionListener {
            // instruccions per a situar-se en la ruta segĆ¼ent, i visualitzar-la
            numActual += 1
            VisRuta()
        }
        ultim.addActionListener {
            // instruccions per a situar-se en l'Ćŗltim ruta, i visualitzar-la
            numActual = llista.size-1
            VisRuta()
        }
        tancar.addActionListener {
            // instruccions per a tancar la BD i el programa
            exitProcess(0)
        }

        inicialitzar()
        VisRuta()
    }

    fun plenarTaula(ll_punts: MutableList<PuntGeo>) {
        var ll = Array(ll_punts.size) { arrayOfNulls<String>(3) }
        for (i in 0 until ll_punts.size) {
            ll[i][0] = ll_punts.get(i).nom
            ll[i][1] = ll_punts.get(i).coord.latitud.toString()
            ll[i][2] = ll_punts.get(i).coord.longitud.toString()
        }
        val caps = arrayOf("Nom punt", "Latitud", "Longitud")
        punts.setModel(javax.swing.table.DefaultTableModel(ll, caps))
    }

    fun inicialitzar() {
        // instruccions per a inicialitzar llista i numActual
        var gestionarRutesBD = GestionarRutesBD()
        llista = gestionarRutesBD.llistat()
        numActual = 0

    }

    fun VisRuta() {
        // instruccions per a visualitzar la ruta actual (l'Ć­ndex el tenim en numActual
        var gestionarRutesBD = GestionarRutesBD()
        qNom.text = llista.get(numActual).nom
        qDesn.text= llista.get(numActual).desnivell.toString()
        qDesnAcum.text= llista.get(numActual).desnivellAcumulat.toString()

        var dist: Double = 0.0
        for (i in 0..llista.get(numActual).llistaDePunts.size - 2) {
            var punto1 = llista.get(numActual).llistaDePunts[i]
            var punto2 = llista.get(numActual).llistaDePunts[i + 1]

            dist+= Dist(punto1.coord.latitud, punto1.coord.longitud, punto2.coord.latitud, punto2.coord.longitud)
        }
        distancia.text = dist.toString() + " km"
        plenarTaula(llista.get(numActual).llistaDePunts)
        ActivarBotons()
    }

    fun ActivarBotons() {
        // instruccions per a activar o desactivar els botons de moviment ( setEnabled(Boolean) )


        if (numActual == 0) {
            primer.isEnabled = false
            anterior.isEnabled = false
            seguent.isEnabled = true
            ultim.isEnabled = true
        } else {
            primer.isEnabled = true
            anterior.isEnabled = true
            seguent.isEnabled = true
            ultim.isEnabled = true
        }

        if (numActual == llista.size - 1) {
            ultim.isEnabled = false
            seguent.isEnabled = false
            primer.isEnabled = true
            anterior.isEnabled = true
        } else {
            ultim.isEnabled = true
            seguent.isEnabled = true
        }


    }

}
fun Dist(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

    val R = 6378.137 // Radi de la Tierra en km
    val dLat = rad(lat2 - lat1)
    val dLong = rad(lon2 - lon1)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val d = R * c
    return Math.round(d*100.0)/100.0
}

fun rad(x: Double): Double {
    return x * Math.PI / 180
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        FinestraComplet().isVisible = true
    }
}