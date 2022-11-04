package ifsp.pdm.julia.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ifsp.pdm.julia.splitthebill.R
import ifsp.pdm.julia.splitthebill.databinding.TileIntegranteBinding
import ifsp.pdm.julia.splitthebill.model.Integrante

class IntegranteAdapter(
    context: Context,
    private val integranteList: MutableList<Integrante>
    ): ArrayAdapter<Integrante>(context, R.layout.tile_integrante, integranteList) {
    //é um jeito de escrever um construtor

    private data class TileIntegrantesHolder(val name:TextView, val valorPago: TextView, val valorAReceber: TextView, val valorAPagar: TextView)

    private lateinit var tcb: TileIntegranteBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val integrante = integranteList[position]
        var integranteTileView = convertView
        var valorTotal: Double =  0.0

        for (i in integranteList) {
            valorTotal+=i.valorPago;
        }

        if (integranteTileView == null) {
            tcb = TileIntegranteBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            integranteTileView = tcb.root;
            val tileIntegrantesHolder = TileIntegrantesHolder(tcb.integranteTv, tcb.valorPagoTv, tcb.valorReceberTv, tcb.valorAPagarTv) //cria um holder e faz ele apontar pr os obj internos da celula
            integranteTileView.tag = tileIntegrantesHolder //associar o holder a celula
        }

        with (integranteTileView.tag as TileIntegrantesHolder){ //troca o valor dos objs que o holder esta apontando
            tcb.valorPagoTv.text = integrante.valorPago.toString()
            tcb.integranteTv.text = integrante.nome
            tcb.valorReceberTv.text = valorAReceber(integranteList.size, valorTotal, integrante.valorPago.toString().toDouble()).toString()
            tcb.valorAPagarTv.text = valorAPagar(integranteList.size, valorTotal, integrante.valorPago.toString().toDouble()).toString()
        }

        return integranteTileView;
    }

    fun valorAReceber(numeroIntegrantes: Int, valorTotal: Double, valorPago: Double): Double{
        val valorAReceber: Double;
        if (numeroIntegrantes == 1){
            valorAReceber = 0.0;
        }  else if (valorPago > (valorTotal / numeroIntegrantes)) {
            valorAReceber = ((valorTotal / numeroIntegrantes) - valorPago) * (-1);
        } else{
            valorAReceber = 0.0
        }
        return valorAReceber;
    }

    fun valorAPagar(numeroIntegrantes: Int, valorTotal: Double, valorPago: Double): Double{
        val valorAPagar: Double;
        if (numeroIntegrantes == 1){
            valorAPagar = 0.0;
        } else if (valorPago == 0.0) {
            valorAPagar = valorTotal / numeroIntegrantes;
        }
        else if (valorPago < (valorTotal / numeroIntegrantes)) {
            valorAPagar = (valorTotal / numeroIntegrantes) - valorPago;
        } else {
            valorAPagar = 0.0
        }
        return valorAPagar;
    }
    }