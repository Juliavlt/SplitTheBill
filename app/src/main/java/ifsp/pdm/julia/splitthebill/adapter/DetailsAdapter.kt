package ifsp.pdm.julia.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ifsp.pdm.julia.splitthebill.R
import ifsp.pdm.julia.splitthebill.databinding.ActivityDetailsBinding
import ifsp.pdm.julia.splitthebill.model.Integrante

class DetailsAdapter(
    context: Context,
    private val integranteList: MutableList<Integrante>
    ): ArrayAdapter<Integrante>(context, R.layout.activity_details, integranteList) {
    //é um jeito de escrever um construtor

    private data class TileIntegrantesHolder(val name:TextView, val valorPago: TextView, val valorAReceber: TextView, val valorAPagar: TextView)

    private lateinit var tcb: ActivityDetailsBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val integrante = integranteList[position]
        var integranteTileView = convertView

        if (integranteTileView == null) {
            tcb = ActivityDetailsBinding.inflate(
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
            tcb.valorReceberTv.text = integrante.valorAReceber.toString()
            tcb.valorAPagarTv.text =integrante.valorAPagar.toString()
        }

        return integranteTileView;
    }
    }